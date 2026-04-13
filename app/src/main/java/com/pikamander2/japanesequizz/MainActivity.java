package com.pikamander2.japanesequizz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_QUIZ_ID = "com.pikamander2.japanesequizz.MODE_ID";

    private TextView textViewBestScore;
    private TextView textViewTotalAnswered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStats();
    }

    private void updateStats() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int bestStreak   = prefs.getInt("best_streak", 0);
        int totalAnswered = prefs.getInt("total_answered", 0);
        textViewBestScore.setText(String.valueOf(bestStreak));
        textViewTotalAnswered.setText(String.valueOf(totalAnswered));
    }

    private void setupListeners() {
        // Quiz mode cards
        MaterialCardView cardHiragana = findViewById(R.id.cardHiragana);
        MaterialCardView cardKatakana = findViewById(R.id.cardKatakana);
        MaterialCardView cardMixture  = findViewById(R.id.cardMixture);

        cardHiragana.setOnClickListener(v -> animateAndLaunchQuiz(v, 1));
        cardKatakana.setOnClickListener(v -> animateAndLaunchQuiz(v, 2));
        cardMixture.setOnClickListener(v  -> animateAndLaunchQuiz(v, 4));

        // Reading mode cards
        MaterialCardView cardReadingHiragana = findViewById(R.id.cardReadingHiragana);
        MaterialCardView cardReadingKatakana = findViewById(R.id.cardReadingKatakana);
        MaterialCardView cardReadingMixed    = findViewById(R.id.cardReadingMixed);
        MaterialCardView cardReadingKanji    = findViewById(R.id.cardReadingKanji);

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
        }, 150);
    }

    private void animateAndLaunchReading(View view, int scriptOrdinal) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.card_press));
        view.postDelayed(() -> {
            Intent intent = new Intent(this, ReadingActivity.class);
            intent.putExtra(ReadingActivity.EXTRA_SCRIPT, scriptOrdinal);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }, 150);
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
