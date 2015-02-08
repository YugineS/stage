package com.yug.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.input.GestureDetector;
import com.yug.core.Constants;
import com.yug.core.game.handlers.GameGestureListener;

/**
 * Created by yugine on 15.1.15.
 */
public class GameScreen implements Screen
{


    private final GameWorld gameWorld;
    private final GameController controller;
    private final GameRenderer renderer;
    private final GameGestureListener gameGestureListener;

    public GameScreen()
    {
        gameWorld = new GameWorld();
        gameWorld.loadLevel(LevelName.LEVEL_TEST);
        controller = new GameController(gameWorld);
        renderer = new GameRenderer(gameWorld);
        gameGestureListener = new GameGestureListener(controller, renderer.getCamera());

//        texture = new Texture(Gdx.files.internal("libgdx-logo.png"));
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(new GestureDetector(gameGestureListener));
    }

    @Override
    public void render(final float deltaT)
    {
        Gdx.graphics.setTitle( "ST_Age -- FPS: " + Gdx.graphics.getFramesPerSecond());
        controller.update(deltaT);
        renderer.render();

    }

    @Override
    public void resize(final int width, final int height)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {

    }
}
