package com.jasonbutwell.flappybirdclone;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

import static com.badlogic.gdx.math.MathUtils.random;

public class FlappyBirdClone extends ApplicationAdapter {

	// Collision detection

	boolean collisionDebug = false;

	Circle birdCircle;

	Rectangle pipeRectangleUp[];
	Rectangle pipeRectangleDown[];

	SpriteBatch batch;

	ShapeRenderer shapeRenderer;

	Texture gameOver;

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
	float tubeOffset[];

	float tubeVelocity = 4;
	float tubeX[];

	int numberOfTubes = 4;
	float distanceBetweenTubes;

	int pointsScore;
	int scoreTube;

	Random ranom;

	BitmapFont font;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		shapeRenderer = new ShapeRenderer();

		batch = new SpriteBatch();
		background = new Texture("bg.png");

		tubeX = new float[numberOfTubes];
		tubeOffset = new float[numberOfTubes];

		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		tubeTop = new Texture("toptube.png");
		tubeBottom = new Texture("bottomtube.png");

		gameOver = new Texture("gameover.png");

		birdY = (Gdx.graphics.getHeight()-birds[0].getHeight())/2;

		maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;

		ranom = new Random();

		distanceBetweenTubes = ( Gdx.graphics.getWidth() * 3 ) / 4;

		pipeRectangleUp = new Rectangle[numberOfTubes];
		pipeRectangleDown = new Rectangle[numberOfTubes];

		for (int i=0; i < numberOfTubes; i++ ) {

			tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()- gap - 200);

			tubeX[i] = (Gdx.graphics.getWidth()-tubeTop.getWidth())/2 + Gdx.graphics.getWidth() + (i*distanceBetweenTubes);

			pipeRectangleUp[i] = new Rectangle();
			pipeRectangleDown[i] = new Rectangle();
		}

		birdCircle = new Circle();

		pointsScore = 0;
		scoreTube = 0;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState == 1) {

			if ( tubeX[scoreTube] < Gdx.graphics.getWidth()/2) {

				pointsScore++;

				Gdx.app.log("score",String.valueOf(pointsScore));

				if ( scoreTube < numberOfTubes ) {
					scoreTube++;
					scoreTube = scoreTube % numberOfTubes;
				}
			}

			if (Gdx.input.justTouched()) {
				velocity = -25;
			}

			for ( int i=0; i< numberOfTubes; i++ ) {

				if ( tubeX[i] < -tubeTop.getWidth() ) {
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()- gap - 200);
				}
				else {
					tubeX[i] -= tubeVelocity;
				}

//				batch.draw(tubeTop, tubeX[i], (Gdx.graphics.getHeight()/2)+ (gap /2) + tubeOffset[i]);
//				batch.draw(tubeBottom, tubeX[i], Gdx.graphics.getHeight()/2 - gap / 2 - tubeBottom.getHeight() + tubeOffset[i] );

				pipeRectangleUp[i] = new Rectangle(tubeX[i],(Gdx.graphics.getHeight()/2) + (gap /2) + tubeOffset[i],tubeTop.getWidth(), tubeTop.getHeight());
				pipeRectangleDown[i] = new Rectangle(tubeX[i],(Gdx.graphics.getHeight()/2) - gap / 2 - tubeBottom.getHeight() + tubeOffset[i],tubeBottom.getWidth(), tubeBottom.getHeight());

			}

			if ( birdY > 0 || velocity < 0) {
				velocity += gravity;
				birdY -= velocity;
			}
			else {
				gameState = 2;
			}
			//System.out.println(dt*10);

		} else if ( gameState == 0) {

			if ( Gdx.input.justTouched() ) {
				//Gdx.app.log("Touched","Yep!");
				gameState = 1;
			}

		} else if (gameState == 2) {

			// Restart the game
			if (Gdx.input.justTouched()) {
				// reset the game state
				pointsScore = 0;
				gameState = 1;
				scoreTube = 0;
				velocity = 0;

				// reset the bird Y position - to centre of screen
				birdY = (Gdx.graphics.getHeight()-birds[0].getHeight())/2;

				// re-initialise the pipes again

				for (int i=0; i < numberOfTubes; i++ ) {

					tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()- gap - 200);

					tubeX[i] = (Gdx.graphics.getWidth()-tubeTop.getWidth())/2 + Gdx.graphics.getWidth() + (i*distanceBetweenTubes);

					pipeRectangleUp[i] = new Rectangle();
					pipeRectangleDown[i] = new Rectangle();
				}
			}
		}

		// Falling when bird has hit pipe
		if ( gameState == 2 && birdY > 0 )
			birdY -= (gravity*10);

		dt += Gdx.graphics.getDeltaTime();

		if (dt > 0.1 && gameState != 2) {
			dt = 0;
			if (flapState == 0)
				flapState = 1;
			else
				flapState = 0;
		}

		for ( int i=0; i< numberOfTubes; i++ ) {
			batch.draw(tubeTop, tubeX[i], (Gdx.graphics.getHeight() / 2) + (gap / 2) + tubeOffset[i]);
			batch.draw(tubeBottom, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - tubeBottom.getHeight() + tubeOffset[i]);
		}

		batch.draw(birds[flapState], (Gdx.graphics.getWidth() - birds[0].getWidth()) / 2, birdY);
		
		if ( gameState == 2)
			batch.draw(gameOver, Gdx.graphics.getWidth()/2-gameOver.getWidth()/2, Gdx.graphics.getHeight()/2-gameOver.getHeight()/2);

		font.draw(batch,String.valueOf(pointsScore),16+16/2,Gdx.graphics.getHeight()-16);

		batch.end();

		birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);

		if ( collisionDebug ) {

			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.setColor(Color.RED);
			shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
		}

		for (int i = 0; i < numberOfTubes; i++ ) {

			if ( collisionDebug ) {
				shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], tubeTop.getWidth(), tubeTop.getHeight());
				shapeRenderer.rect(tubeX[i], (Gdx.graphics.getHeight() / 2) - gap / 2 - tubeBottom.getHeight() + tubeOffset[i], tubeBottom.getWidth(), tubeBottom.getHeight());
			}

			// The actual collision detection

			if (Intersector.overlaps(birdCircle,pipeRectangleUp[i]) || Intersector.overlaps(birdCircle,pipeRectangleDown[i])) {
				// bird has collided with either a top or a bottom pipe
				//Gdx.app.log("Collision","Yes");
				gameState = 2; // game over game state
			}
		}

		if ( collisionDebug )
			shapeRenderer.end();

	}
}
