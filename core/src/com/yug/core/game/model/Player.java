package com.yug.core.game.model;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.yug.core.game.GameWorld;
import com.yug.pf.PathFinder;

import java.util.LinkedList;

/**
 * Created by yugine on 22.1.15.
 */
public class Player extends MovableTile implements NavigationMapObserver
{
    private final float DEFAULT_SPEED = 96;
    private State state;
    private Texture texture;
    private PathFinder pathFinder;
    private LinkedList<Tile> path;
    private Tile nextNavigationPoint;
    private Direction direction;

    public Player()
    {
        setSpeed(DEFAULT_SPEED);
        pathFinder = new PathFinder();
        texture = createTestTexture();

    }

    @Override
    public void update(float deltaT)
    {
        if (State.MOVING.equals(state))
        {
            if (nextNavigationPoint == null)
            {
                if (path != null && path.size() > 0)
                {
                    nextNavigationPoint = path.pop();
                    updateDirection();
                    updatePosition(deltaT);
                }
                else
                {
                    this.state = State.STANDING;
                }
            }
            else
            {
                updatePosition(deltaT);
            }
        }
    }

    @Override
    public void onMapUpdate(final NavigationMapWrapper navigationMap)
    {
        if (State.MOVING.equals(this.state) && path != null && path.size() > 0)
        {
            for (final Tile tile : path)
            {
                Tile navPoint = navigationMap.getPoint(tile.getX(), tile.getY());
                if (navPoint == null || !navPoint.isWalkable())
                {
                    final Tile endPoint = path.getLast();
                    goTo(endPoint.getX(), endPoint.getY());
                }
            }
        }
    }

    private void updateDirection()
    {
        final int nextX = nextNavigationPoint.getX();
        final int nextY = nextNavigationPoint.getY();
        final int deltaX = nextX - getX();
        final int deltaY = nextY - getY();
        if (deltaX < 0 && deltaY == 0)
        {
            direction = Direction.LEFT;
        }
        else if (deltaX > 0 && deltaY == 0)
        {
            direction = Direction.RIGHT;
        }
        else if (deltaX == 0 && deltaY < 0)
        {
            direction = Direction.DOWN;
        }
        else if (deltaX == 0 && deltaY > 0)
        {
            direction = Direction.UP;
        }
        else if (deltaX < 0 && deltaY < 0)
        {
            direction = Direction.DOWN_LEFT;
        }
        else if (deltaX < 0 && deltaY > 0)
        {
            direction = Direction.UP_LEFT;
        }
        else if (deltaX > 0 && deltaY < 0)
        {
            direction = Direction.DOWN_RIGHT;
        }
        else if (deltaX > 0 && deltaY > 0)
        {
            direction = Direction.UP_RIGHT;
        }
    }

    private void updatePosition(float deltaT)
    {
        if (nextNavigationPoint != null)
        {
            direction.getHandler().update(deltaT, this);
        }

    }

    public void goTo(final int destX, final int destY)
    {
        if (State.MOVING_ON_PLATFORM != state && (getX() != destX || getY() != destY))
        {
            path = pathFinder.calculatePath(getX(), getY(), destX, destY, GameWorld.getInstance().getNavigationMap());

            if (State.MOVING.equals(this.state) && path != null)
            {
                //force updating the nextNavigationPoint
                //nextNavigationPoint = path.pop();
                //updateDirection();
            }
            else if (State.MOVING != state && path != null && path.size() > 0)
            {
                this.state = State.MOVING;
            }
        }
    }

    public State getState()
    {
        return state;
    }

    public void setState(final State state)
    {
        this.state = state;
    }

    public Tile getNextNavigationPoint()
    {
        return nextNavigationPoint;
    }

    public void setNextNavigationPoint(final Tile nextNavigationPoint)
    {
        this.nextNavigationPoint = nextNavigationPoint;
    }

    public Texture getTexture()
    {
        return texture;
    }

