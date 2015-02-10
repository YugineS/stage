package com.yug.core.game.model;

import com.yug.core.game.GameWorld;
import com.yug.pf.NavigationMap;

public class Platform extends MovableTile
{
    private PlatformState state;
    private final GameWorld gameWorld;

    public Platform(final GameWorld gameWorld)
    {
        this.gameWorld = gameWorld;
        setSpeed(gameWorld.getTileWidth() * 4);
    }

    public void moveLeft()
    {
        if (getLeftX() > -1)
        {
            if (PlatformState.STAYING.equals(state) || PlatformState.MOVING_RIGHT.equals(state))
            {
                if (PlatformState.STAYING.equals(state))
                {
                    //updating navigation map. The platform moved from original place.
                    gameWorld.getNavigationMap().setPoint(null, getX(), getY());
                }
                state = PlatformState.MOVING_LEFT;
            }
        }
    }

    public void moveRight()
    {
        if (getRightX() > -1)
        {
            if (PlatformState.STAYING.equals(state) || PlatformState.MOVING_LEFT.equals(state))
            {
                if (PlatformState.STAYING.equals(state))
                {
                    //updating navigation map. The platform moved from original place.
                    gameWorld.getNavigationMap().setPoint(null, getX(), getY());
                }
                state = PlatformState.MOVING_RIGHT;
            }
        }
    }

    public void moveUp()
    {
        if (getTopY() > -1)
        {
            if (PlatformState.STAYING.equals(state) || PlatformState.MOVING_DOWN.equals(state))
            {
                if (PlatformState.STAYING.equals(state))
                {
                    //updating navigation map. The platform moved from original place.
                    gameWorld.getNavigationMap().setPoint(null, getX(), getY());
                }
                state = PlatformState.MOVING_UP;
            }
        }
    }

    public void moveDown()
    {
        if (getTopY() > -1)
        {
            if (PlatformState.STAYING.equals(state) || PlatformState.MOVING_UP.equals(state))
            {
                if (PlatformState.STAYING.equals(state))
                {
                    //updating navigation map. The platform moved from original place.
                    gameWorld.getNavigationMap().setPoint(null, getX(), getY());
                }
                state = PlatformState.MOVING_DOWN;
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

        if (PlatformState.MOVING_LEFT.equals(state) && leftX > -1)
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
        else if (PlatformState.MOVING_RIGHT.equals(state) && rightX > -1)
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
        else if (PlatformState.MOVING_UP.equals(state) && topY > -1)
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
        else if (PlatformState.MOVING_DOWN.equals(state) && bottomY > -1)
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
        state = PlatformState.STAYING;
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

    public enum PlatformState
    {
        MOVING_LEFT, MOVING_RIGHT, MOVING_UP, MOVING_DOWN, STAYING
    }
}
