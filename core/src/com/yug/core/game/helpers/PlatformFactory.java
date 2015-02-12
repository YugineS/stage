package com.yug.core.game.helpers;

import com.badlogic.gdx.maps.MapProperties;
import com.yug.core.game.GameWorld;
import com.yug.core.game.model.Platform;

/**
 * Created by yugine on 12.2.15.
 */
public class PlatformFactory
{
    //TODO: add asset helper here to add graphics to platforms
    //TODO: use Pool to obtain platforms
    public Platform createPlatform(final float width, final float height, final MapProperties properties)
    {
        final Platform.Type platformType = Platform.Type.getByValue(TiledMapUtils.getStringPropery(GameWorld.TM_PLATFORM_TYPE, properties));
        assert (platformType != null);
        final int x = TiledMapUtils.getIntProperty(GameWorld.TM_OBJECT_X, properties);
        final int y = TiledMapUtils.getIntProperty(GameWorld.TM_OBJECT_Y, properties);
        final Platform platform = new Platform();
        platform.setType(platformType);
        platform.setWidth(width);
        platform.setHeight(height);
        platform.setX(x);
        platform.setY(y);
        platform.setScreenX(x * width);
        platform.setScreenY(y * height);
        platform.setWalkable(true);
        platform.setSpeed(width * 5);
        return platform;
    }
}
