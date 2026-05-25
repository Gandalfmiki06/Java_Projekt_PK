package io.github.java_projekt_pk.globals;

import com.badlogic.gdx.Gdx;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HurtTextGenerator {

    private static final Random RANDOM = new Random();

    private static final int THRESHOLD_MEDIUM = 3;
    private static final int THRESHOLD_HARD   = 6;

    public static int waveNumber = 0;

    private static final List<String> WORDS_EASY   = loadWords("words_easy.txt");
    private static final List<String> WORDS_MEDIUM = loadWords("words_medium.txt");
    private static final List<String> WORDS_HARD   = loadWords("words_hard.txt");

    private static List<String> loadWords(String filename) {
        List<String> words = new ArrayList<>();
        try {
            String content = Gdx.files.internal(filename).readString("UTF-8");
            for (String line : content.split("\\r?\\n")) {
                String word = line.trim();
                if (!word.isEmpty()) words.add(word);
            }
        } catch (Exception e) {
            Gdx.app.error("HurtTextGenerator", "Failed to load " + filename + ", using fallback", e);
            words.add("public");
            words.add("static");
            words.add("final");
        }
        return words;
    }

    public static void nextWave() {
        waveNumber++;
    }

    public static void reset() {
        waveNumber = 0;
    }

    private static List<String> getCurrentPool() {
        if (waveNumber < THRESHOLD_MEDIUM) return WORDS_EASY;
        if (waveNumber < THRESHOLD_HARD)   return WORDS_MEDIUM;
        return WORDS_HARD;
    }


    public static String getRandomText(List<String> usedWords) {
        List<String> pool = getCurrentPool();
        if (pool.isEmpty()) return "error";

        List<String> available = new ArrayList<>(pool);
        available.removeAll(usedWords);


        if (available.isEmpty()) {
            usedWords.clear();
            available = new ArrayList<>(pool);
        }

        return available.get(RANDOM.nextInt(available.size()));
    }


    public static String getRandomText() {
        return getRandomText(new ArrayList<>());
    }

    public static int getDifficultyTier() {
        if (waveNumber < THRESHOLD_MEDIUM) return 1;
        if (waveNumber < THRESHOLD_HARD)   return 2;
        return 3;
    }
}
