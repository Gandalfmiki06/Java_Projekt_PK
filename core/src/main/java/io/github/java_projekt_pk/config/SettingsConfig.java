package io.github.java_projekt_pk.config;

import io.github.java_projekt_pk.config.impl.Config;
import io.github.java_projekt_pk.config.impl.ConfigEntry;

import java.nio.file.Path;
import java.util.function.Function;

public class SettingsConfig extends Config {
    public ConfigEntry<String> player;
    public ConfigEntry<Float> soundMaster;
    public ConfigEntry<Float> soundMusic;
    public ConfigEntry<Float> soundSFX;

    public SettingsConfig(Path path) {
        super(path);
    }

    @Override
    protected int getVersion() {
        return 1;
    }

    @Override
    protected void onCreate(Config.SpecBuilder spec) {
        player = spec.define("player", "ANONYMOUS", Function.identity(), Function.identity());
        soundMaster = spec.define("sound.master", 1.0f, Float::parseFloat, String::valueOf);
        soundMusic = spec.define("sound.music", .5f, Float::parseFloat, String::valueOf);
        soundSFX = spec.define("sound.sfx", .5f, Float::parseFloat, String::valueOf);
    }
}
