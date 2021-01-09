package com.armadillo.game.model.contactListeners;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import java.util.ArrayList;
import java.util.List;

/**
 * Groups several ContactListeners together. ContactListeners can be added/ removed.
 */
public class GroupContactListener implements ContactListener {

  private List<ContactListener> contactListeners = new ArrayList<>();

  /**
   * Called when two fixtures begin to touch.
   *
   * @param contact
   */
  @Override
  public void beginContact(Contact contact) {
    for(ContactListener c : contactListeners) {
      c.beginContact(contact);
    }
  }

  /**
   * Adds the given ContactListener to the Main Listener.
   * @param ContactListener the contact listener to be added and used
   */
  public void addListener(ContactListener ContactListener) {
    this.contactListeners.add(ContactListener);
  }

  /**
   * Removes the given ContactListener to the Main Listener.
   * @param ContactListener the contact listener to be removed
   */
  public void removeListener(ContactListener ContactListener) {
    this.contactListeners.remove(ContactListener);
  }

  /**
   * Called when two fixtures cease to touch.
   *
   * @param contact
   */
  @Override
  public void endContact(Contact contact) {
    for(ContactListener c : contactListeners) {
      c.endContact(contact);
    }
  }

  @Override
  public void preSolve(Contact contact, Manifold oldManifold) {

  }

  @Override
  public void postSolve(Contact contact, ContactImpulse impulse) {

  }
}
