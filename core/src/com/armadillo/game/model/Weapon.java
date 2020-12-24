package com.armadillo.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Represents a weapon that can be wielded by a Character.
 */
public class Weapon extends Actor {

  //image of the weapon that will be superimposed on the Base character
  private final Sprite sprite;

  public Weapon(Texture texture) {
    this.sprite = new Sprite(texture);
    setBounds(getX(),getY(),texture.getWidth(),texture.getHeight());
  }

  @Override
  public void draw(Batch batch, float alpha){

    this.sprite.setPosition(this.getX(), this.getY());
    this.sprite.setRotation(this.getRotation());
    this.sprite.setOrigin(getOriginX(), getOriginY());

    this.sprite.draw(batch, alpha);

  }



}
