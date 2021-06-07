package com.isoterik.xgdx.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import io.github.isoteriktech.xgdx.GameObject;
import io.github.isoteriktech.xgdx.Scene;
import io.github.isoteriktech.xgdx.asset.GameAssetsLoader;
import io.github.isoteriktech.xgdx.input.KeyCodes;
import io.github.isoteriktech.xgdx.input.KeyTrigger;
import io.github.isoteriktech.xgdx.x2d.GameCamera2d;
import io.github.isoteriktech.xgdx.x2d.components.renderer.TiledMapRenderer;

public class GameCamera2dTest extends Scene {
    private TiledMapRenderer tiledMapRenderer;
    private final float cameraSpeed = 10f;

    @Override
    protected void onConstruction() {
        GameAssetsLoader gameAssetsLoader = xGdx.assets;
        gameAssetsLoader.enqueueAsset("maps/tiled.tmx", TiledMap.class);
        gameAssetsLoader.loadAssetsNow();
        tiledMapRenderer = new TiledMapRenderer(gameAssetsLoader.getAsset("maps/tiled.tmx", TiledMap.class),
                1/21f);

        xGdx.defaultSettings.VIEWPORT_WIDTH = tiledMapRenderer.mapWidth/2f;
        xGdx.defaultSettings.VIEWPORT_HEIGHT = tiledMapRenderer.mapHeight/2f;
        xGdx.defaultSettings.PIXELS_PER_UNIT = 21;
    }

    public GameCamera2dTest() {
        setBackgroundColor(Color.BLACK);

        GameObject tiledMapObject = GameObject.newInstance("TiledMap");
        addGameObject(tiledMapObject);
        tiledMapObject.addComponent(tiledMapRenderer);

        GameCamera2d camera2d = (GameCamera2d) getMainCamera();
        OrthographicCamera camera = camera2d.getCamera();

        input.addKeyListener(KeyTrigger.keyDownTrigger(KeyCodes.RIGHT).setPolled(true), (mappingName, eventData) -> {
            float deltaTime = xGdx.getDeltaTime();
            camera.translate(cameraSpeed * deltaTime, 0);
        });

        input.addKeyListener(KeyTrigger.keyDownTrigger(KeyCodes.LEFT).setPolled(true), (mappingName, eventData) -> {
            float deltaTime = xGdx.getDeltaTime();
            camera.translate(-cameraSpeed * deltaTime, 0);
        });

        input.addKeyListener(KeyTrigger.keyDownTrigger(KeyCodes.UP).setPolled(true), (mappingName, eventData) -> {
            float deltaTime = xGdx.getDeltaTime();
            camera.translate(0, cameraSpeed * deltaTime);
        });

        input.addKeyListener(KeyTrigger.keyDownTrigger(KeyCodes.DOWN).setPolled(true), (mappingName, eventData) -> {
            float deltaTime = xGdx.getDeltaTime();
            camera.translate(0, -cameraSpeed * deltaTime);
        });
    }
}


















