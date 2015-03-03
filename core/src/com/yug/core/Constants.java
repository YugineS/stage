package com.yug.core;

import com.badlogic.gdx.Gdx;

/**
 * Created by yugine on 17.1.15.
 */
public class Constants
{
    public static final float GAME_WIDTH = 960;
    public static final float GAME_HEIGHT = 540;
    public static final float SCREEN_HEIGHT = Gdx.graphics.getHeight();
    public static final float SCREEN_WIDTH = Gdx.graphics.getWidth();
    public static final float VIEWPORT_HEIGHT = GAME_HEIGHT;
    public static final float VIEWPORT_WIDTH = VIEWPORT_HEIGHT * SCREEN_WIDTH/SCREEN_HEIGHT;

}
