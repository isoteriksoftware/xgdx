package io.github.isoteriktech.xgdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import io.github.isoteriktech.xgdx.x2d.scenes.transition.ISceneTransition;

/**
 * Your starter class must extend this class to make it a minGDX game.
 * <code>XGdxGame</code> initializes all the core systems and start your game.
 * <p>
 *
 * If you need to override any of the life cycle methods in your starter classes, don't forget to call the implementation of the super class.
 * @author isoteriksoftware
 */
public abstract class XGdxGame implements ApplicationListener {
    protected XGdx xGdx;
    protected ISceneTransition splashTransition = null;

    /** Log nothing */
    public static final int LOG_NONE = Application.LOG_NONE;

    /** Log errors only */
    public static final int LOG_ERROR = Application.LOG_ERROR;

    /** Log info only */
    public static final int LOG_INFO = Application.LOG_INFO;

    /** Log everything */
    public static final int LOG_DEBUG = Application.LOG_DEBUG;

    @Override
    public void create() {
        XGdx.__init();
        xGdx = XGdx.instance();
        xGdx.setScene(initGame(), splashTransition);
    }

    /**
     * Implement this method to tell xGdx the initial scene of your game. This is where you'll typically initialize your splash scene
     * and return it. You can optionally set {@link #splashTransition} to the transition you want to animate the scene with.
     * @return the initial scene of your game
     */
    protected abstract Scene initGame();

    /**
     * Sets the log level that libGDX uses for logging.
     * @param logLevel the log level. Should be one of {@link #LOG_DEBUG}, {@link #LOG_ERROR}, {@link #LOG_INFO}, {@link #LOG_NONE}
     */
    public void setLogLevel(int logLevel)
    { Gdx.app.setLogLevel(logLevel); }

    @Override
    public void resume()
    { xGdx.__resume(); }

    @Override
    public void pause()
    { xGdx.__pause(); }

    @Override
    public void resize(int width, int height)
    { xGdx.__resize(width, height); }

    @Override
    public void render()
    { xGdx.__render(); }

    @Override
    public void dispose()
    { xGdx.__dispose(); }
}
