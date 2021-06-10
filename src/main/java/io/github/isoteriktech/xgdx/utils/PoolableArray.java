package io.github.isoteriktech.xgdx.utils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class PoolableArray<T> extends Array<T> implements Pool.Poolable {
    public PoolableArray () {
        this(true, 16);
    }

    public PoolableArray (int capacity) {
        this(true, capacity);
    }

    public PoolableArray (boolean ordered, int capacity) {
        super(ordered, capacity);
    }

    public PoolableArray (boolean ordered, int capacity, Class arrayType) {
        super(ordered, capacity, arrayType);
    }

    public PoolableArray (Class arrayType) {
        super(arrayType);
    }

    public PoolableArray (Array<? extends T> array) {
        super(array);
    }

    public PoolableArray (T[] array) {
        super(array);
    }

    @Override
    public void reset() {
        this.clear();
    }

    public static class ArrayPool<T> extends Pool<PoolableArray<T>> {
        @Override
        protected PoolableArray<T> newObject() {
            return new PoolableArray<>();
        }
    }
}
