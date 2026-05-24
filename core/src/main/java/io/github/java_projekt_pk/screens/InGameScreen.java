package io.github.java_projekt_pk.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.java_projekt_pk.Main;
import io.github.java_projekt_pk.Managers.EnemyManager;
import io.github.java_projekt_pk.Managers.InputManager;
import io.github.java_projekt_pk.globals.Box;
import io.github.java_projekt_pk.globals.SystemDText;

import io.github.java_projekt_pk.monsters.Enemy;
import io.github.java_projekt_pk.monsters.Slime;

enum MESSAGETYPE {
    OK,
    WARN,
    ERROR
}

record SystemDMessage(String text, MESSAGETYPE type) {

}

public class InGameScreen implements Screen {

    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final int fontOffset = Math.round(Main.FONT_SIZE * 0.75f);

    private float time;

    private final InputManager inputManager;

    private final List<SystemDMessage> activeTexts = new ArrayList<>();
    private float nextTextDelay = MathUtils.random(0.5f, 3.0f);
    private float timeSinceLastText = 0.f;
    private float breakTimer = 0.f;

    private final int MAX_MESSAGE_COUNT = 30;
    private final float BREAK_DELAY = 1.0f;

    private final float START_SEQUENCE_TIME = 1.0f;
    private final float START_DELAY = 0.5f;
    private final float MESSAGE_DELAY = 0.5f;

    private boolean ok1Triggered = false;
    private boolean ok2Triggered = false;
    private boolean warnTriggered = false;
    private boolean errorTriggered = false;

    private final Box startBox = new Box(50, 150, Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 300);
    private final Box drawBox = new Box(50, 150, Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 300);
    private final Box finalBox = new Box(30, Gdx.graphics.getHeight() * 0.25f, Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f);

    private GAMESTATE currentState;

    private enum GAMESTATE {
        START,
        BREAK,
        WAVE
    }

    public InGameScreen() {
        currentState = GAMESTATE.START;
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        inputManager = new InputManager();
        Gdx.input.setInputProcessor(inputManager);

        font = Main.getFont();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        process(delta);
        var hud = Main.getHud();
        hud.timeStep(delta);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(drawBox.x, drawBox.y, drawBox.width, drawBox.height);
        shapeRenderer.end();

        batch.begin();

        float yPosition = finalBox.y + finalBox.height;
        for (SystemDMessage message : activeTexts) {
            switch (message.type()) {
                case OK -> {
                    font.draw(batch, String.format("[WHITE][[  %s  ] %s", "[GREEN]OK[]", message.text()), finalBox.x, yPosition);
                }
                case WARN -> {
                    font.draw(batch, String.format("[WHITE][[ %s ] %s", "[ORANGE]WARN[]", message.text()), finalBox.x, yPosition);
                }
                case ERROR -> {
                    font.draw(batch, String.format("[WHITE][[%s] %s", "[RED]FAILED[]", message.text()), finalBox.x, yPosition);
                }
            }
            yPosition -= fontOffset;
        }

        for (Enemy enemy : EnemyManager.enemies) {
            enemy.draw(batch, time);
        }
        
        hud.draw(batch);

        batch.end();
    }

    private void process(float delta) {
        time += delta;
        switch (currentState) {
            case START -> {
                manageStart();
            }
            case BREAK -> {
                manageBreak(delta);
            }
            case WAVE -> {
                manageWave(delta);
            }
        }
    }

    private void manageStart() {
        float delay = START_DELAY;
        float t = (time - delay) / START_SEQUENCE_TIME;
        t = Math.max(0.0f, Math.min(t, 1.0f));

        drawBox.x = startBox.x + (finalBox.x - startBox.x) * t;
        drawBox.y = startBox.y + (finalBox.y - startBox.y) * t;
        drawBox.width = startBox.width + (finalBox.width - startBox.width) * t;
        drawBox.height = startBox.height + (finalBox.height - startBox.height) * t;

        delay += START_SEQUENCE_TIME;

        if (!ok1Triggered && time >= delay + MESSAGE_DELAY) {
            addSystemdMessage(MESSAGETYPE.OK, 10);
            ok1Triggered = true;
        }

        if (!ok2Triggered && time >= delay + MESSAGE_DELAY * 2) {
            addSystemdMessage(MESSAGETYPE.OK, 10);
            ok2Triggered = true;
        }

        if (!warnTriggered && time >= delay + MESSAGE_DELAY * 3) {
            addSystemdMessage(MESSAGETYPE.WARN, 10);
            warnTriggered = true;
        }

        if (!errorTriggered && time >= delay + MESSAGE_DELAY * 4) {
            addSystemdMessage(MESSAGETYPE.ERROR, 10);
            errorTriggered = true;
            currentState = GAMESTATE.BREAK;
        }
    }

