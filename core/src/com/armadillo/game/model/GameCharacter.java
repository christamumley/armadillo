package com.armadillo.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import java.util.Objects;

/**
 * <p>
 * A Character represents an actor in the game that is either an Enemy, the Player, or a Neutral,
 * that has the ability to influence the game state by interacting with each other/ the environment.
 * <\p>
 * <p>
 * Each Character has the ability to move, jump, perform an attack, and take damage. Each method
 * that performs this action should be called by an Action object, and not directly by the controller
 * to avoid tight coupling of Events with actions.
 * </p>
 * <p>
 * Character extends Group because a Character can consist of multiple Actors, currently the Base
 * and the Weapon. Every Action performed on this Group will be applied to each of the Actors in
 * the group.
 * </p>
 */
public abstract class GameCharacter extends Group {

  final float PIXELS_TO_METERS = 100f;

  //the Box2d physics simulation object
  protected Body body;
  //Internal sprite for image management
  protected Sprite baseSprite;
  protected Texture texture;

  //health points for this character
  protected int hp;

  //Weapon used by the character
  protected Weapon weapon;
  protected boolean isWeaponHidden;

  //true iff the Character is touching the ground;
  protected boolean ground;

  /**
   * Creates the Character Object. Can only be accessed with the Character Builder class
   * @param hp health points of the Character, cannot be less than or equal to zero.
   * @param weapon the Character's weapon. Can be null if the Character has no weapon.
   */
  public GameCharacter(World world, int hp, Texture texture, Weapon weapon, Vector2 spawn) {

    Objects.requireNonNull(texture);
    this.texture = texture;

    Objects.requireNonNull(weapon);
    this.weapon = weapon;
    isWeaponHidden = false;

    this.setX(spawn.x);
    this.setY(spawn.y);

    setBounds(getX(),getY(),texture.getWidth(),texture.getHeight());

    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    bodyDef.position.set((this.getX() + this.getWidth()/2) /
            PIXELS_TO_METERS,
        (this.getY() + this.getHeight()/2) / PIXELS_TO_METERS);
    this.body = world.createBody(bodyDef);

    this.setDefaultFixture();

    this.setDefaultFixture();

    //init sprite
    this.baseSprite = new Sprite(texture);
    this.baseSprite.setX(this.getX());
    this.baseSprite.setY(this.getY());

    this.ground = true;
  }

  @Override
  public void draw(Batch batch, float alpha){

    float x = (body.getPosition().x * PIXELS_TO_METERS) - this.getWidth()/2;
    float y = (body.getPosition().y * PIXELS_TO_METERS) - this.getHeight()/2;
    float angle = (float)Math.toDegrees(body.getAngle());

    this.setPosition(this.baseSprite.getX(), this.baseSprite.getY());
    this.baseSprite.setPosition(x, y);

    this.setRotation(this.baseSprite.getRotation());
    this.baseSprite.setRotation(angle);

    this.setOrigin(baseSprite.getOriginX(), baseSprite.getOriginY());



    //drawing the base and the weapons
    this.baseSprite.draw(batch, alpha);
    if(!isWeaponHidden) {
      //setting the weapon to the center of the Base
      float weaponY = this.getY() + this.getHeight()/2;
      float weaponX = this.getX() + this.getWidth()/2;
      this.weapon.setPosition(weaponX, weaponY);
      this.weapon.setRotation(angle);
      this.weapon.setOrigin(this.baseSprite.getOriginX(), this.baseSprite.getOriginY());
      this.weapon.draw(batch, alpha);
    }
  }

  /**
   * Sets the weapon of the Character.
   * @param weapon the new Weapon.
   */
  public void setWeapon(Weapon weapon) {
    if(weapon == null) {
      throw new IllegalArgumentException("Weapon cannot be null.");
    }

    //removes actor from the Group
    this.removeActor(this.weapon);
    this.weapon = weapon;

    //setting the weapon to the center of the Base
    float weaponY = this.getY() + (this.getHeight()/2);
    float weaponX = this.getX() + (this.getWidth()/2);
    this.weapon.setPosition(weaponX, weaponY);

    this.addActor(weapon);
  }

  /**
   * Hides the Character's weapon.
   */
  public void hideWeapon() {
    this.isWeaponHidden = true;
  }

  /**
   * Shows the Character's weapon.
   */
  public void showWeapon() {
    this.isWeaponHidden = false;
  }

  /**
   * Returns the Weapon of the Character.
   * @param weapon the Character's Weapon.
   */
  public Weapon getWeapon(Weapon weapon) {
    return this.weapon;
  }

  /**
   * Retrieves the Character's health points.
   * @return int representing Character hp
   */
  public int getHp() {
    return this.hp;
  }

  /**
   * Sets the Character's health points.
   * @param hp the desired hp
   */
  public void setHp(int hp) {
    this.hp = hp;
  }

  /**
   * Gets the Box2d Body of the Character.
   * @return the Body of the Character.
   */
  public Body getBody() {
    return this.body;
  }

  /**
   * Sets the idle physics body fixture
   */
  public abstract void setDefaultFixture();

  /**
   * Sets the physics body fixture for the downward motion
   */
  public abstract void setAlternateFixture();

  /**
   * Sets the Ground field to the given ground state.  If true, the
   * Character is able to Jump.
   *
   * @param groundState is the character is touching the ground?
   */
  public void setGround(boolean groundState) {
    this.ground = groundState;
  }

  /**
   * Gets the current state if the Character is touching the ground.
   * @return true if the character is touching the ground.
   */
  public boolean getGround() {
    return this.ground;
  }

  /**
   * Clears the shape data from the box2d body.
   */
  private void clearFixtures() {
    Array<Fixture> fixtureDefs = body.getFixtureList();
    if(fixtureDefs.size > 0) {
      for(Fixture f: fixtureDefs) {
        body.destroyFixture(f);
      }
    }
  }
}
