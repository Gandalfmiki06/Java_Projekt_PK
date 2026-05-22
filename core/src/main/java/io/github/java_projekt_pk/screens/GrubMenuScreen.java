package io.github.java_projekt_pk.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.java_projekt_pk.Main;
import io.github.java_projekt_pk.Managers.FontManager;

public class GrubMenuScreen extends ScreenAdapter {

    private enum MenuState {
        MAIN_MENU,
        LEADERBOARD,
        SETTINGS
    }

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private MenuState currentState = MenuState.MAIN_MENU;

    private Array<String> mainMenuOptions;
    private Array<String> leaderboardOptions;
    private Array<String> settingsOptions;

    private int selectedIndex = 0;

    private final int FONT_SIZE = 20;

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        var params = FontManager.getDefaultParameters();
        params.size = FONT_SIZE;

        font = FontManager.generateFont("terminus", params);

        mainMenuOptions = new Array<>();
        mainMenuOptions.add("Start Game");
        mainMenuOptions.add("Leaderboard");
        mainMenuOptions.add("Settings");
        mainMenuOptions.add("Quit Game");

        leaderboardOptions = new Array<>();
        leaderboardOptions.add("<- Back to Main Menu");
        leaderboardOptions.add("1. XDDD - 9999 pts");
        leaderboardOptions.add("2. LOL - 5400 pts");
        leaderboardOptions.add("3. KIT - 1200 pts");

        settingsOptions = new Array<>();
        settingsOptions.add("<- Back to Main Menu and Apply");
        settingsOptions.add("Master Volume");
        settingsOptions.add("Music Volume");
        settingsOptions.add("Sfx Volume");

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                int currentListSize = getCurrentList().size;

                if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
                    selectedIndex--;
                    if (selectedIndex < 0) {
                        selectedIndex = currentListSize - 1;
                    }
                    return true;
                }
                if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
                    selectedIndex = (selectedIndex + 1) % currentListSize;
                    return true;
                }
                if (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE) {
                    handleSelection();
                    return true;
                }
                if (keycode == Input.Keys.ESCAPE) {
                    if (currentState == MenuState.LEADERBOARD || currentState == MenuState.SETTINGS) {
                        changeState(MenuState.MAIN_MENU);
                    }
                    return true;
                }
                if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D)
                {
                    if (currentState == MenuState.SETTINGS)
                    {
                        modifySetting(true);
                    }
                }
                if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A)
                {
                    if (currentState == MenuState.SETTINGS)
                    {
                        modifySetting(false);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.f, 0.f, 0.f, 1f);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float boxX = 50;
        float boxY = 150;
        float boxWidth = screenWidth - 100;
        float boxHeight = screenHeight - 300;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(boxX, boxY, boxWidth, boxHeight);
        shapeRenderer.end();

        Array<String> activeOptions = getCurrentList();
        float itemX = boxX + 30;
        float itemY = boxY + boxHeight - 40;

        float itemBoxY = itemY;

        for (int i = 0; i < activeOptions.size; i++) {
            if (i == selectedIndex) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.WHITE);
                shapeRenderer.rect(itemX - 10, itemBoxY - 35 + 5, boxWidth - 40, FONT_SIZE + 35 - 20);
                shapeRenderer.end();

                break;
            }

            itemBoxY -= 35;
        }

        batch.begin();

        for (int i = 0; i < activeOptions.size; i++) {
            String itemText = activeOptions.get(i);

            if (activeOptions == settingsOptions)
            {
                switch (i) {
                    case 1 -> itemText = String.format("%-15s%s", itemText, getVolumeBar(Main.soundManager.MasterVolume));
                    case 2 -> itemText = String.format("%-15s%s", itemText, getVolumeBar(Main.soundManager.MusicVolume));
                    case 3 -> itemText = String.format("%-15s%s", itemText, getVolumeBar(Main.soundManager.SfxVolume));
                    default -> {}
                }
            }

            if (i == selectedIndex) {
                font.draw(batch, "[BLACK]" + itemText, itemX, itemY);
            } else {
                font.draw(batch, "[WHITE]" + itemText, itemX, itemY);
            }
            itemY -= 35;
        }

        font.draw(batch, "GNU GRUB  version 2.06", 50, screenHeight - 50);
        switch (currentState) {
            case MAIN_MENU -> font.draw(batch, "Select an option to proceed.", 50, screenHeight - 90);
            case LEADERBOARD -> font.draw(batch, "GLOBAL LEADERBOARD - Top Scores", 50, screenHeight - 90);
            case SETTINGS -> font.draw(batch, "Settings", 50, screenHeight - 90);
            default -> {}
        }

        if (currentState == MenuState.MAIN_MENU) {
            font.draw(batch, "Press ENTER to select.", 50, 80);
        } else {
            font.draw(batch, "Press ENTER on 'Back' or press ESC key to return.", 50, 80);
        }

        batch.end();
    }

    private Array<String> getCurrentList() {
        return switch (currentState) {
            case MAIN_MENU -> mainMenuOptions;
            case LEADERBOARD -> leaderboardOptions;
            case SETTINGS -> settingsOptions;
            default -> mainMenuOptions;
        };
    }

    private void changeState(MenuState newState) {
        this.currentState = newState;
        this.selectedIndex = 0;
    }

    private void handleSelection() {
        switch (currentState) {
            case MAIN_MENU -> {
                switch (selectedIndex) { // can't we change this to an enum or something like that so it's clear what option coresponds to what?
                    case 0 -> Main.getGameInstance().setScreen(new InGameScreen());
                    case 1 -> changeState(MenuState.LEADERBOARD);
                    case 2 -> changeState(MenuState.SETTINGS);
                    case 3 -> Gdx.app.exit();
                }
            }
            case LEADERBOARD -> {
                if (selectedIndex == 0) {
                    changeState(MenuState.MAIN_MENU);
                }
            }
            case SETTINGS -> {
                switch (selectedIndex) {
                    case 0 -> {
                        changeState(MenuState.MAIN_MENU);
                    }
                }
            }
        }
    }

    private void modifySetting(boolean increase)
    {
        switch (selectedIndex) {
            case 1 -> Main.soundManager.MasterVolume += increase ? 0.05f : -0.05f;
            case 2 -> Main.soundManager.MusicVolume += increase ? 0.05f : -0.05f;
            case 3 -> Main.soundManager.SfxVolume += increase ? 0.05f : -0.05f;
        }

        Main.soundManager.MasterVolume = Math.clamp(Main.soundManager.MasterVolume, 0, 1);
        Main.soundManager.MusicVolume = Math.clamp(Main.soundManager.MusicVolume, 0, 1);
        Main.soundManager.SfxVolume = Math.clamp(Main.soundManager.SfxVolume, 0, 1);

        Main.soundManager.updateVolume();
    }

    private String getVolumeBar(float volume)
    {
        int filled = Math.round(volume * 20);
        StringBuilder bar = new StringBuilder("[[");

        for (int i = 0; i < 20; i++) {
            if (i < filled) {
                bar.append('|');
            } else {
                bar.append('-');
            }
        }

        bar.append("] ");
        bar.append(Math.round(volume*100));
        bar.append('%');

        return bar.toString();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}
