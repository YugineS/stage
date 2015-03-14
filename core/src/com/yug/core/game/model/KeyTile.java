package com.yug.core.game.model;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.yug.core.game.GameWorld;

import java.util.List;

/**
 * Created by yugine on 14.3.15.
 */
public class KeyTile extends Tile
{
    private int id;
    private State state = State.ON_MAP;
    private Texture texture = createTestTexture();

    public void update(final float deltaT)
    {
        state.getStateHandler().update(this, deltaT);
    }

    public enum State
    {
        ON_MAP(new OnMapStateHandler()), PICKED_UP(new PickedUpStateHandler());
        private StateHandler stateHandler;

        private State(final StateHandler stateHandler)
        {
            this.stateHandler = stateHandler;
        }

        public StateHandler getStateHandler()
        {
            return stateHandler;
        }
    }

    public int getId()
    {
        return id;
    }

    public void setId(final int id)
    {
        this.id = id;
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

    private static interface StateHandler
    {
        void enterState(KeyTile tile);

        void update(KeyTile tile, float deltaT);
    }

    private static class OnMapStateHandler implements StateHandler
    {
        @Override
        public void enterState(final KeyTile tile)
        {

        }

        @Override
        public void update(final KeyTile tile, final float deltaT)
        {
            final Player player = GameWorld.getInstance().getPlayer();
            if (player.getX() == tile.getX() && player.getY() == tile.getY())
            {
                tile.setState(State.PICKED_UP);
            }
        }
    }

    private static class PickedUpStateHandler implements StateHandler
    {
        @Override
        public void enterState(final KeyTile tile)
        {
            final List<LockedTile> lockedTiles = GameWorld.getInstance().getLocks();
            for (final LockedTile lockedTile : lockedTiles)
            {
                if (lockedTile.getKeyId() == tile.getId() && LockedTile.State.LOCKED == lockedTile.getState())
                {
                    lockedTile.setState(LockedTile.State.OPENED);
                }
            }
        }

        @Override
        public void update(final KeyTile tile, final float deltaT)
        {

        }
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
        pixmap.setColor(0.0f, 0.8f, 0.2f, .5f);
        pixmap.fill();
        // Draw a yellow-colored X shape on square
        pixmap.setColor(1, 1, 0, 1);
        pixmap.drawLine(0, 0, width, height);
        // Draw a cyan-colored border around square
        pixmap.setColor(0, 1, 1, 1);
        pixmap.drawRectangle(0, 0, width, height);
        return pixmap;
    }

    public Texture getTexture()
    {
        if (State.PICKED_UP == state)
        {
            return null;
        }
        return texture;
    }
}
