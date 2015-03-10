package com.yug.core.game.helpers;

import com.badlogic.gdx.maps.MapProperties;
import com.yug.core.game.model.Teleport;

/**
 * Created by yugine on 4.3.15.
 */
public class TeleportFactory extends TileFactory<Teleport>
{
    @Override
    protected Teleport createObject(final float width, final float height, final MapProperties properties)
    {
        final int id = TiledMapUtils.getIntProperty("id", properties);
        final int targetId = TiledMapUtils.getIntProperty("targetId", properties);
        final Teleport result = new Teleport();
        result.setId(id);
        result.setTargetId(targetId);
        result.setState(Teleport.State.WAITING);
        result.setWalkable(true);
        result.setWalkingResistance(500f);
        result.setWalkableCorners(false);
        return result;
    }
}
