package io.github.java_projekt_pk.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.java_projekt_pk.Managers.FontManager;
import io.github.java_projekt_pk.globals.SystemDText;

import java.util.ArrayList;
import java.util.List;

public class InGameScreen implements Screen {
    private SpriteBatch batch;
    private BitmapFont font;

    private List<String> activeTexts = new ArrayList<>();
    private float nextTextDelay = MathUtils.random(0.5f, 3.0f);
    private float timeSinceLastText = 0.f;

    public InGameScreen() {
        batch = new SpriteBatch();

        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 20;
        parameter.color = Color.WHITE;

        font = FontManager.generateFont("google", parameter);
    }

    private void nextRandomText() {
        activeTexts.add(SystemDText.randomText());

        if(activeTexts.size() > 128)
            activeTexts.removeFirst();

        timeSinceLastText -= nextTextDelay;
        nextTextDelay = MathUtils.random(0.2f, 1.0f);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        timeSinceLastText += delta;

        if (timeSinceLastText >= nextTextDelay && nextTextDelay > 0) {
            nextRandomText();
        }

        batch.begin();

        int yPosition = 50;
        int i = 0;
        for (String text : activeTexts.reversed()) {
            font.draw(batch, String.format("[ %s [WHITE]] %s", "[GREEN]OK", text), 30, yPosition);
            yPosition += 30;
            i++;
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
