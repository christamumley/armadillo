package com.armadillo.game.controller.Actions;

import com.armadillo.game.model.GameCharacter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.physics.box2d.*;

/**
 * If the Actor is a GameCharacter, Jump will apply an upward impulse to the associated body.
 */
public class Jump extends Action {

  /**
   * Updates the action based on time. Typically this is called each frame
   *
   * @param delta Time in seconds since the last frame.
   * @return true if the action is done. This method may continue to be called after the action is
   * done.
   */
  @Override
  public boolean act(float delta) {
    if(this.actor instanceof GameCharacter) {

      GameCharacter gc = ((GameCharacter) this.actor);

      if(gc.getGround()) {
        Body body = gc.getBody();
        body.applyLinearImpulse(new Vector2(0, 3f), body.getWorldCenter(), true);
      }
    }
    return true;
  }
}
