package com.armadillo.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
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
public class GameCharacter extends Group {

  //TODO: manage constants
  final float PIXELS_TO_METERS = 100f;

  //the Box2d physics simulation object
  Body body;
  //Internal sprite for image management
  Sprite baseSprite;
  Texture texture;

  //health points for this character
  private int hp;

  //Weapon used by the character
  private Weapon weapon;

  //intializes the base Character
  //private Actor base;

  /**
   * Creates the Character Object. Can only be accessed with the Character Builder class
   * @param hp health points of the Character, cannot be less than or equal to zero.
   * @param weapon the Character's weapon. Can be null if the Character has no weapon.
   */
  public GameCharacter(World world, int hp, Texture texture, Weapon weapon) {

    Objects.requireNonNull(texture);
    this.texture = texture;

    Objects.requireNonNull(weapon);
    this.weapon = weapon;

    this.setX(400);
    this.setY(700);

    setBounds(getX(),getY(),texture.getWidth(),texture.getHeight());

    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    bodyDef.position.set((this.getX() + this.getWidth()/2) /
            PIXELS_TO_METERS,
        (this.getY() + this.getHeight()/2) / PIXELS_TO_METERS);
    this.body = world.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(this.getWidth()/2 / PIXELS_TO_METERS, this.getHeight()
        /2 / PIXELS_TO_METERS);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = .5f;
    fixtureDef.restitution = .2f;
    fixtureDef.filter.categoryBits = MaskBits.PHYSICS_ENTITY.mask;
    fixtureDef.filter.maskBits = (short) (MaskBits.WORLD_ENTITY.mask | MaskBits.PHYSICS_ENTITY.mask);

    body.createFixture(fixtureDef);

    //init sprite
    this.baseSprite = new Sprite(texture);
    this.baseSprite.setX(this.getX());
    this.baseSprite.setY(this.getY());

    shape.dispose();
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

    //setting the weapon to the center of the Base
    float weaponY = this.getY() + (this.getHeight()/2);
    float weaponX = this.getX() + (this.getWidth()/2);
    this.weapon.setPosition(weaponX, weaponY);
    this.weapon.setRotation(angle);
    this.weapon.setOrigin(baseSprite.getOriginX(), baseSprite.getOriginY());

    //drawing the base and the weapons
    this.baseSprite.draw(batch, alpha);
    this.weapon.draw(batch, alpha);
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
   * Returns the Weapon of the Character.
   * @param weapon the Character's Weapon.
   */
  public Weapon getWeapon(Weapon weapon) {
    return this.weapon;
  }

  /**
   * Subtracts the damage from the Character's health points.
   * @param damage the int damage taken by the character.
   * @return the remaining hp of the character.
   */
  public int takeDamage(int damage) {

    if((this.hp - damage) <= 0) {
      this.hp = 0;
      return 0;
    }

    this.hp -= damage;
    return  this.hp;
  }

  /**
   * Retrieves the Character's health points.
   * @return int representing Character hp
   */
  public int getHp() {
    return this.hp;
  }

  /**
   * Gets the Box2d Body of the Character.
   * @return the Body of the Character.
   */
  public Body getBody() {
    return this.body;
  }


  //inits the Physics simulation
  private void initBody(World world) {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    bodyDef.position.set((this.getX() + this.getWidth()/2) /
            PIXELS_TO_METERS,
        (this.getY() + this.getHeight()/2) / PIXELS_TO_METERS);
    this.body = world.createBody(bodyDef);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(this.getWidth()/2 / PIXELS_TO_METERS, this.getHeight()
        /2 / PIXELS_TO_METERS);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = .5f;
    fixtureDef.restitution = .2f;
    fixtureDef.filter.categoryBits = MaskBits.PHYSICS_ENTITY.mask;
    fixtureDef.filter.maskBits = (short) (MaskBits.WORLD_ENTITY.mask | MaskBits.PHYSICS_ENTITY.mask);

    body.createFixture(fixtureDef);

    shape.dispose();
  }

}
