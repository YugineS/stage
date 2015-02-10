package com.yug.core.game.model;

import com.yug.pf.NavigationMap;
import com.yug.pf.NavigationPoint;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by yugine on 10.2.15.
 */
public class NavigationMapWrapper extends NavigationMap<Tile> implements NavigationMapObserver.Observable
{
    private final List<NavigationMapObserver> observers;

    public NavigationMapWrapper(final Tile[][] map)
    {
        super(map);
        observers = new LinkedList<NavigationMapObserver>();
    }

    @Override
    public void setPoint(final Tile point, final int x, final int y)
    {
        super.setPoint(point, x, y);
        notifyObservers();
    }

    @Override
    public void addObserver(final NavigationMapObserver observer)
    {
        if (observer != null && !observers.contains(observer))
        {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(final NavigationMapObserver observer)
    {
        if (observer != null && observers.contains(observer))
        {
            observers.remove(observer);
        }
    }

    @Override
    public void notifyObservers()
    {
        for (final NavigationMapObserver observer : observers)
        {
            observer.onMapUpdate(this);
        }
    }
}
