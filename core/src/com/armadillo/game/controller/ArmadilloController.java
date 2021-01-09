package com.armadillo.game.controller;

import com.armadillo.game.controller.Actions.Aim;
import com.armadillo.game.controller.Actions.Curl;
import com.armadillo.game.controller.Actions.Jump;
import com.armadillo.game.controller.Actions.HorizontalMotion;
import com.armadillo.game.model.Bullet;
import com.armadillo.game.model.Enemy;
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
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.ArrayList;
import java.util.List;

public class ArmadilloController extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Stage stage;
	MainCharacter arma;

	Box2DDebugRenderer debugRenderer;
	Matrix4 debugMatrix;

	List<Action> left = new ArrayList<>();
	List<Action> right = new ArrayList<>();

	//have the textures init internally
	//Texture img;
	World world;
	Body bodyEdgeScreen;
	OrthographicCamera camera;
	GameMap tiledMap;


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


		ArrayList<Bullet> destroyedBullet = new ArrayList<>();
		for(Bullet b : this.arma.getWeapon().getBulletList()){
			if(b.destroyIfOutsideCamera(this.camera)) {
				destroyedBullet.add(b);
			}
		}
		System.out.println("Bullets" + this.arma.getWeapon().getBulletList().size());
		if(destroyedBullet.size() > 0) {
			for(Bullet b: destroyedBullet) {
				this.arma.getWeapon().getBulletList().remove(b);
			}
		}

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//batch.setProjectionMatrix(camera.combined);

		debugMatrix=new Matrix4(camera.combined);
		debugMatrix.scale(100f, 100f, 1f);

		tiledMap.render();

		batch.setTransformMatrix(this.getCamera().view);
		batch.setProjectionMatrix(this.getCamera().projection);
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


	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
		camera.update();
	}


	/**
	 * Called when a key was pressed
	 *
	 * @param keycode one of the constants in {@link Input.Keys}
	 * @return whether the input was processed
	 */
	@Override
	public boolean keyDown(int keycode) {

		if(keycode == Keys.UP || keycode == Keys.W) {
			arma.addAction(new Jump());
		}
		if(keycode == Keys.LEFT || keycode == Keys.A) {
			Action a = new HorizontalMotion(true, false);
			left.add(a);
			arma.addAction(a);
		}
		if(keycode == Keys.RIGHT || keycode == Keys.D) {
			Action a = new HorizontalMotion(true, true);
			right.add(a);
			arma.addAction(a);
		}
		if(keycode == Keys.DOWN || keycode == Keys.S) {
			arma.addAction(new Curl());
		}

		if(keycode == Keys.SHIFT_LEFT) {
			arma.addAction(new Aim(90));
		}

		return true;
	}

	/**
	 * Called when a key was released
	 *
	 * @param keycode one of the constants in {@link Input.Keys}
	 * @return whether the input was processed
	 */
	@Override
	public boolean keyUp(int keycode) {

		if(keycode == Keys.LEFT || keycode == Keys.A) {
			for(Action a : left) {
				arma.removeAction(a);
			}
		}

		if(keycode == Keys.RIGHT || keycode == Keys.D) {
			for(Action a : right) {
				arma.removeAction(a);
			}
		}



		if(keycode == Keys.DOWN || keycode == Keys.S) {
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
		this.arma.getWeapon().shoot(this.world);
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
		this.arma.getWeapon().shoot(this.world);
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
		Vector2 s = stage.screenToStageCoordinates(new Vector2(screenX, screenY));
		float deltax = s.x - arma.getWeapon().getX();
		float deltay = s.y - arma.getWeapon().getY();
		float m = deltay/deltax;
		float angle = (float)Math.toDegrees(Math.atan2(deltay, deltax));
		arma.getWeapon().face((int)angle, arma.isRight());
		return true;
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

	public List<Enemy> getEnemies() {
		//TODO: get the list of enemies
		return new ArrayList<>();
	}

	public MainCharacter getMainCharacter() {
		return this.arma;
	}

	public List<Bullet> getEnemyBullets() {
		//TODO:
		return new ArrayList<>();
	}

	public List<Bullet> getMainCharacterBullets() {
		return arma.getWeapon().getBulletList();
	}

}