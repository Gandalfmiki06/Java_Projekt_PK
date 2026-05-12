package io.github.java_projekt_pk.monsters;

import io.github.java_projekt_pk.globals.HurtTextGenerator;

public class Enemy {
    protected boolean selected = false;
    protected String hurtText = "";
    protected int health = 1;

    public String inputText = "";

    public Enemy()
    {
        hurtText = HurtTextGenerator.getRandomText();
    }

    public void unselect()
    {
        selected = false;
    }

    public void select()
    {
        selected = true;
    }

    public void hurt()
    {
        health--;
        if (health > 0) hurtText = HurtTextGenerator.getRandomText();
    }

    protected String getRenderString()
    {
        return selected ? ">[RED]" + hurtText + "[]" : hurtText;
    }
}
