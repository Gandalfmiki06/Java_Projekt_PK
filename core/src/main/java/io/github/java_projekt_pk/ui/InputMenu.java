package io.github.java_projekt_pk.ui;

import io.github.java_projekt_pk.Main;

public class InputMenu extends Menu {
    protected InputMenuItem inputItem;

    public InputMenu(String title, InputMenuItem.InputMenuAction applyAction, MenuItem.MenuAction backAction) {
        super(title);

        inputItem = new InputMenuItem(Main.getSettingsConfig().player.get());

        addItem(inputItem);
        addItem(new MenuItem("Confirm", () -> applyAction.execute(inputItem.getValue())));
        addItem(new MenuItem("Cancel", backAction));
    }
}
