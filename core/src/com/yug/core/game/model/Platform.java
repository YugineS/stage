package com.yug.core.game.model;

public class Platform extends MovableTile
{
    private PlatformState state;

    @Override
    public void update(float deltaT)
    {
        if (PlatformState.MOVING_LEFT.equals(state))
        {
            
        }
    }

    public enum PlatformState
    {
        MOVING_LEFT, MOVING_RIGHT, MOVING_UP, MOVING_DOWN, STAYING
    }
}
