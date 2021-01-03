package com.armadillo.game.model;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Represents a weapon that can be wielded by a Character.
 */
public class Weapon extends Actor {

  //image of the weapon that will be superimposed on the Base character
  private Sprite sprite;
  private Texture upsidedown_texture;
  private Texture texture;

  public Weapon(Texture texture) {
    this.sprite = new Sprite(texture);
    setBounds(getX(),getY(),texture.getWidth(),texture.getHeight());
    this.texture = texture;
    texture.getTextureData().prepare();
    this.upsidedown_texture = new Texture(flip(texture.getTextureData().consumePixmap()));
  }

  @Override
  public void draw(Batch batch, float alpha){

    this.sprite.setPosition(this.getX(), this.getY());
    this.sprite.setRotation(this.getRotation());
    this.sprite.draw(batch, alpha);

  }

  public void resize(float factor) {
    if(factor <= 0) {
      throw new IllegalArgumentException("Weapon Resize factor cannot be zero or negative.");
    }
    int w = (int)(this.getWidth() * factor);
    int h = (int)(this.getHeight() * factor);
    Texture t = new Texture(resize(sprite.getTexture(), w, h));

    this.sprite = new Sprite(t);
  }

  public void face(int angle, boolean isRight) {
    int pointAngle = 0;
    if(isRight) {
      this.sprite.setTexture(this.texture);
      if(angle < -90) {
        pointAngle = angle + 180;
      } else if (angle > 90) {
        pointAngle = 180 - angle;
      } else {
        pointAngle = angle;
      }
    } else {
      this.sprite.setTexture(this.upsidedown_texture);
      if (angle < 90 && angle > 0) {
        pointAngle = 180 - angle;
      } else if (angle > -90 && angle < 0 ) {
        pointAngle = -180 - angle;
      } else {
        pointAngle = angle;
      }
    }
    this.setRotation(pointAngle);
  }

  /**
   * Takes the Pixmap from a texture and returns a resized version.
   * Side effect: removes the Pixmap from the texture.
   * @param t the given texture to be resized.
   * @param w the new width of the texture
   * @param h the new height of the texture
   * @return the resized Pixmap
   */
  private Pixmap resize(Texture t, int w, int h) {
    TextureData td = t.getTextureData();
    td.prepare();
    Pixmap p = td.consumePixmap();
    Pixmap p2 = new Pixmap(w, h, p.getFormat());
    p2.drawPixmap(p, 0, 0, p.getWidth(), p.getHeight(),
        0, 0, w, h);
    p.dispose();
    return p2;
  }

  /**
   * Flips the given Pixmap vertically.
   * @param p the given Pixmap.
   * @return Horizontally flipped Pixmap.
   */
  private Pixmap flip(Pixmap p) {
    int w = p.getWidth();
    int h = p.getHeight();
    Pixmap p2 = new Pixmap(w, h, p.getFormat());
    for(int i =0; i < w; i++) {
      for(int j = 0; j < h; j++) {
        p2.drawPixel(i,j,p.getPixel(i, h-j));
      }
    }
    p.dispose();
    return p2;
  }


}
