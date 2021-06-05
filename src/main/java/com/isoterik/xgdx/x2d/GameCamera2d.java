package com.isoterik.xgdx.x2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.isoterik.xgdx.GameCamera;
import com.isoterik.xgdx.XGdx;
import com.isoterik.xgdx.utils.GameUnits;

/**
 * A game camera that wraps an {@link OrthographicCamera} for rendering in 2D space.
 *
 * @author isoteriksoftware
 */
public class GameCamera2d extends GameCamera {
    private SpriteBatch spriteBatch;

    private Color backgroundColor;

    /**
     * Creates a new scene given a viewport and an instance of {@link GameUnits} for unit conversions.
     * <strong>Note:</strong> an {@link OrthographicCamera} will be created.
     * @param viewport the viewport
     * @param gameUnits an instance of {@link GameUnits}
     */
    public GameCamera2d(Viewport viewport, GameUnits gameUnits) {
        super(viewport, gameUnits);
        this.spriteBatch = new SpriteBatch();
        this.backgroundColor = new Color(1, 0, 0, 1);

        if (camera == null || !(camera instanceof  OrthographicCamera))
            camera = new OrthographicCamera(gameUnits.getWorldWidth(), gameUnits.getWorldHeight());

        getCamera().position.set(gameUnits.getWorldWidth() * .5f, gameUnits.getWorldHeight() * .5f, 0);
        viewport.setCamera(camera);
    }

    /**
     * Creates a new instance given an instance of {@link GameUnits} for unit conversions. The viewport defaults to an {@link ExtendViewport}.
     * @param gameUnits an instance of {@link GameUnits}
     */
    public GameCamera2d(GameUnits gameUnits) {
        this(new ExtendViewport(gameUnits.getWorldWidth(), gameUnits.getWorldHeight(),
                new OrthographicCamera(gameUnits.getWorldWidth(), gameUnits.getWorldHeight())),
                gameUnits);
    }

    /**
     * Creates a new scene. The screen dimensions defaults to (1280 x 720) taking 100 pixels per meter for unit conversions.
     * The viewport defaults to an {@link ExtendViewport}.
     */
    public GameCamera2d() {
        this(new GameUnits(XGdx.instance().defaultSettings.VIEWPORT_WIDTH, XGdx.instance().defaultSettings.VIEWPORT_HEIGHT,
                XGdx.instance().defaultSettings.PIXELS_PER_UNIT));
    }

    /**
     * Sets the sprite batch used for rendering
     * @param spriteBatch the sprite batch
     */
    public void setSpriteBatch(SpriteBatch spriteBatch)
    { this.spriteBatch = spriteBatch; }

    /**
     *
     * @return the sprite batch used for rendering
     */
    public SpriteBatch getSpriteBatch()
    { return spriteBatch; }

    /**
     * Sets the color used for clearing the scene every frame.
     * @param backgroundColor color used for clearing the scene every frame
     */
    public void setBackgroundColor(Color backgroundColor)
    { this.backgroundColor = backgroundColor; }

    /**
     *
     * @return color used for clearing the scene every frame
     */
    public Color getBackgroundColor()
    { return backgroundColor; }

    @Override
    public OrthographicCamera getCamera()
    { return (OrthographicCamera)camera; }

    @Override
    public void setup(Viewport viewport, GameUnits gameUnits) {
        super.setup(viewport, gameUnits);
        camera = new OrthographicCamera(gameUnits.getWorldWidth(), gameUnits.getWorldHeight());
        camera.position.set(gameUnits.getWorldWidth() * .5f, gameUnits.getWorldHeight() * .5f, 0);
        viewport.setCamera(camera);
    }

    @Override
    public void __dispose()
    { spriteBatch.dispose(); }
}





























