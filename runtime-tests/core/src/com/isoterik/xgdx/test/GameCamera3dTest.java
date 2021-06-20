package com.isoterik.xgdx.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import io.github.isoteriktech.xgdx.Component;
import io.github.isoteriktech.xgdx.GameObject;
import io.github.isoteriktech.xgdx.Scene;
import io.github.isoteriktech.xgdx.x2d.components.debug.BoxDebugRenderer;
import io.github.isoteriktech.xgdx.x3d.components.ModelRenderer;

public class GameCamera3dTest extends Scene {
    public GameCamera3dTest() {
        super(true);
        setRenderCustomDebugLines(true);

        GameObject bg = GameObject.newInstance();
        bg.transform.setSize(gameWorldUnits.getWorldWidth(), gameWorldUnits.getWorldHeight());
        bg.addComponent(new BoxDebugRenderer().setColor(Color.WHITE));
        addGameObject(bg);

        ModelBuilder modelBuilder = new ModelBuilder();
        Model model = modelBuilder.createBox(1f, 1f, 1f,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        ModelRenderer modelRenderer = new ModelRenderer(model);
        GameObject gameObject = GameObject.newInstance();
        gameObject.addComponent(modelRenderer);
        addGameObject(gameObject);

        gameObject.addComponent(new Component(){
            @Override
            public void update(float deltaTime) {
                gameObject.transform.rotation.y += 10 * deltaTime;
            }
        });
    }
}
