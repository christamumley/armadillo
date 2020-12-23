package com.armadillo.game.controller;

import com.armadillo.game.model.MyActor;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class ArmadilloController extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	MyActor actor1, actor2;
	Stage stage;

	//have the textures init internally
	Texture img;
	World world;
	Body bodyEdgeScreen;
	OrthographicCamera camera;
	final float PIXELS_TO_METERS = 100f;
	final short PHYSICS_ENTITY = 0x1;    // 0001
	final short WORLD_ENTITY = 0x1 << 1; // 0010 or 0x2 in hex


	@Override
	public void create() {
		Gdx.input.setInputProcessor(this);

		world = new World(new Vector2(0, -9f),true);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();

		stage = new Stage();
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		// Create two identical sprites slightly offset from each other vertically
		actor1 = new MyActor(world, img, 47, 100);
		actor1.setVisible(true);
		actor1.setDebug(true);
		stage.addActor(actor1);

		actor2 = new MyActor(world, img, 300, 600);
		actor2.setVisible(true);
		actor2.setDebug(true);
		stage.addActor(actor2);

		stage.getViewport().setCamera(camera);

		// Now the physics body of the bottom edge of the screen
		BodyDef bodyDef3 = new BodyDef();
		bodyDef3.type = BodyDef.BodyType.StaticBody;
		float w = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
		float h = Gdx.graphics.getHeight()/PIXELS_TO_METERS;
		bodyDef3.position.set(0,0);
		FixtureDef fixtureDef3 = new FixtureDef();
		fixtureDef3.filter.categoryBits = WORLD_ENTITY;
		fixtureDef3.filter.maskBits = PHYSICS_ENTITY;
		EdgeShape edgeShape = new EdgeShape();
		edgeShape.set(0,0,w,0);
		fixtureDef3.shape = edgeShape;
		bodyEdgeScreen = world.createBody(bodyDef3);
		bodyEdgeScreen.createFixture(fixtureDef3);

		// Now the physics body of the bottom edge of the screen
		BodyDef bodyDef4 = new BodyDef();
		bodyDef4.type = BodyDef.BodyType.StaticBody;
		bodyDef4.position.set(0,0);
		FixtureDef fixtureDef4 = new FixtureDef();
		fixtureDef4.filter.categoryBits = WORLD_ENTITY;
		fixtureDef4.filter.maskBits = PHYSICS_ENTITY;
		EdgeShape edgeShape2 = new EdgeShape();
		edgeShape2.set(w,0,w,h);
		fixtureDef4.shape = edgeShape2;
		bodyEdgeScreen = world.createBody(bodyDef4);
		bodyEdgeScreen.createFixture(fixtureDef4);

		edgeShape.dispose();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.
				getHeight());
	}
	@Override
	public void render() {
		camera.update();
		// Step the physics simulation forward at a rate of 60hz
		world.step(1f/60f, 6, 2);

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		stage.draw();
		batch.end();
	}
	@Override
	public void dispose() {
		img.dispose();
		world.dispose();
	}

	/**
	 * Called when a key was pressed
	 *
	 * @param keycode one of the constants in {@link Input.Keys}
	 * @return whether the input was processed
	 */
	@Override
	public boolean keyDown(int keycode) {

		return false;
	}

	/**
	 * Called when a key was released
	 *
	 * @param keycode one of the constants in {@link Input.Keys}
	 * @return whether the input was processed
	 */
	@Override
	public boolean keyUp(int keycode) {
		System.out.print("keyup");
		actor2.getBody().applyForceToCenter(0f,1000f,true);
		return true;
	}

	/**
	 * Called when a key was typed
	 *
	 * @param character The character
	 * @return whether the input was processed
	 */
	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	/**
	 * Called when the screen was touched or a mouse button was pressed. The button parameter will be
	 * {@link Buttons#LEFT} on iOS.
	 *
	 * @param screenX The x coordinate, origin is in the upper left corner
	 * @param screenY The y coordinate, origin is in the upper left corner
	 * @param pointer the pointer for the event.
	 * @param button  the button
	 * @return whether the input was processed
	 */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	/**
	 * Called when a finger was lifted or a mouse button was released. The button parameter will be
	 * {@link Buttons#LEFT} on iOS.
	 *
	 * @param screenX
	 * @param screenY
	 * @param pointer the pointer for the event.
	 * @param button  the button
	 * @return whether the input was processed
	 */
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	/**
	 * Called when a finger or the mouse was dragged.
	 *
	 * @param screenX
	 * @param screenY
	 * @param pointer the pointer for the event.
	 * @return whether the input was processed
	 */
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	/**
	 * Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
	 *
	 * @param screenX
	 * @param screenY
	 * @return whether the input was processed
	 */
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	/**
	 * Called when the mouse wheel was scrolled. Will not be called on iOS.
	 *
	 * @param amountX the horizontal scroll amount, negative or positive depending on the direction
	 *                the wheel was scrolled.
	 * @param amountY the vertical scroll amount, negative or positive depending on the direction the
	 *                wheel was scrolled.
	 * @return whether the input was processed.
	 */
	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}
}