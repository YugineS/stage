package com.yug.core.game.handlers;

/**
 * Created by yugine on 3.2.15.
 */
public interface GameInputHandler
{
    void onTap(final float x, final float y);
    void onFling(final float startX, final float startY, final float velocityX, final float velocityY);
}
