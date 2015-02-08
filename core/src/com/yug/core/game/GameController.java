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
        //final DirectionKey dirKey = inputHandler.getPressedKey();
        final Player player = gameWorld.getPlayer();
        player.update(deltaT);
    }

    @Override
    public void onTap(final float x, final float y)
    {
        int col = (int)(x / gameWorld.getTileWidth());
        int row = (int)(y / gameWorld.getTileWidth());
        if (gameWorld.getRowsAmt()>row && gameWorld.getCollsAmt()>col)
        {
            gameWorld.getPlayer().goTo(col, row);
        }
    }
}
