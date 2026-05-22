package io.github.java_projekt_pk.Managers;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
    public HashMap<SfxNames, Sound> soundEffects = new HashMap<>();
    public HashMap<MusicNames, Music> music = new HashMap<>();

    public static float MasterVolume = 1;
    public static float MusicVolume = 0.5f;
    public static float SfxVolume = 0.5f;

    private final float VOLUME_SCALE = 0.5f;

    public static enum SfxNames
    {
        CLICK,
        DAMAGE_ENEMY,
        DEATH_ENEMY
    }

    public static enum MusicNames
    {
        MAIN_THEME
    }

    public void init()
    {
        Sound sfx = Gdx.audio.newSound(Gdx.files.internal("audio/click.wav"));
        soundEffects.put(SfxNames.CLICK, sfx);

        sfx = Gdx.audio.newSound(Gdx.files.internal("audio/damage.wav"));
        soundEffects.put(SfxNames.DAMAGE_ENEMY, sfx);

        sfx = Gdx.audio.newSound(Gdx.files.internal("audio/death.wav"));
        soundEffects.put(SfxNames.DEATH_ENEMY, sfx);

        //Music bgm = Gdx.audio.newMusic(Gdx.files.internal("audio/music.wav"));
        //music.put(MusicNames.MAIN_THEME, bgm);
    }

    public void dispose()
    {
        for (Sound entry : soundEffects.values())
        {
            entry.dispose();
        }

        for (Music entry : music.values())
        {
            entry.dispose();
        }
    }

    public void playSfx(SfxNames sfx, float pitchRange)
    {
        Random random = new Random();
        if (soundEffects.containsKey(sfx))
        {
            long id = soundEffects.get(sfx).play(SfxVolume * MasterVolume * VOLUME_SCALE);
            float pitch = 1 - pitchRange/2 + random.nextFloat() * (pitchRange);
            soundEffects.get(sfx).setPitch(id, pitch);
        }
    }

    public void playMusic(MusicNames bgm)
    {
        music.get(bgm).play();
        music.get(bgm).setVolume(MusicVolume * MasterVolume * VOLUME_SCALE);
    }
}
