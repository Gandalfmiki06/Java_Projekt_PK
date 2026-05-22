package io.github.java_projekt_pk.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.java_projekt_pk.Main;
import io.github.java_projekt_pk.Managers.EnemyManager;
import io.github.java_projekt_pk.Managers.InputManager;
import io.github.java_projekt_pk.globals.SystemDText;
import io.github.java_projekt_pk.hud.Hud;

import io.github.java_projekt_pk.monsters.Enemy;
import io.github.java_projekt_pk.monsters.Slime;

public class InGameScreen implements Screen {

    private BitmapFont font;

    private float time;

    private InputManager inputManager;
    
    private Hud hud;

    private List<String> activeTexts = new ArrayList<>();
    private float nextTextDelay = MathUtils.random(0.5f, 3.0f);
    private float timeSinceLastText = 0.f;

    public InGameScreen() {
        inputManager = new InputManager();
        Gdx.input.setInputProcessor(inputManager);

        Slime slime1 = new Slime(Main.redSlimeSpecies);
        slime1.setAnimation("Attack3");
        slime1.setPos(800, 500);
        EnemyManager.enemies.add(slime1);

        Slime slime2 = new Slime(Main.greenSlimeSpecies);
        slime2.setAnimation("Hurt");
        slime2.setPos(400, 600);
        EnemyManager.enemies.add(slime2);

        Slime slime3 = new Slime(Main.greenSlimeSpecies);
        slime3.setAnimation("Run");
        slime3.setScale(0.5f);
        slime3.setPos(100, 100);
        EnemyManager.enemies.add(slime3);

        Slime slime4 = new Slime(Main.blueSlimeSpecies);
        slime4.setAnimation("Jump");
        slime4.setPos(300, 300);
        EnemyManager.enemies.add(slime4);

        Slime slime5 = new Slime(Main.blueSlimeSpecies);
        slime5.setAnimation("Walk");
        slime5.setPos(600, 400);
        EnemyManager.enemies.add(slime5);

        EnemyManager.selectNextEnemy();        
        hud = new Hud(Main.getTextureAtlas());
        font = Main.getFont();
    }

    private void nextRandomText() {
        activeTexts.add(SystemDText.randomText());

        if (activeTexts.size() > 128) {
            activeTexts.removeFirst();
        }

        timeSinceLastText -= nextTextDelay;
        nextTextDelay = MathUtils.random(0.2f, 1.0f);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        if (InputManager.wasTabJustPressed()) {
            EnemyManager.selectNextEnemy();
        }

        timeSinceLastText += delta;

        if (timeSinceLastText >= nextTextDelay && nextTextDelay > 0) {
            nextRandomText();
        }

        var spriteBatch = Main.getSpriteBatch();

        spriteBatch.begin();

        int yPosition = 50;
        for (String text : activeTexts.reversed()) {
            font.draw(spriteBatch, String.format("[ %s [WHITE]] %s", "[GREEN]OK", text), 30, yPosition);
            yPosition += 30;
        }

        for (Enemy enemy : EnemyManager.enemies) {
            enemy.draw(spriteBatch, time);
        }

        hud.timeStep(delta);
        hud.draw(spriteBatch);

        spriteBatch.end();
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
