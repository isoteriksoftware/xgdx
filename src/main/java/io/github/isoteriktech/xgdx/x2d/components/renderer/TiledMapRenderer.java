package io.github.isoteriktech.xgdx.x2d.components.renderer;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import io.github.isoteriktech.xgdx.Component;
import io.github.isoteriktech.xgdx.GameCamera;
import io.github.isoteriktech.xgdx.GameObject;
import io.github.isoteriktech.xgdx.x2d.GameCamera2d;

import java.util.Iterator;

/**
 * A component capable of rendering a {@link TiledMap}. It uses {@link OrthoCachedTiledMapRenderer} for rendering by default but can be changed.
 * This component requires a {@link GameCamera2d} and will not render anything if none exists.
 * @author isoteriksoftware
 */
public class TiledMapRenderer extends Component {
    /** The dimension of the map. */
    public final int mapWidth, mapHeight;

    /** The dimension of individual tiles. */
    public final int tileWidth, tileHeight;

    /** The number of tiles. */
    public final int horizontalTilesCount, verticalTilesCount;

    protected final TiledMap tiledMap;
    protected final float unitScale;

    protected MapRenderer tiledMapRenderer;

    protected GameCamera2d camera;

    /**
     * Creates a new instance given a tiled map and the unit scale to use.
     * The unit scale is the equivalence of 1 pixel in the game (1 / Pixels Per Unit).
     * Setting 1 as the unit scale means 1 pixel = 1 world unit.
     * It will use the default main camera until it is changed.
     * @param tiledMap the tiled map
     * @param unitScale the unit scale
     */
    public TiledMapRenderer(TiledMap tiledMap, float unitScale) {
        this.tiledMap = tiledMap;
        this.unitScale = unitScale;

        tileWidth = (int)tiledMap.getProperties().get("tilewidth");
        tileHeight = (int)tiledMap.getProperties().get("tileheight");
        horizontalTilesCount = (int)tiledMap.getProperties().get("width");
        verticalTilesCount = (int)tiledMap.getProperties().get("height");
        mapWidth = tileWidth * horizontalTilesCount;
        mapHeight = tileHeight * verticalTilesCount;

        tiledMapRenderer = new OrthoCachedTiledMapRenderer(tiledMap, unitScale);
        ((OrthoCachedTiledMapRenderer)tiledMapRenderer).setBlending(true);
    }

    /**
     * Creates a new instance given the fileName of the map file and a unit scale. This constructor will block until the map is loaded.
     * @param mapFileName the fileName of the map file
     * @param unitScale the unit scale.
     */
    public TiledMapRenderer(String mapFileName, float unitScale)
    { this(new TmxMapLoader().load(mapFileName), unitScale); }

    /**
     * @return the current camera used for projection
     */
    public GameCamera2d getCamera() {
        return camera;
    }

    /**
     * Sets the camera to use for projection.
     * @param camera the camera
     */
    public void setCamera(GameCamera2d camera) {
        this.camera = camera;
    }

    /**
     * Renders the map on the scene. Subclasses can override this method to provide custom rendering.
     */
    protected void renderTiledMap() {
        tiledMapRenderer.render();
    }

    @Override
    public void preUpdate(float deltaTime) {
        if (camera == null) {
            GameCamera cam = scene.getMainCamera();
            if (cam instanceof GameCamera2d)
                camera = (GameCamera2d) cam;
        }

        if (camera != null)
            tiledMapRenderer.setView(camera.getCamera());
    }

    @Override
    public void render(Array<GameObject> gameObjects) {
        if (camera != null)
            renderTiledMap();
    }

    /**
     *
     * @return the current map renderer
     */
    public MapRenderer getTiledMapRenderer()
    { return tiledMapRenderer; }

    /**
     * Sets the current map renderer.
     * <strrong>Note that you may have to dispose the renderer if possible.</strrong>
     * @param tiledMapRenderer a map renderer
     */
    public void setTiledMapRenderer(MapRenderer tiledMapRenderer)
    { this.tiledMapRenderer = tiledMapRenderer; }

