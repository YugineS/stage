package com.yug.core.game.helpers;

import com.badlogic.gdx.maps.MapProperties;
import com.yug.core.game.GameWorld;
import com.yug.core.game.model.Tile;

/**
 * Created by yugine on 4.3.15.
 */
public abstract class TileFactory<T extends Tile>
{
    public T create(final float width, final float height, final MapProperties properties)
    {
        final T tile = createObject(width, height, properties);
        final int x = TiledMapUtils.getIntProperty(GameWorld.TM_OBJECT_X, properties);
        final int y = TiledMapUtils.getIntProperty(GameWorld.TM_OBJECT_Y, properties);
        tile.setWidth(width);
        tile.setHeight(height);
        tile.setX(x);
        tile.setY(y);
        tile.setScreenX(x * width);
        tile.setScreenY(y * height);
        return tile;
    }

    protected abstract T createObject(final float width, final float height, final MapProperties properties);
}
