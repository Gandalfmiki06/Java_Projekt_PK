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

    enum TextColor
    {
        WHITE,
        RED,
        GREEN
    }

    protected String getRenderString()
    {
        if (!selected) return hurtText;

        StringBuilder result = new StringBuilder();

        result.append("[WHITE]>");
        TextColor color = TextColor.WHITE;

        int maxLen = Math.max(inputText.length(), hurtText.length());

        for (int i = 0; i < maxLen; i++)
        {
            char c1 = i < inputText.length() ? inputText.charAt(i) : 0;
            char c2 = i < hurtText.length() ? hurtText.charAt(i) : 0;

            TextColor targetColor;
            char nextChar;

            if (c1 == 0)
            {
                targetColor = TextColor.WHITE;
                nextChar = c2;
            }
            else if (c1 == c2) 
            {
                targetColor = TextColor.GREEN;
                nextChar = c1;
            }
            else
            {
                targetColor = TextColor.RED;
                nextChar = c1;
            }

            if (color != targetColor)
            {
                result.append("[][").append(targetColor.name()).append("]");
                color = targetColor;
            }

            result.append(escapeMarkup(nextChar));
        }

        result.append("[][]");

        System.out.println(result.toString());
        return result.toString();
    }

    private String escapeMarkup(char c)
    {
        if (c == '[')
        {
            return "[["; //this avoids starting new markup tag
        }

        return String.valueOf(c);
    }

    public void typeCharacter(char c)
    {
        inputText += c;
    }

    public void backspace()
    {
        if (!inputText.isEmpty())
        {
            inputText = inputText.substring(0, inputText.length() - 1);
            System.out.println(inputText);
        }
    }

    public void delete()
    {
        ///
    }
}
