package com.isoterik.xgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.isoterik.xgdx.input.InputManager;
import com.isoterik.xgdx.x2d.GameCamera2d;
import com.isoterik.xgdx.x2d.components.SpriteRenderer;
import com.isoterik.xgdx.utils.GameUnits;

/**
 * A Scene contains the {@link GameObject}s of your game. Think of each Scene as a unique level of your game.
 * Every scene has its own {@link InputManager} for managing input.
 * <p>
 * A {@link GameCamera} is used to display a portion of the scene or the whole scene at a time. While its possible to use multiple cameras, scenes currently
 * support only one main camera for projection.
 * <p>
 *
 * {@link GameObject}s are manged with {@link Layer}s.
 * Layers are processed top-down; layers added first are processed first (this can be used to manipulate how GameObjects are rendered.)
 * A default layer is provided so you don't have to use layers if you don't need to.
 * <p>
 * Every scene has a {@link Stage} instance for working with UI elements. The stage is already setup to update, receive input and render; you don't have do these yourself.
 *
 * @author isoteriksoftware
 */
public class Scene {
    /** A reference to the shared instance of {@link XGdx} */
    XGdx xGdx;

    /** The name of the default layer. Use this to add {@link GameObject}s to the default layer. */
    public static final String DEFAULT_LAYER = "MGDX_DEFAULT_LAYER";

    private final Layer defaultLayer;
    protected Array<Layer> layers;

    /** The main camera object used for projecting a portion of the scene. */
    protected GameObject mainCamera;

    /** The default {@link GameUnits} used for this scene */
    protected GameUnits gameUnits;

    /** The input manager for handling input. */
    protected final InputManager inputManager;

    /* For components iteration that needs the current delta time */
    private float deltaTime;

    // These iteration listeners prevent us from creating new instances every time!
    protected GameObject.__ComponentIterationListener startIter, pauseIter,
            resumeIter, updateIter, resizeIter, lateUpdateIter, renderIter,
            debugLineIter, debugFilledIter, debugPointIter, destroyIter;

    // The state of the Scene
    private boolean isActive;

    /** {@link com.badlogic.gdx.scenes.scene2d.Stage} instance used for managing UI elements */
    protected Stage canvas;

    /** {@link com.badlogic.gdx.scenes.scene2d.Stage} instance used for managing GameObjects that wants to use the Scene2d API for animations */
    protected Stage animationCanvas;

    /** ShapeRenderer for debug drawings */
    protected ShapeRenderer shapeRenderer;

    /** This flag determines whether custom debug renderings should be done. */
    protected boolean renderCustomDebugLines;

    /** Determines whether this stack can be stacked. */
    protected boolean stackable = true;

    private int resizedWidth, resizedHeight;

    // An array of game objects
    Array<GameObject> gameObjects = new Array<>();

    /**
     * Creates a new instance given a gravity.
     */
    public Scene() {
        xGdx = XGdx.instance();

        onConstruction();

        defaultLayer = new Layer(DEFAULT_LAYER);
        layers = new Array<>();
        layers.add(defaultLayer);

        mainCamera = GameObject.newInstance("MainCamera");

        inputManager = new InputManager(this);

        startIter = component -> {
            if (component.isEnabled())
                component.start();
        };

        resumeIter = component -> {
            if (component.isEnabled())
                component.resume();
        };

        pauseIter = component -> {
            if (component.isEnabled())
                component.pause();
        };

        resizeIter = component -> {
            if (component.isEnabled())
                component.resize(resizedWidth, resizedHeight);
        };

        updateIter = component -> {
            if (component.isEnabled())
                component.update(deltaTime);
        };

        lateUpdateIter = component -> {
            if (component.isEnabled())
                component.postUpdate(deltaTime);
        };

        renderIter = component -> {
            if (component.isEnabled())
                component.render();
        };

        debugLineIter = component -> {
            if (component.isEnabled())
                component.drawDebugLine(shapeRenderer);
        };

        debugFilledIter = component -> {
            if (component.isEnabled())
                component.drawDebugFilled(shapeRenderer);
        };

        debugPointIter = component -> {
            if (component.isEnabled())
                component.drawDebugPoint(shapeRenderer);
        };

        destroyIter = Component::destroy;

        GameCamera camera = new GameCamera2d();
        mainCamera.addComponent(camera);

        gameUnits = new GameUnits(xGdx.defaultSettings.VIEWPORT_WIDTH, xGdx.defaultSettings.VIEWPORT_HEIGHT,
                xGdx.defaultSettings.PIXELS_PER_UNIT);

        setupCanvas(new StretchViewport(gameUnits.getScreenWidth(),
                gameUnits.getScreenHeight()));
        setupAnimationCanvas(camera.getViewport());

        shapeRenderer = new ShapeRenderer();
    }

