package io.github.isoteriktech.xgdx.x3d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.isoteriktech.xgdx.GameCamera;
import io.github.isoteriktech.xgdx.GameObject;
import io.github.isoteriktech.xgdx.XGdx;
import io.github.isoteriktech.xgdx.utils.GameWorldUnits;

public class GameCamera3d extends GameCamera {
    protected ModelBatch modelBatch;
    protected Environment environment;

    /**
     * Creates a new instance given a viewport.
     * Creates a default environment with an Ambient and Directional light.
     * * <strong>Note:</strong> an {@link com.badlogic.gdx.graphics.PerspectiveCamera} will be created if it doesn't exist.
     * @param viewport the viewport
     * @param fieldOfView the field of view of the height, in degrees, the field of view for the width will be calculated
     *                    according to the aspect ratio.
     * @param near the near clipping plane distance, has to be positive
     * @param far the far clipping plane distance, has to be positive
     */
    public GameCamera3d(Viewport viewport, float fieldOfView, float near, float far) {
        super(viewport);
        setup(viewport, fieldOfView, near, far);
        modelBatch = new ModelBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
    }

    private void prepCamera(float near, float far) {
        camera.position.set(5, 5, 5);
        camera.lookAt(0, 0, 0);
        camera.near = near;
        camera.far = far;
        camera.update();
    }

    /**
     * Creates a new instance given an instance of {@link GameWorldUnits} for unit conversions. The viewport defaults to an {@link ExtendViewport}.
     * Creates a default environment with an Ambient and Directional light.
     * @param gameWorldUnits an instance of {@link GameWorldUnits}
     * @param fieldOfView the field of view of the height, in degrees, the field of view for the width will be calculated
     *                    according to the aspect ratio.
     * @param near the near clipping plane distance, has to be positive
     * @param far the far clipping plane distance, has to be positive
     */
    public GameCamera3d(GameWorldUnits gameWorldUnits, float fieldOfView, float near, float far) {
        this(new ExtendViewport(gameWorldUnits.getWorldWidth(), gameWorldUnits.getWorldHeight(),
                        gameWorldUnits.getWorldWidth(), gameWorldUnits.getWorldHeight()),
                fieldOfView, near, far);
    }

    /**
     * Creates a new scene. The screen dimensions and camera properties are taken from {@link XGdx#defaultSettings}.
     * Creates a default environment with an Ambient and Directional light.
     * The viewport defaults to an {@link ExtendViewport}.
     */
    public GameCamera3d() {
        this(new GameWorldUnits(XGdx.instance().defaultSettings.VIEWPORT_WIDTH, XGdx.instance().defaultSettings.VIEWPORT_HEIGHT,
                XGdx.instance().defaultSettings.PIXELS_PER_UNIT), XGdx.instance().defaultSettings.CAMERA_FIELD_OF_VIEW,
                XGdx.instance().defaultSettings.CAMERA_NEAR, XGdx.instance().defaultSettings.CAMERA_FAR);
    }

    @Override
    public PerspectiveCamera getCamera() {
        return (PerspectiveCamera) camera;
    }

    /**
     * Sets up the camera using the provided viewport and settings defined in {@link XGdx#defaultSettings}.
     * @param viewport the new viewport
     */
    @Override
    public void setup(Viewport viewport) {
        super.setup(viewport);
        setup(viewport, XGdx.instance().defaultSettings.CAMERA_FIELD_OF_VIEW,
                XGdx.instance().defaultSettings.CAMERA_NEAR, XGdx.instance().defaultSettings.CAMERA_FAR);
    }

    /**
     * Sets up the camera using the provided viewport and other camera settings.
     * @param viewport the viewport
     * @param fieldOfView the field of view of the height, in degrees, the field of view for the width will be calculated
     *                    according to the aspect ratio.
     * @param near the near clipping plane distance, has to be positive
     * @param far the far clipping plane distance, has to be positive
     */
    public void setup(Viewport viewport, float fieldOfView, float near, float far) {
        super.setup(viewport);

        if (camera == null || !(camera instanceof PerspectiveCamera)) {
            camera = new PerspectiveCamera(fieldOfView, viewport.getWorldWidth(), viewport.getWorldHeight());
            prepCamera(near, far);

            viewport.setCamera(camera);
        }
    }

    public ModelBatch getModelBatch() {
        return modelBatch;
    }

    public void setModelBatch(ModelBatch modelBatch) {
        this.modelBatch = modelBatch;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void destroy() {
        if (modelBatch != null)
            modelBatch.dispose();
    }

    @Override
    public void preRender(Array<GameObject> gameObjects) {
        ScreenUtils.clear(Color.BLACK, true);
        viewport.apply(centerCameraOnResize);
        modelBatch.begin(camera);
    }

    @Override
    public void postRender(Array<GameObject> gameObjects) {
        modelBatch.end();
    }
}
