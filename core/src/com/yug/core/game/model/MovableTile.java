package com.yug.core.game.model;

/**
 * Created by yugine on 19.1.15.
 */
public abstract class MovableTile extends Cell
{
    private float x;
    private float y;
    private int width;
    private int height;
    private float speed;

    public abstract void update(final float deltaT);

    public float getX()
    {
        return x;
    }

    public void setX(final float x)
    {
        this.x = x;
    }

    public float getY()
    {
        return y;
    }

    public void setY(final float y)
    {
        this.y = y;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(final int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(final int height)
    {
        this.height = height;
    }

    public float getSpeed()
    {
        return speed;
    }

    public void setSpeed(final float speed)
    {
        this.speed = speed;
    }
}
