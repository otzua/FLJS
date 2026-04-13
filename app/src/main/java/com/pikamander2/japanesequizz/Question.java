package com.pikamander2.japanesequizz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import androidx.preference.PreferenceManager;

public class Question {
    private final QuizActivity context;
    private final Random random = new Random();

    private int currentAnswer;

    private ArrayList<String[]> listToUse;

    private int listId;

    private boolean romajiFirst = true;

    // --- Hiragana ---

    private static final ArrayList<String[]> gojuuOnHiragana = new ArrayList<>(Arrays.asList(new String[][]{
            {"あ", "a"}, {"い", "i"}, {"う", "u"}, {"え", "e"}, {"お", "o"},
            {"か", "ka"}, {"き", "ki"}, {"く", "ku"}, {"け", "ke"}, {"こ", "ko"},
            {"さ", "sa"}, {"し", "shi"}, {"す", "su"}, {"せ", "se"}, {"そ", "so"},
            {"た", "ta"}, {"ち", "chi"}, {"つ", "tsu"}, {"て", "te"}, {"と", "to"},
            {"な", "na"}, {"に", "ni"}, {"ぬ", "nu"}, {"ね", "ne"}, {"の", "no"},
            {"は", "ha"}, {"ひ", "hi"}, {"ふ", "fu"}, {"へ", "he"}, {"ほ", "ho"},
            {"ま", "ma"}, {"み", "mi"}, {"む", "mu"}, {"め", "me"}, {"も", "mo"},
            {"や", "ya"}, {"ゆ", "yu"}, {"よ", "yo"},
            {"ら", "ra"}, {"り", "ri"}, {"る", "ru"}, {"れ", "re"}, {"ろ", "ro"},
            {"わ", "wa"}, {"を", "wo"},
            {"ん", "n"}}));

    private static final ArrayList<String[]> dakutenHiragana = new ArrayList<>(Arrays.asList(new String[][]{
            {"が", "ga"}, {"ぎ", "gi"}, {"ぐ", "gu"}, {"げ", "ge"}, {"ご", "go"},
            {"ざ", "za"}, {"じ", "ji"}, {"ず", "zu"}, {"ぜ", "ze"}, {"ぞ", "zo"},
            {"だ", "da"}, {"ぢ", "dji"}, {"づ", "dzu"}, {"で", "de"}, {"ど", "do"},
            {"ば", "ba"}, {"び", "bi"}, {"ぶ", "bu"}, {"べ", "be"}, {"ぼ", "bo"},
            {"ぱ", "pa"}, {"ぴ", "pi"}, {"ぷ", "pu"}, {"ぺ", "pe"}, {"ぽ", "po"}
    }));

    private static final ArrayList<String[]> youOnHiragana = new ArrayList<>(Arrays.asList(new String[][]{
            {"きゃ", "kya"}, {"きゅ", "kyu"}, {"きょ", "kyo"},
            {"しゃ", "sha"}, {"しゅ", "shu"}, {"しょ", "sho"},
            {"ちゃ", "cha"}, {"ちゅ", "chu"}, {"ちょ", "cho"},
            {"にゃ", "nya"}, {"にゅ", "nyu"}, {"にょ", "nyo"},
            {"ひゃ", "hya"}, {"ひゅ", "hyu"}, {"ひょ", "hyo"},
            {"みゃ", "mya"}, {"みゅ", "myu"}, {"みょ", "myo"},
            {"りゃ", "rya"}, {"りゅ", "ryu"}, {"りょ", "ryo"},
            {"ぎゃ", "gya"}, {"ぎゅ", "gyu"}, {"ぎょ", "gyo"},
            {"じゃ", "ja" }, {"じゅ", "ju" }, {"じょ", "jo" },
            {"ぢゃ", "dja"}, {"ぢゅ", "dju"}, {"ぢょ", "djo"},
            {"びゃ", "bya"}, {"びゅ", "byu"}, {"びょ", "byo"},
            {"ぴゃ", "pya"}, {"ぴゅ", "pyu"}, {"ぴょ", "pyo"}
    }));

    // --- Katakana ---

    private static final ArrayList<String[]> plainKatakana = new ArrayList<>(Arrays.asList(new String[][]{
            {"ア", "a"}, {"イ", "i"}, {"ウ", "u"}, {"エ", "e"}, {"オ", "o"},
            {"カ", "ka"}, {"キ", "ki"}, {"ク", "ku"}, {"ケ", "ke"}, {"コ", "ko"},
            {"サ", "sa"}, {"シ", "shi"}, {"ス", "su"}, {"セ", "se"}, {"ソ", "so"},
            {"タ", "ta"}, {"チ", "chi"}, {"ツ", "tsu"}, {"テ", "te"}, {"ト", "to"},
            {"ナ", "na"}, {"ニ", "ni"}, {"ヌ", "nu"}, {"ネ", "ne"}, {"ノ", "no"},
            {"ハ", "ha"}, {"ヒ", "hi"}, {"フ", "fu"}, {"ヘ", "he"}, {"ホ", "ho"},
            {"マ", "ma"}, {"ミ", "mi"}, {"ム", "mu"}, {"メ", "me"}, {"モ", "mo"},
            {"ヤ", "ya"}, {"ユ", "yu"}, {"ヨ", "yo"},
            {"ラ", "ra"}, {"リ", "ri"}, {"ル", "ru"}, {"レ", "re"}, {"ロ", "ro"},
            {"ワ", "wa"}, {"ヲ", "wo"},
            {"ン", "n"}
    }));

