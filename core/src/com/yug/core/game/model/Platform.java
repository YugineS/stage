package com.yug.core.game.model;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.yug.core.game.GameWorld;

public class Platform extends MovableTile
{
    private State state = State.STANDING;
    private Type type = Type.PLATFORM;
    private Texture testTexture = createTestTexture();
    private Player player;

    public void onFling(final FlingDirection flingDirection)
    {
        this.state.getHandler().handleInput(flingDirection, this);
    }

    @Override
    public void update(float deltaT)
    {
        state.getHandler().update(deltaT, this);
        if (this.player != null)
        {
            this.player.setScreenX(getScreenX());
            this.player.setScreenY(getScreenY());
            this.player.setX(getX());
            this.player.setY(getY());
            if (Player.State.MOVING_ON_PLATFORM != player.getState())
            {
                this.player = null;
            }
        }
    }

    private void onArrive()
    {
        if (this.player != null)
        {
            this.player.setState(Player.State.STANDING);
        }

        getNavigationMap().setPoint(this, getX(), getY());
    }

    private void onStartMoving()
    {
        if (this.player != null)
        {
            this.player.setState(Player.State.MOVING_ON_PLATFORM);
        }
        //updating navigation map. The platform moved from original place.
        getNavigationMap().setPoint(null, getX(), getY());
    }

    public Type getType()
    {
        return type;
    }

    public void setType(final Type type)
    {
        this.type = type;
    }

    public State getState()
    {
        return state;
    }

    public void setState(final State state)
    {
        this.state = state;
        this.state.getHandler().enterState(this);
    }

    public Player getPlayer()
    {
        return player;
    }

    public void setPlayer(final Player player)
    {
        this.player = player;
    }

    public enum State
    {
        MOVING_LEFT(new MovingLeftStateHandler()),
        MOVING_RIGHT(new MovingRightStateHandler()),
        MOVING_UP(new MovingUpStateHandler()),
        MOVING_DOWN(new MovingDownStateHandler()),
        STANDING(new StandingStateHandler());
        private final StateHandler handler;

        private State(final StateHandler handler)
        {
            this.handler = handler;
        }

        public StateHandler getHandler()
        {
            return handler;
        }
    }

    public enum Type
    {
        PLATFORM("platform"),
        VERTICAL_PLATFORM("verticalPlatform"),
        HORIZONTAL_PLATFORM("horizontalPlatform"),
        UP_PLATFORM("upPlatform"),
        DOWN_PLATFORM("downPlatform"),
        LEFT_PLATFORM("leftPlatform"),
        RIGHT_PLATFORM("rightPlatform");

        private final String value;

        private Type(final String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return this.value;
        }

        public static Type getByValue(final String value)
        {
            Type result = null;
            for (final Type type : Type.values())
            {
                if (type.getValue().equals(value))
                {
                    result = type;
                    break;
                }
            }
            return result;
        }
    }

    public enum FlingDirection
    {
        LEFT, RIGHT, UP, DOWN
    }


