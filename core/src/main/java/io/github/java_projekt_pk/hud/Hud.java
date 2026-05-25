package io.github.java_projekt_pk.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import io.github.java_projekt_pk.Managers.FontManager;

public class Hud {
    private final int PLAYER_HEALTH = 4;
    private final int HEALTH_BAR_OFFSET_X = 20;
    private final int HEALTH_BAR_OFFSET_Y = 50;
    private final int HEART_SIZE = 16 * 2;
    private final int HEART_MARGIN = 3;
    private final float DAMAGE_HEART_DURATION = 0.2f;
    private final int SCORE_TEXT_OFFSET = 20;

    private AtlasRegion heartFull;
    private AtlasRegion heartEmpty;
    private AtlasRegion heartDamage;

    private boolean damaged;
    private float timeSinceDamage;
    public int health;

    private BitmapFont scoreFont;
    private int score = 0;
    private String scoreText;

    public Hud(TextureAtlas atlas) {
        heartFull = atlas.findRegion("heart-full");
        heartDamage = atlas.findRegion("heart-damage");
        heartEmpty = atlas.findRegion("heart-empty");
        damaged = false;
        health = PLAYER_HEALTH;

        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 40;
        parameter.color = Color.WHITE;
        scoreFont = FontManager.generateFont("terminus-bold", parameter);
        updateScore();
    }

    public void reset() {
        score = 0;
        updateScore();
    }

    public void timeStep(double time) {
        if (damaged) {
            timeSinceDamage += time;
            if (timeSinceDamage > DAMAGE_HEART_DURATION) {
                damaged = false;
            }
        }
    }

    public void draw(SpriteBatch spriteBatch) {
        drawHealthBar(spriteBatch);
        drawScoreText(spriteBatch);
    }

    public void addScore(int score) {
        this.score += score;
        updateScore();
    }

    public void damage() {
        health -= 1;
        damaged = true;
        timeSinceDamage = 0.0f;
    }

    private void drawHealthBar(SpriteBatch spriteBatch) {
        for (int i = 0; i < PLAYER_HEALTH; i++) {
            AtlasRegion heartType;
            if (i < health)
                heartType = heartFull;
            else if (i == health && damaged)
                heartType = heartDamage;
            else
                heartType = heartEmpty;

            spriteBatch.draw(heartType,
                    Gdx.graphics.getWidth() - HEALTH_BAR_OFFSET_X - HEART_SIZE
                            - (PLAYER_HEALTH - i - 1) * (HEART_SIZE + HEART_MARGIN),
                    Gdx.graphics.getHeight() - HEALTH_BAR_OFFSET_Y, HEART_SIZE, HEART_SIZE);
        }
    }

    private void updateScore() {
        scoreText = String.format("%04d", this.score);
    }

    private void drawScoreText(SpriteBatch spriteBatch) {
        GlyphLayout layout = new GlyphLayout(scoreFont, scoreText);
        scoreFont.draw(spriteBatch, scoreText, Gdx.graphics.getWidth() / 2 - (int) layout.width / 2,
                Gdx.graphics.getHeight() - SCORE_TEXT_OFFSET);
    }
}
