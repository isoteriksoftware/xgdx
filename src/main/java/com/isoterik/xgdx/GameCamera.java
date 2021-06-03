package com.isoterik.xgdx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * A GameCamera encapsulates a {@link Camera} object. Concrete subclasses makes use of a specific kind of camera.
 * The class also uses {@link GameUnitsHelper} object to define the visible regions of the world at a time (viewport) and also for unit conversions.
 *
 * @author isoteriksoftware
 */
public abstract class GameCamera {
    protected Camera camera;

    public Viewport getViewport() {
        return viewport;
    }

    protected Viewport viewport;

    protected GameUnitsHelper gameUnitsHelper;

    /**
     * Creates a new scene given a viewport and an instance of {@link GameUnitsHelper} for unit conversions.
     * <strong>Note:</strong> the {@link Camera} is retrieved from the viewport passed.
     * @param viewport the viewport
     * @param gameUnitsHelper an instance of {@link GameUnitsHelper}
     */
    public GameCamera(Viewport viewport, GameUnitsHelper gameUnitsHelper) {
        this.viewport = viewport;
        setup(viewport, gameUnitsHelper);
    }

    /**
     * Creates a new instance given an instance of {@link GameUnitsHelper} for unit conversions. The viewport defaults to an {@link ExtendViewport}.
     * @param gameUnitsHelper an instance of {@link GameUnitsHelper}
     * @param camera the camera to use
     */
    public GameCamera(GameUnitsHelper gameUnitsHelper, Camera camera)
    { this(new ExtendViewport(gameUnitsHelper.getWorldWidth(), gameUnitsHelper.getWorldHeight(),
            gameUnitsHelper.getWorldWidth(), gameUnitsHelper.getWorldHeight(), camera), gameUnitsHelper); }

    /**
     *
     * @return the {@link GameUnitsHelper} that defines the visible region of the world seen by this camera at a time
     */
    public GameUnitsHelper getUnitsHelper()
    { return gameUnitsHelper; }

    /**
     * Changes the viewport and world units used by this camera
     * @param viewport the new viewport
     * @param gameUnitsHelper the new world units
     */
    public void setup(Viewport viewport, GameUnitsHelper gameUnitsHelper) {
        this.gameUnitsHelper = gameUnitsHelper;
        this.viewport = viewport;
        this.camera = viewport.getCamera();
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

    /**
     * Called when the screen is resized.
     * This method is called internally and should never be called directly
     * @param width the new screen width
     * @param height the new screen height
     */
    public void __resize(int width, int height)
    { viewport.update(width, height, true); }

    /**
     * Called when the {@link Scene} is disposing used resources.
     */
    public abstract void __dispose();
}