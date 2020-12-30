package com.armadillo.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 */
public class MainCharacter extends GameCharacter {

  //indicates if in the crouched state or not.
  boolean curled = false;

  /**
   * Creates the Character Object. Can only be accessed with the Character Builder class
   * @param hp health points of the Character, cannot be less than or equal to zero.
   * @param weapon the Character's weapon. Can be null if the Character has no weapon.
   */
  public MainCharacter(World world, int hp, Texture texture, Weapon weapon, Vector2 spawn) {
    super(world, hp, texture, weapon, spawn);
  }

  /**
   * Sets the idle physics body fixture
   */
  public void setDefaultFixture() {

    //clear fixtures
    this.clearFixtures();

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
    body.setTransform(body.getPosition(), 0);
    body.setFixedRotation(true);

    shape.dispose();

    this.curled = false;
    this.showWeapon();
  }

  /**
   * Sets the physics body fixture for the downward motion
   */
  public void setAlternateFixture() {
    Vector2 center = body.getLocalCenter();
    this.clearFixtures();

    CircleShape shape = new CircleShape();
    shape.setRadius(this.getHeight()/(PIXELS_TO_METERS * 2));
    shape.setPosition(center);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = .5f;
    fixtureDef.restitution = .2f;
    fixtureDef.filter.categoryBits = MaskBits.PHYSICS_ENTITY.mask;
    fixtureDef.filter.maskBits = (short) (MaskBits.WORLD_ENTITY.mask | MaskBits.PHYSICS_ENTITY.mask);

    body.createFixture(fixtureDef);
    body.setFixedRotation(false);

    shape.dispose();

    this.curled = true;
    this.hideWeapon();
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

  /**
   * Is the Main Character curled?
   * @return returns true if the Character is in the Curled state.
   */
  public boolean getCurled() {
    return this.curled;
  }

}
