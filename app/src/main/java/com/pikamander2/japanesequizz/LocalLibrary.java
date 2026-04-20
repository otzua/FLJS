package com.pikamander2.japanesequizz;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Reads japanese_library.txt from assets and serves passages by script + difficulty.
 *
 * File format (one passage per line):
 *   SCRIPT|DIFFICULTY|japanese|romaji|english|source
 *
 * SCRIPT:     H=hiragana  K=katakana  M=mixed  J=kanji
 * DIFFICULTY: B=beginner  I=intermediate  A=advanced
 *
 * Newlines inside a passage are stored as the two-character sequence \n (backslash + n).
 */
public class LocalLibrary {

    private static List<ReadingPassage> ALL_PASSAGES = null;

    /** Load all passages from assets (cached after first call). */
    public static synchronized List<ReadingPassage> getAll(Context ctx) {
        if (ALL_PASSAGES != null) return ALL_PASSAGES;
        ALL_PASSAGES = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(ctx.getAssets().open("japanese_library.txt"), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                ReadingPassage p = parseLine(line);
                if (p != null) ALL_PASSAGES.add(p);
            }
            reader.close();
        } catch (IOException e) {
            // Fallback: return empty, ReadingActivity handles gracefully
        }
        return ALL_PASSAGES;
    }

    /** Return all passages matching script, shuffled. */
    public static List<ReadingPassage> getByScript(Context ctx,
            ReadingPassage.Script script, Random rng) {
        List<ReadingPassage> result = new ArrayList<>();
        for (ReadingPassage p : getAll(ctx)) {
            if (p.script == script) result.add(p);
        }
        Collections.shuffle(result, rng);
        return result;
    }

    /** Return passages matching script + difficulty, shuffled. */
    public static List<ReadingPassage> getByScriptAndDifficulty(Context ctx,
            ReadingPassage.Script script, ReadingPassage.Difficulty difficulty, Random rng) {
        List<ReadingPassage> result = new ArrayList<>();
        for (ReadingPassage p : getAll(ctx)) {
            if (p.script == script && p.difficulty == difficulty) result.add(p);
        }
        Collections.shuffle(result, rng);
        return result;
    }

    private static ReadingPassage parseLine(String line) {
        String[] parts = line.split("\\|", 6);
        if (parts.length < 6) return null;

        ReadingPassage.Script script = parseScript(parts[0].trim());
        ReadingPassage.Difficulty diff = parseDiff(parts[1].trim());
        if (script == null || diff == null) return null;

        String japanese = unescape(parts[2]);
        String romaji   = unescape(parts[3]);
        String english  = unescape(parts[4]);
        String source   = parts[5].trim();

        return new ReadingPassage(japanese, romaji, english, source, script, diff);
    }

    /** Replace literal \n (two chars) with real newline. */
    private static String unescape(String s) {
        return s.replace("\\n", "\n");
    }

    private static ReadingPassage.Script parseScript(String s) {
        switch (s) {
            case "H": return ReadingPassage.Script.HIRAGANA;
            case "K": return ReadingPassage.Script.KATAKANA;
            case "M": return ReadingPassage.Script.MIXED;
            case "J": return ReadingPassage.Script.KANJI;
            default:  return null;
        }
    }

    private static ReadingPassage.Difficulty parseDiff(String s) {
        switch (s) {
            case "B": return ReadingPassage.Difficulty.BEGINNER;
            case "I": return ReadingPassage.Difficulty.INTERMEDIATE;
            case "A": return ReadingPassage.Difficulty.ADVANCED;
            default:  return null;
        }
    }
}
