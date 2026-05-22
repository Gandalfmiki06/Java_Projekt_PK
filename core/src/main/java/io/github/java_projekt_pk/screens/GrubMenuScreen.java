package io.github.java_projekt_pk.screens;

import java.time.format.DateTimeFormatter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.java_projekt_pk.Main;
import io.github.java_projekt_pk.Managers.FontManager;
import io.github.java_projekt_pk.globals.Credits;
import io.github.java_projekt_pk.ui.Menu;
import io.github.java_projekt_pk.ui.MenuItem;

public class GrubMenuScreen extends ScreenAdapter {

    private enum MenuState {
        MAIN_MENU,
        LEADERBOARD,
        CREDITS,
        LICENSE
    }
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private MenuState currentState = MenuState.MAIN_MENU;

    private Menu mainMenuOptions;
    private Menu leaderboardOptions;
    private Menu creditsOptions;
    private Menu licensesOptions;

    private final int FONT_SIZE = 20;

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        var params = FontManager.getDefaultParameters();
        params.size = FONT_SIZE;

        font = FontManager.generateFont("terminus", params);

        MenuItem backToMenuItem = new MenuItem("Back do menu", () -> changeState(MenuState.MAIN_MENU));

        mainMenuOptions = new Menu("Main Menu")
            .addItem(new MenuItem("Start Game", () -> Main.getGameInstance().setScreen(new InGameScreen())))
            .addItem(new MenuItem("Leaderboard", () -> {
                this.leaderboardOptions.clearItems();
                this.leaderboardOptions.addItem(backToMenuItem);

                var lb = Main.getLeaderboard();

                leaderboardOptions.addItem(new MenuItem("Generate new Score", () -> {
                    lb.addEntry("ANONYMOUS", MathUtils.random(0, 100000));
                    lb.save();
                }));

                for (var entry : lb.getScores()) {
                    leaderboardOptions.addItem(new MenuItem(
                        entry.score() + " - " + entry.player() + "     [" + entry.time().format(FORMATTER) + "]",
                        () -> {},
                        false)
                    );
                }

                changeState(MenuState.LEADERBOARD);
            }))
            .addItem(new MenuItem("Credits", () -> changeState(MenuState.CREDITS)))
            .addItem(new MenuItem("Licenses", () -> changeState(MenuState.LICENSE)))
            .addItem(new MenuItem("Quit Game", () -> Gdx.app.exit()));

        leaderboardOptions = new Menu("Leaderboards");

        creditsOptions = new Menu("Credits")
            .addItem(backToMenuItem);

        for (var txt : Credits.authors) {
            creditsOptions.addItem(new MenuItem(txt, () -> {}, false));
        }

        licensesOptions = new Menu("Licenses")
            .addItem(backToMenuItem);

        for (var txt : Credits.licenses) {
            licensesOptions.addItem(new MenuItem(txt, () -> {}, false));
        }

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                Menu menu = getCurrentMenu();

                if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
                    menu.moveSelectionUp();
                    return true;
                }
                if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
                    menu.moveSelectionDown();
                    return true;
                }
                if (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE) {
                    menu.executeCurrentSelection();
                    return true;
                }
                if (keycode == Input.Keys.ESCAPE) {
                    if (currentState != MenuState.MAIN_MENU) {
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

        Menu menu = getCurrentMenu();
        Array<MenuItem> items = menu.getItems();
        MenuItem selectedItem = menu.getSelectedItem();
        int selectedIndex = menu.getSelectedIndex();

        int TEXT_SPACE = 10;
        float itemX = boxX + TEXT_SPACE;
        float itemY = boxY + boxHeight - TEXT_SPACE;

        float itemBoxY = itemY;

        for (int i = 0; i < items.size; i++) {
            if (i == selectedIndex) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.WHITE);
                shapeRenderer.rect(itemX - TEXT_SPACE, itemBoxY - FONT_SIZE - TEXT_SPACE, boxWidth, FONT_SIZE + TEXT_SPACE);
                shapeRenderer.end();

                break;
            }

            itemBoxY -= TEXT_SPACE * 3;
        }

        batch.begin();

        for (int i = 0; i < items.size; i++) {
            MenuItem item = items.get(i);

            if (item.equals(selectedItem)) {
                font.draw(batch, "[BLACK]" + item.getLabel(), itemX, itemY);
            } else {
                font.draw(batch, "[" + (item.isSelectable() ? "WHITE" : "GRAY") + "]" + item.getLabel(), itemX, itemY);
            }
            itemY -= TEXT_SPACE * 3;
        }

        font.draw(batch, "GNU GRUB  version 2.06", 50, screenHeight - 50);
        font.draw(batch, menu.getTitle(), 50, screenHeight - 90);

        if (currentState == MenuState.MAIN_MENU) {
            font.draw(batch, "Press ENTER to select.", 50, 80);
        } else {
            font.draw(batch, "Press ENTER on 'Back' or press ESC key to return.", 50, 80);
        }

        batch.end();
    }

    private Menu getCurrentMenu() {
        if (currentState == MenuState.MAIN_MENU) {
            return mainMenuOptions;
        } else if (currentState == MenuState.LEADERBOARD) {
            return leaderboardOptions;
        } else if (currentState == MenuState.CREDITS) {
            return creditsOptions;
        } else if (currentState == MenuState.LICENSE) {
            return licensesOptions;
        } else {
            return new Menu("FALLBACK MENU");
        }
    }

    private void changeState(MenuState newState) {
        this.currentState = newState;
        getCurrentMenu().setSelectedIndex(0);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}
