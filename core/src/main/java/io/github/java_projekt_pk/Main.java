package io.github.java_projekt_pk;

import com.badlogic.gdx.Game;

import io.github.java_projekt_pk.Managers.SoundManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public static SoundManager soundManager = new SoundManager();

    @Override
    public void create() {
        soundManager.init();
        //soundManager.playMusic(SoundManager.MusicNames.MAIN_THEME);
        setScreen(new FirstScreen());
    }

    @Override
    public void dispose()
    {
        soundManager.dispose();
    }
}