    public Texture getTexture()
    {
        return testTexture;
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
        pixmap.setColor(0, 0, 1, .5f);
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

    private static interface StateHandler
    {
        void handleInput(final FlingDirection flingDirection, final Platform platform);

        void update(final float deltaT, final Platform platform);

        void enterState(final Platform platform);
    }

    private static class MovingLeftStateHandler implements StateHandler
    {
        @Override
        public void handleInput(final FlingDirection flingDirection, final Platform platform)
        {
            if (Type.HORIZONTAL_PLATFORM == platform.getType() && FlingDirection.RIGHT == flingDirection)
            {
                platform.setState(State.MOVING_RIGHT);
            }
        }

        @Override
        public void enterState(final Platform platform)
        {
            if (canMoveLeft(platform.getX(), platform.getY(), platform.getNavigationMap()))
            {
                platform.onStartMoving();
            }
        }

        @Override
        public void update(float deltaT, final Platform platform)
        {
            final NavigationMapWrapper navigationMap = platform.getNavigationMap();
            final int platformX = platform.getX();
            final int platformY = platform.getY();
            if (canMoveLeft(platformX, platformY, navigationMap))
            {
                final int leftX = platformX - 1;
                float newScreenX = platform.getScreenX() - platform.getSpeed() * deltaT;
                final float leftScreenX = leftX * platform.getWidth();
                if (newScreenX <= leftScreenX)
                {
                    newScreenX = leftScreenX;
                    platform.setX(leftX);
                    if (!canMoveLeft(leftX, platformY, navigationMap))
                    {
                        platform.onArrive();
                        platform.setState(State.STANDING);
                    }
                }
                platform.setScreenX(newScreenX);
            }
            else
            {
                platform.setState(State.STANDING);
            }
        }

        private boolean canMoveLeft(final int x, final int y, final NavigationMapWrapper navigationMap)
        {
            final int leftX = x - 1;
            return navigationMap.isInBounds(leftX, y) && navigationMap.getPoint(leftX, y) == null;
        }
    }

    private static class MovingRightStateHandler implements StateHandler
    {
        @Override
        public void handleInput(final FlingDirection flingDirection, final Platform platform)
        {
            if (Type.HORIZONTAL_PLATFORM == platform.getType() && FlingDirection.LEFT == flingDirection)
            {
                platform.setState(State.MOVING_LEFT);
            }
        }

        @Override
        public void update(final float deltaT, final Platform platform)
        {
            final NavigationMapWrapper navigationMap = platform.getNavigationMap();
            final int platformX = platform.getX();
            final int platformY = platform.getY();
            if (canMoveRight(platformX, platformY, navigationMap))
            {
                final int rightX = platformX + 1;
                float newScreenX = platform.getScreenX() + platform.getSpeed() * deltaT;
                final float rightScreenX = rightX * platform.getWidth();
                if (newScreenX >= rightScreenX)
                {
                    newScreenX = rightScreenX;
                    platform.setX(rightX);
                    if (!canMoveRight(rightX, platformY, navigationMap))
                    {
                        platform.onArrive();
                        platform.setState(State.STANDING);
                    }
                }
                platform.setScreenX(newScreenX);
            }
            else
            {
                platform.setState(State.STANDING);
            }
        }

        @Override
        public void enterState(final Platform platform)
        {
            if (canMoveRight(platform.getX(), platform.getY(), platform.getNavigationMap()))
            {
                platform.onStartMoving();
            }
        }

        private boolean canMoveRight(final int x, final int y, final NavigationMapWrapper navigationMap)
        {
            final int rightX = x + 1;
            return navigationMap.isInBounds(rightX, y) && navigationMap.getPoint(rightX, y) == null;
        }
    }

    private static class MovingUpStateHandler implements StateHandler
    {
        @Override
        public void handleInput(final FlingDirection flingDirection, final Platform platform)
        {
            if (Type.VERTICAL_PLATFORM == platform.getType() && FlingDirection.DOWN == flingDirection)
            {
                platform.setState(State.MOVING_DOWN);
            }
        }

        @Override
        public void update(final float deltaT, final Platform platform)
        {
            final NavigationMapWrapper navigationMap = platform.getNavigationMap();
            final int platformX = platform.getX();
            final int platformY = platform.getY();
            if (canMoveUp(platformX, platformY, navigationMap))
            {
                final int topY = platformY + 1;
                float newScreenY = platform.getScreenY() + platform.getSpeed() * deltaT;
                final float topScreenY = topY * platform.getHeight();
                if (newScreenY >= topScreenY)
                {
                    newScreenY = topScreenY;
                    platform.setY(topY);
                    if (!canMoveUp(platformX, topY, navigationMap))
                    {
                        platform.onArrive();
                        platform.setState(State.STANDING);
                    }
                }
                platform.setScreenY(newScreenY);
            }
            else
            {
                platform.setState(State.STANDING);
            }
        }

        @Override
        public void enterState(final Platform platform)
        {
            if (canMoveUp(platform.getX(), platform.getY(), platform.getNavigationMap()))
            {
                platform.onStartMoving();
            }
        }

        private boolean canMoveUp(final int x, final int y, final NavigationMapWrapper navigationMap)
        {
            final int topY = y + 1;
            return navigationMap.isInBounds(x, topY) && navigationMap.getPoint(x, topY) == null;
        }
    }

    private static class MovingDownStateHandler implements StateHandler
    {
        @Override
        public void handleInput(final FlingDirection flingDirection, final Platform platform)
        {
            if (Type.VERTICAL_PLATFORM == platform.getType() && FlingDirection.UP == flingDirection)
            {
                platform.setState(State.MOVING_UP);
            }
        }

        @Override
        public void update(final float deltaT, final Platform platform)
        {
            final NavigationMapWrapper navigationMap = platform.getNavigationMap();
            final int platformX = platform.getX();
            final int platformY = platform.getY();
            if (canMoveDown(platformX, platformY, navigationMap))
            {
                final int bottomY = platformY - 1;
                float newScreenY = platform.getScreenY() - platform.getSpeed() * deltaT;
                final float bottomScreenY = bottomY * platform.getHeight();
                if (newScreenY <= bottomScreenY)
                {
                    newScreenY = bottomScreenY;
                    platform.setY(bottomY);
                    if (!canMoveDown(platformX, bottomY, navigationMap))
                    {
                        platform.onArrive();
                        platform.setState(State.STANDING);
                    }
                }
                platform.setScreenY(newScreenY);
            }
            else
            {
                platform.setState(State.STANDING);
            }
        }

        @Override
        public void enterState(final Platform platform)
        {
            if (canMoveDown(platform.getX(), platform.getY(), platform.getNavigationMap()))
            {
                platform.onStartMoving();
            }
        }

        private boolean canMoveDown(final int x, final int y, final NavigationMapWrapper navigationMap)
        {
            final int bottomY = y - 1;
            return navigationMap.isInBounds(x, bottomY) && navigationMap.getPoint(x, bottomY) == null;
        }
    }

    private static class StandingStateHandler implements StateHandler
    {
        @Override
        public void handleInput(FlingDirection flingDirection, Platform platform)
        {
            final Type platformType = platform.getType();
            State nextState = null;
            if (FlingDirection.LEFT == flingDirection && (Type.PLATFORM == platformType || Type.HORIZONTAL_PLATFORM == platformType || Type.LEFT_PLATFORM == platformType))
            {
                nextState = State.MOVING_LEFT;
            }
            else if (FlingDirection.RIGHT == flingDirection && (Type.PLATFORM == platformType || Type.HORIZONTAL_PLATFORM == platformType || Type.RIGHT_PLATFORM == platformType))
            {
                nextState = State.MOVING_RIGHT;
            }
            else if (FlingDirection.UP == flingDirection && (Type.PLATFORM == platformType || Type.VERTICAL_PLATFORM == platformType || Type.UP_PLATFORM == platformType))
            {
                nextState = State.MOVING_UP;
            }
            else if (FlingDirection.DOWN == flingDirection && (Type.PLATFORM == platformType || Type.VERTICAL_PLATFORM == platformType || Type.DOWN_PLATFORM == platformType))
            {
                nextState = State.MOVING_DOWN;
            }
            final Player player = GameWorld.getInstance().getPlayer();
            final Tile playerNextNavigationPoint = player.getNextNavigationPoint();
            final boolean blockedByPlayer = playerNextNavigationPoint != null && playerNextNavigationPoint.getX() == platform.getX() && playerNextNavigationPoint.getY() == platform.getY();
            if (nextState != null && !blockedByPlayer)
            {
                if (player.getX() == platform.getX() && player.getY() == platform.getY())
                {
                    platform.setPlayer(player);
                }
                else
                {
                    platform.setPlayer(null);
                }
                platform.setState(nextState);
            }
        }

        @Override
        public void update(float deltaT, Platform platform)
        {

        }

        @Override
        public void enterState(Platform platform)
        {

        }
    }

}
