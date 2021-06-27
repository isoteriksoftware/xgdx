package com.isoterik.xgdx.test;

import com.badlogic.gdx.utils.Timer;
import io.github.isoteriktech.xgdx.Scene;
import io.github.isoteriktech.xgdx.x2d.scenes.transition.SceneTransitions;

public class SceneTest extends Scene {
    @Override
    public void transitionedToThisScene(Scene previousScene) {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                xGdx.setScene(new DebugRendererTest(), SceneTransitions.fade(1f));
            }
        }, 1);
    }
}