    /**
     * This is called during construction before instance fields are initialized. This is useful for setting default properties
     * that will be used during construction.
     *
     * <strong>Most instance fields are not initialized yet, it is not safe to make use of them here!</strong>
     */
    protected void onConstruction() {
    }

    /**
     * @return the current {@link GameUnits} instance.
     */
    public GameUnits getGameUnits() {
        return gameUnits;
    }

    /**
     *
     * @return whether this scene can be stacked
     */
    public boolean isStackable()
    { return stackable; }

    /**
     * Stackable scenes are scenes that can be added to a stack when the {@link SceneManager} switches scenes. Instances of stackable scenes are always retained and can
     * be switched back to using the same instance. Scenes that are not stackable are disposed as soon as the scene manager switches from them.
     * <p>
     * <strong>A good rule of thumb:</strong>
     * <ul>
     *     <li>
     *         If the scene takes a considerable amount of time to load resources and the scene is very likely to be returned to then it may be a
     *         good choice to make it stackable. That way the resources are loaded only once.
     *     </li>
     *     <li>
     *         If the scene is very resource intensive and other scenes need to be loaded then it may be a good
     *         idea to NOT make it stackable. That way the resources allocated by that scene is disposed as soon as it is no longer needed.
     *     </li>
     *     <li>
     *         If the scene is a UI scene (like a main menu scene) then it may be a good idea to make it stackable since UI scenes are usually visited many times.
     *     </li>
     * </ul>
     * <p>
     *
     * <strong>Scenes are stackable by default</strong>
     * @param stackable whether this scene should be stackable
     */
    public void setStackable(boolean stackable)
    { this.stackable = stackable; }

    /**
     * Custom debug lines can be rendered around game objects. This is useful for debugging purposes.
     * This is also useful for tracking invisible game objects (game objects that are not rendered).
     * Use this method to decide if those debug lines should be rendered or not.
     * @param renderCustomDebugLines whether custom debug lines are rendered
     */
    public void setRenderCustomDebugLines(boolean renderCustomDebugLines)
    { this.renderCustomDebugLines = renderCustomDebugLines; }

    /**
     *
     * @return whether custom debug lines are rendered or not
     */
    public boolean isRenderCustomDebugLines()
    { return renderCustomDebugLines; }

    /**
     * By default, the ui canvas (an instance of {@link Stage}) is setup with an {@link com.badlogic.gdx.utils.viewport.StretchViewport}.
     * Use this method to change the viewport to your desired viewport.
     * @param viewport a viewport for scaling UI elements
     */
    public void setupCanvas(Viewport viewport) {
        if (canvas != null)
            inputManager.getInputMultiplexer().removeProcessor(canvas);

        canvas = new Stage(viewport);
        inputManager.getInputMultiplexer().addProcessor(canvas);
    }

    /**
     * By default, the animation canvas (an instance of {@link Stage}) is setup with the same viewport as the main camera
     * Use this method to change the viewport to your desired viewport.
     * @param viewport the viewport for scaling UI elements
     */
    public void setupAnimationCanvas(Viewport viewport) {
        animationCanvas = new Stage(viewport);
    }

    /**
     *
     * @return the {@link Stage} used for managing UI elements.
     */
    public Stage getCanvas()
    { return canvas; }

