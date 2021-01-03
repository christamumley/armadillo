package com.armadillo.game.controller.Actions;

import com.armadillo.game.model.GameCharacter;
import com.badlogic.gdx.scenes.scene2d.Action;

public class Aim extends Action {
  private final int angle;
  public Aim(int angle) {
    this.angle = angle;
  }
  /**
   * Updates the action based on time. Typically this is called each frame by {@link
   *
   * @param delta Time in seconds since the last frame.
   * @return true if the action is done. This method may continue to be called after the action is
   * done.
   */
  @Override
  public boolean act(float delta) {
    if(this.actor instanceof GameCharacter) {
      GameCharacter gc = (GameCharacter) this.actor;

    }
    return true;
  }
}
