package com.armadillo.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Represents a weapon that can be wielded by a Character.
 */
public class Weapon extends Actor {

  //image of the weapon that will be superimposed on the Base character
  private Sprite gunImg;

  public Weapon(Texture texture) {

    this.gunImg = new Sprite(texture);
    setBounds(getX(),getY(),texture.getWidth(),texture.getHeight());

  }




}
