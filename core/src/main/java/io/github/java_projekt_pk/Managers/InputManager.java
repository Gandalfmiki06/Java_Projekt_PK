package io.github.java_projekt_pk.Managers;

import com.badlogic.gdx.InputAdapter;

public class InputManager extends InputAdapter {
    
    @Override
    public boolean keyTyped(char c)
    {
        if (EnemyManager.enemies.isEmpty())
        {
            return false;
        }

        if (c == '\b')
        {
            EnemyManager.enemies.get(EnemyManager.enemyPointer).backspace();
            return true;
        }

        if (c < 32 ||
            c == 127) //delete key
        {
            return false;
        }

        EnemyManager.enemies.get(EnemyManager.enemyPointer).typeCharacter(c);

        return true;
    }
}
