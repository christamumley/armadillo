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
public abstract class Character extends Group {

  //TODO: manage constants
  final float PIXELS_TO_METERS = 100f;
  final short PHYSICS_ENTITY = 0x1;    // 0001
  final short WORLD_ENTITY = 0x1 << 1; // 0010 or 0x2 in hex

  //character texture
  Texture texture;
  //the Box2d physics simulation object
  Body body;
  //Internal sprite for image management
  Sprite baseSprite;

  //health points for this character
  private int hp;

  //Weapon used by the character
  private Weapon weapon;

  //intializes the base Character
  private Actor base;

  /**
   * Creates the Character Object. Can only be accessed with the Character Builder class
   * @param hp health points of the Character, cannot be less than or equal to zero.
   * @param weapon the Character's weapon. Can be null if the Character has no weapon.
   */
  public Character(World world, int hp, Texture basetexure, Weapon weapon) {

    if(hp <= 0) {
      throw new IllegalArgumentException("Health points cannot be initialized as zero, or less.");
    }

    if(base == null) {
      throw new IllegalArgumentException("Base Character cannot be null.");
    }

    this.hp = hp;

    //set base image
    this.baseSprite = new Sprite(basetexure);
    this.baseSprite.setY(this.getY());

    //init physics body
    this.initBody(world);

    //sets the weapon and centers it at the character
    this.setWeapon(weapon);

    //add base character and weapon to this Group
    this.addActor(this.base);
    this.addActor(this.weapon);
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

    //update weapon position

    //setting the weapon to the center of the Base
    float weaponY = base.getY() + (base.getHeight()/2);
    float weaponX = base.getX() + (base.getWidth()/2);
    this.weapon.setPosition(weaponX, weaponY);
    this.weapon.setRotation(angle);
    this.weapon.setOrigin(baseSprite.getOriginX(), baseSprite.getOriginY());

    //drawing the base and the weapons
    this.weapon.draw(batch, alpha);
    this.baseSprite.draw(batch, alpha);
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
    float weaponY = base.getY() + (base.getHeight()/2);
    float weaponX = base.getX() + (base.getWidth()/2);
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
    fixtureDef.filter.categoryBits = PHYSICS_ENTITY;
    fixtureDef.filter.maskBits = WORLD_ENTITY | PHYSICS_ENTITY;

    body.createFixture(fixtureDef);

    shape.dispose();
  }

}
