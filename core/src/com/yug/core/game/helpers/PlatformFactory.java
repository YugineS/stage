package com.yug.core.game.helpers;

import com.badlogic.gdx.maps.MapProperties;
import com.yug.core.game.GameWorld;
import com.yug.core.game.model.Platform;

/**
 * Created by yugine on 12.2.15.
 */
public class PlatformFactory extends TileFactory<Platform>
{
    //TODO: add asset helper here to add graphics to platforms
    //TODO: use Pool to obtain platforms
    @Override
    protected Platform createObject(final float width, final float height, final MapProperties properties)
    {
        final Platform.Type platformType = Platform.Type.getByValue(TiledMapUtils.getStringPropery(GameWorld.TM_PLATFORM_TYPE, properties));
        assert (platformType != null);
        final Platform platform = new Platform();
        platform.setType(platformType);
        platform.setWalkable(true);
        platform.setSpeed(width * 5);
        platform.setState(Platform.State.STANDING);
        return platform;
    }
}
