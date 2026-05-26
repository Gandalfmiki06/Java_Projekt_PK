package io.github.java_projekt_pk.screens;

import java.time.format.DateTimeFormatter;
import java.util.Map;

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
import io.github.java_projekt_pk.globals.Box;
import io.github.java_projekt_pk.globals.Credits;
import io.github.java_projekt_pk.ui.*;

public class GrubMenuScreen extends ScreenAdapter {

    private enum MenuState {
        MAIN_MENU,
        SETTINGS,
        LEADERBOARD,
        CREDITS,
        LICENSE,
        SETTINGS_CHANGE_NICKNAME
    }

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private SpriteBatch batch;
    private BitmapFont font;

    private MenuState currentState = MenuState.MAIN_MENU;
    private Interactive inputControlledBy;

    private Menu mainMenuOptions;
    private Menu settingsOptions;
    private Menu leaderboardOptions;
    private Menu creditsOptions;
    private Menu licensesOptions;

    private InputMenu changePlayerNameInputMenu;

    public static final int FONT_SIZE = 20;

    @Override
    public void show() {
        batch = new SpriteBatch();

        var params = FontManager.getDefaultParameters();
        params.size = FONT_SIZE;

        font = FontManager.generateFont("terminus", params);

        MenuItem backToMenuItem = new MenuItem("Back do menu", () -> changeState(MenuState.MAIN_MENU));

        mainMenuOptions = new Menu("Main Menu")
                .addItem(new MenuItem("Start Game", () -> Main.getGameInstance().setScreen(new InGameScreen())))
                .addItem(new MenuItem("Settings", () -> {
                    changeState(MenuState.SETTINGS);
                }))
                .addItem(new MenuItem("Leaderboard", () -> {
                    this.leaderboardOptions.clearItems();
                    this.leaderboardOptions.addItem(backToMenuItem);

                    var lb = Main.getLeaderboard();

                    leaderboardOptions.addItem(new MenuItem("[DEBUG] Generate new random Score", () -> {
                        lb.addEntry("ANONYMOUS", MathUtils.random(0, 100000));
                        lb.save();
                        changeState(MenuState.MAIN_MENU);
                    }));

                    for (var entry : lb.getScores()) {
                        leaderboardOptions.addItem(new MenuItem(
                                entry.score() + " - " + entry.player() + "     [" + entry.time().format(FORMATTER) + "]",
                                () -> {
                                },
                                false)
                        );
                    }

                    changeState(MenuState.LEADERBOARD);
                }))
                .addItem(new MenuItem("Credits", () -> changeState(MenuState.CREDITS)))
                .addItem(new MenuItem("Licenses", () -> changeState(MenuState.LICENSE)))
                .addItem(new MenuItem("Quit Game", () -> Gdx.app.exit()));

        settingsOptions = new SettingsMenu(backToMenuItem);
        settingsOptions.addItem(new MenuItem("Change Player Name", () -> changeState(MenuState.SETTINGS_CHANGE_NICKNAME)));

        leaderboardOptions = new Menu("Leaderboards");

        creditsOptions = new Menu("Credits")
                .addItem(backToMenuItem);

        for (var txt : Credits.authors) {
            creditsOptions.addItem(new MenuItem(txt, () -> {
            }, false));
        }

        licensesOptions = new Menu("Licenses")
                .addItem(backToMenuItem);

        for (var txt : Credits.licenses) {
            licensesOptions.addItem(new MenuItem(txt, () -> {
            }, false));
        }

        changePlayerNameInputMenu = new InputMenu(
            "Change Player Name",
            (name) -> {
                Gdx.app.log("DEBUG", "Zmieniono nazwe uzytkownika na: " + name);
                changeState(MenuState.SETTINGS);
            },
            () -> changeState(MenuState.SETTINGS)
        );

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                Menu menu = getCurrentMenu();

                if (inputControlledBy != null) {
                    if(keycode == Input.Keys.ESCAPE) {
                        inputControlledBy = null;
                        return true;
                    }
                    return inputControlledBy.handleKeyDown(keycode);
                }

                switch (keycode) {
                    case Input.Keys.UP, Input.Keys.W -> {
                        menu.moveSelectionUp();
                        return true;
                    }
                    case Input.Keys.DOWN, Input.Keys.S -> {
                        menu.moveSelectionDown();
                        return true;
                    }
                    case Input.Keys.LEFT, Input.Keys.A -> {
                        menu.leftPressed();
                        return true;
                    }
                    case Input.Keys.RIGHT, Input.Keys.D -> {
                        menu.rightPressed();
                        return true;
                    }
                    case Input.Keys.ENTER, Input.Keys.SPACE -> {
                        var item = menu.getSelectedItem();
                        if(item instanceof Interactive) {
                            inputControlledBy = (Interactive) item;
                        }
                        menu.executeCurrentSelection();
                        return true;
                    }
                    case Input.Keys.ESCAPE -> {
                        if (currentState != MenuState.MAIN_MENU) {
                            changeState(MenuState.MAIN_MENU);
                        }
                        return true;
                    }
                    default -> {
                    }
                }
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                Menu menu = getCurrentMenu();

                if (inputControlledBy != null) {
                    return inputControlledBy.handleKeyTyped(character);
                }

                return false;
            }
        });
    }

    @Override
    public void render (float delta) {
        ScreenUtils.clear(0.f, 0.f, 0.f, 1f);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float boxX = 50;
        float boxY = 150;
        float boxWidth = screenWidth - 100;
        float boxHeight = screenHeight - 300;

        var shapeRenderer = Main.getShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(boxX, boxY, boxWidth, boxHeight);
        shapeRenderer.end();

        Menu menu = getCurrentMenu();
        Array<MenuItem> items = menu.getItems();
        MenuItem selectedItem = menu.getSelectedItem();

        int TEXT_SPACE = 10;
        float itemX = boxX + TEXT_SPACE;
        float itemY = boxY + boxHeight - TEXT_SPACE;

        batch.begin();
        for (int i = 0; i < items.size; i++){
            MenuItem item = items.get(i);
            boolean selected = item.equals(selectedItem);

            if(item instanceof Interactive interactiveItem) {
                batch.end();
                interactiveItem.setSize(new Box( itemX - TEXT_SPACE, itemY - FONT_SIZE - TEXT_SPACE, boxWidth, FONT_SIZE + TEXT_SPACE ));
                interactiveItem.render(interactiveItem.getSize(), delta, selected, item.equals(inputControlledBy));
                batch.begin();

                itemY -= TEXT_SPACE * 3;
                continue;
            }

            if(selected) {
                batch.end();
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.WHITE);
                shapeRenderer.rect(itemX - TEXT_SPACE, itemY - FONT_SIZE - TEXT_SPACE, boxWidth, FONT_SIZE + TEXT_SPACE);
                shapeRenderer.end();
                batch.begin();

                font.draw(batch, "[BLACK]" + item.getLabel(), itemX, itemY);
            }
            else {
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
        return switch (currentState) {
            case MAIN_MENU ->
                mainMenuOptions;
            case SETTINGS ->
                settingsOptions;
            case LEADERBOARD ->
                leaderboardOptions;
            case CREDITS ->
                creditsOptions;
            case LICENSE ->
                licensesOptions;
            case SETTINGS_CHANGE_NICKNAME ->
                changePlayerNameInputMenu;
            default ->
                new Menu("FALLBACK MENU");
        };
    }

    private void changeState(MenuState newState) {
        this.currentState = newState;
        getCurrentMenu().setSelectedIndex(0);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        changePlayerNameInputMenu.dispose();
    }
}
