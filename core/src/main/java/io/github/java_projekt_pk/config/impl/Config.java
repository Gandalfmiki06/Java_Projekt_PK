package io.github.java_projekt_pk.config.impl;

import com.badlogic.gdx.Gdx;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class Config {
    private final Path path;
    private final Map<String, ConfigEntry<?>> entries;

    public Config(Path path) {
        this.path = path;

        SpecBuilder specBuilder = new SpecBuilder();

        onCreate(specBuilder);

        entries = specBuilder.build();
        this.reload();

        for(var entry : entries.entrySet()) {
            entry.getValue().subscribe((e) -> this.save());
        }
    }

    abstract protected void onCreate(SpecBuilder spec);

    abstract protected int getVersion();

    public void reload() {
        var file = path.toFile();

        if (!file.exists()) {
            try {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            } catch (IOException e) {
                Gdx.app.log("ERR", "[" + this.getClass().getName() +"] Nie można utworzyć pliku");
                return;
            }
        }

        try {
            var readable = new BufferedReader(new FileReader(file));
            String line;
            int readedVersion = -1;
            while ((line = readable.readLine()) != null) {
                String[] raw = line.split("=");

                if (raw.length != 2) {
                    Gdx.app.log("ERR", "[" + this.getClass().getName() +"] Nie można wyciągnąć danych config");
                    break;
                }

                if(readedVersion <= 0 && raw[0].equals("@version")) {
                    readedVersion = Integer.parseInt(raw[1]);

                    if(readedVersion != getVersion()){
                        Gdx.app.log("WARN", "[" + this.getClass().getName() +"] Inna wersja configa. Przywracanie domyslnych opcji");
                        break;
                    }
                }

                if(readedVersion <= 0) {
                    Gdx.app.log("ERR", "[" + this.getClass().getName() +"] Nie znaleziono '@version' w config");
                    break;
                }

                if(entries.containsKey(raw[0])){
                    var entry = entries.get(raw[0]);
                    updateEntry(entry, raw[1]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.save();
    }

    private <T> void updateEntry(ConfigEntry<T> entry, String rawValue) {
        T deserializedValue = entry.serialize(rawValue);
        entry.set(deserializedValue);
    }

    private <T> String deserializeValue(ConfigEntry<T> entry) {
        return entry.deserialize(entry.get());
    }

    public void save() {
        try (var writer = new BufferedWriter(new FileWriter(path.toFile()))) {
            writer.write("@version=" + getVersion() + "\n");
            for (var entry : entries.entrySet()) {
                String b = entry.getKey() + "=" + deserializeValue(entry.getValue()) + "\n";

                writer.write(b);
            }
        } catch (IOException e) {
            Gdx.app.log("ERR", "[" + this.getClass().getName() +"] Nie mozna zapisać configa");
        }
    }

    public static class SpecBuilder {
        private boolean consumed = false;
        private final Map<String, ConfigEntry<?>> entries = new HashMap<>();

        public <T> ConfigEntry<T> define(String id, T value, Function<String, T> serializer, Function<T, String> deserializer) {
            if(entries.containsKey(id)){
                throw new RuntimeException("Key '" + id + "' is already defined");
            }
            var entry = new ConfigEntry<>(id, value, serializer, deserializer);
            entries.put(id, entry);
            return entry;
        }

        public Map<String, ConfigEntry<?>> build() {
            if(consumed) {
                throw new RuntimeException("Builder already consumed");
            }

            consumed = true;
            return this.entries;
        }
    }
}
