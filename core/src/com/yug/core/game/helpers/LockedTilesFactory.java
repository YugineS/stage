package com.yug.core.game.helpers;

import com.badlogic.gdx.maps.MapProperties;
import com.yug.core.game.model.LockedTile;

/**
 * Created by yugine on 14.3.15.
 */
public class LockedTilesFactory extends TileFactory<LockedTile>
{
    private static final String KEY_ID_PROPERTY = "keyId";
    @Override
    protected LockedTile createObject(final float width, final float height, final MapProperties properties)
    {
        final LockedTile result = new LockedTile();
        final int keyId = TiledMapUtils.getIntProperty(KEY_ID_PROPERTY, properties);
        result.setKeyId(keyId);
        result.setState(LockedTile.State.LOCKED);
        result.setWalkable(false);
        return result;
    }
}
