package com.armadillo.game.controller.Actions;

import com.armadillo.game.model.MainCharacter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Action;

/**
 *
 */
public class Curl extends Action {

  /**
   * Updates the action based on time. Typically this is called each frame by {@link

   *
   * @param delta Time in seconds since the last frame.
   * @return true if the action is done. This method may continue to be called after the action is
   * done.
   */
  @Override
  public boolean act(float delta) {
    if(this.actor instanceof MainCharacter) {

      MainCharacter gc = ((MainCharacter) this.actor);

      //will only apply the impluse of the character is touching the ground.
      if(!gc.getGround()) {
        Body body = gc.getBody();
        body.applyLinearImpulse(new Vector2(0, -4f), body.getWorldCenter(), true);
      }

      gc.setAlternateFixture();
    }
    return true;
  }
}
