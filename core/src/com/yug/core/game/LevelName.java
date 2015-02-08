package com.yug.core.game;

/**
 * Created by yugine on 18.1.15.
 */
public enum LevelName
{
    LEVEL_TEST("levels/testLevel.tmx"), LEVEL_1("");
    private String tmxFilePath;

    private LevelName(final String tmxFilePath)
    {
        this.tmxFilePath = tmxFilePath;
    }

    public String getTmxFilePath()
    {
        return tmxFilePath;
    }
}
