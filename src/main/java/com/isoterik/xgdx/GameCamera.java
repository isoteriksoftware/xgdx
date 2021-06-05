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
public abstract class GameCamera {
    protected Camera camera;

    protected Viewport viewport;

    protected GameUnits gameUnits;

    /**
     * Creates a new scene given a viewport and an instance of {@link GameUnits} for unit conversions.
     * <strong>Note:</strong> the {@link Camera} is retrieved from the viewport passed.
     * @param viewport the viewport
     * @param gameUnits an instance of {@link GameUnits}
     */
    public GameCamera(Viewport viewport, GameUnits gameUnits) {
        this.viewport = viewport;
        setup(viewport, gameUnits);
    }

    /**
     * Creates a new instance given an instance of {@link GameUnits} for unit conversions. The viewport defaults to an {@link ExtendViewport}.
     * @param gameUnits an instance of {@link GameUnits}
     * @param camera the camera to use
     */
    public GameCamera(GameUnits gameUnits, Camera camera)
    { this(new ExtendViewport(gameUnits.getWorldWidth(), gameUnits.getWorldHeight(),
            gameUnits.getWorldWidth(), gameUnits.getWorldHeight(), camera), gameUnits); }

    /**
     *
     * @return the {@link GameUnits} that defines the visible region of the world seen by this camera at a time
     */
    public GameUnits getWorldUnits()
    { return gameUnits; }

    /**
     * Changes the viewport and world units used by this camera
     * @param viewport the new viewport
     * @param gameUnits the new world units
     */
    public void setup(Viewport viewport, GameUnits gameUnits) {
        this.gameUnits = gameUnits;
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