    private static final ArrayList<String[]> dakutenKatakana = new ArrayList<>(Arrays.asList(new String[][]{
            {"ガ", "ga"}, {"ギ", "gi"}, {"グ", "gu"}, {"ゲ", "ge"}, {"ゴ", "go"},
            {"ザ", "za"}, {"ジ", "ji"}, {"ズ", "zu"}, {"ゼ", "ze"}, {"ゾ", "zo"},
            {"ダ", "da"}, {"ヂ", "dji"}, {"ヅ", "dzu"}, {"デ", "de"}, {"ド", "do"},
            {"バ", "ba"}, {"ビ", "bi"}, {"ブ", "bu"}, {"ベ", "be"}, {"ボ", "bo"},
            {"パ", "pa"}, {"ピ", "pi"}, {"プ", "pu"}, {"ペ", "pe"}, {"ポ", "po"}
    }));

    private static final ArrayList<String[]> youOnKatakana = new ArrayList<>(Arrays.asList(new String[][]{
            {"キャ", "kya"}, {"キュ", "kyu"}, {"キョ", "kyo"},
            {"シャ", "sha"}, {"シュ", "shu"}, {"ショ", "sho"},
            {"チャ", "cha"}, {"チュ", "chu"}, {"チョ", "cho"},
            {"ニャ", "nya"}, {"ニュ", "nyu"}, {"ニョ", "nyo"},
            {"ヒャ", "hya"}, {"ヒュ", "hyu"}, {"ヒョ", "hyo"},
            {"ミャ", "mya"}, {"ミュ", "myu"}, {"ミョ", "myo"},
            {"リャ", "rya"}, {"リュ", "ryu"}, {"リョ", "ryo"},
            {"ギャ", "gya"}, {"ギュ", "gyu"}, {"ギョ", "gyo"},
            {"ジャ", "ja" }, {"ジュ", "ju" }, {"ジョ", "jo" },
            {"ヂャ", "dja"}, {"ヂュ", "dju"}, {"ヂョ", "djo"},
            {"ビャ", "bya"}, {"ビュ", "byu"}, {"ビョ", "byo"},
            {"ピャ", "pya"}, {"ピュ", "pyu"}, {"ピョ", "pyo"}
    }));

    public Question(int tempListId, QuizActivity context) {
        listId = tempListId;
        this.context = context;
        setList(tempListId);
    }

    public void switchRomajiFirst() {
        romajiFirst = !romajiFirst;
    }

    public void shuffleQuestions() {
        setList(listId);
        Collections.shuffle(listToUse);
    }

    public void setList(int tempListId) {
        boolean includeYouon = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean("is_full_switch", false);

        // Mixed mode: randomly pick hiragana or katakana each round
        if (tempListId == 4) {
            tempListId = random.nextInt(2) + 1;
        }

        switch (tempListId) {
            case 1: { // Hiragana
                int totalSize = gojuuOnHiragana.size() + dakutenHiragana.size()
                        + (includeYouon ? youOnHiragana.size() : 0);
                int randNum = random.nextInt(totalSize);
                if (randNum < gojuuOnHiragana.size()) {
                    listToUse = gojuuOnHiragana;
                } else if (randNum < gojuuOnHiragana.size() + dakutenHiragana.size()) {
                    listToUse = dakutenHiragana;
                } else {
                    listToUse = youOnHiragana;
                }
                break;
            }
            case 2: { // Katakana
                int totalSize = plainKatakana.size() + dakutenKatakana.size()
                        + (includeYouon ? youOnKatakana.size() : 0);
                int randNum = random.nextInt(totalSize);
                if (randNum < plainKatakana.size()) {
                    listToUse = plainKatakana;
                } else if (randNum < plainKatakana.size() + dakutenKatakana.size()) {
                    listToUse = dakutenKatakana;
                } else {
                    listToUse = youOnKatakana;
                }
                break;
            }
            default:
                listToUse = gojuuOnHiragana;
                break;
        }
    }

    /** Returns the display string for position charID (the side shown as answer buttons). */
    public String getAnswer(int charID) {
        if (charID < 0 || charID >= listToUse.size()) return "";
        return listToUse.get(charID)[romajiFirst ? 0 : 1];
    }

    public void setCurrentAnswer(int answer) {
        this.currentAnswer = answer;
    }

    /** Returns the correct answer string for the currently displayed question. */
    public String getCurrentAnswer() {
        return listToUse.get(currentAnswer)[romajiFirst ? 0 : 1];
    }

    /** Returns the string shown as the question (the side to be guessed). */
    public String getQuestion(int charID) {
        if (charID < 0 || charID >= listToUse.size()) return "";
        return listToUse.get(charID)[romajiFirst ? 1 : 0];
    }

    public int getListSize() {
        return listToUse != null ? listToUse.size() : 0;
    }

    public ArrayList<String[]> getUsedList() {
        return listToUse;
    }
}
