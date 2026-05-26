package io.github.java_projekt_pk.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.java_projekt_pk.Main;
import io.github.java_projekt_pk.Managers.FontManager;
import io.github.java_projekt_pk.globals.Box;

public class InputMenuItem extends MenuItem implements Interactive {
    public static final int FONT_SIZE = 20;

    protected Box size;
    protected String value;

    private SpriteBatch batch;
    private BitmapFont font;

    public InputMenuItem(String label, boolean selectable) {
        super(label, () -> {}, selectable);
        this.size = new Box(0,0,0,0);
        this.value = "";

        var params = FontManager.getDefaultParameters();
        params.size = FONT_SIZE;

        font = FontManager.generateFont("terminus-bold", params);

        batch = new SpriteBatch();
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void render(Box coors, float delta, boolean selected, boolean focus) {
        var TEXT_SPACE = 10;
        if(selected || focus) {
            var shapeRenderer = Main.getShapeRenderer();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(focus ? Color.BLUE : Color.WHITE);
            shapeRenderer.rect(coors.x + 1, coors.y, coors.width - 2, coors.height);
            shapeRenderer.end();
        }

        batch.begin();
        if(focus) {
            font.draw(batch, "[WHITE]" + value, coors.x + TEXT_SPACE, coors.y + FONT_SIZE + TEXT_SPACE);
        }
        else {
            font.draw(batch, "["+ (selected ? "BLACK" : "WHITE") +"]" + value, coors.x + TEXT_SPACE, coors.y + FONT_SIZE + TEXT_SPACE);
        }
        batch.end();
    }

    @Override
    public void setSize(Box size) {
        this.size = size;
    }

    @Override
    public Box getSize() {
        return size;
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    @Override
    public boolean handleKeyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.BACKSPACE -> {
                if (!value.isEmpty()) {
                    setValue(value.substring(0, value.length() - 1));
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handleKeyTyped(char character) {
        if (Character.isLetterOrDigit(character) || character == ' ') {
            char upperChar = Character.toUpperCase(character);
            setValue(value + upperChar);
            return true;
        }

        return false;
    }

    @FunctionalInterface
    public interface InputMenuAction {
        void execute(String input);
    }
}
