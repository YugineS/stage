package com.yug.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.yug.core.Constants;
import com.yug.core.game.model.KeyTile;
import com.yug.core.game.model.LockedTile;
import com.yug.core.game.model.Platform;
import com.yug.core.game.model.Teleport;
import com.yug.core.game.model.VanishingTile;

public class GameRenderer
{
    private final SpriteBatch batch;
    private final OrthographicCamera cam;
    private OrthogonalTiledMapRenderer tmRenderer;
    private final GameWorld gameWorld = GameWorld.getInstance();

    public GameRenderer()
    {
        batch = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        cam.position.y = gameWorld.getHeight() * gameWorld.getTileHeight() / 2;
        cam.position.x = gameWorld.getWidth() * gameWorld.getTileWidth() / 2;
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        init();
    }

    public void init()
    {
        tmRenderer = new OrthogonalTiledMapRenderer(gameWorld.getTiledMap(), batch);
        tmRenderer.setView(cam);
    }

    public void render()
    {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        tmRenderer.render();

        batch.begin();
        for (final Platform platform : gameWorld.getPlatforms())
        {
            batch.draw(platform.getTexture(), platform.getScreenX(), platform.getScreenY());
        }
        for (final VanishingTile vanishingTile : gameWorld.getVanishingTiles())
        {
            batch.draw(vanishingTile.getTexture(), vanishingTile.getScreenX(), vanishingTile.getScreenY());
        }
        for (final Teleport teleport : gameWorld.getTeleports())
        {
            batch.draw(teleport.getTexture(), teleport.getScreenX(), teleport.getScreenY());
        }
        for (final KeyTile keyTile : gameWorld.getKeys())
        {
            final Texture keyTileTexture = keyTile.getTexture();
            if (keyTileTexture != null)
            {
                batch.draw(keyTileTexture, keyTile.getScreenX(), keyTile.getScreenY());
            }
        }
        for (final LockedTile lockedTile : gameWorld.getLocks())
        {
            final Texture lockedTileTexture = lockedTile.getTexture();
            if (lockedTileTexture != null)
            {
                batch.draw(lockedTileTexture, lockedTile.getScreenX(), lockedTile.getScreenY());
            }
        }
        batch.draw(gameWorld.getPlayer().getTexture(), gameWorld.getPlayer().getScreenX(), gameWorld.getPlayer().getScreenY());
        batch.end();
    }

    public OrthographicCamera getCamera()
    {
        return cam;
    }
}