    //creating test texture
    private Texture createTestTexture()
    {
        final Pixmap pixmap = createTestPixmap(32, 32);
        return new Texture(pixmap);
    }

    //creating test texture
    private Pixmap createTestPixmap(final int width, final int height)
    {
        final Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        // Fill square with red color at 50% opacity
        pixmap.setColor(1, 0, 0, .5f);
        pixmap.fill();
        // Draw a yellow-colored X shape on square
        pixmap.setColor(1, 1, 0, 1);
        pixmap.drawLine(0, 0, width, height);
        pixmap.drawLine(width, 0, 0, height);
        // Draw a cyan-colored border around square
        pixmap.setColor(0, 1, 1, 1);
        pixmap.drawRectangle(0, 0, width, height);
        return pixmap;
    }

    public enum State
    {
        MOVING, STANDING, MOVING_ON_PLATFORM
    }

    private enum Direction
    {
        LEFT(new LeftDirectionHandler()),
        RIGHT(new RightDirectionHandler()),
        UP(new UpDirectionHandler()),
        DOWN(new DownDirectionHandler()),
        UP_LEFT(new UpLeftDirectionHandler()),
        UP_RIGHT(new UpRightDirectionHandler()),
        DOWN_LEFT(new DownLeftDirectionHandler()),
        DOWN_RIGHT(new DownRightDirectionHandler());
        private DirectionHandler handler;

        private Direction(final DirectionHandler handler)
        {
            this.handler = handler;
        }

        public DirectionHandler getHandler()
        {
            return handler;
        }
    }

    private static abstract class DirectionHandler
    {
        public void update(final float deltaT, final Player player)
        {
            final Tile nextNavigationPoint = player.getNextNavigationPoint();
            final int nextX = nextNavigationPoint.getX();
            final int nextY = nextNavigationPoint.getY();
            final float nextPointScreenX = nextX * player.getWidth();
            final float nextPointScreenY = nextY * player.getHeight();
            final float velocity = player.getSpeed() * deltaT;
            final float newScreenX = getNewScreenX(player.getScreenX(), nextPointScreenX, velocity);
            final float newScreenY = getNewScreenY(player.getScreenY(), nextPointScreenY, velocity);
            player.setScreenX(newScreenX);
            player.setScreenY(newScreenY);
            player.setX(nextX);
            player.setY(nextY);
            //call onChangePosition
            if (newScreenX == nextPointScreenX && newScreenY == nextPointScreenY)
            {
                //call onArrivedToPoint
                player.setNextNavigationPoint(null);
            }
        }

        protected abstract float getNewScreenX(final float playerScreenX, final float nextPointScreenX, final float velocity);

        protected abstract float getNewScreenY(final float playerScreenY, final float nextPointScreenY, final float velocity);
    }

    private static class LeftDirectionHandler extends DirectionHandler
    {
        public static float calcNewScreenX(final float playerScreenX, final float nextPointScreenX, final float velocity)
        {
            float newScreenX = playerScreenX - velocity;
            if (newScreenX <= nextPointScreenX)
            {
                newScreenX = nextPointScreenX;
            }
            return newScreenX;
        }

        @Override
        protected float getNewScreenX(final float playerScreenX, final float nextPointScreenX, final float velocity)
        {
            return calcNewScreenX(playerScreenX, nextPointScreenX, velocity);
        }

        @Override
        protected float getNewScreenY(final float playerScreenY, final float nextPointScreenY, final float velocity)
        {
            return playerScreenY;
        }
    }

    private static class RightDirectionHandler extends DirectionHandler
    {
        public static float calcNewScreenX(final float playerScreenX, final float nextPointScreenX, final float velocity)
        {
            float newScreenX = playerScreenX + velocity;
            if (newScreenX >= nextPointScreenX)
            {
                newScreenX = nextPointScreenX;
            }
            return newScreenX;
        }

