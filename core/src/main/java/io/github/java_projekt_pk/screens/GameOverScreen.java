package io.github.java_projekt_pk.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.java_projekt_pk.Main;
import io.github.java_projekt_pk.Managers.FontManager;

public class GameOverScreen extends  ScreenAdapter {
    private final BitmapFont fontTitle;
    private final BitmapFont font;

    private final int TITLE_SIZE = 100;
    private final int TEXT_SIZE = 30;

    private float frameTime = 0;
    private final float FRAME_LENGTH = 3;

    public GameOverScreen() {
        FreeTypeFontParameter parameter = FontManager.getDefaultParameters();
        parameter.size = TITLE_SIZE;
        parameter.color = Color.WHITE;

        fontTitle = FontManager.generateFont("google", parameter);

        parameter.size = TEXT_SIZE;
        font = FontManager.generateFont("google", parameter);
    }

    @Override
    public void render(float delta) {
        frameTime += delta;
        if (frameTime >= FRAME_LENGTH) {
            Gdx.app.exit();
        }

        ScreenUtils.clear(0.14f, 0.34f, 0.7f, 1f);

        var batch = Main.getSpriteBatch();
        batch.begin();

        fontTitle.draw(batch, ":(", 50, Gdx.graphics.getHeight() - 50);

        font.draw(batch, "Witaj!", 50, 450);
        font.draw(batch, "Zauważylismy, ze twój komputer napotkał problem.", 50, 420);
        font.draw(batch, "Oczywiście nie zajmiemy się tym od razu.", 50, 390);
        font.draw(batch, "Zamiast tego damy ci SMUTNĄ BUŹKĘ. PO PROSTU SMUTEK!", 50, 360);
        font.draw(batch, "Ze ci sie komputer wywalił.", 50, 330);
        font.draw(batch, "Ale popatrz na to z innej strony.", 50, 270);
        font.draw(batch, "Zdobyłeś niezły wynik: " + Main.getHud().getScore(), 50, 240);
        font.draw(batch, "Ukonczono 10%", 50, 150);

        batch.end();
    }

    @Override
    public void dispose() {
        fontTitle.dispose();
        font.dispose();
    }
}
