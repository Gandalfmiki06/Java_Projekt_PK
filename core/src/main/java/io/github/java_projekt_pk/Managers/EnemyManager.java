package io.github.java_projekt_pk.Managers;

import java.util.ArrayList;
import java.util.List;

import io.github.java_projekt_pk.monsters.Enemy;

public class EnemyManager {
    public static List<Enemy> enemies = new ArrayList<>();
    public static int enemyPointer = 0;

    public static void selectNextEnemy()
    {
        if (enemies.isEmpty()) return;

        if (enemyPointer != enemies.size()) enemies.get(enemyPointer).unselect();
        enemyPointer = (enemyPointer + 1) % enemies.size();
        enemies.get(enemyPointer).select();
    }

    public static void deleteEnemy(Enemy instance)
    {
        enemies.remove(instance);
        selectNextEnemy();
    }
}
