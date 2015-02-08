package com.yug.core.game.model;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.yug.core.game.GameWorld;
import com.yug.pf.NavigationPoint;
import com.yug.pf.PathFinder;

import java.util.LinkedList;

/**
 * Created by yugine on 22.1.15.
 */
public class Player extends MovableTile
{
    private enum Direction
    {
        LEFT, RIGHT, UP, DOWN, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
    }

    private final float DEFAULT_SPEED = 96;
    private final GameWorld gameWorld;
    private Texture texture;
    private PathFinder pathFinder;
    private LinkedList<Tile> path;
    private Tile nextNavigationPoint;
    private boolean moving;
    private Direction direction;

    public Player(final GameWorld gameWorld)
    {
        this.gameWorld = gameWorld;
        setSpeed(DEFAULT_SPEED);
        pathFinder = new PathFinder();
        texture = createTestTexture();

    }

    @Override
    public void update(float deltaT)
    {
        if (moving)
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
                    moving = false;
                }
            }
            else
            {
                updatePosition(deltaT);
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
        else if(deltaX>0 && deltaY ==0)
        {
            direction = Direction.RIGHT;
        }
        else if(deltaX == 0 && deltaY <0)
        {
            direction = Direction.DOWN;
        }
        else if(deltaX == 0 && deltaY>0)
        {
            direction = Direction.UP;
        }
        else if(deltaX<0 && deltaY<0)
        {
            direction = Direction.DOWN_LEFT;
        }
        else if(deltaX<0 && deltaY>0)
        {
            direction = Direction.UP_LEFT;
        }
        else if(deltaX>0 && deltaY<0)
        {
            direction = Direction.DOWN_RIGHT;
        }
        else if(deltaX>0 && deltaY>0)
        {
            direction = Direction.UP_RIGHT;
        }
    }

    private void updatePosition(float deltaT)
    {
        if (nextNavigationPoint == null)
        {
            return;
        }
        final int nextX = nextNavigationPoint.getX();
        final int nextY = nextNavigationPoint.getY();
        final float nextPointScreenX = nextX * gameWorld.getTileWidth();
        final float nextPointScreenY = nextY * gameWorld.getTileHeight();
        setX(nextX);
        setY(nextY);
        float newScreenX = getScreenX();
        float newScreenY = getScreenY();
        if (Direction.LEFT.equals(direction))
        {
            //moving left
            newScreenX = getScreenX() - getSpeed() * deltaT;
            if (newScreenX <= nextPointScreenX)
            {
                newScreenX = nextPointScreenX;
            }
        }
        else if(Direction.RIGHT.equals(direction))
        {
            //moving right
            newScreenX = getScreenX() + getSpeed() * deltaT;
            if (newScreenX >= nextPointScreenX)
            {
                newScreenX = nextPointScreenX;
            }
        }
        else if(Direction.DOWN.equals(direction))
        {
            //moving down
            newScreenY = getScreenY() - getSpeed()*deltaT;
            if(newScreenY <=nextPointScreenY)
            {
                newScreenY = nextPointScreenY;
            }
        }
        else if(Direction.UP.equals(direction))
        {
            //moving up
            newScreenY = getScreenY() + getSpeed()*deltaT;
            if(newScreenY >=nextPointScreenY)
            {
                newScreenY = nextPointScreenY;
            }
        }
        else if(Direction.DOWN_LEFT.equals(direction))
        {
            //moving down left
            newScreenX = getScreenX() - getSpeed() * deltaT;
            newScreenY = getScreenY() - getSpeed()*deltaT;
            if (newScreenX <= nextPointScreenX)
            {
                newScreenX = nextPointScreenX;
            }
            if(newScreenY <=nextPointScreenY)
            {
                newScreenY = nextPointScreenY;
            }
        }
        else if(Direction.UP_LEFT.equals(direction))
        {
            //moving up left
            newScreenX = getScreenX() - getSpeed() * deltaT;
            newScreenY = getScreenY() + getSpeed()*deltaT;
            if (newScreenX <= nextPointScreenX)
            {
                newScreenX = nextPointScreenX;
            }
            if(newScreenY >=nextPointScreenY)
            {
                newScreenY = nextPointScreenY;
            }
        }
        else if(Direction.DOWN_RIGHT.equals(direction))
        {
            //moving down right
            newScreenX = getScreenX() + getSpeed() * deltaT;
            newScreenY = getScreenY() - getSpeed()*deltaT;
            if (newScreenX >= nextPointScreenX)
            {
                newScreenX = nextPointScreenX;
            }
            if(newScreenY <=nextPointScreenY)
            {
                newScreenY = nextPointScreenY;
            }
        }
        else if(Direction.UP_RIGHT.equals(direction))
        {
            //moving up right
            newScreenX = getScreenX() + getSpeed() * deltaT;
            newScreenY = getScreenY() + getSpeed()*deltaT;
            if (newScreenX >= nextPointScreenX)
            {
                newScreenX = nextPointScreenX;
            }
            if(newScreenY >=nextPointScreenY)
            {
                newScreenY = nextPointScreenY;
            }
        }
        if (newScreenX == nextPointScreenX && newScreenY == nextPointScreenY)
        {
            nextNavigationPoint = null;
        }
        setScreenX(newScreenX);
        setScreenY(newScreenY);
    }

    public void goTo(final int destX, final int destY)
    {
        if(getX()!=destX || getY()!=destY)
        {
            path = pathFinder.calculatePath(getX(), getY(), destX, destY, gameWorld.getNavigationMap());

            if (moving && path != null)
            {
                //force updating the nextNavigationPoint
                //nextNavigationPoint = path.pop();
                //updateDirection();
            }
            else if (!moving && path != null)
            {
                moving = true;
            }
        }
    }

    public boolean isMoving()
    {
        return moving;
    }

    public void setMoving(final boolean moving)
    {
        this.moving = moving;
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

}
