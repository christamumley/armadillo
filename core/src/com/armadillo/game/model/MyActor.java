package com.armadillo.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.Objects;

/**
 * Dummy class I was using to figure out libgdx
 */

public class MyActor extends Actor {

  final float PIXELS_TO_METERS = 100f;
  final short PHYSICS_ENTITY = 0x1;    // 0001
  final short WORLD_ENTITY = 0x1 << 1; // 0010 or 0x2 in hex

  Texture texture;
  Body body;
  Sprite sprite;

  public MyActor(World world, Texture texture, int spawnx, int spawny) {

    Objects.requireNonNull(texture);
    this.texture = texture;

    this.setX(spawnx);
    this.setY(spawny);

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
    fixtureDef.filter.categoryBits = PHYSICS_ENTITY;
    fixtureDef.filter.maskBits = WORLD_ENTITY | PHYSICS_ENTITY;

    body.createFixture(fixtureDef);

    //init sprite
    this.sprite = new Sprite(texture);
    this.sprite.setX(this.getX());
    this.sprite.setY(this.getY());

    shape.dispose();
  }


  @Override
  public void draw(Batch batch, float alpha){

    float x = (body.getPosition().x * PIXELS_TO_METERS) - this.getWidth()/2;
    float y = (body.getPosition().y * PIXELS_TO_METERS) - this.getHeight()/2;
    float angle = (float)Math.toDegrees(body.getAngle());

    this.setPosition(this.sprite.getX(), this.sprite.getY());
    this.sprite.setPosition(x, y);

    this.setRotation(this.sprite.getRotation());
    this.sprite.setRotation(angle);

    this.setOrigin(sprite.getOriginX(), sprite.getOriginY());

    this.sprite.draw(batch, alpha);

  }


  public Body getBody() {
    return this.body;
  }
}