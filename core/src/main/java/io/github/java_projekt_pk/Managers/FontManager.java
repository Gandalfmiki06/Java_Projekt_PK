package io.github.java_projekt_pk.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import java.util.HashMap;
import java.util.Map;

public class FontManager {
    private static final Map<String, FreeTypeFontGenerator> fontMap = new HashMap<>();

    public static void registerFont(String id, FileHandle handle) {
        if (!handle.exists()) {
            Gdx.app.log("[WARN][FontManager]", "Plik nie istnieje");
            return;
        }

        if (fontMap.containsKey(id)) {
            Gdx.app.log("[WARN][FontManager]", "Czcionka '" + id + "' jest juz zarejestrowana");
            return;
        }

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(handle);
        fontMap.put(id, generator);
    }

    public static BitmapFont generateFont(String id, FreeTypeFontParameter params) {
        var generator = fontMap.get(id);

        if (generator == null) {
            System.out.println("[WARN] [FontManager] Czcionka '" + id + "' nie istnieje. Zostanie zwrocona domyslna czcionka");
            return new BitmapFont();
        }

        BitmapFont font = generator.generateFont(params);
        font.getData().markupEnabled = true;

        return font;
    }

    public static BitmapFont generateFont(String id) {
        return generateFont(id, getDefaultParameters());
    }

    public static FreeTypeFontParameter getDefaultParameters() {
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 12;
        parameter.color = Color.WHITE;
        return parameter;
    }
}
