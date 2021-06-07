package com.isoterik.xgdx.test;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import io.github.isoterik.xgdx.Component;
import io.github.isoterik.xgdx.GameObject;
import io.github.isoterik.xgdx.Scene;

public class ComponentTest extends Scene {
    public ComponentTest() {
        GameObject gameObject = GameObject.newInstance();
        gameObject.addComponent(new TestComponent());
        addGameObject(gameObject);

        setRenderCustomDebugLines(true);
    }

    private static class TestComponent extends Component {
        @Override
        public void attach() {
            System.out.println("attach() called");
        }

        @Override
        public void start() {
            System.out.println("start() called");
        }

        @Override
        public void resume() {
            System.out.println("resume() called");
        }

        @Override
        public void preUpdate(float deltaTime) {
            System.out.println("preUpdate() called");
        }

        @Override
        public void update(float deltaTime) {
            System.out.println("update() called");
        }

        @Override
        public void postUpdate(float deltaTime) {
            System.out.println("postUpdate() called");
        }

        @Override
        public void resize(int newScreenWidth, int newScreenHeight) {
            System.out.println("resize() called: " + newScreenWidth + "x" + newScreenHeight);
        }

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

        @Override
        public void drawDebugLine(ShapeRenderer shapeRenderer) {
            System.out.println("drawDebugLine() called");
        }

        @Override
        public void drawDebugFilled(ShapeRenderer shapeRenderer) {
            System.out.println("drawDebugFilled() called");
        }

        @Override
        public void drawDebugPoint(ShapeRenderer shapeRenderer) {
            System.out.println("drawDebugPoint() called");
        }

        @Override
        public void pause() {
            System.out.println("pause() called");
        }

        @Override
        public void detach() {
            System.out.println("detach() called");
        }

        @Override
        public void stop() {
            System.out.println("stop() called");
        }

        @Override
        public void destroy() {
            System.out.println("destroy() called");
        }
    }
}





















