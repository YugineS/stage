package com.yug.core.game.model;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class Platform extends MovableTile
{
    private State state = State.STAYING;
    private Type type = Type.PLATFORM;
    private Texture testTexture;

    public Platform()
    {
        testTexture = createTestTexture();
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
                    getNavigationMap().setPoint(null, getX(), getY());
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
                    getNavigationMap().setPoint(null, getX(), getY());
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
                    getNavigationMap().setPoint(null, getX(), getY());
                }
                state = State.MOVING_UP;
            }
        }
    }

    public void moveDown()
    {
        if (getBottomY() > -1 && (Type.PLATFORM.equals(type) || Type.VERTICAL_PLATFORM.equals(type) || Type.DOWN_PLATFORM.equals(type)))
        {
            if (State.STAYING.equals(state) || State.MOVING_UP.equals(state))
            {
                if (State.STAYING.equals(state))
                {
                    //updating navigation map. The platform moved from original place.
                    getNavigationMap().setPoint(null, getX(), getY());
                }
                state = State.MOVING_DOWN;
            }
        }
    }

    @Override
    public void update(float deltaT)
    {
        final NavigationMapWrapper navigationMap = getNavigationMap();
        final int leftX = getLeftX();
        final int rightX = getRightX();
        final int topY = getTopY();
        final int bottomY = getBottomY();

        if (State.MOVING_LEFT.equals(state) && leftX > -1)
        {
            float newScreenX = getScreenX() - getSpeed() * deltaT;
            final float leftScreenX = leftX * getWidth();
            if (newScreenX <= leftScreenX)
            {
                newScreenX = leftScreenX;
                setX(leftX);
                if (navigationMap.getLeftOf(getX(), getY()) != null)
                {
                    arrive();
                }
            }
            setScreenX(newScreenX);
        }
        else if (State.MOVING_RIGHT.equals(state) && rightX > -1)
        {
            float newScreenX = getScreenX() + getSpeed() * deltaT;
            final float rightScreenX = rightX * getWidth();
            if (newScreenX >= rightScreenX)
            {
                newScreenX = rightScreenX;
                setX(rightX);
                if (navigationMap.getRightOf(getX(), getY()) != null)
                {
                    arrive();
                }
            }
            setScreenX(newScreenX);
        }
        else if (State.MOVING_UP.equals(state) && topY > -1)
        {
            float newScreenY = getScreenY() + getSpeed() * deltaT;
            final float topScreenY = topY * getHeight();
            if (newScreenY >= topScreenY)
            {
                newScreenY = topScreenY;
                setY(topY);
                if (navigationMap.getTopOf(getX(), getY()) != null)
                {
                    arrive();
                }
            }
            setScreenY(newScreenY);
        }
        else if (State.MOVING_DOWN.equals(state) && bottomY > -1)
        {
            float newScreenY = getScreenY() - getSpeed() * deltaT;
            final float bottomScreenY = bottomY * getHeight();
            if (newScreenY <= bottomScreenY)
            {
                newScreenY = bottomScreenY;
                setY(bottomY);
                if (navigationMap.getBottomOf(getX(), getY()) != null)
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
        getNavigationMap().setPoint(this, getX(), getY());
    }

    private int getLeftX()
    {
        final int leftX = getX() - 1;
        return leftX > -1 && getNavigationMap().getLeftOf(getX(), getY()) == null ? leftX : -1;
    }

    private int getRightX()
    {
        final int rightX = getX() + 1;
        final NavigationMapWrapper navigationMap = getNavigationMap();
        return rightX < navigationMap.getMapWidth() && navigationMap.getRightOf(getX(), getY()) == null ? rightX : -1;
    }

    private int getTopY()
    {
        final int topY = getY() + 1;
        final NavigationMapWrapper navigationMap = getNavigationMap();
        return topY < navigationMap.getMapHeight() && navigationMap.getTopOf(getX(), getY()) == null ? topY : -1;
    }

    private int getBottomY()
    {
        final int bottomY = getY() - 1;
        return bottomY > -1 && getNavigationMap().getBottomOf(getX(), getY()) == null ? bottomY : -1;
    }

    public Type getType()
    {
        return type;
    }

    public void setType(final Type type)
    {
        this.type = type;
    }

    public enum State
    {
        MOVING_LEFT, MOVING_RIGHT, MOVING_UP, MOVING_DOWN, STAYING
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

}
