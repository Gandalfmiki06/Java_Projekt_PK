package io.github.java_projekt_pk;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.java_projekt_pk.Managers.FontManager;
import io.github.java_projekt_pk.Managers.SoundManager;
import io.github.java_projekt_pk.config.SettingsConfig;
import io.github.java_projekt_pk.hud.Hud;
import io.github.java_projekt_pk.monsters.SlimeSpecies;
import io.github.java_projekt_pk.screens.GrubMenuScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends Game {

    public static final int FONT_SIZE = 16;

    private static Main instance;

    private TextureAtlas atlas;
    private SpriteBatch spriteBatch;

    public SlimeSpecies redSlimeSpecies;
    public SlimeSpecies greenSlimeSpecies;
    public SlimeSpecies blueSlimeSpecies;

    private BitmapFont font;

    private Leaderboard leaderboard;

    private SettingsConfig settingsConfig;

    private SoundManager soundManager = new SoundManager();

    private ShapeRenderer shapeRenderer;

    private Hud hud;

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

        settingsConfig = new SettingsConfig(getProjectDir().resolve("config/settings"));
        soundManager.MasterVolume = settingsConfig.soundMaster.get();
        soundManager.MusicVolume = settingsConfig.soundMusic.get();
        soundManager.SfxVolume = settingsConfig.soundSFX.get();
        soundManager.updateVolume();

        registerFonts();

        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = FONT_SIZE;
        parameter.color = Color.WHITE;

        font = FontManager.generateFont("terminus-bold", parameter);

        atlas = new TextureAtlas(Gdx.files.internal("texture_atlas.atlas"));

        redSlimeSpecies = new SlimeSpecies(atlas, "Red_Slime");
        greenSlimeSpecies = new SlimeSpecies(atlas, "Green_Slime");
        blueSlimeSpecies = new SlimeSpecies(atlas, "Blue_Slime");

        spriteBatch = new SpriteBatch();

        soundManager.init();
        //soundManager.playMusic(SoundManager.MusicNames.MAIN_THEME);

        shapeRenderer = new ShapeRenderer();

        hud = new Hud(atlas);

        setScreen(new GrubMenuScreen());
    }

    private void registerFonts() {
        FontManager.registerFont("google", Gdx.files.internal("GoogleSansCode.ttf"));
        FontManager.registerFont("google-bold", Gdx.files.internal("GoogleSansCode-Bold.ttf"));
        FontManager.registerFont("terminus", Gdx.files.internal("TerminusTTF-4.49.3.ttf"));
        FontManager.registerFont("terminus-bold", Gdx.files.internal("TerminusTTF-Bold-4.49.3.ttf"));
    }

    public TextureAtlas getTextureAtlas() {
        return atlas;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public BitmapFont getFont() {
        return font;
    }

    public Leaderboard getLeaderboard() {
        return leaderboard;
    }

    public SettingsConfig getSettingsConfig() {
        return settingsConfig;
    }
    
    public SoundManager getSoundManager() {
        return soundManager;
    }

    public Hud getHud() {
        return hud;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    @Override
    public void dispose() {
        super.dispose();
        spriteBatch.dispose();
        atlas.dispose();
        soundManager.dispose();
        shapeRenderer.dispose();
        instance = null;
    }
}
