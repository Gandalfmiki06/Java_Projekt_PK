package io.github.java_projekt_pk.config.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ConfigEntry<T> {
    private final List<ConfigEvent<T>> events = new ArrayList<>();

    private final T defaultValue;
    private final Function<String, T> serializer;
    private final Function<T, String> deserializer;
    private T value;
    private final String id;

    public ConfigEntry(String id, T defaultValue, Function<String, T> serializer, Function<T, String> deserializer) {
        this.id = id;
        this.defaultValue = defaultValue;
        this.value = defaultValue;

        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    public String getId() {
        return id;
    }

    public T serialize(String value) {
        return serializer.apply(value);
    }

    public String deserialize(T value) {
        return deserializer.apply(value);
    }

    public void set(T value) {
        this.value = value;

        for (var ev : events) {
            ev.execute(this);
        }
    }

    public T get() {
        return this.value;
    }

    public T getDefault() {
        return this.defaultValue;
    }

    public boolean subscribe(ConfigEvent<T> ev) {
        return events.add(ev);
    }

    public boolean unsubscribe(ConfigEvent<T> ev) {
        return events.remove(ev);
    }

    @FunctionalInterface
    public interface ConfigEvent<T> {
        void execute(ConfigEntry<T> entry);
    }
}
