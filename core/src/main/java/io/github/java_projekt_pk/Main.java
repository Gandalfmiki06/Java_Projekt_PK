package io.github.java_projekt_pk;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import io.github.java_projekt_pk.Managers.FontManager;
import io.github.java_projekt_pk.monsters.SlimeSpecies;
import io.github.java_projekt_pk.screens.GrubMenuScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends Game {
    private static Main instance;
    
    private static TextureAtlas atlas;
    private static SpriteBatch spriteBatch;

    public static SlimeSpecies redSlimeSpecies;
    public static SlimeSpecies greenSlimeSpecies;
    public static SlimeSpecies blueSlimeSpecies;
    
    private static BitmapFont font;
    
    public static Main getGameInstance() {
        return instance;
    }

    @Override
    public void create() {
        instance = this;

        registerFonts();
        
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 20;
        parameter.color = Color.WHITE;

        font = FontManager.generateFont("google", parameter);
        
        atlas = new TextureAtlas(Gdx.files.internal("texture_atlas.atlas"));
        
        redSlimeSpecies = new SlimeSpecies(atlas, "Red_Slime");
        greenSlimeSpecies = new SlimeSpecies(atlas, "Green_Slime");
        blueSlimeSpecies = new SlimeSpecies(atlas, "Blue_Slime");
        
        spriteBatch = new SpriteBatch();

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

    @Override
    public void dispose() {
        super.dispose();
        spriteBatch.dispose();
        atlas.dispose();
        instance = null;
    }
}
