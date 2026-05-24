package io.github.java_projekt_pk.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.java_projekt_pk.Main;
import io.github.java_projekt_pk.Managers.EnemyManager;
import io.github.java_projekt_pk.Managers.SoundManager;
import io.github.java_projekt_pk.globals.HurtTextGenerator;

public class Enemy {

    static final float INITIAL_SCALE = 3.0f;
    protected float x = 0.0f;
    protected float y = 0.0f;
    protected float scale = 1.0f;
    protected Animation<TextureRegion> animation;

    protected boolean selected = false;
    protected int health = 1;

    public String hurtText = "";
    public String inputText = "";

    public Enemy() {
        hurtText = HurtTextGenerator.getRandomText();
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
            die();
        }
    }

    private void die() {
        Main.soundManager.playSfx(SoundManager.SfxNames.DEATH_ENEMY, 0.1f);
        EnemyManager.deleteEnemy(this);
    }

    public void draw(SpriteBatch batch, float time) {
        var windowScale = Gdx.graphics.getHeight() / 720.0f * INITIAL_SCALE * scale;
        TextureRegion currentFrame = animation.getKeyFrame(time, true);
        float width = currentFrame.getRegionWidth() * windowScale;
        float height = currentFrame.getRegionHeight() * windowScale;
        // batch.draw(currentFrame, x - (int) (width / 2), Gdx.graphics.getHeight() - y
        // - (int) (height * 0.15), width,
        // height);
        batch.draw(currentFrame, x - (int) (width / 2), y, width, height);

        String renderString = getRenderString();
        GlyphLayout layout = new GlyphLayout(Main.getFont(), renderString);
        Main.getFont().draw(batch, renderString, x - (int) (layout.width) / 2, y);
    }

    public void setAnimation(String animationName) {

    }

    // Set the position of the slime center ((0, 0) is lower left corner)
    public void setPos(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // Set slime scale (default: 1)
    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getPosX() {
        return x;
    }

    public float getPosY() {
        return y;
    }

    public float getScale() {
        return scale;
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
