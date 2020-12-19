package com.armadillo.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class ArmadilloMain extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture img;
	private BitmapFont font;

	/**
	 * Is called when the application is created.
	 */
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.RED);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		font.draw(batch, "Hello World", 10, 200);
		batch.end();
	}

	/**
	 * Is called when the application is closed.
	 */
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
