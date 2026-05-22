package io.github.java_projekt_pk.Managers;

import com.badlogic.gdx.InputAdapter;

import io.github.java_projekt_pk.Main;
import io.github.java_projekt_pk.monsters.Enemy;

public class InputManager extends InputAdapter {

    private char lastChar = 0;

    @Override
    public boolean keyTyped(char c) {
        if (EnemyManager.enemies.isEmpty()) {
            return false;
        }

        if ((c < 32 && c != '\b') || c >= 127 /* delete key and regional characters */) {
            return false;
        }

        typeCharacter(c);

        return true;
    }

    @Override
    public boolean keyUp(int c)
    {
        lastChar = 0;
        return true;
    }

    private void typeCharacter(char c) {
        Enemy enemy = EnemyManager.enemies.get(EnemyManager.enemyPointer);

        if (c == '\b')
        {
            if (!enemy.inputText.isEmpty()) {
                enemy.inputText = enemy.inputText.substring(0, enemy.inputText.length() - 1);
                playClickSound(c);
            }
        }
        else
        {
            enemy.inputText += c;
            playClickSound(c);

            if (enemy.inputText.equals(enemy.hurtText))
            {
                enemy.hurt();
            }
        }

    }

    private void playClickSound(char c)
    {
        if (c != lastChar)
        {
            lastChar = c;
            Main.soundManager.playSfx(SoundManager.SfxNames.CLICK, 0.1f);
        }
    }
}
