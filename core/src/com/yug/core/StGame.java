package com.yug.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yug.core.game.GameScreen;

public class StGame extends Game {

	private GameScreen gameScreen;

	@Override
	public void create () {
		gameScreen = new GameScreen();
		setScreen(gameScreen);
	}

}