    /**
     * Gets all rectangle objects in a given layer that has a property whose value matches the one given.
     * @param mapLayer the layer
     * @param propertyName the property name. <strong>use *</strong> to retrieve everything
     * @param value the property value to check
     * @return an array of found objects
     */
    public Array<RectangleMapObject> getRectangleObjects(MapLayer mapLayer, String propertyName, String value) {
        Array<RectangleMapObject> found = new Array<>();

        for (MapObject mapObject : mapLayer.getObjects()) {
            if (!(mapObject instanceof  RectangleMapObject))
                continue;

            if (propertyName.equals("*")) {
                found.add((RectangleMapObject) mapObject);
                continue;
            }

            MapProperties mapProperties = mapObject.getProperties();
            if (mapProperties.containsKey(propertyName) && mapProperties.get(propertyName).equals(value))
                found.add((RectangleMapObject)mapObject);
        }

        return found;
    }

    /**
     * Gets all rectangle objects in every layer that has a property whose value matches the one given.
     * @param propertyName the property name. <strong>use *</strong> to retrieve everything
     * @param value the property value to check
     * @return an array of found objects
     */
    public Array<RectangleMapObject> getRectangleObjects(String propertyName, String value) {
        Array<RectangleMapObject> found = new Array<>();

        for (MapLayer mapLayer : tiledMap.getLayers()) {
            for (MapObject mapObject : mapLayer.getObjects()) {
                if (!(mapObject instanceof  RectangleMapObject))
                    continue;

                if (propertyName.equals("*")) {
                    found.add((RectangleMapObject) mapObject);
                    continue;
                }

                MapProperties mapProperties = mapObject.getProperties();
                if (mapProperties.containsKey(propertyName) && mapProperties.get(propertyName).equals(value))
                    found.add((RectangleMapObject)mapObject);
            }
        }

        return found;
    }

    /**
     * Gets all circle objects in a given layer that has a property whose value matches the one given.
     * @param mapLayer the layer
     * @param propertyName the property name. <strong>use *</strong> to retrieve everything
     * @param value the property value to check
     * @return an array of found objects
     */
    public Array<CircleMapObject> getCircleObjects(MapLayer mapLayer, String propertyName, String value) {
        Array<CircleMapObject> found = new Array<>();

        for (MapObject mapObject : mapLayer.getObjects()) {
            if (!(mapObject instanceof  CircleMapObject))
                continue;

            if (propertyName.equals("*")) {
                found.add((CircleMapObject) mapObject);
                continue;
            }

            MapProperties mapProperties = mapObject.getProperties();
            if (mapProperties.containsKey(propertyName) && mapProperties.get(propertyName).equals(value))
                found.add((CircleMapObject)mapObject);
        }

        return found;
    }

    /**
     * Gets all circle objects in every layer that has a property whose value matches the one given.
     * @param propertyName the property name. <strong>use *</strong> to retrieve everything
     * @param value the property value to check
     * @return an array of found objects
     */
    public Array<CircleMapObject> getCircleObjects(String propertyName, String value) {
        Array<CircleMapObject> found = new Array<>();

        for (MapLayer mapLayer : tiledMap.getLayers()) {
            for (MapObject mapObject : mapLayer.getObjects()) {
                if (!(mapObject instanceof  CircleMapObject))
                    continue;

                if (propertyName.equals("*")) {
                    found.add((CircleMapObject) mapObject);
                    continue;
                }

                MapProperties mapProperties = mapObject.getProperties();
                if (mapProperties.containsKey(propertyName) && mapProperties.get(propertyName).equals(value))
                    found.add((CircleMapObject)mapObject);
            }
        }

        return found;
    }

    /**
     * Gets all ellipse objects in a given layer that has a property whose value matches the one given.
     * @param mapLayer the layer
     * @param propertyName the property name. <strong>use *</strong> to retrieve everything
     * @param value the property value to check
     * @return an array of found objects
     */
    public Array<EllipseMapObject> getEllipseObjects(MapLayer mapLayer, String propertyName, String value) {
        Array<EllipseMapObject> found = new Array<>();

        for (MapObject mapObject : mapLayer.getObjects()) {
            if (!(mapObject instanceof  EllipseMapObject))
                continue;

            if (propertyName.equals("*")) {
                found.add((EllipseMapObject) mapObject);
                continue;
            }

            MapProperties mapProperties = mapObject.getProperties();
            if (mapProperties.containsKey(propertyName) && mapProperties.get(propertyName).equals(value))
                found.add((EllipseMapObject)mapObject);
        }

        return found;
    }

