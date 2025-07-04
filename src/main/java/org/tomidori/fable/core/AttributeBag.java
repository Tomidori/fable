package org.tomidori.fable.core;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

public final class AttributeBag {
    private final Map<String, Object> attributes = new HashMap<>();

    private AttributeBag() {
    }

    public static AttributeBag create() {
        return new AttributeBag();
    }

    public boolean containsKey(String key) {
        Objects.requireNonNull(key);
        return attributes.containsKey(key);
    }

    public boolean containsValue(Object value) {
        Objects.requireNonNull(value);
        return attributes.containsValue(value);
    }

    @SuppressWarnings("unchecked")
    public <T> @Nullable T get(String key) {
        Objects.requireNonNull(key);
        return (T) attributes.get(key);
    }

    public <T> T getOrThrow(String key) {
        Objects.requireNonNull(key);
        T value = get(key);
        if (value == null) {
            throw new NoSuchElementException();
        }

        return value;
    }

    public <T> T getOrDefault(String key, T defaultValue) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(defaultValue);
        T value = get(key);
        return value == null ? defaultValue : value;
    }

    public void set(String key, Object value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        attributes.put(key, value);
    }

    public void remove(String key) {
        Objects.requireNonNull(key);
        attributes.remove(key);
    }
}
