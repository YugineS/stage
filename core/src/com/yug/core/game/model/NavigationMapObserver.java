package com.yug.core.game.model;

import com.yug.pf.NavigationMap;

/**
 * Created by yugine on 10.2.15.
 */
public interface NavigationMapObserver
{
    public interface Observable
    {
        void addObserver(final NavigationMapObserver observer);
        void removeObserver(final NavigationMapObserver observer);
        void notifyObservers();
    }
    void onMapUpdate(final NavigationMapWrapper navigationMap);
}
