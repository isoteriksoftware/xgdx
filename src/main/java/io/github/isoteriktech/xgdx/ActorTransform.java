package io.github.isoteriktech.xgdx;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * An ActorTransform holds a reference to an {@link Actor}. The transform will copy the properties of the actor every
 * frame. This allows {@link GameObject}s to use Scene2d APIs.
 * <p>
 * Because this transform will always copy the properties of the referenced Actor, modifying the transform properties
 * will not work because those modifications will be overwritten in the next frame update. A workaround is to
 * use the setter methods of the transform to manipulate it. This will automatically sync the actor with it. Another
 * workaround is to simply modify the referenced Actor directly. The transform will be automatically synced.
 * <p>
 * <strong>Note:</strong> the referenced actor will be rendered if it can be rendered. If the intention is to simply use
 * the animation API of Scene2d, a blank Actor that cannot render will be more appropriate.
 *
 * @author isoterik
 */
public class ActorTransform extends Transform {
    /** A reference to the {@link Actor} instance for this transform. */
    public Actor actor;

    /**
     * Creates a new instance given the Actor to reference.
     * @param actor the referenced actor
     */
    public ActorTransform(Actor actor) {
        this.actor = actor;
    }

    /**
     * Creates a new instance. A blank Actor will be created and referenced.
     */
    public ActorTransform() {
        this(new Actor());
    }

    @Override
    public void setOrigin(float originX, float originY) {
        super.setOrigin(originX, originY);
        actor.setOrigin(originX, originY);
    }

    @Override
    public void setOrigin(float originX, float originY, float originZ) {
        super.setOrigin(originX, originY, originZ);
        actor.setOrigin(originX, originY);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        actor.setPosition(x, y);
    }

    @Override
    public void setPosition(float x, float y, float z) {
        super.setPosition(x, y, z);
        actor.setPosition(x, y);
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        actor.setX(x);
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        actor.setY(y);
    }

    @Override
    public void setRotation(float rotation) {
        super.setRotation(rotation);
        actor.setRotation(rotation);
    }

    @Override
    public void setRotation(float rotationX, float rotationY, float rotationZ) {
        super.setRotation(rotationX, rotationY, rotationZ);
        actor.setRotation(rotationY);
    }

    @Override
    public void setScale(float scaleX, float scaleY) {
        super.setScale(scaleX, scaleY);
        actor.setScale(scaleX, scaleY);
    }

    @Override
    public void setScaleX(float scaleX) {
        super.setScaleX(scaleX);
        actor.setScaleX(scaleX);
    }

    @Override
    public void setScaleY(float scaleY) {
        super.setScaleY(scaleY);
        actor.setScaleY(scaleY);
    }

    @Override
    public void setScale(float scaleX, float scaleY, float scaleZ) {
        super.setScale(scaleX, scaleY, scaleZ);
        actor.setScale(scaleX, scaleY);
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        actor.setSize(width, height);
    }

    @Override
    public void setSize(float width, float height, float depth) {
        super.setSize(width, height, depth);
        actor.setSize(width, height);
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        actor.setWidth(width);
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        actor.setHeight(height);
    }

    @Override
    public float getHeight() {
        return actor.getHeight();
    }

    @Override
    public float getWidth() {
        return actor.getWidth();
    }

    @Override
    public float getOriginX() {
        return actor.getOriginX();
    }

    @Override
    public float getOriginY() {
        return actor.getOriginY();
    }

    @Override
    public float getRotation() {
        return actor.getRotation();
    }

    @Override
    public float getScaleX() {
        return actor.getScaleX();
    }

    @Override
    public float getScaleY() {
        return actor.getScaleY();
    }

    @Override
    public float getX() {
        return actor.getX();
    }

    @Override
    public float getY() {
        return actor.getY();
    }

    @Override
    public void update(float deltaTime) {
        scale.set(actor.getScaleX(), actor.getScaleY(), 1);
        size.set(actor.getWidth(), actor.getHeight(), 0);
        rotation.set(0, actor.getRotation(), 0);
        origin.set(actor.getOriginX(), actor.getOriginY(), 0);
        position.set(actor.getX(), actor.getY(), 0);
    }
}
