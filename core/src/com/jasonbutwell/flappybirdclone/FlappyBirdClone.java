package com.jasonbutwell.flappybirdclone;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappyBirdClone extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background, bird;
	Texture birds[];
	int flapState = 0;

	float dt;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		batch = new SpriteBatch();
		background = new Texture("bg.png");

		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		bird = new Texture("bird.png");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		dt += Gdx.graphics.getDeltaTime();

		if ( dt >= 0.1 ) {
			dt = 0;
			if (flapState == 0)
				flapState = 1;
			else
				flapState = 0;
		}

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		batch.draw(birds[flapState], (Gdx.graphics.getWidth()-bird.getWidth())/2, (Gdx.graphics.getHeight()-bird.getHeight())/2);
		batch.end();
	}
}
