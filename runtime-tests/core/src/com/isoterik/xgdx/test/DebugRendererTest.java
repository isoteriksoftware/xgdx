package com.isoterik.xgdx.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.isoterik.xgdx.GameObject;
import com.isoterik.xgdx.Scene;
import com.isoterik.xgdx.x2d.components.debug.BoxDebugRenderer;
import com.isoterik.xgdx.x2d.components.debug.CircleDebugRenderer;
import com.isoterik.xgdx.x2d.components.debug.EllipseDebugRenderer;
import com.isoterik.xgdx.x2d.components.debug.XDebugRenderer;

public class DebugRendererTest extends Scene {
    public DebugRendererTest() {
        setBackgroundColor(Color.BLACK);
        setRenderCustomDebugLines(true);

        GameObject boundary = GameObject.newInstance("BG");
        addGameObject(boundary);
        boundary.transform.setSize(gameWorldUnits.getWorldWidth(), gameWorldUnits.getWorldHeight());
        boundary.addComponent(new BoxDebugRenderer(ShapeRenderer.ShapeType.Filled, Color.DARK_GRAY));

        GameObject box = GameObject.newInstance("Box");
        addGameObject(box);
        box.addComponent(new BoxDebugRenderer().setColor(Color.GREEN));
        box.addComponent(new XDebugRenderer());
        box.addComponent(new CircleDebugRenderer().setColor(Color.YELLOW));
        box.addComponent(new EllipseDebugRenderer().setColor(Color.BLUE));
        box.transform.setSize(2, 3);
        box.transform.setPosition((gameWorldUnits.getWorldWidth() - 2) * .5f,
                (gameWorldUnits.getWorldHeight() - 3) * .5f);
    }
}
