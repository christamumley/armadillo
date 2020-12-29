package com.armadillo.game.model;

import com.armadillo.game.controller.ArmadilloController;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.List;

public class GameMap {
  final float PIXELS_TO_METERS = 100f;
  TiledMap tiledMap;
  TiledMapRenderer tiledMapRenderer;
  ArmadilloController ac;
  List<Vector2> playerPoints = new ArrayList<>();

  public GameMap(String tmx, ArmadilloController armadilloController) {
    tiledMap = new TmxMapLoader().load(tmx);
    tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    ac = armadilloController;

    //make bodies for the tilemap
    MapObjects objects = tiledMap.getLayers().get("ground").getObjects();
    World world = armadilloController.getWorld();

      for (MapObject object: objects) {

        Shape bodyshape  = null;

        if (object instanceof PolygonMapObject) {
          PolygonShape polygon = new PolygonShape();
          float[] vertices = ((PolygonMapObject)object).getPolygon().getTransformedVertices();

          float[] worldVertices = new float[vertices.length];

          for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = vertices[i] / PIXELS_TO_METERS;
          }

          polygon.set(worldVertices);
          bodyshape = polygon;

        } else if (object instanceof RectangleMapObject){

          //if the polygon isn't a point
          if(object.getName() == null || !object.getName().equals("start")) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            float[] vertices = {
                rectangle.x, rectangle.y,
                rectangle.x + rectangle.width, rectangle.y,
                rectangle.x + rectangle.width, rectangle.y + rectangle.height,
                rectangle.x, rectangle.y + rectangle.height};


            float[] worldVertices = new float[vertices.length];

            for (int i = 0; i < vertices.length; ++i) {
              worldVertices[i] = vertices[i] / PIXELS_TO_METERS;
            }

            PolygonShape polygonShape = new PolygonShape();
            polygonShape.set(worldVertices);

            bodyshape = polygonShape;
          } else if (object.getName().equals("start")) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            playerPoints.add(new Vector2(rectangle.x, rectangle.y));
            break;
          }
        } else {
          break;
        }

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;
        Body body = world.createBody(bodyDef);

        //create a fixture for each body from the shape

        Fixture fixture = body.createFixture(bodyshape, 0.5f);
        fixture.setFriction(1F);
      }
  }

  /**
   * Renders the game map
   */
  public void render() {
    tiledMapRenderer.setView(ac.getCamera());
    tiledMapRenderer.render();
  }

  /**
   * Gets a point that describes a place where the player is able to be spawned.
   * the 0th point is assumed to be the starting position.
   * @param pointIndex the numbered point where the player can be spawned.
   * @return the x,y vector that the player can be spawned at
   */
  public Vector2 getPlayerPoint(int pointIndex) {
    if(playerPoints.size() <= pointIndex) {
      throw new IllegalArgumentException("This player point does not exist.");
    }
    return playerPoints.get(pointIndex);
  }

}
