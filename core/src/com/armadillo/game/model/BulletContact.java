package com.armadillo.game.model;

import com.armadillo.game.controller.ArmadilloController;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import java.util.Objects;

/**
 * TODO:
 */
public class BulletContact implements ContactListener {

  ArmadilloController ac;

  public BulletContact(ArmadilloController ac) {
    this.ac = ac;
  }

  /**
   * Called when two fixtures begin to touch.
   *
   * @param contact
   */
  @Override
  public void beginContact(Contact contact) {
//    Fixture fa = contact.getFixtureA();
//    Fixture fb = contact.getFixtureB();
//    //checking if a Bullet is touching a GameCharacter
//    for(Enemy e : ac.getEnemies()) {
//      for(Bullet b : ac.getMainCharacterBullets()) {
//        if(checkFixtures(e.getBody().getFixtureList().get(0),
//            b.getBody().getFixtureList().get(0),
//            fa, fb)) {
//          //take hit
//        }
//      }
//    }

  }

  /**
   * Called when two fixtures cease to touch.
   *
   * @param contact
   */
  @Override
  public void endContact(Contact contact) {

  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {

  }

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {

  }

  private boolean checkFixtures(Fixture f1, Fixture f2, Fixture fa, Fixture fb) {
    return (fa == f1 && fb == f2 || fa == f2 || fb == f1);
  }

}
