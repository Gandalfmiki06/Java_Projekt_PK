package io.github.java_projekt_pk;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import io.github.java_projekt_pk.Managers.FontManager;
import io.github.java_projekt_pk.screens.GrubMenuScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends Game {
    private static Main instance;

    public static Main getGameInstance() {
        return instance;
    }

    @Override
    public void create() {
        instance = this;

        registerFonts();

        setScreen(new GrubMenuScreen());
    }

    private void registerFonts() {
        FontManager.registerFont("google", Gdx.files.internal("GoogleSansCode.ttf"));
        FontManager.registerFont("google-bold", Gdx.files.internal("GoogleSansCode-Bold.ttf"));
        FontManager.registerFont("terminus", Gdx.files.internal("TerminusTTF-4.49.3.ttf"));
        FontManager.registerFont("terminus-bold", Gdx.files.internal("TerminusTTF-Bold-4.49.3.ttf"));
    }

    @Override
    public void dispose() {
        super.dispose();

        instance = null;
    }
}