    /**
     * A scene becomes active when the scene is resumed. It goes back to an inactive state when the scene is paused.
     * @return whether this scene is active or not
     */
    public boolean isActive()
    { return isActive; }

    /**
     *
     * @return the input manager for this scene
     */
    public InputManager getInputManager()
    { return inputManager; }

    /**
     * Changes the main camera used for projecting this scene.
     * @param mainCamera the main camera used for projecting this scene.
     */
    public void setMainCamera(GameCamera mainCamera) {

    }

    /**
     *
     * @return the main camera used for projecting this scene.
     */
    public GameCamera getMainCamera()
    { return mainCamera; }

    /**
     * Finds a layer given a name.
     * @param name the name of the layer to find.
     * @return the layer if found or null if not found
     */
    public Layer findLayer(String name) {
        for (Layer layer : layers) {
            if (layer.getName().equals(name))
                return layer;
        }

        return null;
    }

    /**
     *
     * @return the default layer for this scene.
     */
    public Layer getDefaultLayer()
    { return defaultLayer; }

    /**
     * Checks if a given layer is one of the layers of this scene.
     * @param layer the layer to check
     * @return true if the layer exists. false otherwise
     */
    public boolean hasLayer(Layer layer)
    { return layers.contains(layer, true); }

    /**
     * Checks if a layer with a given name is one of the layers of this scene.
     * @param layerName the name of the layer.
     * @return true if the layer exists. false otherwise.
     */
    public boolean hasLayer(String layerName) {
        for (Layer layer : layers) {
            if (layer.getName().equals(layerName))
                return true;
        }

        return false;
    }

    /**
     * Adds a new layer to this scene
     * @param layer the layer to add
     */
    public void addLayer(Layer layer)
    { layers.add(layer); }

    /**
     * Removes a given layer from this scene.
     * <strong>This will also remove all game objects that belongs to the layer!</strong>
     * @param layer the layer to remove
     * @throws IllegalArgumentException if the layer is the default layer for this scene.
     */
    public void removeLayer(Layer layer) throws IllegalArgumentException {
        if (layer == defaultLayer)
            throw new IllegalArgumentException("You cannot remove the default layer!");

        layers.removeValue(layer, true);
    }

    /**
     * Removes a layer from this scene given the name of the layer to remove.
     * <strong>This will also remove all game objects that belongs to the layer!</strong>
     * @param layerName the name of the layer to remove
     * @throws IllegalArgumentException if the layer is the default layer for this scene.
     */
    public void removeLayer(String layerName) throws IllegalArgumentException {
        if (layerName.equals(DEFAULT_LAYER))
            throw new IllegalArgumentException("You cannot remove the default layer!");

        Layer layer = findLayer(layerName);
        if (layer != null)
            layers.removeValue(layer, true);
    }

    /**
     *
     * @return the layers of this scene
     */
    public Array<Layer> getLayers()
    { return layers; }

    /**
     * Adds a game object to this scene given a layer to add it to.
     * @param gameObject the game object to add
     * @param layer the layer to add the game object to
     * @throws IllegalArgumentException if the given layer does not exist in this scene
     */
    public void addGameObject(GameObject gameObject, Layer layer) throws IllegalArgumentException {
        if (!hasLayer(layer))
            throw new IllegalArgumentException("This layer does not exist in this scene");

        // If this game object is an ActorGameObject, add it to the animation canvas
        if (gameObject instanceof ActorGameObject)
            animationCanvas.addActor(((ActorGameObject)gameObject).actorTransform.actor);

        gameObject.__setHostScene(this);
        layer.addGameObject(gameObject);

        gameObject.__forEachComponent(startIter);
    }

