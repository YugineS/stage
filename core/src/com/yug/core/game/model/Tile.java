package com.yug.core.game.model;

import com.yug.pf.NavigationPoint;

/**
 * Created by yugine on 19.1.15.
 */
public class Tile extends NavigationPoint
{
    public Tile(){}

    public Tile(final int x, final int y)
    {
        super(x, y);
    }

    private float screenX;
    private float screenY;
    private float width;
    private float height;
    private NavigationMapWrapper navigationMap;

    public float getWidth()
    {
        return width;
    }

    public void setWidth(final float width)
    {
        this.width = width;
    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(final float height)
    {
        this.height = height;
    }

    public float getScreenX()
    {
        return screenX;
    }

    public void setScreenX(final float screenX)
    {
        this.screenX = screenX;
    }

    public float getScreenY()
    {
        return screenY;
    }

    public void setScreenY(final float screenY)
    {
        this.screenY = screenY;
    }

    public NavigationMapWrapper getNavigationMap()
    {
        return navigationMap;
    }

    public void setNavigationMap(final NavigationMapWrapper navigationMap)
    {
        this.navigationMap = navigationMap;
    }
}
