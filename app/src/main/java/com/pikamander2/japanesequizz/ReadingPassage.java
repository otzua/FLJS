package com.pikamander2.japanesequizz;

/**
 * A single reading passage with its romaji transliteration, English translation,
 * and metadata.
 */
public class ReadingPassage {

    public enum Script {
        HIRAGANA,   // passage uses only hiragana (+ punctuation)
        KATAKANA,   // passage uses only katakana (+ punctuation)
        MIXED,      // hiragana + katakana, no kanji
        KANJI       // hiragana + katakana + kanji (most realistic)
    }

    public enum Difficulty {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED
    }

    public final String japanese;       // The actual passage text
    public final String romaji;         // Full romaji transliteration
    public final String english;        // English translation
    public final String source;         // Attribution / source description
    public final Script script;
    public final Difficulty difficulty;

    public ReadingPassage(String japanese, String romaji, String english,
                          String source, Script script, Difficulty difficulty) {
        this.japanese   = japanese;
        this.romaji     = romaji;
        this.english    = english;
        this.source     = source;
        this.script     = script;
        this.difficulty = difficulty;
    }
}