    /**
     * Adds a game object to this scene given the name of a layer to add it to.
     * @param gameObject the game object to add
     * @param layerName the name of the layer to add the game object to
     * @throws IllegalArgumentException if there is no existing layer with such name
     */
    public void addGameObject(GameObject gameObject, String layerName) throws IllegalArgumentException {
        Layer layer = findLayer(layerName);
        if (layer == null)
            throw new IllegalArgumentException("This layer does not exist in this scene");

        // If this game object is an ActorGameObject, add it to the animation canvas
        if (gameObject instanceof ActorGameObject)
            animationCanvas.addActor(((ActorGameObject)gameObject).actorTransform.actor);

        gameObject.__setHostScene(this);
        layer.addGameObject(gameObject);

        gameObject.__forEachComponent(startIter);
    }

    /**
     * Adds a game object to this scene. The game object is added to the default layer.
     * @param gameObject the game object to add.
     */
    public void addGameObject(GameObject gameObject) {
        // If this game object is an ActorGameObject, add it to the animation canvas
        if (gameObject instanceof ActorGameObject)
            animationCanvas.addActor(((ActorGameObject)gameObject).actorTransform.actor);

        gameObject.__setHostScene(this);
        defaultLayer.addGameObject(gameObject);

        gameObject.__forEachComponent(startIter);
    }

    /**
     * Removes a game object from this scene given the layer where the game object belongs to.
     * @param gameObject the game object to remove
     * @param layer the layer
     * @return true if the game object was removed. false otherwise.
     */
    public boolean removeGameObject(GameObject gameObject, Layer layer) {
        if (!hasLayer(layer))
            return false;

        // If this game object is an ActorGameObject, remove it to the animation canvas
        if (gameObject instanceof ActorGameObject)
            ((ActorGameObject)gameObject).actorTransform.actor.remove();

        gameObject.__removeFromScene();
        gameObject.__setHostScene(null);
        return layer.removeGameObject(gameObject);
    }

    /**
     * Removes a game object from this scene given the name of the name where the game objects belongs to.
     * @param gameObject the game object to remove
     * @param layerName the name of the layer
     * @return true if the game object was removed. false otherwise.
     */
    public boolean removeGameObject(GameObject gameObject, String layerName) {
        Layer layer = findLayer(layerName);
        if (layer == null)
            return false;

        // If this game object is an ActorGameObject, remove it to the animation canvas
        if (gameObject instanceof ActorGameObject)
            ((ActorGameObject)gameObject).actorTransform.actor.remove();

        gameObject.__removeFromScene();
        gameObject.__setHostScene(null);
        return layer.removeGameObject(gameObject);
    }

    /**
     * Removes a game object from the default layer.
     * @param gameObject the game object to remove.
     * @return true if the game object was removed. false otherwise.
     */
    public boolean removeGameObject(GameObject gameObject) {
        // If this game object is an ActorGameObject, remove it to the animation canvas
        if (gameObject instanceof ActorGameObject)
            ((ActorGameObject)gameObject).actorTransform.actor.remove();

        gameObject.__removeFromScene();
        gameObject.__setHostScene(null);
        return defaultLayer.removeGameObject(gameObject);
    }

    /**
     *
     * @return all the game objects added to this scene
     */
    public Array<GameObject> getGameObjects() {
        gameObjects.clear();

        for (Layer layer : layers) {
            gameObjects.addAll(layer.getGameObjects());
        }

        return gameObjects;
    }

    /**
     * Sets the background color of this scene if it uses a {@link GameCamera2d}
     * @param color the background color
     */
    public void setBackgroundColor(Color color) {
        if (mainCamera instanceof GameCamera2d)
            ((GameCamera2d)mainCamera).setBackgroundColor(color);
    }

    private void updateComponents(Array<GameObject> gameObjects, final float deltaTime) {
        this.deltaTime = deltaTime;
        for (GameObject go : gameObjects) {
            go.__forEachComponent(updateIter);
        }
    }

    /**
     * Called when the screen is resized.
     * <strong>DO NOT CALL THIS METHOD!</strong>
     * @param width the new width
     * @param height the new height
     */
    public void __resize(int width, int height) {
        this.resizedWidth = width;
        this.resizedHeight = height;

        mainCamera.__resize(width, height);

        Array<GameObject> gameObjects = getGameObjects();

        for (GameObject go : gameObjects) {
            go.__forEachComponent(resizeIter);
        }

        canvas.getViewport().update(width, height, true);
    }

