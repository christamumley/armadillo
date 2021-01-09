package com.armadillo.game.model.contactListeners;

import com.armadillo.game.model.Bullet;
import com.armadillo.game.model.GameCharacter;
import com.armadillo.game.model.MaskBits;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import java.util.Objects;

/**
 * This class checks to see if one of the Box2D Fixtures that are making contact is the player
 * that gets passed to the constructor. If it is, and it is touching a Bullet Body, the player will
 * take damage proportional to the density of the Bullet.
 */
public class HitContact implements ContactListener {

  GameCharacter player;
  GameCharacter enemy = null;

  Fixture playerFixture;
  int weaponID = 0;

  /**
   * This constructor takes in a GameCharacter, where if the bullet doesn't come from their own
   * weapon, it will damage the player.
   * @param player the player that we wish to check if comes in contact with bullets
   */
  public HitContact(GameCharacter player) {
    Objects.requireNonNull(player);
    this.player = player;
    this.playerFixture = player.getBody().getFixtureList().get(0);
    this.weaponID = this.player.getWeapon().getID();
  }

  /**
   * This constructor takes in two GameCharacters, where if the bullet comes from any weapon
   * EXCEPT the given enemy's weapon, it will not take damage. If the bullet came from the
   * named enemy's weapon it will take damage.
   * @param player the player that may get damaged.
   * @param enemy the enemy that shoots damaging bullets
   */
  public HitContact(GameCharacter player, GameCharacter enemy) {
    Objects.requireNonNull(player);
    Objects.requireNonNull(enemy);
    this.player = player;
    this.enemy = enemy;
    this.playerFixture = player.getBody().getFixtureList().get(0);
    this.weaponID = this.player.getWeapon().getID();
  }

  /**
   * Called when two fixtures begin to touch.
   *
   * @param contact
   */
  @Override
  public void beginContact(Contact contact) {

    Fixture a = contact.getFixtureA();
    Fixture b = contact.getFixtureB();

    if(a == this.playerFixture && b.getFilterData().maskBits == MaskBits.BULLET_ENTITY.mask) {

      //if there are multiple enemies, and the bullet didn't come from the player's own weapon,
      if(enemy == null &&
          this.player.getWeapon().getID() != Bullet.restitutionToWeaponID(b.getRestitution())) {
        //take damage encoded in density
        this.player.takeDamage((int)Bullet.densityToDamage(b.getDensity()));

        //if there is only one enemy, and the bullet came from that enemy
      } else if (enemy != null &&
          this.enemy.getWeapon().getID() == Bullet.restitutionToWeaponID(b.getRestitution())) {
        //take damage encoded in density
        this.player.takeDamage((int)Bullet.densityToDamage(b.getDensity()));
      }

      b.getBody().destroyFixture(b);

    } else if(b == this.playerFixture && a.getFilterData().maskBits == MaskBits.BULLET_ENTITY.mask) {

      //if there are multiple enemies, and the bullet didn't come from the player's own weapon,
      if(enemy == null &&
          this.player.getWeapon().getID() != Bullet.restitutionToWeaponID(a.getRestitution())) {
        //take damage encoded in density
        this.player.takeDamage((int)Bullet.densityToDamage(a.getDensity()));

        //if there is only one enemy, and the bullet came from that enemy
      } else if (enemy != null &&
          this.enemy.getWeapon().getID() == Bullet.restitutionToWeaponID(a.getRestitution())) {
        //take damage encoded in density
        this.player.takeDamage((int)Bullet.densityToDamage(a.getDensity()));
      }

      a.getBody().destroyFixture(a);
    }

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
}
