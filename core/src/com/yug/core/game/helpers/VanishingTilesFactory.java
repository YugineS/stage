package com.yug.core.game.helpers;

import com.badlogic.gdx.maps.MapProperties;
import com.yug.core.game.GameWorld;
import com.yug.core.game.model.VanishingTile;

/**
 * Created by yugine on 24.2.15.
 */
public class VanishingTilesFactory extends TileFactory<VanishingTile>
{
    @Override
    protected VanishingTile createObject(final float width, final float height, final MapProperties properties)
    {
        final VanishingTile result = new VanishingTile();
        result.setWalkable(true);
        result.setState(VanishingTile.State.SOLID);
        return result;
    }
}
