package io.github.java_projekt_pk;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import io.github.java_projekt_pk.Managers.FontManager;
import io.github.java_projekt_pk.Managers.SoundManager;
import io.github.java_projekt_pk.monsters.SlimeSpecies;
import io.github.java_projekt_pk.screens.GrubMenuScreen;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends Game {

    private static Main instance;

    private static TextureAtlas atlas;
    private static SpriteBatch spriteBatch;

    public static SlimeSpecies redSlimeSpecies;
    public static SlimeSpecies greenSlimeSpecies;
    public static SlimeSpecies blueSlimeSpecies;

    private static BitmapFont font;

    private static Leaderboard leaderboard;

    public static SoundManager soundManager = new SoundManager();

    public static Main getGameInstance() {
        return instance;
    }

    public static String getPackageId() {
        return Main.class.getPackageName();
    }

    public static Path getProjectDir() {
        String os = System.getProperty("os.name").toLowerCase();
        String packageId = Main.getPackageId();

        if (os.contains("win")) {
            return Paths.get(System.getenv("APPDATA"), packageId);
        } else if (os.contains("mac")) {
            return Paths.get(System.getProperty("user.home"), "Library", "Application Support", packageId);
        } else {
            String xdgPath = System.getenv("XDG_CONFIG_HOME");

            if (xdgPath != null) {
                return Paths.get(xdgPath, "share", packageId);
            } else {
                return Paths.get(System.getProperty("user.home"), ".config", "share", packageId);
            }
        }
    }

    @Override
    public void create() {
        instance = this;

        leaderboard = new Leaderboard(getProjectDir().resolve("leaderboard"), 10);
        leaderboard.setAutosave(true);

        registerFonts();

        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 20;
        parameter.color = Color.WHITE;

        font = FontManager.generateFont("terminus-bold", parameter);

        atlas = new TextureAtlas(Gdx.files.internal("texture_atlas.atlas"));

        redSlimeSpecies = new SlimeSpecies(atlas, "Red_Slime");
        greenSlimeSpecies = new SlimeSpecies(atlas, "Green_Slime");
        blueSlimeSpecies = new SlimeSpecies(atlas, "Blue_Slime");

        spriteBatch = new SpriteBatch();

        soundManager.init();
        //soundManager.playMusic(SoundManager.MusicNames.MAIN_THEME);

        setScreen(new GrubMenuScreen());
    }

    private void registerFonts() {
        FontManager.registerFont("google", Gdx.files.internal("GoogleSansCode.ttf"));
        FontManager.registerFont("google-bold", Gdx.files.internal("GoogleSansCode-Bold.ttf"));
        FontManager.registerFont("terminus", Gdx.files.internal("TerminusTTF-4.49.3.ttf"));
        FontManager.registerFont("terminus-bold", Gdx.files.internal("TerminusTTF-Bold-4.49.3.ttf"));
    }

    public static TextureAtlas getTextureAtlas() {
        return atlas;
    }

    public static SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public static BitmapFont getFont() {
        return font;
    }

    public static Leaderboard getLeaderboard() {
        return leaderboard;
    }

    @Override
    public void dispose() {
        super.dispose();
        spriteBatch.dispose();
        atlas.dispose();
        soundManager.dispose();
        instance = null;
    }
}