        @Override
        protected float getNewScreenX(final float playerScreenX, final float nextPointScreenX, final float velocity)
        {
            return calcNewScreenX(playerScreenX, nextPointScreenX, velocity);
        }

        @Override
        protected float getNewScreenY(final float playerScreenY, final float nextPointScreenY, final float velocity)
        {
            return playerScreenY;
        }
    }

    private static class UpDirectionHandler extends DirectionHandler
    {
        public static float calcNewScreenY(final float playerScreenY, final float nextPointScreenY, final float velocity)
        {
            float newScreenY = playerScreenY + velocity;
            if (newScreenY >= nextPointScreenY)
            {
                newScreenY = nextPointScreenY;
            }
            return newScreenY;
        }

        @Override
        protected float getNewScreenX(final float playerScreenX, final float nextPointScreenX, final float velocity)
        {
            return playerScreenX;
        }

        @Override
        protected float getNewScreenY(final float playerScreenY, final float nextPointScreenY, final float velocity)
        {
            return calcNewScreenY(playerScreenY, nextPointScreenY, velocity);
        }
    }

    private static class DownDirectionHandler extends DirectionHandler
    {
        public static float calcNewScreenY(final float playerScreenY, final float nextPointScreenY, final float velocity)
        {
            float newScreenY = playerScreenY - velocity;
            if (newScreenY <= nextPointScreenY)
            {
                newScreenY = nextPointScreenY;
            }
            return newScreenY;
        }

        @Override
        protected float getNewScreenX(final float playerScreenX, final float nextPointScreenX, final float velocity)
        {
            return playerScreenX;
        }

        @Override
        protected float getNewScreenY(final float playerScreenY, final float nextPointScreenY, final float velocity)
        {
            return calcNewScreenY(playerScreenY, nextPointScreenY, velocity);
        }
    }

    private static class DownLeftDirectionHandler extends DirectionHandler
    {
        @Override
        protected float getNewScreenX(float playerScreenX, float nextPointScreenX, float velocity)
        {
            return LeftDirectionHandler.calcNewScreenX(playerScreenX, nextPointScreenX, velocity);
        }

        @Override
        protected float getNewScreenY(float playerScreenY, float nextPointScreenY, float velocity)
        {
            return DownDirectionHandler.calcNewScreenY(playerScreenY, nextPointScreenY, velocity);
        }
    }

    private static class DownRightDirectionHandler extends DirectionHandler
    {
        @Override
        protected float getNewScreenX(float playerScreenX, float nextPointScreenX, float velocity)
        {
            return RightDirectionHandler.calcNewScreenX(playerScreenX, nextPointScreenX, velocity);
        }

        @Override
        protected float getNewScreenY(float playerScreenY, float nextPointScreenY, float velocity)
        {
            return DownDirectionHandler.calcNewScreenY(playerScreenY, nextPointScreenY, velocity);
        }
    }

    private static class UpLeftDirectionHandler extends DirectionHandler
    {
        @Override
        protected float getNewScreenX(float playerScreenX, float nextPointScreenX, float velocity)
        {
            return LeftDirectionHandler.calcNewScreenX(playerScreenX, nextPointScreenX, velocity);
        }

        @Override
        protected float getNewScreenY(float playerScreenY, float nextPointScreenY, float velocity)
        {
            return UpDirectionHandler.calcNewScreenY(playerScreenY, nextPointScreenY, velocity);
        }
    }

    private static class UpRightDirectionHandler extends DirectionHandler
    {
        @Override
        protected float getNewScreenX(float playerScreenX, float nextPointScreenX, float velocity)
        {
            return RightDirectionHandler.calcNewScreenX(playerScreenX, nextPointScreenX, velocity);
        }

        @Override
        protected float getNewScreenY(float playerScreenY, float nextPointScreenY, float velocity)
        {
            return UpDirectionHandler.calcNewScreenY(playerScreenY, nextPointScreenY, velocity);
        }
    }

}
