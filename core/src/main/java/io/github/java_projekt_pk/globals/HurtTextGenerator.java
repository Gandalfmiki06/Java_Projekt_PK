package io.github.java_projekt_pk.globals;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class HurtTextGenerator {

    private static final Random RANDOM = new Random();

    private static final List<String> possibleWords = Arrays.asList("public", "static", "final");

    public static String getRandomText() {
        int idx = RANDOM.nextInt(possibleWords.size());
        return possibleWords.get(idx);
    }
}
