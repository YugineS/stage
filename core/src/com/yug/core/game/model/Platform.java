package com.yug.core.game.model;

import com.yug.core.game.GameWorld;
import com.yug.pf.NavigationMap;

public class Platform extends MovableTile
{
    private State state = State.STAYING;
    private Type type = Type.PLATFORM;
    private final GameWorld gameWorld;

    public Platform(final GameWorld gameWorld)
    {
        this.gameWorld = gameWorld;
        setSpeed(gameWorld.getTileWidth() * 4);
    }

    public void moveLeft()
    {
        if (getLeftX() > -1 && (Type.PLATFORM.equals(type) || Type.HORIZONTAL_PLATFORM.equals(type) || Type.LEFT_PLATFORM.equals(type)))
        {
            if (State.STAYING.equals(state) || State.MOVING_RIGHT.equals(state))
            {
                if (State.STAYING.equals(state))
                {
                    //updating navigation map. The platform moved from original place.
                    gameWorld.getNavigationMap().setPoint(null, getX(), getY());
                }
                state = State.MOVING_LEFT;
            }
        }
    }

    public void moveRight()
    {
        if (getRightX() > -1 && (Type.PLATFORM.equals(type) || Type.HORIZONTAL_PLATFORM.equals(type) || Type.RIGHT_PLATFORM.equals(type)))
        {
            if (State.STAYING.equals(state) || State.MOVING_LEFT.equals(state))
            {
                if (State.STAYING.equals(state))
                {
                    //updating navigation map. The platform moved from original place.
                    gameWorld.getNavigationMap().setPoint(null, getX(), getY());
                }
                state = State.MOVING_RIGHT;
            }
        }
    }

    public void moveUp()
    {
        if (getTopY() > -1 && (Type.PLATFORM.equals(type) || Type.VERTICAL_PLATFORM.equals(type) || Type.UP_PLATFORM.equals(type)))
        {
            if (State.STAYING.equals(state) || State.MOVING_DOWN.equals(state))
            {
                if (State.STAYING.equals(state))
                {
                    //updating navigation map. The platform moved from original place.
                    gameWorld.getNavigationMap().setPoint(null, getX(), getY());
                }
                state = State.MOVING_UP;
            }
        }
    }

    public void moveDown()
    {
        if (getTopY() > -1 && (Type.PLATFORM.equals(type) || Type.VERTICAL_PLATFORM.equals(type) || Type.DOWN_PLATFORM.equals(type)))
        {
            if (State.STAYING.equals(state) || State.MOVING_UP.equals(state))
            {
                if (State.STAYING.equals(state))
                {
                    //updating navigation map. The platform moved from original place.
                    gameWorld.getNavigationMap().setPoint(null, getX(), getY());
                }
                state = State.MOVING_DOWN;
            }
        }
    }

    @Override
    public void update(float deltaT)
    {
        final NavigationMap<Tile> navigationMap = gameWorld.getNavigationMap();
        final int leftX = getLeftX();
        final int rightX = getRightX();
        final int topY = getTopY();
        final int bottomY = getBottomY();

        if (State.MOVING_LEFT.equals(state) && leftX > -1)
        {
            float newScreenX = getScreenX() - getSpeed() * deltaT;
            final float leftScreenX = leftX * gameWorld.getTileWidth();
            if (newScreenX <= leftScreenX)
            {
                newScreenX = leftScreenX;
                setX(leftX);
                if (gameWorld.getNavigationMap().getLeftOf(getX(), getY()) != null)
                {
                    arrive();
                }
            }
            setScreenX(newScreenX);
        }
        else if (State.MOVING_RIGHT.equals(state) && rightX > -1)
        {
            float newScreenX = getScreenX() + getSpeed() * deltaT;
            final float rightScreenX = rightX * gameWorld.getTileWidth();
            if (newScreenX >= rightScreenX)
            {
                newScreenX = rightScreenX;
                setX(rightX);
                if (gameWorld.getNavigationMap().getRightOf(getX(), getY()) != null)
                {
                    arrive();
                }
            }
            setScreenX(newScreenX);
        }
        else if (State.MOVING_UP.equals(state) && topY > -1)
        {
            float newScreenY = getScreenY() + getSpeed() * deltaT;
            final float topScreenY = topY * gameWorld.getTileHeight();
            if (newScreenY >= topScreenY)
            {
                newScreenY = topScreenY;
                setY(topY);
                if (gameWorld.getNavigationMap().getTopOf(getX(), getY()) != null)
                {
                    arrive();
                }
            }
            setScreenY(newScreenY);
        }
        else if (State.MOVING_DOWN.equals(state) && bottomY > -1)
        {
            float newScreenY = getScreenY() - getSpeed() * deltaT;
            final float bottomScreenY = bottomY * gameWorld.getTileHeight();
            if (newScreenY <= bottomScreenY)
            {
                newScreenY = bottomScreenY;
                setY(bottomY);
                if (gameWorld.getNavigationMap().getBottomOf(getX(), getY()) != null)
                {
                    arrive();
                }
            }
            setScreenY(newScreenY);
        }
    }

    private void arrive()
    {
        state = State.STAYING;
        gameWorld.getNavigationMap().setPoint(this, getX(), getY());
    }

    private int getLeftX()
    {
        final int leftX = getX() - 1;
        return leftX > -1 && gameWorld.getNavigationMap().getLeftOf(getX(), getY()) == null ? leftX : -1;
    }

    private int getRightX()
    {
        final int rightX = getX() + 1;
        return rightX < gameWorld.getWidth() && gameWorld.getNavigationMap().getRightOf(getX(), getY()) == null ? rightX : -1;
    }

    private int getTopY()
    {
        final int topY = getY() + 1;
        return topY < gameWorld.getHeight() && gameWorld.getNavigationMap().getTopOf(getX(), getY()) == null ? topY : -1;
    }

    private int getBottomY()
    {
        final int bottomY = getY() - 1;
        return bottomY > -1 && gameWorld.getNavigationMap().getBottomOf(getX(), getY()) == null ? bottomY : -1;
    }

    public Type getType()
    {
        return type;
    }

    public void setType(final Type type)
    {
        this.type = type;
    }

    public GameWorld getGameWorld()
    {
        return gameWorld;
    }

    public enum State
    {
        MOVING_LEFT, MOVING_RIGHT, MOVING_UP, MOVING_DOWN, STAYING
    }

    public enum Type
    {
        PLATFORM, VERTICAL_PLATFORM, HORIZONTAL_PLATFORM, UP_PLATFORM, DOWN_PLATFORM, LEFT_PLATFORM, RIGHT_PLATFORM
    }
}
