package com.pikamander2.japanesequizz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_QUIZ_ID = "com.pikamander2.japanesequizz.MODE_ID";
    public static final String PREF_DARK_MODE = "dark_mode";

    private TextView textViewBestScore;
    private TextView textViewTotalAnswered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply dark/light mode before setContentView
        applyThemeFromPrefs();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        textViewBestScore    = findViewById(R.id.textViewBestScore);
        textViewTotalAnswered = findViewById(R.id.textViewTotalAnswered);

        findViewById(R.id.textCredits).setOnClickListener(v -> {
            android.net.Uri uri = android.net.Uri.parse("https://github.com/otzua");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        setupListeners();

        // Staggered fade-in animation for cards — Apple-like entrance
        if (savedInstanceState == null) {
            animateEntrance();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStats();
    }

    private void applyThemeFromPrefs() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean darkMode = prefs.getBoolean(PREF_DARK_MODE, false);
        AppCompatDelegate.setDefaultNightMode(
                darkMode ? AppCompatDelegate.MODE_NIGHT_YES
                         : AppCompatDelegate.MODE_NIGHT_NO);
    }

    /** Staggered fade-up entrance animation for all cards */
    private void animateEntrance() {
        int[] viewIds = {
            R.id.textTitleJapanese,
            R.id.textTitleCharacters,
            R.id.textSubtitle,
            R.id.layoutStats,
            R.id.cardHiragana,
            R.id.cardKatakana,
            R.id.cardMixture,
            R.id.cardReadingHiragana,
            R.id.cardReadingKatakana,
            R.id.cardReadingMixed,
            R.id.cardReadingKanji,
            R.id.cardHiraganaChart,
            R.id.cardKatakanaChart
        };

        for (int i = 0; i < viewIds.length; i++) {
            View v = findViewById(viewIds[i]);
            if (v == null) continue;
            v.setAlpha(0f);
            final int delay = i * 40; // 40ms stagger between each card
            v.postDelayed(() -> {
                Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_in_up);
                anim.setFillAfter(true);
                v.startAnimation(anim);
                v.setAlpha(1f);
            }, delay);
        }
    }

    private void updateStats() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int bestStreak    = prefs.getInt("best_streak", 0);
        int totalAnswered = prefs.getInt("total_answered", 0);
        textViewBestScore.setText(String.valueOf(bestStreak));
        textViewTotalAnswered.setText(String.valueOf(totalAnswered));
    }

    private void setupListeners() {
        MaterialCardView cardHiragana = findViewById(R.id.cardHiragana);
        MaterialCardView cardKatakana = findViewById(R.id.cardKatakana);
        MaterialCardView cardMixture  = findViewById(R.id.cardMixture);

        cardHiragana.setOnClickListener(v -> animateAndLaunchQuiz(v, 1));
        cardKatakana.setOnClickListener(v -> animateAndLaunchQuiz(v, 2));
        cardMixture.setOnClickListener(v  -> animateAndLaunchQuiz(v, 4));

        MaterialCardView cardReadingHiragana = findViewById(R.id.cardReadingHiragana);
        MaterialCardView cardReadingKatakana = findViewById(R.id.cardReadingKatakana);
        MaterialCardView cardReadingMixed    = findViewById(R.id.cardReadingMixed);
        MaterialCardView cardReadingKanji    = findViewById(R.id.cardReadingKanji);

        findViewById(R.id.cardHiraganaChart).setOnClickListener(v ->
                launchChart(v, ChartActivity.TYPE_HIRAGANA));
        findViewById(R.id.cardKatakanaChart).setOnClickListener(v ->
                launchChart(v, ChartActivity.TYPE_KATAKANA));

        cardReadingHiragana.setOnClickListener(v ->
                animateAndLaunchReading(v, ReadingPassage.Script.HIRAGANA.ordinal()));
        cardReadingKatakana.setOnClickListener(v ->
                animateAndLaunchReading(v, ReadingPassage.Script.KATAKANA.ordinal()));
        cardReadingMixed.setOnClickListener(v ->
                animateAndLaunchReading(v, ReadingPassage.Script.MIXED.ordinal()));
        cardReadingKanji.setOnClickListener(v ->
                animateAndLaunchReading(v, ReadingPassage.Script.KANJI.ordinal()));
    }

    private void animateAndLaunchQuiz(View view, int quizID) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.card_press));
        view.postDelayed(() -> {
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra(EXTRA_QUIZ_ID, quizID);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }, 120);
    }

    private void animateAndLaunchReading(View view, int scriptOrdinal) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.card_press));
        view.postDelayed(() -> {
            Intent intent = new Intent(this, ReadingActivity.class);
            intent.putExtra(ReadingActivity.EXTRA_SCRIPT, scriptOrdinal);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }, 120);
    }

    private void launchChart(View view, int chartType) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.card_press));
        view.postDelayed(() -> {
            Intent intent = new Intent(this, ChartActivity.class);
            intent.putExtra(ChartActivity.EXTRA_CHART_TYPE, chartType);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }, 120);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return true;
        }
        // Dark mode toggle from menu
        if (item.getItemId() == R.id.action_dark_mode) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            boolean currentlyDark = prefs.getBoolean(PREF_DARK_MODE, false);
            prefs.edit().putBoolean(PREF_DARK_MODE, !currentlyDark).apply();
            AppCompatDelegate.setDefaultNightMode(
                    !currentlyDark ? AppCompatDelegate.MODE_NIGHT_YES
                                   : AppCompatDelegate.MODE_NIGHT_NO);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
