package com.armadillo.game.model;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import java.util.Objects;

/**
 * JumpContact is a ContactListener that is attached to the Box2D World. When attached to the World,
 * whenever a contact occurs the beginContact and endContact methods will be called. This class
 * checks to see if one of the Box2D Fixtures that are making contact is the player that gets passed
 * to the constructor. If it is, and it is touching a Static Body, the ground field of the player
 * will be set to true. Otherwise, it will be set to false.
 */
public class JumpContact implements ContactListener {

  MainCharacter player;
  boolean playerContact;
  int numContacts = 0;

  public JumpContact(MainCharacter player) {
    Objects.requireNonNull(player);
    this.player = player;
    this.playerContact = false;
  }

  /**
   * Called when two fixtures begin to touch.
   *
   * @param contact
   */
  @Override
  public void beginContact(Contact contact) {

    //checking if the player is touching the ground.
    if (this.checkIfPlayerAndStatic(contact)) {
      this.playerContact = true;
      this.player.setGround(true);
      numContacts += 1;
      System.out.println("num contacts: " + numContacts);
    }

  }

  /**
   * Called when two fixtures cease to touch.
   *
   * @param contact
   */
  @Override
  public void endContact(Contact contact) {

    //checking if the player was touching the ground.
    if (this.checkIfPlayerAndStatic(contact)) {
      numContacts--;
      if (numContacts <= 0) {
        this.player.setGround(false);
      }
      System.out.println("end: " + numContacts);
    }

  }

  //checks if the contact consists of the Player and a static body
  private boolean checkIfPlayerAndStatic(Contact contact) {
    return (contact.getFixtureA() == this.player.getBody().getFixtureList().get(0)
        && contact.getFixtureB().getBody().getType() == BodyType.StaticBody) ||
        (contact.getFixtureB() == this.player.getBody().getFixtureList().get(0) &&
            contact.getFixtureA().getBody().getType() == BodyType.StaticBody);
  }


  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {

  }

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {

  }
}