    /**
     * Gets all ellipse objects in every layer that has a property whose value matches the one given.
     * @param propertyName the property name. <strong>use *</strong> to retrieve everything
     * @param value the property value to check
     * @return an array of found objects
     */
    public Array<EllipseMapObject> getEllipseObjects(String propertyName, String value) {
        Array<EllipseMapObject> found = new Array<>();

        for (MapLayer mapLayer : tiledMap.getLayers()) {
            for (MapObject mapObject : mapLayer.getObjects()) {
                if (!(mapObject instanceof  EllipseMapObject))
                    continue;

                if (propertyName.equals("*")) {
                    found.add((EllipseMapObject) mapObject);
                    continue;
                }

                MapProperties mapProperties = mapObject.getProperties();
                if (mapProperties.containsKey(propertyName) && mapProperties.get(propertyName).equals(value))
                    found.add((EllipseMapObject)mapObject);
            }
        }

        return found;
    }

    /**
     * Gets all polygon objects in a given layer that has a property whose value matches the one given.
     * @param mapLayer the layer
     * @param propertyName the property name. <strong>use *</strong> to retrieve everything
     * @param value the property value to check
     * @return an array of found objects
     */
    public Array<PolygonMapObject> getPolygonObjects(MapLayer mapLayer, String propertyName, String value) {
        Array<PolygonMapObject> found = new Array<>();

        for (MapObject mapObject : mapLayer.getObjects()) {
            if (!(mapObject instanceof  PolygonMapObject))
                continue;

            if (propertyName.equals("*")) {
                found.add((PolygonMapObject) mapObject);
                continue;
            }

            MapProperties mapProperties = mapObject.getProperties();
            if (mapProperties.containsKey(propertyName) && mapProperties.get(propertyName).equals(value))
                found.add((PolygonMapObject)mapObject);
        }

        return found;
    }

    /**
     * Gets all polygon objects in every layer that has a property whose value matches the one given.
     * @param propertyName the property name. <strong>use *</strong> to retrieve everything
     * @param value the property value to check
     * @return an array of found objects
     */
    public Array<PolygonMapObject> getPolygonObjects(String propertyName, String value) {
        Array<PolygonMapObject> found = new Array<>();

        for (MapLayer mapLayer : tiledMap.getLayers()) {
            for (MapObject mapObject : mapLayer.getObjects()) {
                if (!(mapObject instanceof  PolygonMapObject))
                    continue;

                if (propertyName.equals("*")) {
                    found.add((PolygonMapObject) mapObject);
                    continue;
                }

                MapProperties mapProperties = mapObject.getProperties();
                if (mapProperties.containsKey(propertyName) && mapProperties.get(propertyName).equals(value))
                    found.add((PolygonMapObject)mapObject);
            }
        }

        return found;
    }

    /**
     * Gets all polyline objects in a given layer that has a property whose value matches the one given.
     * @param mapLayer the layer
     * @param propertyName the property name. <strong>use *</strong> to retrieve everything
     * @param value the property value to check
     * @return an array of found objects
     */
    public Array<PolylineMapObject> getPolylineObjects(MapLayer mapLayer, String propertyName, String value) {
        Array<PolylineMapObject> found = new Array<>();

        for (MapObject mapObject : mapLayer.getObjects()) {
            if (!(mapObject instanceof  PolylineMapObject))
                continue;

            if (propertyName.equals("*")) {
                found.add((PolylineMapObject) mapObject);
                continue;
            }

            MapProperties mapProperties = mapObject.getProperties();
            if (mapProperties.containsKey(propertyName) && mapProperties.get(propertyName).equals(value))
                found.add((PolylineMapObject)mapObject);
        }

        return found;
    }

