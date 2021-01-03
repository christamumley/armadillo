package com.armadillo.game.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import java.io.Serializable;

/**
 * Level represents a game state where the Character
 */
public class Level implements Serializable {

  MainCharacter armadillo;

  //TODO:add enemies

  GameMap tiledMap;
  World world;
  SpriteBatch batch;
  Stage stage;

  //TODO: when done, remove debug
  Box2DDebugRenderer debugRenderer;
  Matrix4 debugMatrix;




}
