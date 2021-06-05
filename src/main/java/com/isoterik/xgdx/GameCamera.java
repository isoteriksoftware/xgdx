package com.isoterik.xgdx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.isoterik.xgdx.utils.GameUnits;

/**
 * A GameCamera encapsulates a {@link Camera} object. Concrete subclasses makes use of a specific kind of camera.
 * The class also uses {@link GameUnits} object to define the visible regions of the world at a time (viewport) and also for unit conversions.
 *
 * @see com.isoterik.xgdx.x2d.GameCamera2d
 *
 * @author isoteriksoftware
 */
public abstract class GameCamera extends Component {
    protected Camera camera;

    protected Viewport viewport;

    /**
     * Creates a new instance given a viewport.
     * <strong>Note:</strong> the {@link Camera} is retrieved from the viewport passed.
     * @param viewport the viewport
     */
    public GameCamera(Viewport viewport) {
        this.viewport = viewport;
        setup(viewport);
    }

    /**
     * Creates a new instance given an instance of {@link GameUnits} for viewport dimensions. The viewport defaults to an {@link ExtendViewport}.
     * @param gameUnits an instance of {@link GameUnits}
     * @param camera the camera to use
     */
    public GameCamera(GameUnits gameUnits, Camera camera)
    { this(new ExtendViewport(gameUnits.getWorldWidth(), gameUnits.getWorldHeight(),
            gameUnits.getWorldWidth(), gameUnits.getWorldHeight(), camera)); }

    /**
     * Changes the viewport used by this camera
     * @param viewport the new viewport
     */
    public void setup(Viewport viewport) {
        this.viewport = viewport;
        this.camera = viewport.getCamera();
    }

    public Viewport getViewport() {
        return viewport;
    }

    /**
     *
     * @return the viewport width
     */
    public float getViewportWidth()
    { return camera.viewportWidth; }

    /**
     *
     * @return the viewport height
     */
    public float getViewportHeight()
    { return camera.viewportHeight; }

    /**
     *
     * @return the {@link Camera} object used internally
     */
    public Camera getCamera()
    { return camera; }

    @Override
    public void resize(int newScreenWidth, int newScreenHeight) {
        viewport.update(newScreenWidth, newScreenHeight, true);
    }
}