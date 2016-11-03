package com.jasonbutwell.flappybirdclone;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

import static com.badlogic.gdx.math.MathUtils.random;

public class FlappyBirdClone extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture birds[];

	Texture tubeTop, tubeBottom;

	int flapState = 0;

	float birdY = 0;
	float velocity = 0;

	int gameState = 0;
	float gravity = 2;

	float dt;

	float gap = 400;

	float maxTubeOffset;
	float tubeOffset;

	Random ranom;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		batch = new SpriteBatch();
		background = new Texture("bg.png");

		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		tubeTop = new Texture("toptube.png");
		tubeBottom = new Texture("bottomtube.png");

		birdY = (Gdx.graphics.getHeight()-birds[0].getHeight())/2;

		maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;

		ranom = new Random();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (gameState != 0) {

			if (Gdx.input.justTouched()) {
				velocity = -25;
				tubeOffset = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()- gap - 200);
			}

			if ( birdY > 0 || velocity < 0) {
				velocity += gravity;
				birdY -= velocity;
			}
			//System.out.println(dt*10);

		} else {

			if (Gdx.input.justTouched()) {
				//Gdx.app.log("Touched","Yep!");
				gameState = 1;
			}
		}

		dt += Gdx.graphics.getDeltaTime();

		if (dt > 0.1) {
			dt = 0;
			if (flapState == 0)
				flapState = 1;
			else
				flapState = 0;
		}

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		batch.draw(tubeTop, (Gdx.graphics.getWidth()-tubeTop.getWidth())/2, (Gdx.graphics.getHeight()/2)+ (gap /2) + tubeOffset);
		batch.draw(tubeBottom, (Gdx.graphics.getWidth()-tubeBottom.getWidth())/2, Gdx.graphics.getHeight()/2 - gap / 2 - tubeBottom.getHeight() + tubeOffset );

		batch.draw(birds[flapState], (Gdx.graphics.getWidth() - birds[0].getWidth()) / 2, birdY);
		batch.end();
	}
}
