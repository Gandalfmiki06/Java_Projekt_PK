package io.github.java_projekt_pk.monsters;

import java.util.Random;

public class Slime extends Enemy {

    private final SlimeSpecies species;
    private static final Random RANDOM = new Random();

    public Slime(SlimeSpecies species) {
        this.species = species;
        setAnimation(SlimeSpecies.ANIMATION_NAMES[RANDOM.nextInt(SlimeSpecies.ANIMATION_NAMES.length)]);
        health = 2;
    }

    @Override
    public final void setAnimation(String animationName) {
        animation = species.getAnimation(animationName);
    }
}
