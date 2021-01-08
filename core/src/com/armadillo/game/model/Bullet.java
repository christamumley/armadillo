package com.armadillo.game.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Bullet extends Actor {

  final float PIXELS_TO_METERS = 100f;
  Body body;

  public Bullet(int damage, float x, float y, float angle, World world) {

    this.setX(x);
    this.setY(y);

    setBounds(getX(),getY(),10, 10);

    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyDef.BodyType.DynamicBody;
    bodyDef.position.set((this.getX() + this.getWidth()/2) /
            PIXELS_TO_METERS,
        (this.getY() + this.getHeight()/2) / PIXELS_TO_METERS);
    this.body = world.createBody(bodyDef);

    this.setDefaultFixture();


    float magnitude = (float)Math.sqrt(x*x + y*y);
    Vector2 trajectory = new Vector2(x/magnitude, y/magnitude);
    this.body.applyForceToCenter(trajectory, true);

  }

  /**
   * Sets the idle physics body fixture
   */
  public void setDefaultFixture() {
    Vector2 pos = new Vector2();
    body.getLocalPoint(pos);

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

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = .5f;
    fixtureDef.restitution = .2f;
    fixtureDef.filter.categoryBits = MaskBits.BULLET_ENTITY.mask;
    fixtureDef.filter.maskBits = (short) (MaskBits.WORLD_ENTITY.mask | MaskBits.PHYSICS_ENTITY.mask);
    fixtureDef.friction = 1F;

    body.createFixture(fixtureDef);
    body.setTransform(body.getPosition(), 0);
    body.setFixedRotation(true);

    shape.dispose();

  }

}
