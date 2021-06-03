package com.isoterik.xgdx;

import com.badlogic.gdx.utils.Array;

public class GameObject {
    protected String tag;

    protected Scene scene;

    public Transform transform;

    protected Array<Component> components;
    protected Array.ArrayIterator<Component> componentsIterator;

    private GameObject() {
        this("Untagged");
    }

    private GameObject(String tag) {
        this.tag = tag;
        components = new Array<>();
        componentsIterator = new Array.ArrayIterator<>(components, true);

        transform = new Transform();
        transform.__setGameObject(this);
        components.add(transform);
    }

    public void __setScene(Scene scene) {
        this.scene = scene;
        for (Component comp : components)
            comp.__setScene(scene);
    }

    public void __removeFromScene() {
        for (Component comp : components)
            comp.stop();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
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
        if (scene != null) {
            component.__setScene(scene);
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

    public <T extends Component> void removeComponents(Class<T> clazz) {
        for (Component c : components) {
            if (c.getClass() == clazz)
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
            if (c.getClass() == componentClass)
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
            if (c.getClass() == componentClass)
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
        componentsIterator.reset();

        while (componentsIterator.hasNext())
            iterationListener.onComponent(componentsIterator.next());
    }

    /**
     * An iteration listener that can be used to iterate the components of a {@link GameObject}.
     */
    public interface __ComponentIterationListener {
        void onComponent(Component component);
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























