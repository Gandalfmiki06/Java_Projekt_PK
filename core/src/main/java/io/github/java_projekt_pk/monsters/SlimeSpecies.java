package io.github.java_projekt_pk.monsters;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.java_projekt_pk.SpriteAnimation;

public class SlimeSpecies {

    public static final String[] ANIMATION_NAMES = {"Idle", "Walk", "Run", "Jump", "Hurt", "Attack1", "Attack2",
        "Attack3", "Run+Attack", "Dead"};
    Map<String, Animation<TextureRegion>> animations;

    public SlimeSpecies(TextureAtlas atlas, String species) {
        animations = new HashMap<>();
        for (var animation : ANIMATION_NAMES) {
            animations.put(animation, SpriteAnimation.fromAtlas(atlas, species + "/" + animation));
        }
    }

    Animation<TextureRegion> getAnimation(String animationName) {
        if (animations.containsKey(animationName)) {
            return animations.get(animationName);
        } else {
            throw new RuntimeException("Animation not found: " + animationName);
        }
    }
}