    /**
     * Gets all polyline objects in every layer that has a property whose value matches the one given.
     * @param propertyName the property name. <strong>use *</strong> to retrieve everything
     * @param value the property value to check
     * @return an array of found objects
     */
    public Array<PolylineMapObject> getPolylineObjects(String propertyName, String value) {
        Array<PolylineMapObject> found = new Array<>();

        for (MapLayer mapLayer : tiledMap.getLayers()) {
            for (MapObject mapObject : mapLayer.getObjects()) {
                if (!(mapObject instanceof  PolylineMapObject))
                    continue;

                if (propertyName.equals("*")) {
                    found.add((PolylineMapObject) mapObject);
                    continue;
                }

                MapProperties mapProperties = mapObject.getProperties();
                if (mapProperties.containsKey(propertyName) && mapProperties.get(propertyName).equals(value))
                    found.add((PolylineMapObject)mapObject);
            }
        }

        return found;
    }

    /**
     * Gets all tile objects in a given layer that has a property whose value matches the one given.
     * Because {@link TiledMapTileMapObject}s have two kinds of properties (default and own properties), this method will merge the properties
     * before making the search.
     * @param mapLayer the layer
     * @param propertyName the property name. <strong>use *</strong> to retrieve everything
     * @param value the property value to check
     * @return an array of found objects
     */
    public Array<TiledMapTileMapObject> getTileObjects(MapLayer mapLayer, String propertyName, String value) {
        Array<TiledMapTileMapObject> found = new Array<>();

        for (MapObject mapObject : mapLayer.getObjects()) {
            if (!(mapObject instanceof  TiledMapTileMapObject))
                continue;

            MapProperties mapProperties = mapObject.getProperties();

            TiledMapTileMapObject tiledMapTileMapObject = (TiledMapTileMapObject)mapObject;
            TiledMapTile tiledMapTile = tiledMapTileMapObject.getTile();
            MapProperties defaultProperties = tiledMapTile.getProperties();

            if (propertyName.equals("*")) {
                found.add((TiledMapTileMapObject) mapObject);
                continue;
            }

            // Copy all the default properties
            Iterator<String> propertyKeys = defaultProperties.getKeys();
            while (propertyKeys.hasNext()) {
                String key = propertyKeys.next();

                if (mapProperties.containsKey(key))
                    continue;

                mapProperties.put(key, defaultProperties.get(key));
            }

            if (mapProperties.containsKey(propertyName) && mapProperties.get(propertyName).equals(value))
                found.add((TiledMapTileMapObject)mapObject);
        }

        return found;
    }

    /**
     * Gets all tile objects in every layer that has a property whose value matches the one given.
     * Because {@link TiledMapTileMapObject}s have two kinds of properties (default and own properties), this method will merge the properties
     * before making the search.
     * @param propertyName the property name. <strong>use *</strong> to retrieve everything
     * @param value the property value to check
     * @return an array of found objects
     */
    public Array<TiledMapTileMapObject> getTileObjects(String propertyName, String value) {
        Array<TiledMapTileMapObject> found = new Array<>();

        for (MapLayer mapLayer : tiledMap.getLayers()) {
            for (MapObject mapObject : mapLayer.getObjects()) {
                if (!(mapObject instanceof  TiledMapTileMapObject))
                    continue;

                MapProperties mapProperties = mapObject.getProperties();

                TiledMapTileMapObject tiledMapTileMapObject = (TiledMapTileMapObject)mapObject;
                TiledMapTile tiledMapTile = tiledMapTileMapObject.getTile();
                MapProperties defaultProperties = tiledMapTile.getProperties();

                if (propertyName.equals("*")) {
                    found.add((TiledMapTileMapObject) mapObject);
                    continue;
                }

                // Copy all the default properties
                Iterator<String> propertyKeys = defaultProperties.getKeys();
                while (propertyKeys.hasNext()) {
                    String key = propertyKeys.next();

                    if (mapProperties.containsKey(key))
                        continue;

                    mapProperties.put(key, defaultProperties.get(key));
                }

                if (mapProperties.containsKey(propertyName) && mapProperties.get(propertyName).equals(value))
                    found.add((TiledMapTileMapObject)mapObject);
            }
        }

        return found;
    }
}
