    /**
     * Called when the scene is resumed.
     * <strong>DO NOT CALL THIS METHOD!</strong>
     */
    public void __resume() {
        isActive = true;

        Array<GameObject> gameObjects = getGameObjects();

        for (GameObject go : gameObjects) {
            go.__forEachComponent(resumeIter);
        }
    }

    /**
     * Called when this scene is paused.
     * <strong>DO NOT CALL THIS METHOD!</strong>
     */
    public void __pause() {
        isActive = false;

        Array<GameObject> gameObjects = getGameObjects();

        for (GameObject go : gameObjects) {
            go.__forEachComponent(pauseIter);
        }
    }

    /**
     * Called when this scene is updated.
     * <strong>DO NOT CALL THIS METHOD!</strong>
     * @param deltaTime the time difference between this frame and the previous frame
     */
    public void __update(final float deltaTime) {
        this.deltaTime = deltaTime;

        inputManager.__update();

        Array<GameObject> gameObjects = getGameObjects();

        updateComponents(gameObjects, deltaTime);

        for (GameObject go : gameObjects) {
            go.__forEachComponent(lateUpdateIter);
        }

        animationCanvas.act(deltaTime);
        canvas.act(deltaTime);
    }

    /**
     * Called when this scene is rendered.
     * <strong>DO NOT CALL THIS METHOD!</strong>
     */
    public void __render() {
        Array<GameObject> gameObjects = getGameObjects();

        if (mainCamera instanceof GameCamera2d) {
            GameCamera2d gc2d = (GameCamera2d)mainCamera;
            SpriteBatch batch = gc2d.getSpriteBatch();

            Color bg = gc2d.getBackgroundColor();

            Gdx.gl.glClearColor(bg.r, bg.g, bg.b, bg.a);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            gc2d.getCamera().update();
            batch.setProjectionMatrix(gc2d.getCamera().combined);
            batch.begin();
            for (GameObject go : gameObjects) {
                go.__forEachComponent(renderIter);
            }
            batch.end();
        }

        // Render debug drawings
        if (renderCustomDebugLines) {
            shapeRenderer.setProjectionMatrix(mainCamera.getCamera().combined);

            // Line
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            for (GameObject go : gameObjects) {
                go.__forEachComponent(debugLineIter);
            }
            shapeRenderer.end();

            // Filled
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            for (GameObject go : gameObjects) {
                go.__forEachComponent(debugFilledIter);
            }
            shapeRenderer.end();

            // Point
            shapeRenderer.begin(ShapeRenderer.ShapeType.Point);
            for (GameObject go : gameObjects) {
                go.__forEachComponent(debugPointIter);
            }
            shapeRenderer.end();
        }

        canvas.draw();
    }

    /**
     * Called when this scene is getting destroyed.
     * <strong>DO NOT CALL THIS METHOD!</strong>
     */
    public void __destroy() {
        Array<GameObject> gameObjects = getGameObjects();

        for (GameObject go : gameObjects) {
            go.__forEachComponent(destroyIter);
        }

        mainCamera.__dispose();
        canvas.dispose();
    }

    /**
     * Called when a transition was made from this scene to another scene.
     * <strong>This wont be called if there was no transition animation set to leave this scene!</strong>
     * @param nextScene the scene that was transitioned to
     */
    public void transitionedFromThisScene(Scene nextScene) {}

    /**
     * Called when a transition was made from another scene to this scene.
     * <strong>This wont be called if there was no transition animation set to enter this scene!</strong>
     * @param previousScene the scene that was transitioned from
     */
    public void transitionedToThisScene(Scene previousScene) {}

    /**
     * Called when this scene needs to pause before scenes are switched.
     * This is useful for pausing stuffs that you don't need to be active during transition.
     * <strong>This will be called even if no transition animation is set.</strong>
     */
    public void pauseForTransition() {}

