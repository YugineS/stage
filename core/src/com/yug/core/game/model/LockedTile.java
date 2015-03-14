package com.yug.core.game.model;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.yug.core.game.GameWorld;

import java.util.List;

/**
 * Created by yugine on 13.3.15.
 */
public class LockedTile extends Tile
{
    private int keyId;
    private State state = State.LOCKED;
    private Texture texture = createTestTexture();

    public void update(final float deltaT)
    {
        state.getStateHandler().update(this, deltaT);
    }

    public State getState()
    {
        return state;
    }

    public void setState(final State state)
    {
        state.getStateHandler().enterState(this);
        this.state = state;
    }

    public int getKeyId()
    {
        return keyId;
    }

    public void setKeyId(final int keyId)
    {
        this.keyId = keyId;
    }

    public enum State
    {
        LOCKED(new LockedStateHandler()),
        OPENED(new OpenedStateHandler()),
        UNLOCKING(new UnlockingStateHandler()),
        UNLOCKED(new UnlockedStateHandler());
        private StateHandler stateHandler;

        private State(final StateHandler stateHandler)
        {
            this.stateHandler = stateHandler;
        }

        public StateHandler getStateHandler()
        {
            return this.stateHandler;
        }
    }

    private static interface StateHandler
    {
        void enterState(LockedTile tile);

        void update(LockedTile tile, float deltaT);
    }

    private static class LockedStateHandler implements StateHandler
    {
        @Override
        public void enterState(final LockedTile tile)
        {
            tile.setWalkable(false);
        }

        @Override
        public void update(final LockedTile tile, final float deltaT)
        {

        }
    }

    private static class OpenedStateHandler implements StateHandler
    {
        @Override
        public void enterState(final LockedTile tile)
        {
            tile.setWalkable(true);
        }

        @Override
        public void update(final LockedTile tile, final float deltaT)
        {
            final Player player = GameWorld.getInstance().getPlayer();
            if (player.getX() == tile.getX() && player.getY() == tile.getY())
            {
                tile.setState(State.UNLOCKING);
            }
        }
    }

    private static class UnlockingStateHandler implements StateHandler
    {
        @Override
        public void enterState(final LockedTile tile)
        {

        }

        @Override
        public void update(final LockedTile tile, final float deltaT)
        {
            //waiting till unlocking animation finishes
            tile.setState(State.UNLOCKED);
        }
    }

    private static class UnlockedStateHandler implements StateHandler
    {
        @Override
        public void enterState(final LockedTile tile)
        {
            final List<LockedTile> lockedTiles = GameWorld.getInstance().getLocks();
            for (final LockedTile lockedTile : lockedTiles)
            {
                if (lockedTile == tile)
                {
                    continue;
                }
                if (lockedTile.getKeyId() == tile.keyId && State.OPENED == lockedTile.getState())
                {
                    lockedTile.setState(State.LOCKED);
                }
            }
        }

        @Override
        public void update(final LockedTile tile, final float deltaT)
        {

        }
    }

    public Texture getTexture()
    {
        if (State.UNLOCKED == state)
        {
            return null;
        }
        return texture;
    }

    public void setTexture(Texture texture)
    {
        this.texture = texture;
    }

    //creating test texture
    private Texture createTestTexture()
    {
        final Pixmap pixmap = createTestPixmap(60, 60);
        return new Texture(pixmap);
    }

    //creating test texture
    private Pixmap createTestPixmap(final int width, final int height)
    {
        final Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        // Fill square with red color at 50% opacity
        pixmap.setColor(0.5f, 0.5f, 0.5f, .5f);
        pixmap.fill();
        // Draw a yellow-colored X shape on square
        pixmap.setColor(1, 1, 0, 1);
        pixmap.drawLine(0, 0, width, height);
        // Draw a cyan-colored border around square
        pixmap.setColor(0, 1, 1, 1);
        pixmap.drawRectangle(0, 0, width, height);
        return pixmap;
    }

}
