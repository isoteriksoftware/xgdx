package com.isoterik.xgdx.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMap;
import io.github.isoteriktech.xgdx.GameObject;
import io.github.isoteriktech.xgdx.Scene;
import io.github.isoteriktech.xgdx.asset.GameAssetsLoader;
import io.github.isoteriktech.xgdx.x2d.components.renderer.TiledMapRenderer;

public class TiledMapTest extends Scene {
    private TiledMapRenderer tiledMapRenderer;

    @Override
    protected void onConstruction() {
        GameAssetsLoader gameAssetsLoader = xGdx.assets;
        gameAssetsLoader.enqueueAsset("maps/tiled.tmx", TiledMap.class);
        gameAssetsLoader.loadAssetsNow();
        tiledMapRenderer = new TiledMapRenderer(gameAssetsLoader.getAsset("maps/tiled.tmx", TiledMap.class),
                1/21f);

        xGdx.defaultSettings.VIEWPORT_WIDTH = tiledMapRenderer.mapWidth;
        xGdx.defaultSettings.VIEWPORT_HEIGHT = tiledMapRenderer.mapHeight;
        xGdx.defaultSettings.PIXELS_PER_UNIT = 21;
    }

    public TiledMapTest() {
        setBackgroundColor(Color.BLACK);

        GameObject tiledMapObject = GameObject.newInstance("TiledMap");
        addGameObject(tiledMapObject);
        tiledMapObject.addComponent(tiledMapRenderer);
    }
}