    private void manageBreak(float delta) {
        // this function exist because, maybe we will want something to happen between waves
        breakTimer += delta;

        if (breakTimer >= BREAK_DELAY) {
            generateWave();
            breakTimer = 0;
            currentState = GAMESTATE.WAVE;
        }
    }

    private void generateWave() {
        // TODO: change this to more complex wave generation, make enemy spawn off screen on the right and slowly move towards "terminal", damaging player when they get there
        Slime slime1 = new Slime(Main.redSlimeSpecies);
        slime1.setAnimation("Attack3");
        slime1.setPos(800, 500);
        EnemyManager.enemies.add(slime1);

        Slime slime2 = new Slime(Main.greenSlimeSpecies);
        slime2.setAnimation("Hurt");
        slime2.setPos(700, 600);
        EnemyManager.enemies.add(slime2);

        Slime slime3 = new Slime(Main.greenSlimeSpecies);
        slime3.setAnimation("Run");
        slime3.setScale(0.5f);
        slime3.setPos(600, 100);
        EnemyManager.enemies.add(slime3);

        Slime slime4 = new Slime(Main.blueSlimeSpecies);
        slime4.setAnimation("Jump");
        slime4.setPos(700, 300);
        EnemyManager.enemies.add(slime4);

        Slime slime5 = new Slime(Main.blueSlimeSpecies);
        slime5.setAnimation("Walk");
        slime5.setPos(800, 400);
        EnemyManager.enemies.add(slime5);

        EnemyManager.selectNextEnemy();
    }

    private void manageWave(float delta) {
        if (InputManager.wasTabJustPressed()) {
            EnemyManager.selectNextEnemy();
        }

        timeSinceLastText += delta;

        if (timeSinceLastText >= nextTextDelay && nextTextDelay > 0) {
            nextRandomText();
        }

        // hope we can somehow make it not check each frame :C
        if (EnemyManager.enemies.isEmpty()) {
            currentState = GAMESTATE.BREAK;
        }
    }

    private void addSystemdMessage(MESSAGETYPE type) {
        switch (type) {
            case OK -> {
                activeTexts.add(new SystemDMessage(SystemDText.generateOKMessage(), MESSAGETYPE.OK));
            }
            case WARN -> {
                activeTexts.add(new SystemDMessage(SystemDText.generateWARNMessage(), MESSAGETYPE.WARN));
            }
            case ERROR -> {
                activeTexts.add(new SystemDMessage(SystemDText.generateERRORMessage(), MESSAGETYPE.ERROR));
            }
        }

        while (activeTexts.size() >= MAX_MESSAGE_COUNT) {
            activeTexts.removeFirst();
        }
    }

    private void addSystemdMessage(MESSAGETYPE type, int count) {
        for (int i = 0; i < count; i++) {
            switch (type) {
                case OK -> {
                    activeTexts.add(new SystemDMessage(SystemDText.generateOKMessage(), MESSAGETYPE.OK));
                }
                case WARN -> {
                    activeTexts.add(new SystemDMessage(SystemDText.generateWARNMessage(), MESSAGETYPE.WARN));
                }
                case ERROR -> {
                    activeTexts.add(new SystemDMessage(SystemDText.generateERRORMessage(), MESSAGETYPE.ERROR));
                }
            }
        }

        while (activeTexts.size() >= MAX_MESSAGE_COUNT) {
            activeTexts.removeFirst();
        }
    }

    private void nextRandomText() {
        // TODO: this should be based on current health
        int rng = MathUtils.random(2);
        switch (rng) {
            case 0 -> {
                addSystemdMessage(MESSAGETYPE.OK);
            }
            case 1 -> {
                addSystemdMessage(MESSAGETYPE.WARN);
            }
            case 2 -> {
                addSystemdMessage(MESSAGETYPE.ERROR);
            }
        }

        timeSinceLastText -= nextTextDelay;
        nextTextDelay = MathUtils.random(0.5f, 2.0f);
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
