package com.yug.core.game.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by yugine on 3.2.15.
 */
public class GameGestureListener implements GestureDetector.GestureListener
{
    private final GameInputHandler gameInputHandler;
    private final OrthographicCamera cam;
    private final Vector3 tapCoordinates = new Vector3();

    public GameGestureListener(final GameInputHandler gameInputHandler, final OrthographicCamera cam)
    {
        this.gameInputHandler = gameInputHandler;
        this.cam = cam;
    }

    @Override
    public boolean touchDown(float v, float v1, int i, int i1)
    {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button)
    {
        tapCoordinates.x = x;
        tapCoordinates.y = y;
        tapCoordinates.z = 0;
        final Vector3 coord = cam.unproject(tapCoordinates);
        gameInputHandler.onTap(coord.x, coord.y);
        return true;
    }

    @Override
    public boolean longPress(float v, float v1)
    {
        return false;
    }

    @Override
    public boolean fling(float v, float v1, int i)
    {
        return false;
    }

    @Override
    public boolean pan(float v, float v1, float v2, float v3)
    {
        return false;
    }

    @Override
    public boolean panStop(float v, float v1, int i, int i1)
    {
        return false;
    }

    @Override
    public boolean zoom(float v, float v1)
    {
        return false;
    }

    @Override
    public boolean pinch(Vector2 vector2, Vector2 vector21, Vector2 vector22, Vector2 vector23)
    {
        return false;
    }
}
