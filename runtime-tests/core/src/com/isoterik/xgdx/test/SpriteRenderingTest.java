package com.isoterik.xgdx.test;

import com.badlogic.gdx.graphics.Texture;
import io.github.isoteriktech.xgdx.GameObject;
import io.github.isoteriktech.xgdx.Scene;
import io.github.isoteriktech.xgdx.asset.GameAssetsLoader;

public class SpriteRenderingTest extends Scene {
    public SpriteRenderingTest() {
        GameAssetsLoader assetsLoader = xGdx.assets;
        assetsLoader.enqueueAsset("badlogic.jpg", Texture.class);
        assetsLoader.loadAssetsNow();

        GameObject badlogic = newSpriteObject(assetsLoader.regionForTexture("badlogic.jpg", true));
        addGameObject(badlogic);
    }
}
