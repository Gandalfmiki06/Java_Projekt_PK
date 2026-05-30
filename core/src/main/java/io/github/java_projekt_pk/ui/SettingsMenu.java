package io.github.java_projekt_pk.ui;

import io.github.java_projekt_pk.Main;
import io.github.java_projekt_pk.Managers.SoundManager;

public final class SettingsMenu extends Menu {

    private final MenuItem backToMenuItem;

    public SettingsMenu(MenuItem backToMenuItem) {
        super("Settings");
        this.backToMenuItem = backToMenuItem;
        generateItems();
    }

    @Override
    public void leftPressed() {
        modifySetting(false);
    }

    @Override
    public void rightPressed() {
        modifySetting(true);
    }

    public void generateItems() {
        addItem(backToMenuItem);
        var soundManager = Main.getGameInstance().getSoundManager();
        addItem(new MenuItem(String.format("%-15s%s", "Master volume",
                getVolumeBar(soundManager.MasterVolume)), () -> { }, true));
        addItem(new MenuItem(String.format("%-15s%s", "Music Volume",
                getVolumeBar(soundManager.MusicVolume)), () -> { }, true));
        addItem(new MenuItem(String.format("%-15s%s", "Sfx Volume",
                getVolumeBar(soundManager.SfxVolume)), () -> { }, true));
    }

    private String getVolumeBar(float volume)
    {
        int filled = Math.round(volume * 20);
        StringBuilder bar = new StringBuilder("[[");

        for (int i = 0; i < 20; i++) {
            if (i < filled) {
                bar.append('|');
            } else {
                bar.append('-');
            }
        }

        bar.append("] ");
        bar.append(Math.round(volume * 100));
        bar.append('%');

        return bar.toString();
    }

    private void modifySetting(boolean increase) {
        var soundManager = Main.getGameInstance().getSoundManager();
        soundManager.playSfx(SoundManager.SfxNames.SELECT, 0.1f);

        switch (currentIndex) {
            case 1 -> soundManager.MasterVolume += increase ? 0.05f : -0.05f;
            case 2 -> soundManager.MusicVolume += increase ? 0.05f : -0.05f;
            case 3 -> soundManager.SfxVolume += increase ? 0.05f : -0.05f;
        }

        soundManager.MasterVolume = Math.clamp(soundManager.MasterVolume, 0, 1);
        soundManager.MusicVolume = Math.clamp(soundManager.MusicVolume, 0, 1);
        soundManager.SfxVolume = Math.clamp(soundManager.SfxVolume, 0, 1);

        var cfg = Main.getGameInstance().getSettingsConfig();
        cfg.soundMaster.set(soundManager.MasterVolume);
        cfg.soundMusic.set(soundManager.MusicVolume);
        cfg.soundSFX.set(soundManager.SfxVolume);

        soundManager.updateVolume();
        items.clear();
        generateItems();
    }
}
