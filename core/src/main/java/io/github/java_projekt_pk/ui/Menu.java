package io.github.java_projekt_pk.ui;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private final Array<MenuItem> items = new Array<>();
    private int currentIndex = 0;
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

    public String getTitle() { return title; }

    public void setSelectedIndex(int index) {
        currentIndex = MathUtils.clamp(index, 0, items.size - 1);
    }

    public void moveSelectionUp() {
        currentIndex = (currentIndex - 1 + items.size) % items.size;
        int iter = 0;

        while(!getSelectedItem().isSelectable() && iter <= items.size){
            currentIndex = (currentIndex - 1 + items.size) % items.size;
            iter++;
        }
    }

    public void moveSelectionDown() {
        currentIndex = (currentIndex + 1) % items.size;
        int iter = 0;

        while(!getSelectedItem().isSelectable() && iter <= items.size){
            currentIndex = (currentIndex + 1) % items.size;
            iter++;
        }
    }

    public void executeCurrentSelection() {
        items.get(currentIndex).executeAction();
    }

    public int size() {
        return items.size;
    }
}