    /**
     * A convenient method for quickly creating a game object that renders a sprite ({@link TextureRegion}).
     * <strong>The returned game object is not added to the scene; you have to add it yourself!</strong>
     * @param tag a tag for the game object.
     * @param sprite a {@link TextureRegion} to render
     * @param gameUnits a {@link GameUnits} instance used for converting the sprite's pixel dimensions to world units
     * @return the created game object
     */
    public GameObject newSpriteObject(String tag, TextureRegion sprite,
                                      GameUnits gameUnits) {
        GameObject go = GameObject.newInstance(tag);
        SpriteRenderer sr = new SpriteRenderer(sprite, gameUnits);
        go.addComponent(sr);

        return go;
    }

    /**
     * A convenient method for quickly creating a game object that renders a sprite ({@link TextureRegion}).
     * The {@link GameUnits} instance of the current main camera will be used for unit conversions.
     * <strong>The returned game object is not added to the scene; you have to add it yourself!</strong>
     * @param tag a tag for the game object.
     * @param sprite a {@link TextureRegion} to render
     * @return the created game object
     */
    public GameObject newSpriteObject(String tag, TextureRegion sprite)
    { return newSpriteObject(tag, sprite, mainCamera.getWorldUnits()); }

    /**
     * A convenient method for quickly creating a game object that renders a sprite ({@link TextureRegion}).
     * <strong>The returned game object is not added to the scene; you have to add it yourself!</strong>
     * @param sprite a {@link TextureRegion} to render
     * @param gameUnits a {@link GameUnits} instance used for converting the sprite's pixel dimensions to world units
     * @return the created game object
     */
    public GameObject newSpriteObject(TextureRegion sprite, GameUnits gameUnits)
    { return newSpriteObject("Untagged", sprite, gameUnits); }

    /**
     * A convenient method for quickly creating a game object that renders a sprite ({@link TextureRegion}).
     * The {@link GameUnits} instance of the current main camera will be used for unit conversions.
     * <strong>The returned game object is not added to the scene; you have to add it yourself!</strong>
     * @param sprite a {@link TextureRegion} to render
     * @return the created game object
     */
    public GameObject newSpriteObject(TextureRegion sprite)
    { return newSpriteObject("Untagged", sprite); }

    /**
     * A convenient method for quickly creating a game object that renders a sprite ({@link TextureRegion}).
     * <strong>The returned game object is not added to the scene; you have to add it yourself!</strong>
     * @param tag a tag for the game object.
     * @param sprite a {@link Texture} to render. <strong>The entire texture will be rendered!</strong>
     * @param gameUnits a {@link GameUnits} instance used for converting the sprite's pixel dimensions to world units
     * @return the created game object
     */
    public GameObject newSpriteObject(String tag, Texture sprite, GameUnits gameUnits)
    { return newSpriteObject(tag, new TextureRegion(sprite), gameUnits); }

    /**
     * A convenient method for quickly creating a game object that renders a sprite ({@link TextureRegion}).
     * The {@link GameUnits} instance of the current main camera will be used for unit conversions.
     * <strong>The returned game object is not added to the scene; you have to add it yourself!</strong>
     * @param tag a tag for the game object.
     * @param sprite a {@link Texture} to render. <strong>The entire texture will be rendered!</strong>
     * @return the created game object
     */
    public GameObject newSpriteObject(String tag, Texture sprite)
    { return newSpriteObject(tag, sprite, mainCamera.getWorldUnits()); }

    /**
     * A convenient method for quickly creating a game object that renders a sprite ({@link TextureRegion}).
     * The {@link GameUnits} instance of the current main camera will be used for unit conversions.
     * <strong>The returned game object is not added to the scene; you have to add it yourself!</strong>
     * @param sprite a {@link Texture} to render. <strong>The entire texture will be rendered!</strong>
     * @return the created game object
     */
    public GameObject newSpriteObject(Texture sprite)
    { return newSpriteObject("Untagged", sprite); }

