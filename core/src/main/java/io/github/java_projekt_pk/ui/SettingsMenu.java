package io.github.java_projekt_pk.ui;

import io.github.java_projekt_pk.Main;

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
        addItem(new MenuItem(String.format("%-15s%s", "Master volume",
                getVolumeBar(Main.soundManager.MasterVolume)), () -> { }, true));
        addItem(new MenuItem(String.format("%-15s%s", "Music Volume",
                getVolumeBar(Main.soundManager.MusicVolume)), () -> { }, true));
        addItem(new MenuItem(String.format("%-15s%s", "Sfx Volume",
                getVolumeBar(Main.soundManager.SfxVolume)), () -> { }, true));
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
        bar.append(Math.round(volume*100));
        bar.append('%');

        return bar.toString();
    }

    private void modifySetting(boolean increase)
    {
        switch (currentIndex) {
            case 1 -> Main.soundManager.MasterVolume += increase ? 0.05f : -0.05f;
            case 2 -> Main.soundManager.MusicVolume += increase ? 0.05f : -0.05f;
            case 3 -> Main.soundManager.SfxVolume += increase ? 0.05f : -0.05f;
        }

        Main.soundManager.MasterVolume = Math.clamp(Main.soundManager.MasterVolume, 0, 1);
        Main.soundManager.MusicVolume = Math.clamp(Main.soundManager.MusicVolume, 0, 1);
        Main.soundManager.SfxVolume = Math.clamp(Main.soundManager.SfxVolume, 0, 1);

        Main.soundManager.updateVolume();
        items.clear();
        generateItems();
    }
}
