package com.armadillo.game.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
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
    bodyDef.type = BodyType.DynamicBody;
    bodyDef.bullet = true;
    bodyDef.position.set((this.getX() + this.getWidth()/2) /
            PIXELS_TO_METERS,
        (this.getY() + this.getHeight()/2) / PIXELS_TO_METERS);
    this.body = world.createBody(bodyDef);

    this.setDefaultFixture();

    Vector2 trajectory = new Vector2((float)Math.cos(Math.toRadians(angle)),
        (float)Math.sin(Math.toRadians(angle)));

    this.body.applyForceToCenter(trajectory, true);

  }

  /**
   * Sets the idle physics body fixture
   */
  public void setDefaultFixture() {
    Vector2 pos = new Vector2();
    body.getLocalPoint(pos);

    CircleShape shape = new CircleShape();
    shape.setRadius(this.getWidth()/(2 * PIXELS_TO_METERS));
    shape.setPosition(pos);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = shape;
    fixtureDef.density = .11f;
    fixtureDef.restitution = .2f;
    fixtureDef.filter.categoryBits = MaskBits.BULLET_ENTITY.mask;
    fixtureDef.filter.maskBits = (short) (MaskBits.WORLD_ENTITY.mask | MaskBits.PHYSICS_ENTITY.mask);
    fixtureDef.friction = 1F;

    body.createFixture(fixtureDef);
    body.setTransform(body.getPosition(), 0);
    body.setFixedRotation(true);

    shape.dispose();

  }

  /**
   * Destroys the fixture of the Bullet if it appears outside of the camera.
   * @param camera the camera that the bullet checks its position against
   * @return true if the Bullet Fixture is destroyed.
   */
  public boolean destroyIfOutsideCamera(OrthographicCamera camera) {
    Vector2 pos = this.body.getPosition();
    float z = camera.frustum.planePoints[0].z;
    if(!camera.frustum.pointInFrustum(pos.x * 100f, pos.y * 100f, z)) {
      for(Fixture f: this.body.getFixtureList()) {
        this.body.destroyFixture(f);
      }
      return true;
    } else {
      return false;
    }
  }

}
