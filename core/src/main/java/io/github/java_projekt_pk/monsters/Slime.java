package io.github.java_projekt_pk.monsters;

public class Slime extends Enemy {

    private final SlimeSpecies species;

    public Slime(SlimeSpecies species) {
        this.species = species;
        setAnimation("Idle");
        health = 2;
    }

    @Override
    public final void setAnimation(String animationName) {
        animation = species.getAnimation(animationName);
    }
}
