package io.github.isoteriktech.xgdx;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * An ActorGameObject is a type of {@link GameObject} that allows the use of Scene2d APIs while still preserving the
 * goodies that GameObjects provide. It uses an {@link ActorTransform} to reference an {@link Actor} instance.
 * <p>
 * Because the transform will always copy the properties of the referenced Actor, modifying the transform properties
 * will not work because those modifications will be overwritten in the next frame update. A workaround is to
 * use the setter methods of the transform to manipulate it. This will automatically sync the actor with it. Another
 * workaround is to simply modify the referenced Actor directly. The transform will be automatically synced.
 * <p>
 * <strong>Note:</strong> the referenced actor will be rendered if it can be rendered. If the intention is to simply use
 * the animation API of Scene2d, a blank Actor that cannot render will be more appropriate.
 *
 * @author isoterik
 */
public class ActorGameObject extends GameObject {
    public final ActorTransform actorTransform;

    protected ActorGameObject(String tag, Actor actor) {
        super(tag);

        components.removeValue(transform, true);
        transform = new ActorTransform(actor);
        transform.__setGameObject(this);
        components.add(transform);

        actorTransform = (ActorTransform) transform;
    }

    protected ActorGameObject(String tag) {
        this(tag, new Actor());
    }

    protected ActorGameObject(Actor actor) {
        this("Untagged", actor);
    }

    protected ActorGameObject()
    { this("Untagged"); }

    /**
     * Creates a new {@link ActorGameObject} given a tag and an {@link Actor} to reference.
     * @param tag the tag for the game object
     * @param actor the actor to reference
     * @return the created game object
     */
    public static ActorGameObject newInstance(String tag, Actor actor)
    { return new ActorGameObject(tag, actor); }

    /**
     * Creates a new {@link ActorGameObject} given a tag.
     * @param tag the tag for the game object
     * @return the created game object
     */
    public static ActorGameObject newInstance(String tag)
    { return new ActorGameObject(tag); }

    /**
     * Creates a new {@link ActorGameObject} given an {@link Actor} to reference.
     * @param actor the actor to reference
     * @return the created game object
     */
    public static ActorGameObject newInstance(Actor actor)
    { return new ActorGameObject(actor); }

    /**
     * Creates a new {@link ActorGameObject} using 'Untagged' as the default tag.
     * @return the created game object
     */
    public static ActorGameObject newInstance()
    { return new ActorGameObject(); }

    public ActorTransform transform()
    { return actorTransform; }
}
