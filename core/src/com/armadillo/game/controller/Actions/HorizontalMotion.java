package com.armadillo.game.controller.Actions;
import com.armadillo.game.model.GameCharacter;
import com.armadillo.game.model.MainCharacter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
public class HorizontalMotion extends Action {

  private boolean move;
  private boolean right;

  public HorizontalMotion(boolean move, boolean right) {
    this.move = move;
    this.right = right;
  }

  /**
   * Updates the action based on time. Typically this is called each frame by
   *
   * @param delta Time in seconds since the last frame.
   * @return true if the action is done. This method may continue to be called after the action is
   * done.
   */
  @Override
  public boolean act(float delta) {
    if(this.actor instanceof MainCharacter) {
      MainCharacter mc = (MainCharacter)this.actor;
      if(mc.getCurled()) return true;
    }
    if(this.actor instanceof GameCharacter) {
      GameCharacter gc = (GameCharacter)this.actor;
      if (right) {
        gc.faceRight();
      } else {
        gc.faceLeft();
      }

      int isRight = right? 1 : -1;
      if (move) {
        float y = gc.getBody().getLinearVelocity().y;
        gc.getBody().setLinearVelocity(3f * isRight, y);
      }
    }
    return false;
  }

}
