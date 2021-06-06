package com.isoterik.xgdx.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.isoterik.xgdx.Component;
import com.isoterik.xgdx.GameObject;
import com.isoterik.xgdx.Scene;
import com.isoterik.xgdx.x2d.GameCamera2d;
import com.isoterik.xgdx.x2d.components.debug.BoxDebugRenderer;

public class ComponentTest extends Scene {
    public ComponentTest() {
        GameObject gameObject = GameObject.newInstance();
       // gameObject.addComponent(new TestComponent());
        gameObject.addComponent(new BoxDebugRenderer());
        gameObject.transform.setSize(1, 1);
        addGameObject(gameObject);

        setBackgroundColor(Color.BLACK);
    }

    private static class TestComponent extends Component {
        @Override
        public void preRender(Array<GameObject> gameObjects) {
            System.out.println("preRender() called");
        }

        @Override
        public void render(Array<GameObject> gameObjects) {
            System.out.println("render() called");
        }

        @Override
        public void postRender(Array<GameObject> gameObjects) {
            System.out.println("postRender() called");
        }
    }
}





