    /**
     * A convenient method for quickly creating a game object that renders a sprite ({@link TextureRegion}).
     * <strong>The returned game object is not added to the scene; you have to add it yourself!</strong>
     * @param tag a tag for the game object.
     * @param sprite a {@link TextureRegion} to render
     * @param gameUnits a {@link GameUnits} instance used for converting the sprite's pixel dimensions to world units
     * @return the created game object
     */
    public ActorGameObject newActorSpriteObject(String tag, TextureRegion sprite,
                                      GameUnits gameUnits) {
        ActorGameObject go = ActorGameObject.newInstance(tag);
        SpriteRenderer sr = new SpriteRenderer(sprite, gameUnits);
        go.addComponent(sr);

        return go;
    }

    /**
     * A convenient method for quickly creating a game object that renders a sprite ({@link TextureRegion}).
     * The {@link GameUnits} instance of the current main camera will be used for unit conversions.
     * <strong>The returned game object is not added to the scene; you have to add it yourself!</strong>
     * @param tag a tag for the game object.
     * @param sprite a {@link TextureRegion} to render
     * @return the created game object
     */
    public ActorGameObject newActorSpriteObject(String tag, TextureRegion sprite)
    { return newActorSpriteObject(tag, sprite, mainCamera.getWorldUnits()); }

    /**
     * A convenient method for quickly creating a game object that renders a sprite ({@link TextureRegion}).
     * <strong>The returned game object is not added to the scene; you have to add it yourself!</strong>
     * @param sprite a {@link TextureRegion} to render
     * @param gameUnits a {@link GameUnits} instance used for converting the sprite's pixel dimensions to world units
     * @return the created game object
     */
    public ActorGameObject newActorSpriteObject(TextureRegion sprite, GameUnits gameUnits)
    { return newActorSpriteObject("Untagged", sprite, gameUnits); }

    /**
     * A convenient method for quickly creating a game object that renders a sprite ({@link TextureRegion}).
     * The {@link GameUnits} instance of the current main camera will be used for unit conversions.
     * <strong>The returned game object is not added to the scene; you have to add it yourself!</strong>
     * @param sprite a {@link TextureRegion} to render
     * @return the created game object
     */
    public ActorGameObject newActorSpriteObject(TextureRegion sprite)
    { return newActorSpriteObject("Untagged", sprite); }

    /**
     * A convenient method for quickly creating a game object that renders a sprite ({@link TextureRegion}).
     * <strong>The returned game object is not added to the scene; you have to add it yourself!</strong>
     * @param tag a tag for the game object.
     * @param sprite a {@link Texture} to render. <strong>The entire texture will be rendered!</strong>
     * @param gameUnits a {@link GameUnits} instance used for converting the sprite's pixel dimensions to world units
     * @return the created game object
     */
    public ActorGameObject newActorSpriteObject(String tag, Texture sprite, GameUnits gameUnits)
    { return newActorSpriteObject(tag, new TextureRegion(sprite), gameUnits); }

    /**
     * A convenient method for quickly creating a game object that renders a sprite ({@link TextureRegion}).
     * The {@link GameUnits} instance of the current main camera will be used for unit conversions.
     * <strong>The returned game object is not added to the scene; you have to add it yourself!</strong>
     * @param tag a tag for the game object.
     * @param sprite a {@link Texture} to render. <strong>The entire texture will be rendered!</strong>
     * @return the created game object
     */
    public ActorGameObject newActorSpriteObject(String tag, Texture sprite)
    { return newActorSpriteObject(tag, sprite, mainCamera.getWorldUnits()); }

    /**
     * A convenient method for quickly creating a game object that renders a sprite ({@link TextureRegion}).
     * The {@link GameUnits} instance of the current main camera will be used for unit conversions.
     * <strong>The returned game object is not added to the scene; you have to add it yourself!</strong>
     * @param sprite a {@link Texture} to render. <strong>The entire texture will be rendered!</strong>
     * @return the created game object
     */
    public ActorGameObject newActorSpriteObject(Texture sprite)
    { return newActorSpriteObject("Untagged", sprite); }
}
