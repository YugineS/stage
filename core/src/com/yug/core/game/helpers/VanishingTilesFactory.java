package com.yug.core.game.helpers;

import com.badlogic.gdx.maps.MapProperties;
import com.yug.core.game.GameWorld;
import com.yug.core.game.model.VanishingTile;

/**
 * Created by yugine on 24.2.15.
 */
public class VanishingTilesFactory
{
    public VanishingTile createVanishingTile(final float width, final float height, final MapProperties properties)
    {
        final int x = TiledMapUtils.getIntProperty(GameWorld.TM_OBJECT_X, properties);
        final int y = TiledMapUtils.getIntProperty(GameWorld.TM_OBJECT_Y, properties);
        final VanishingTile result = new VanishingTile();
        result.setX(x);
        result.setY(y);
        result.setScreenX(x * width);
        result.setScreenY(y * height);
        result.setWidth(width);
        result.setHeight(height);
        result.setWalkable(true);
        result.setState(VanishingTile.State.SOLID);
        return result;
    }
}
