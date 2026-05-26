package io.github.java_projekt_pk.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import io.github.java_projekt_pk.globals.Box;
import io.github.java_projekt_pk.screens.GrubMenuScreen;

public class Menu implements Disposable {
    protected final Array<MenuItem> items = new Array<>();
    protected int currentIndex = 0;
    private final String title;

    public Menu(String title) {
        this.title = title;
    }

    public Menu addItem(MenuItem item) {
        items.add(item);

        return this;
    }

    public MenuItem getSelectedItem() {
        return items.get(currentIndex);
    }

    public int getSelectedIndex() {
        return currentIndex;
    }

    public Array<MenuItem> getItems() {
        return items;
    }

    public String getTitle() {
        return title;
    }

    public void setSelectedIndex(int index) {
        currentIndex = MathUtils.clamp(index, 0, items.size - 1);
    }

    public void moveSelectionUp() {
        currentIndex = (currentIndex - 1 + items.size) % items.size;
        int iter = 0;

        while (!getSelectedItem().isSelectable() && iter <= items.size) {
            currentIndex = (currentIndex - 1 + items.size) % items.size;
            iter++;
        }
    }

    public void moveSelectionDown() {
        currentIndex = (currentIndex + 1) % items.size;
        int iter = 0;

        while (!getSelectedItem().isSelectable() && iter <= items.size) {
            currentIndex = (currentIndex + 1) % items.size;
            iter++;
        }
    }

    public void leftPressed() {

    }

    public void rightPressed() {

    }

    public void executeCurrentSelection() {
        items.get(currentIndex).executeAction();
    }

    public int size() {
        return items.size;
    }

    public void clearItems() {
        this.items.clear();
    }

    @Override
    public void dispose() {
        for (var item : items) {
            if (item instanceof Disposable) {
                ((Disposable) item).dispose();
            }
        }
    }
}
