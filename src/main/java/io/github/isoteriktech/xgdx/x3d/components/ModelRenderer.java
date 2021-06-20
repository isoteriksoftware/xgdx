package io.github.isoteriktech.xgdx.x3d.components;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import io.github.isoteriktech.xgdx.Component;
import io.github.isoteriktech.xgdx.GameCamera;
import io.github.isoteriktech.xgdx.GameObject;
import io.github.isoteriktech.xgdx.Transform;
import io.github.isoteriktech.xgdx.x3d.GameCamera3d;

public class ModelRenderer extends Component {
    protected GameCamera3d gameCamera;

    protected Model model;
    protected ModelInstance modelInstance;
    protected Environment environment;

    protected boolean visible;

    public ModelRenderer(Model model) {
        this.model = model;
        modelInstance = new ModelInstance(model);
        visible = true;
    }

    /**
     * Changes the visibility of the model.
     * @param visible whether model should be rendered or not
     */
    public void setVisible(boolean visible)
    { this.visible = visible; }

    /**
     *
     * @return whether the model is currently rendered or not
     */
    public boolean isVisible()
    { return visible; }

    @Override
    public void postUpdate(float deltaTime) {
        Transform transform = gameObject.transform;
        Matrix4 matrix = modelInstance.transform;

        matrix.setFromEulerAngles(transform.getRotationZ(), transform.getRotationY(), transform.getRotationX())
            .trn(transform.position).scl(transform.scale);
    }

    @Override
    public void render(Array<GameObject> gameObjects) {
        // Render only if visible
        if (!visible)
            return;

        // Use the default mainCamera if none is provided
        if (gameCamera == null) {
            GameCamera camera = scene.getMainCamera().getComponent(GameCamera.class);
            if (!(camera instanceof GameCamera3d))
                return;

            gameCamera = (GameCamera3d) camera;
        }

        // Use the default mainCamera environment if none is provided
        if (environment == null) {
            environment = gameCamera.getEnvironment();
        }

        if (environment == null)
            gameCamera.getModelBatch().render(modelInstance);
        else
            gameCamera.getModelBatch().render(modelInstance, environment);
    }
}














