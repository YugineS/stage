package com.yug.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.yug.core.Constants;

public class GameRenderer
{
    private final SpriteBatch batch;
    private final GameWorld gameWorld;
    private final OrthographicCamera cam;
    private OrthogonalTiledMapRenderer tmRenderer;

    public GameRenderer(final GameWorld gameWorld)
    {
        this.gameWorld = gameWorld;
        batch = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        cam.position.y=gameWorld.getRowsAmt()*gameWorld.getTileHeight()/2;
        cam.position.x=gameWorld.getCollsAmt()*gameWorld.getTileWidth()/2;
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
        batch.draw(gameWorld.getPlayer().getTexture(), gameWorld.getPlayer().getX(), gameWorld.getPlayer().getY());
        batch.end();
    }

    public OrthographicCamera getCamera()
    {
        return cam;
    }
}
