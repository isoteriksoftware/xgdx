package com.isoterik.xgdx.test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import io.github.isoterik.xgdx.ActorGameObject;
import io.github.isoterik.xgdx.Scene;
import io.github.isoterik.xgdx.asset.GameAssetsLoader;

public class ActorGameObjectTest extends Scene {
    public ActorGameObjectTest() {
        GameAssetsLoader assetsLoader = xGdx.assets;
        assetsLoader.enqueueAsset("badlogic.jpg", Texture.class);
        assetsLoader.loadAssetsNow();

        ActorGameObject badlogic = newActorSpriteObject(assetsLoader.regionForTexture("badlogic.jpg", true));
        addGameObject(badlogic);

        Action shrink = Actions.scaleTo(.5f, .5f, .6f);
        Action grow = Actions.scaleTo(1f, 1f, .6f);
        Action turn = Actions.rotateBy(360, 1f);
        Action animation = Actions.forever(Actions.parallel(turn, Actions.sequence(shrink, grow)));

        badlogic.actorTransform.actor.addAction(animation);
    }
}
