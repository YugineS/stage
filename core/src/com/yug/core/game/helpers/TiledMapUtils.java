package com.yug.core.game.helpers;

import com.badlogic.gdx.maps.MapProperties;

/**
 * Created by yugine on 12.2.15.
 */
public class TiledMapUtils
{
    public static String getStringPropery(final String key, final MapProperties properties)
    {
        return (String)properties.get(key);
    }

    public static boolean getBooleanProperty(final String key, final MapProperties properties)
    {
        return Boolean.TRUE.toString().equalsIgnoreCase(getStringPropery(key, properties));
    }

    public static int getIntProperty(final String key, final MapProperties properties)
    {
        return Integer.parseInt(getStringPropery(key, properties));
    }
}
