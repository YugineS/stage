package com.yug.core.game;

import com.yug.core.game.handlers.GameInputHandler;
import com.yug.core.game.model.Platform;
import com.yug.core.game.model.Player;
import com.yug.core.game.model.Tile;

/**
 * Created by yugine on 15.1.15.
 */
public class GameController implements GameInputHandler
{
    private final GameWorld gameWorld = GameWorld.getInstance();

    /**
     * Updates game state.
     *
     * @param deltaT time delta between lifecycle iterations
     */
    public void update(float deltaT)
    {
        final Player player = gameWorld.getPlayer();
        player.update(deltaT);
        for (final Platform platform : gameWorld.getPlatforms())
        {
            platform.update(deltaT);
        }
    }

    @Override
    public void onTap(final float screenX, final float screenY)
    {
        final int x = (int) (screenX / gameWorld.getTileWidth());
        final int y = (int) (screenY / gameWorld.getTileWidth());
        if (gameWorld.getHeight() > y && gameWorld.getWidth() > x)
        {
            gameWorld.getPlayer().goTo(x, y);
        }
    }

    @Override
    public void onFling(float startX, float startY, float velocityX, float velocityY)
    {
        final int x = (int) (startX / gameWorld.getTileWidth());
        final int y = (int) (startY / gameWorld.getTileWidth());
        final Tile point = gameWorld.getNavigationMap().getPoint(x, y);
        if (point != null && point instanceof Platform)
        {
            final Platform platform = (Platform) point;
            if (Math.abs(velocityX) > Math.abs(velocityY))
            {
                //horizontal flip
                if (velocityX > 0)
                {
                    platform.onFling(Platform.FlingDirection.RIGHT);
                }
                else
                {
                    platform.onFling(Platform.FlingDirection.LEFT);
                }
            }
            else
            {
                //vertical flip
                if (velocityY > 0)
                {
                    platform.onFling(Platform.FlingDirection.DOWN);
                }
                else
                {
                    platform.onFling(Platform.FlingDirection.UP);
                }
            }
        }
    }
}
