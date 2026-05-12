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
        if (selected)
        {
            StringBuilder result = new StringBuilder();

            result.append("[WHITE]>");

            int maxLen = Math.max(inputText.length(), hurtText.length());

            for (int i = 0; i < maxLen; i++) {
                char c1 = i < inputText.length() ? inputText.charAt(i) : 0;
                char c2 = i < hurtText.length() ? hurtText.charAt(i) : 0;

                if (c1 == 0) result.append(c2);
                else if (c1 == c2) result.append("[GREEN]").append(c1).append("[]");
                else result.append("[RED]").append(c1).append("[]");
            }

            result.append("[]");

            return result.toString();
        }
        else
        {
            return hurtText;
        }
    }
}
