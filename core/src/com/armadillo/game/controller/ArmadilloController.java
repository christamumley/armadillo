package com.armadillo.game.controller;

import com.armadillo.game.controller.Actions.Curl;
import com.armadillo.game.controller.Actions.Jump;
import com.armadillo.game.model.MainCharacter;
import com.armadillo.game.model.GameMap;
import com.armadillo.game.model.JumpContact;
import com.armadillo.game.model.Weapon;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class ArmadilloController extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Stage stage;
	MainCharacter arma;

	Box2DDebugRenderer debugRenderer;
	Matrix4 debugMatrix;

	//have the textures init internally
	//Texture img;
	World world;
	Body bodyEdgeScreen;
	OrthographicCamera camera;
	final float PIXELS_TO_METERS = 100f;
	final short PHYSICS_ENTITY = 0x1;    // 0001
	final short WORLD_ENTITY = 0x1 << 1; // 0010 or 0x2 in hex
	long starttime;
	Texture img;
	GameMap tiledMap;
	TiledMapRenderer tiledMapRenderer;


	@Override
	public void create() {
		Gdx.input.setInputProcessor(this);

		world = new World(new Vector2(0, -9f),true);

  	camera = new OrthographicCamera();
 		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();

		tiledMap = new GameMap("tilemaps/testmap2.tmx", this);

		AssetManager manager = new AssetManager();
		manager.load("badlogic.jpg", Texture.class);
		manager.load("gun.png", Texture.class);
		manager.load("arma.png", Texture.class);
		manager.finishLoading();
		Texture img = manager.get("badlogic.jpg", Texture.class);
		Texture gun = manager.get("gun.png", Texture.class);
		Texture armatext = manager.get("arma.png", Texture.class);


		stage = new Stage();
		batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
		//Texture gun = new Texture("gun.png");

		// Create two identical sprites slightly offset from each other vertically
//		actor1 = new MyActor(world, img, 47, 100);
//		actor1.setVisible(true);
//		actor1.setDebug(true);
//		stage.addActor(actor1);

		Weapon gunw = new Weapon(gun);
		arma = new MainCharacter(world, 100, armatext, gunw, tiledMap.getPlayerPoint(0));
		arma.setDebug(true);

		world.setContactListener(new JumpContact(arma));

		stage.addActor(arma);

//		actor2 = new MyActor(world, img, 300, 600);
//		actor2.setVisible(true);
//		actor2.setDebug(true);
//		stage.addActor(actor2);

//		// Now the physics body of the bottom edge of the screen
//		BodyDef bodyDef3 = new BodyDef();
//		bodyDef3.type = BodyDef.BodyType.StaticBody;
//		float w = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
//		float h = Gdx.graphics.getHeight()/PIXELS_TO_METERS;
//		bodyDef3.position.set(0,0);
//		FixtureDef fixtureDef3 = new FixtureDef();
//		fixtureDef3.filter.categoryBits = WORLD_ENTITY;
//		fixtureDef3.filter.maskBits = PHYSICS_ENTITY;
//		EdgeShape edgeShape = new EdgeShape();
//		edgeShape.set(0,0,w,0);
//		fixtureDef3.shape = edgeShape;
//		bodyEdgeScreen = world.createBody(bodyDef3);
//		bodyEdgeScreen.createFixture(fixtureDef3);
//
//		// Now the physics body of the bottom edge of the screen
//		BodyDef bodyDef4 = new BodyDef();
//		bodyDef4.type = BodyDef.BodyType.StaticBody;
//		bodyDef4.position.set(0,0);
//		FixtureDef fixtureDef4 = new FixtureDef();
//		fixtureDef4.filter.categoryBits = WORLD_ENTITY;
//		fixtureDef4.filter.maskBits = PHYSICS_ENTITY;
//		EdgeShape edgeShape2 = new EdgeShape();
//		edgeShape2.set(w,0,w,h);
//		fixtureDef4.shape = edgeShape2;
//		bodyEdgeScreen = world.createBody(bodyDef4);
//		bodyEdgeScreen.createFixture(fixtureDef4);

		//edgeShape2.dispose();
		//edgeShape.dispose();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.
				getHeight());
		stage.getViewport().setCamera(camera);

		//Create a copy of camera projection matrix


//BoxObjectManager.BOX_TO_WORLD = 100f
//Scale it by 100 as our box physics bodies are scaled down by 100


		debugRenderer=new Box2DDebugRenderer();
	}
	@Override
	public void render() {
		// Step the physics simulation forward at a rate of 60hz
		world.step(1f/60f, 6, 2);
		stage.act();

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//batch.setProjectionMatrix(camera.combined);

		debugMatrix=new Matrix4(camera.combined);
		debugMatrix.scale(100f, 100f, 1f);


		tiledMap.render();

		batch.begin();
		stage.draw();
		debugRenderer.render(this.world, debugMatrix);
		batch.end();

		Vector3 position = camera.position;
		position.x += (arma.getX() + (arma.getWidth()/2) - position.x);
		position.y += (arma.getY() + (arma.getHeight()/2) - position.y);
		camera.update();


	}
	@Override
	public void dispose() {
		stage.dispose();
		batch.dispose();
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

		//TODO: put movement feature inside of Character class
		if(keycode == Keys.UP) {
			arma.addAction(new Jump());
		}
		if(keycode == Keys.LEFT) {
			arma.getBody().applyForceToCenter(-50f,0,true);
		}
		if(keycode == Keys.RIGHT) {
			arma.getBody().applyForceToCenter(50f,0,true);
		}
		if(keycode == Keys.DOWN) {
			arma.addAction(new Curl());
		}


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

		if(keycode == Keys.DOWN) {
			arma.setDefaultFixture();
		}
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

	/**
	 * Retrieves the camera from the controller.
	 * @return an orthographic camera from the controller
	 */
	public OrthographicCamera getCamera() {
		return camera;
	}

	public World getWorld() {
		return this.world;
	}
}