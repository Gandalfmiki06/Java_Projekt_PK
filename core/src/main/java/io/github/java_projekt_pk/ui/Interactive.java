package io.github.java_projekt_pk.ui;

import com.badlogic.gdx.utils.Disposable;
import io.github.java_projekt_pk.globals.Box;

public interface Interactive extends Disposable {
    void render(Box coors, float delta, boolean selected, boolean focus);

    void setSize(Box size);
    Box getSize();

    boolean handleKeyDown(int keycode);
    boolean handleKeyTyped(char character);
}
