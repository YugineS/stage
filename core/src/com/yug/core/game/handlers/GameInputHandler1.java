package com.yug.core.game.handlers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * Created by yugine on 20.1.15.
 */
public class GameInputHandler1 extends InputAdapter
{
    private DirectionKey pressedKey = null;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean keyDown(final int keyCode)
    {
        final DirectionKey key = getDirectionByKeyCode(keyCode);
        if (key != null)
        {
            pressedKey = key;
        }
        return true;
    }

    @Override
    public boolean keyUp(final int keyCode)
    {
        final DirectionKey key = getDirectionByKeyCode(keyCode);
        if (key != null && key.equals(pressedKey))
        {
            pressedKey = null;
        }
        return true;
    }

    private DirectionKey getDirectionByKeyCode(final int keyCode)
    {
        DirectionKey key = null;
        if(Input.Keys.LEFT == keyCode)
        {
            key = DirectionKey.LEFT;
        }
        else if(Input.Keys.RIGHT == keyCode)
        {
            key = DirectionKey.RIGHT;
        }
        else if(Input.Keys.UP == keyCode)
        {
            key = DirectionKey.UP;
        }
        else if(Input.Keys.DOWN == keyCode)
        {
            key = DirectionKey.DOWN;
        }
        return key;
    }

    public DirectionKey getPressedKey()
    {
        return pressedKey;
    }
}
