package io.github.java_projekt_pk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;

public class Leaderboard {
    private final int maxSize;
    private final Path path;
    private final Array<PlayerScore> entries = new Array<>();
    public boolean autosave;

    public Leaderboard(Path path, int maxSize) {
        this.maxSize = maxSize;
        this.path = path;
        this.autosave = false;

        var file = path.toFile();

        if (!file.exists()) {
            try {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            } catch (IOException e) {
                Gdx.app.log("[ERR]", "[Leaderboard] Nie można utworzyć pliku");
                return;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] raw = line.split(";");

                if (raw.length != 3) {
                    Gdx.app.log("[ERR]", "[Leaderboard] Błędnie zapisana linia `" + line + "`");
                    continue;
                }

                String player = raw[0];
                int score = Integer.parseInt(raw[1]);
                LocalDateTime time = LocalDateTime.ofEpochSecond(Long.parseLong(raw[2]), 0, ZoneOffset.UTC);

                addEntry(player, score, time);
            }
        } catch (IOException e) {
            Gdx.app.log("[ERR]", "[Leaderboard] Nie można otworzyć pliku do odczytu");
        }
    }

    public void addEntry(String player, int score, LocalDateTime time) {
        entries.add(new PlayerScore(player, score, time));
        entries.sort(Comparator.comparing(PlayerScore::score, Comparator.reverseOrder()));

        if (entries.size > maxSize)
            entries.pop();

        if (autosave) {
            save();
        }
    }

    public void addEntry(String player, int score) {
        addEntry(player, score, LocalDateTime.now());
    }

    public void setAutosave(boolean v) {
        autosave = v;
    }

    public void save() {
        try (var writer = new BufferedWriter(new FileWriter(path.toFile()))) {
            for (var entry : entries) {
                String b = entry.player + ";" + entry.score + ";" + entry.time.toEpochSecond(ZoneOffset.UTC) + "\n";

                writer.write(b);
            }
        } catch (IOException e) {
            Gdx.app.log("[ERR]", "[Leaderboard] Nie można zapisać wyników do pliku");
        }
    }

    public Array<PlayerScore> getScores() {
        return entries;
    }

    public record PlayerScore(String player, int score, LocalDateTime time) implements Comparable<PlayerScore> {
        public PlayerScore(String player, int score) {
            this(player, score, LocalDateTime.now());
        }

        @Override
        public int compareTo(PlayerScore other) {
            return Integer.compare(other.score(), this.score);
        }
    }

}
