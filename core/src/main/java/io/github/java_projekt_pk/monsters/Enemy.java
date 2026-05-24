package io.github.java_projekt_pk.monsters;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.java_projekt_pk.Main;
import io.github.java_projekt_pk.Managers.EnemyManager;
import io.github.java_projekt_pk.Managers.SoundManager;
import io.github.java_projekt_pk.globals.HurtTextGenerator;

public class Enemy {

    enum State {
        APPROACHING,
        IDLE,
        MOVING
    }

    private static final Random RANDOM = new Random();

    private static final float INITIAL_SCALE = 3.0f;
    private static final float SCALE_MIN = 0.4f;
    private static final float SCALE_MAX = 1.5f;
    private static final float APPROACHING_TIME = 0.7f;
    private static final float IDLE_TIME_MIN = 0.1f;
    private static final float IDLE_TIME_MAX = 1.0f;
    private static final float MOVE_TIME_MIN = 0.5f;
    private static final float MOVE_TIME_MAX = 2.0f;
    private static final float PLAYER_DAMAGE_BORDER = 200.0f;
    private static final float TEXTBOX_PADDING = 5.0f;
    private static final float SCREEN_BORDER = 100.0f;
    private static final float SPAWN_RADIOUS = 200.0f;

    private float x = 0.0f;
    private float y = 0.0f;
    private final float scale;
    private float destinationX;
    private float destinationY;
    private float currentScale;
    private float currentX;
    private float currentY;
    private float timerEnd;

    protected Animation<TextureRegion> animation;
    protected boolean selected = false;
    protected int health = 1;
    protected int playerScore = 100;

    private State state = State.APPROACHING;
    private float timer = 0.0f;

    public String hurtText = "";
    public String inputText = "";

    public Enemy() {
        hurtText = HurtTextGenerator.getRandomText();

        var displayWidth = Gdx.graphics.getWidth();
        x = RANDOM.nextFloat(displayWidth - SPAWN_RADIOUS, displayWidth);
        y = RANDOM.nextFloat(SCREEN_BORDER, Gdx.graphics.getHeight() - SCREEN_BORDER);
        currentX = x;
        currentY = y;

        scale = RANDOM.nextFloat(SCALE_MIN, SCALE_MAX);
        currentScale = 0.0f;
    }

    public void timeStep(float delta) {
        switch (state) {
            case APPROACHING: {
                timer += delta;
                if (timer >= APPROACHING_TIME) {
                    timer = 0.0f;
                    timerEnd = RANDOM.nextFloat(IDLE_TIME_MIN, IDLE_TIME_MAX);
                    state = State.IDLE;
                }

                currentScale = timer / APPROACHING_TIME;
                currentX = x;
                currentY = y;
                break;
            }
            case IDLE: {
                timer += delta;
                if (timer >= timerEnd) {
                    timer = 0.0f;
                    timerEnd = RANDOM.nextFloat(MOVE_TIME_MIN, MOVE_TIME_MAX);
                    state = State.MOVING;
                    destinationX = RANDOM.nextFloat(x - 100.0f, x - 50.0f);
                    destinationY = RANDOM.nextFloat(Math.max(y - 50.0f, SCREEN_BORDER),
                            Math.min(y + 50.0f, Gdx.graphics.getHeight() - SCREEN_BORDER));
                }

                currentScale = scale;
                currentX = x;
                currentY = y;
                break;
            }
            case MOVING: {
                timer += delta;
                if (timer >= timerEnd) {
                    timer = 0.0f;
                    timerEnd = RANDOM.nextFloat(IDLE_TIME_MIN, IDLE_TIME_MAX);
                    state = State.IDLE;
                    x = destinationX;
                    y = destinationY;
                }

                currentScale = scale;
                currentX = x + (destinationX - x) * timer / timerEnd;
                currentY = y + (destinationY - y) * timer / timerEnd;
                break;
            }
        }

        if (currentX < PLAYER_DAMAGE_BORDER) {
            Main.getHud().damage();
            die();
        }
    }

    public void unselect() {
        selected = false;
    }

    public void select() {
        selected = true;
    }

    public void hurt() {
        health--;
        Main.soundManager.playSfx(SoundManager.SfxNames.DAMAGE_ENEMY, 0.1f);
        if (health > 0) {
            hurtText = HurtTextGenerator.getRandomText();
            inputText = "";
        } else {
            Main.getHud().addScore(playerScore);
            die();
        }
    }

    private void die() {
        Main.soundManager.playSfx(SoundManager.SfxNames.DEATH_ENEMY, 0.1f);
        EnemyManager.deleteEnemy(this);
    }

    public void draw(SpriteBatch batch, float time) {
        var windowScale = Gdx.graphics.getHeight() / 720.0f * INITIAL_SCALE * currentScale;
        TextureRegion currentFrame = animation.getKeyFrame(time, true);
        float width = currentFrame.getRegionWidth() * windowScale;
        float height = currentFrame.getRegionHeight() * windowScale;
        batch.draw(currentFrame, currentX - (int) (width / 2), currentY, width, height);
    }

    public void drawText(SpriteBatch batch) {
        String renderString = getRenderString();
        GlyphLayout layout = new GlyphLayout(Main.getFont(), renderString);
        Main.getFont().draw(batch, renderString, currentX - (int) (layout.width) / 2, currentY);
    }

    public void drawTextBox(ShapeRenderer shapeRenderer) {
        String renderString = getRenderString();
        GlyphLayout layout = new GlyphLayout(Main.getFont(), renderString);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(currentX - (int) (layout.width) / 2 - TEXTBOX_PADDING,
                currentY - layout.height - TEXTBOX_PADDING, layout.width + TEXTBOX_PADDING * 2,
                layout.height + TEXTBOX_PADDING);
    }

    public void setAnimation(String animationName) {

    }

    public float getPosX() {
        return currentX;
    }

    public float getPosY() {
        return currentY;
    }

    private enum TextColor {
        WHITE, RED, GREEN
    }

    protected String getRenderString() {
        if (!selected) {
            return String.format("[GRAY]%s[]", hurtText);
        }

        StringBuilder result = new StringBuilder(">");

        TextColor color = TextColor.WHITE;

        int maxLen = Math.max(inputText.length(), hurtText.length());

        for (int i = 0; i < maxLen; i++) {
            char c1 = i < inputText.length() ? inputText.charAt(i) : 0;
            char c2 = i < hurtText.length() ? hurtText.charAt(i) : 0;

            TextColor targetColor;
            char nextChar;

            if (c1 == 0) {
                targetColor = TextColor.WHITE;
                nextChar = c2;
            } else if (c1 == c2) {
                targetColor = TextColor.GREEN;
                nextChar = c1;
            } else {
                targetColor = TextColor.RED;
                nextChar = c1;
            }

            if (color != targetColor) {
                result.append("[][").append(targetColor.name()).append("]");
                color = targetColor;
            }

            result.append(escapeMarkup(nextChar));
        }

        result.append("[]");

        return result.toString();
    }

    private String escapeMarkup(char c) {
        if (c == '[') {
            return "[["; // this avoids starting new markup tag
        }

        return String.valueOf(c);
    }

    public void typeCharacter(char c) {
        inputText += c;
        if (inputText.equals(hurtText)) {
            hurt();
        }
    }

    public void backspace() {
        if (!inputText.isEmpty()) {
            inputText = inputText.substring(0, inputText.length() - 1);
        }
    }
}
