package io.github.java_projekt_pk.globals;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class RenderingGlobals {

    public static BitmapFont font = new BitmapFont();

    public static void init() {
        font.getData().setScale(2f);
        font.getData().markupEnabled = true;
        font.setColor(Color.GRAY);
    }
}