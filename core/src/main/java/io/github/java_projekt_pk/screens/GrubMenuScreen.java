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
        LEADERBOARD
    }

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private MenuState currentState = MenuState.MAIN_MENU;

    private Array<String> mainMenuOptions;
    private Array<String> leaderboardOptions;

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
        mainMenuOptions.add("Quit Game");

        leaderboardOptions = new Array<>();
        leaderboardOptions.add("<- Back to Main Menu");
        leaderboardOptions.add("1. XDDD - 9999 pts");
        leaderboardOptions.add("2. LOL - 5400 pts");
        leaderboardOptions.add("3. KIT - 1200 pts");

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                int currentListSize = getCurrentList().size;

                if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
                    selectedIndex--;
                    if (selectedIndex < 0)
                        selectedIndex = currentListSize - 1;
                    return true;
                }
                if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
                    selectedIndex++;
                    if (selectedIndex >= currentListSize)
                        selectedIndex = 0;
                    return true;
                }
                if (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE) {
                    handleSelection();
                    return true;
                }
                if (keycode == Input.Keys.ESCAPE) {
                    if (currentState == MenuState.LEADERBOARD) {
                        changeState(MenuState.MAIN_MENU);
                    }
                    return true;
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

            if (i == selectedIndex) {
                font.draw(batch, "[BLACK]" + itemText, itemX, itemY);
            } else {
                font.draw(batch, "[WHITE]" + itemText, itemX, itemY);
            }
            itemY -= 35;
        }

        font.draw(batch, "GNU GRUB  version 2.06", 50, screenHeight - 50);
        if (currentState == MenuState.MAIN_MENU) {
            font.draw(batch, "Select an option to proceed.", 50, screenHeight - 90);
        } else {
            font.draw(batch, "GLOBAL LEADERBOARD - Top Scores", 50, screenHeight - 90);
        }

        if (currentState == MenuState.MAIN_MENU) {
            font.draw(batch, "Press ENTER to select.", 50, 80);
        } else {
            font.draw(batch, "Press ENTER on 'Back' or press ESC key to return.", 50, 80);
        }

        batch.end();
    }

    private Array<String> getCurrentList() {
        if (currentState == MenuState.MAIN_MENU) {
            return mainMenuOptions;
        } else {
            return leaderboardOptions;
        }
    }

    private void changeState(MenuState newState) {
        this.currentState = newState;
        this.selectedIndex = 0;
    }

    private void handleSelection() {
        if (currentState == MenuState.MAIN_MENU) {
            switch (selectedIndex) {
                case 0: // Start Game
                    Main.getGameInstance().setScreen(new InGameScreen());
                    break;
                case 1: // Leaderboard
                    changeState(MenuState.LEADERBOARD);
                    break;
                case 2: // Quit Game
                    Gdx.app.exit();
                    break;
            }
        } else if (currentState == MenuState.LEADERBOARD) {
            if (selectedIndex == 0) {
                changeState(MenuState.MAIN_MENU);
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}
