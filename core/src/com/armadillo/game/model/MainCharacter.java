package com.armadillo.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
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
    Vector2 pos = new Vector2();
    body.getLocalPoint(pos);
    this.clearFixtures();

    PolygonShape shape = new PolygonShape();
    float w = this.getWidth()/PIXELS_TO_METERS;
    float h = this.getHeight()/PIXELS_TO_METERS;
    float x = pos.x + w/2;
    float y = pos.y + h/2;
    float s = w/2;
    //making a hexagon
    Vector2[] points = {
      new Vector2(x, y - (float)(Math.sqrt(3)*s)/2),
        new Vector2(x - s/2, y - (float)(Math.sqrt(3)*s)),
        new Vector2(x - (3*s)/2, y - (float)(Math.sqrt(3)*s)),
        new Vector2(x - w, y - (float)(Math.sqrt(3)*s)/2),
        new Vector2(x - (3*s)/2, y),
        new Vector2(x - s/2, y)
    };
    shape.set(points);

    //TODO:Figure out mutliple fixtures per body
//    shape = new PolygonShape();
//    shape.setAsBox(this.getHeight()/(PIXELS_TO_METERS),
//        this.getWidth()/(PIXELS_TO_METERS), new Vector2((this.getX())/PIXELS_TO_METERS,
//            (this.getY())/PIXELS_TO_METERS),
//        0);
//
//    footFix = new FixtureDef();
//    footFix.shape = shape;
//    footFix.density = .5f;
//    footFix.restitution = .2f;
//    footFix.filter.categoryBits = MaskBits.CHARACTER_ENTITY.mask;
//    footFix.filter.maskBits = (short) (MaskBits.WORLD_ENTITY.mask | MaskBits.PHYSICS_ENTITY.mask);
//    footFix.friction = 1F;
//
//    body.createFixture(footFix);

    this.bodyFix = new FixtureDef();
    bodyFix.shape = shape;
    bodyFix.density = .5f;
    bodyFix.restitution = .2f;
    bodyFix.filter.categoryBits = MaskBits.CHARACTER_ENTITY.mask;
    bodyFix.filter.maskBits = (short) (MaskBits.WORLD_ENTITY.mask | MaskBits.PHYSICS_ENTITY.mask);
    bodyFix.friction = 1F;

    body.createFixture(bodyFix);
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
    float centerx = body.getLocalCenter().x;
    float centery = body.getLocalCenter().y;
    Vector2 center = new Vector2(centerx, centery);

    this.clearFixtures();

    CircleShape shape = new CircleShape();
    shape.setRadius(this.getHeight()/(PIXELS_TO_METERS * 2));
    shape.setPosition(center);

    bodyFix = new FixtureDef();
    bodyFix.shape = shape;
    bodyFix.density = .5f;
    bodyFix.restitution = .2f;
    bodyFix.filter.categoryBits = MaskBits.CHARACTER_ENTITY.mask;
    bodyFix.filter.maskBits = (short) (MaskBits.WORLD_ENTITY.mask | MaskBits.PHYSICS_ENTITY.mask);
    bodyFix.friction = 1F;

    body.createFixture(bodyFix);
    body.setFixedRotation(false);

    shape.dispose();

    this.curled = true;
    this.hideWeapon();
  }

  @Override
  public void draw(Batch batch, float alpha){

    float x = (body.getPosition().x * PIXELS_TO_METERS) - this.getWidth()/2;
    float y = (body.getPosition().y * PIXELS_TO_METERS) - this.getHeight()/2;
    float angle = (float)Math.toDegrees(body.getAngle());

    this.setPosition(this.baseSprite.getX(), this.baseSprite.getY());
    this.baseSprite.setPosition(x, y);

    this.setRotation(this.baseSprite.getRotation());
    if(curled) {
      this.baseSprite.setRotation(angle);
    } else {
      this.baseSprite.setRotation(0);
    }


    this.setOrigin(baseSprite.getOriginX(), baseSprite.getOriginY());

    //drawing the base and the weapons
    this.baseSprite.draw(batch, alpha);
    if(!isWeaponHidden) {
      //setting the weapon to the center of the Base
      this.weapon.setPosition(this.baseSprite.getX(), this.baseSprite.getY() + this.getHeight()/3);
      this.weapon.draw(batch, alpha);
    }
  }

  /**
   * Clears the shape data from the box2d body.
   */
  private void clearFixtures() {
    for (Fixture f: this.body.getFixtureList()) {
      this.body.destroyFixture(f);
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
