package com.yug.core.game.model;

/**
 * Created by yugine on 19.1.15.
 */
public abstract class MovableTile extends Tile
{
    public MovableTile(){}
    public MovableTile(final int x, final int y)
    {
        super(x, y);
    }

    private float speed;

    public abstract void update(final float deltaT);

    public float getSpeed()
    {
        return speed;
    }

    public void setSpeed(final float speed)
    {
        this.speed = speed;
    }
}
