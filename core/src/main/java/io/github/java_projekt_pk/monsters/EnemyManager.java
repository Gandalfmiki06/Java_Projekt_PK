package io.github.java_projekt_pk.monsters;

import java.util.ArrayList;
import java.util.List;

public class EnemyManager {
    public static List<Enemy> enemies = new ArrayList<>();
    private static int enemyPointer = 0;

    public static void selectNextEnemy()
    {
        if (enemies.isEmpty()) return;

        enemies.get(enemyPointer).unselect();
        enemyPointer = (enemyPointer + 1) % enemies.size();
        enemies.get(enemyPointer).select();
    }
}
