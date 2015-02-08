package com.yug.core.game;

import com.yug.core.game.handlers.GameInputHandler;
import com.yug.core.game.model.Player;

/**
 * Created by yugine on 15.1.15.
 */
public class GameController implements GameInputHandler
{
    private final GameWorld gameWorld;

    public GameController(final GameWorld gameWorld)
    {
        this.gameWorld = gameWorld;
    }

    /**
     * Updates game state.
     *
     * @param deltaT time delta between lifecycle iterations
     */
    public void update(float deltaT)
    {
        final Player player = gameWorld.getPlayer();
        player.update(deltaT);
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
}
