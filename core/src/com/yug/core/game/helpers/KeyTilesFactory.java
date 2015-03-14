package com.yug.core.game.helpers;

import com.badlogic.gdx.maps.MapProperties;
import com.yug.core.game.model.KeyTile;
import com.yug.core.game.model.Tile;

/**
 * Created by yugine on 14.3.15.
 */
public class KeyTilesFactory extends TileFactory<KeyTile>
{
    private static final String ID_PROPERTY = "id";
    @Override
    protected KeyTile createObject(final float width, final float height, final MapProperties properties)
    {
        final KeyTile result = new KeyTile();
        final int id = TiledMapUtils.getIntProperty(ID_PROPERTY, properties);
        result.setId(id);
        result.setState(KeyTile.State.ON_MAP);
        result.setWalkable(true);
        return result;
    }
}
