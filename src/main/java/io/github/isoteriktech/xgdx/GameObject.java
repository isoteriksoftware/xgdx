package io.github.isoteriktech.xgdx;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import io.github.isoteriktech.xgdx.utils.PoolableArrayIterator;

/**
 * A GameObject represents an entity in the game. A GameObject can't do anything on its own; you have to give it properties before it can do anything.
 * A GameObject is a container; we have to add pieces to it to make into a character, a tree, a space ship or whatever else you would like it to be. Each piece
 * is called a {@link Component}.
 * <p>
 *
 * Every GameObject has a {@link Transform} component attached automatically and cannot be removed. This is because the Transform defines where the GameObject is located and
 * how it is rotated and scaled; without a Transform, the GameObject would not have a location in the game world.
 * <p>
 *
 * To create game objects, use the static factory methods: {@link #newInstance(String)} and {@link #newInstance()}
 *
 * @see Component
 *
 * @author isoteriksoftware
 */
public class GameObject {
    protected final Array<Component> components;
    protected final ComponentIteratorPool componentIteratorPool;

    public Transform transform;

    protected String tag;

    protected Scene hostScene;

    protected GameObject()
    { this("Untagged"); }

    protected GameObject(String tag) {
        components = new Array<>();
        componentIteratorPool = new ComponentIteratorPool(components);

        transform = new Transform();
        transform.__setGameObject(this);
        components.add(transform);

        this.tag = tag;
    }

    /**
     * Sets the scene where this game object resides.
     * This method is called internally by the system. Do not call it directly!
     * @param hostScene the host scene
     */
    public void __setHostScene(Scene hostScene) {
        this.hostScene = hostScene;
        for (Component comp : components)
            comp.__setHostScene(hostScene);
    }

    /**
     *
     * @return the scene where this game object resides
     */
    public Scene getHostScene()
    { return hostScene; }

    /**
     * Sets the tag for this game object. It is not required to be unique.
     * @param tag the tag
     */
    public void setTag(String tag)
    { this.tag = tag; }

    /**
     *
     * @return the tag for this game object
     */
    public String getTag()
    { return tag; }

    /**
     * Called when this game object is removed from a scene.
     * DO NOT CALL THIS METHOD!
     */
    public void __removeFromScene() {
        for (Component comp : components)
            comp.stop();
    }

    /**
     * Adds a component to this game object.
     * @param component the component
     */
    public void addComponent(Component component) {
        if (components.contains(component, true))
            return;

        component.__setGameObject(this);
        component.attach();

        for (Component comp : components)
            comp.componentAdded(component);

        // If this game object is already added to a scene then we need to alert the component
        if (hostScene != null) {
            component.__setHostScene(hostScene);
            component.start();
        }

        components.add(component);
    }

    /**
     * Removes a component attached to this game object.
     * <strong>Note:</strong> a component can remove itself.
     * @param component the component to remove.
     * @return true if the component was removed. false otherwise
     */
    public boolean removeComponent(Component component) {
        if (components.contains(component, true) &&
                components.removeValue(component, true)) {
            for (Component comp : components)
                comp.componentRemoved(component);

            // detach
            component.detach();

            component.__setGameObject(null);
            return true;
        }

        return false;
    }

    /**
     * Removes the first component found for a particular type that is attached to this host game object.
     * <strong>Note:</strong> a component can remove itself.
     * @param componentClass the class of the component
     * @param <T> the type of component
     * @return true if a component of such type is removed. false otherwise
     */
    public <T extends Component> boolean removeComponent(Class<T> componentClass)
    { return removeComponent(getComponent(componentClass)); }

    public <T extends Component> void removeComponents(Class<T> componentClass) {
        for (Component c : components) {
            if (ClassReflection.isAssignableFrom(componentClass, c.getClass()))
                removeComponent(c);
        }
    }

    /**
     * Gets a component of a particular type that is attached to this game object.
     * <strong>Note:</strong> a component can remove itself.
     * @param componentClass the class of the component
     * @param <T> the type of component
     * @return the component. null if not found
     */
    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (ClassReflection.isAssignableFrom(componentClass, c.getClass()))
                return (T)c;
        }

        return null;
    }

    /**
     * Gets components of a particular type that is attached to this game object.
     * @param componentClass the class of the component
     * @param <T> the type of component
     * @return the components found or empty list if none found
     */
    public <T extends Component> Array<T> getComponents(Class<T> componentClass) {
        Array<T> comps = new Array<>();

        for (Component c : components) {
            if (ClassReflection.isAssignableFrom(componentClass, c.getClass()))
                comps.add((T)c);
        }

        return comps;
    }

    /**
     *
     * @return all the components attached to this game object.
     */
    public Array<Component> getComponents()
    { return components; }

    /**
     * Checks if a component of a particular type is attached to the host game object.
     * @param componentClass the class of the component
     * @param <T> the type of component
     * @return true if a component of such type exists. false otherwise
     */
    public <T extends Component> boolean hasComponent(Class<T> componentClass)
    { return getComponent(componentClass) != null; }

    /**
     * Checks if a component is attached to this game object.
     * @param component the component to check.
     * @return true if a component of such type exists. false otherwise
     */
    public boolean hasComponent(Component component)
    { return components.contains(component, true); }

    /**
     * Calls the given IterationListener on all components attached to this game object.
     * This method is used internally by the system. While it is safe to call it, you usually don't need to.
     * @param iterationListener the iteration listener
     */
    public void __forEachComponent(__ComponentIterationListener iterationListener) {
        PoolableArrayIterator<Component> componentArrayIterator = componentIteratorPool.obtain();

        while (componentArrayIterator.hasNext())
            iterationListener.onComponent(componentArrayIterator.next());

        componentIteratorPool.free(componentArrayIterator);
    }

    /**
     * Checks if the provided tag equals the current tag of this gameObject.
     * @param otherTag the tag to compare to.
     * @return true if the tags are similar. false otherwise
     */
    public boolean sameTag(String otherTag) {
        return this.tag.equals(otherTag);
    }

    /**
     * An iteration listener that can be used to iterate the components of a {@link GameObject}.
     */
    public interface __ComponentIterationListener {
        void onComponent(Component component);
    }

    private class ComponentIteratorPool extends Pool<PoolableArrayIterator<Component>> {
        private final Array<Component> componentArray;

        public ComponentIteratorPool(Array<Component> componentArray) {
            this.componentArray = componentArray;
        }

        @Override
        protected PoolableArrayIterator<Component> newObject() {
            return new PoolableArrayIterator<>(componentArray);
        }
    }

    /**
     * Creates a new {@link GameObject} given a tag.
     * @param tag the tag for the game object
     * @return the created game object
     */
    public static GameObject newInstance(String tag)
    { return new GameObject(tag); }

    /**
     * Creates a new {@link GameObject} using 'Untagged' as the default tag.
     * @return the created game object
     */
    public static GameObject newInstance()
    { return new GameObject(); }
}
