package com.yug.core.game.model;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.yug.core.game.GameWorld;

/**
 * Created by yugine on 24.2.15.
 */
public class VanishingTile extends Tile
{
    private State state = State.SOLID;
    private Texture texture = createTestTexture();

    public void update(final float deltaT)
    {
        this.state.getStateHandler().update(this, deltaT);
    }

    public State getState()
    {
        return state;
    }

    public void setState(final State state)
    {
        this.state = state;
        this.state.getStateHandler().enterState(this);
    }

    public enum State
    {
        SOLID(new SolidStateHandler()),
        CRACKED(new CrackedStateHandler()),
        DESTROYING(new DestroyingStateHandler()),
        DESTROYED(new DestroyedStateHandler());
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

    //creating test texture
    private Texture createTestTexture()
    {
        final Pixmap pixmap = createTestPixmap(32, 32);
        return new Texture(pixmap);
    }

    //creating test texture
    private Pixmap createTestPixmap(final int width, final int height)
    {
        final Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        // Fill square with green color at 50% opacity
        pixmap.setColor(0, 1, 0, .5f);
        pixmap.fill();
        // Draw a yellow-colored X shape on square
        pixmap.setColor(1, 1, 0, 1);
        pixmap.drawLine(0, 0, width, height);
        pixmap.drawLine(width, 0, 0, height);
        // Draw a cyan-colored border around square
        pixmap.setColor(0, 1, 1, 1);
        pixmap.drawRectangle(0, 0, width, height);
        return pixmap;
    }

    public Texture getTexture()
    {
        return texture;
    }

    public void setTexture(final Texture texture)
    {
        this.texture = texture;
    }

    private static interface StateHandler
    {
        void enterState(VanishingTile tile);
        void update(VanishingTile tile, float deltaT);
    }

    private static class SolidStateHandler implements StateHandler
    {
        @Override
        public void enterState(final VanishingTile tile)
        {

        }

        @Override
        public void update(final VanishingTile tile, final float deltaT)
        {
            final Player player = GameWorld.getInstance().getPlayer();
            if (tile.getX() == player.getX() && tile.getY() == player.getY())
            {
                tile.setState(State.CRACKED);
            }
        }
    }

    private static class CrackedStateHandler implements StateHandler
    {
        @Override
        public void enterState(final VanishingTile tile)
        {

        }

        @Override
        public void update(final VanishingTile tile, final float deltaT)
        {
            final Player player = GameWorld.getInstance().getPlayer();
            if (tile.getX() != player.getX() || tile.getY() != player.getY())
            {
                tile.setState(State.DESTROYING);
            }
        }
    }

    private static class DestroyingStateHandler implements StateHandler
    {
        @Override
        public void enterState(final VanishingTile tile)
        {
            tile.getNavigationMap().setPoint(null, tile.getX(), tile.getY());
            //set destroying animation timer = 0
        }

        @Override
        public void update(final VanishingTile tile, final float deltaT)
        {
            //play destroying animation
            //if animation finished
            tile.setState(State.DESTROYED);
        }
    }

    private static class DestroyedStateHandler implements StateHandler
    {
        @Override
        public void enterState(VanishingTile tile)
        {

        }

        @Override
        public void update(VanishingTile tile, float deltaT)
        {

        }
    }
}
