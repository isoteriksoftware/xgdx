package com.isoterik.xgdx.test;

import com.badlogic.gdx.graphics.Texture;
import com.isoterik.xgdx.GameObject;
import com.isoterik.xgdx.Scene;
import com.isoterik.xgdx.asset.GameAssetsLoader;

public class SpriteRenderingTest extends Scene  {
    public SpriteRenderingTest() {
        GameAssetsLoader assetsLoader = xGdx.assets;
        assetsLoader.enqueueAsset("badlogic.jpg", Texture.class);
        assetsLoader.loadAssetsNow();

        GameObject badlogic = newSpriteObject(assetsLoader.regionForTexture("badlogic.jpg", true));
        addGameObject(badlogic);
    }
}
