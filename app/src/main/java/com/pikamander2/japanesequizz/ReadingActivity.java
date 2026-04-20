package com.pikamander2.japanesequizz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReadingActivity extends AppCompatActivity {

    public static final String EXTRA_SCRIPT = "com.pikamander2.japanesequizz.READING_SCRIPT";

    private ReadingPassage.Script currentScript;
    private List<ReadingPassage> passagePool = new ArrayList<>();
    private int poolIndex = 0;
    private final Random random = new Random();

    private boolean romajiVisible  = false;
    private boolean englishVisible = false;

    private ReadingPassage.Difficulty selectedDifficulty = null;

    // Views
    private TextView textPassage, textRomaji, textEnglish,
                     textSource, textDifficulty, textCounter;
    private View     cardPassage;
    private MaterialButton btnRomaji, btnEnglish, btnNext, btnFetchOnline;
    private TextView        textFetchStatus;
    private View            progressFetch;
    private ChipGroup       chipGroupDifficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        int scriptOrdinal = getIntent().getIntExtra(EXTRA_SCRIPT, 0);
        currentScript = ReadingPassage.Script.values()[scriptOrdinal];

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(scriptLabel());
        }

        bindViews();
        setupDifficultyChips();
        setupButtons();

        // Load from local library immediately
        loadLocalPassages();
        showCurrentPassage();
    }

    private void bindViews() {
        cardPassage     = findViewById(R.id.cardPassage);
        textPassage     = findViewById(R.id.textPassage);
        textRomaji      = findViewById(R.id.textRomaji);
        textEnglish     = findViewById(R.id.textEnglish);
        textSource      = findViewById(R.id.textSource);
        textDifficulty  = findViewById(R.id.textDifficulty);
        textCounter     = findViewById(R.id.textCounter);
        btnRomaji       = findViewById(R.id.btnRomaji);
        btnEnglish      = findViewById(R.id.btnEnglish);
        btnNext         = findViewById(R.id.btnNext);
        btnFetchOnline  = findViewById(R.id.btnFetchOnline);
        progressFetch   = findViewById(R.id.progressFetch);
        textFetchStatus = findViewById(R.id.textFetchStatus);
        chipGroupDifficulty = findViewById(R.id.chipGroupDifficulty);
    }

    private void setupDifficultyChips() {
        chipGroupDifficulty.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                selectedDifficulty = null;
            } else {
                int id = checkedIds.get(0);
                if      (id == R.id.chipBeginner)     selectedDifficulty = ReadingPassage.Difficulty.BEGINNER;
                else if (id == R.id.chipIntermediate) selectedDifficulty = ReadingPassage.Difficulty.INTERMEDIATE;
                else if (id == R.id.chipAdvanced)     selectedDifficulty = ReadingPassage.Difficulty.ADVANCED;
                else                                  selectedDifficulty = null;
            }
            loadLocalPassages();
            showCurrentPassage();
        });
        Chip chipAll = findViewById(R.id.chipAll);
        chipAll.setChecked(true);
    }

    private void setupButtons() {
        btnRomaji.setOnClickListener(v  -> toggleRomaji());
        btnEnglish.setOnClickListener(v -> toggleEnglish());
        btnNext.setOnClickListener(v    -> nextPassage());

        // "Fetch" button now shuffles the local library for a fresh random order
        btnFetchOnline.setText("↺ Refresh");
        btnFetchOnline.setOnClickListener(v -> {
            loadLocalPassages();
            if (passagePool.isEmpty()) {
                Toast.makeText(this, "No passages for this selection.", Toast.LENGTH_SHORT).show();
                return;
            }
            // Jump to a random starting point for variety
            poolIndex = random.nextInt(passagePool.size());
            textFetchStatus.setVisibility(View.VISIBLE);
            textFetchStatus.setText("✓ Showing " + passagePool.size() + " local passages");
            progressFetch.setVisibility(View.GONE);
            crossfadeToPassage(poolIndex);
        });

        // Hide unused progress bar
        progressFetch.setVisibility(View.GONE);
        textFetchStatus.setVisibility(View.GONE);
    }

    // ── Local library ─────────────────────────────────────────────────────────

    private void loadLocalPassages() {
        if (selectedDifficulty != null) {
            passagePool = LocalLibrary.getByScriptAndDifficulty(
                    this, currentScript, selectedDifficulty, random);
        } else {
            passagePool = LocalLibrary.getByScript(this, currentScript, random);
        }
        // Fallback to ReadingLibrary if local file has nothing for this combo
        if (passagePool.isEmpty()) {
            if (selectedDifficulty != null) {
                passagePool = ReadingLibrary.getByScriptAndDifficulty(
                        currentScript, selectedDifficulty);
            } else {
                passagePool = ReadingLibrary.getShuffled(currentScript, random);
            }
        }
        poolIndex = 0;
    }

    // ── Display ───────────────────────────────────────────────────────────────

    private void showCurrentPassage() {
        if (passagePool == null || passagePool.isEmpty()) {
            textPassage.setText(R.string.no_passages_filter);
            textRomaji.setVisibility(View.GONE);
            textEnglish.setVisibility(View.GONE);
            textSource.setVisibility(View.GONE);
            textDifficulty.setVisibility(View.GONE);
            textCounter.setVisibility(View.GONE);
            btnRomaji.setEnabled(false);
            btnEnglish.setEnabled(false);
            btnNext.setEnabled(false);
            return;
        }

        btnRomaji.setEnabled(true);
        btnEnglish.setEnabled(true);
        btnNext.setEnabled(true);

        romajiVisible  = false;
        englishVisible = false;
        textRomaji.setVisibility(View.GONE);
        textEnglish.setVisibility(View.GONE);
        textSource.setVisibility(View.GONE);
        btnRomaji.setText(R.string.btn_show_romaji);
        btnEnglish.setText(R.string.btn_show_english);

        ReadingPassage p = passagePool.get(poolIndex);
        textPassage.setText(p.japanese);
        textRomaji.setText(p.romaji.isEmpty() ? "(romaji not available)" : p.romaji);
        textEnglish.setText(p.english);
        textSource.setText(getString(R.string.source_label, p.source));

        textDifficulty.setVisibility(View.VISIBLE);
        textDifficulty.setText(difficultyLabel(p.difficulty));
        textDifficulty.setBackgroundResource(difficultyBackground(p.difficulty));

        textCounter.setVisibility(View.VISIBLE);
        textCounter.setText((poolIndex + 1) + " / " + passagePool.size());
    }

    private void nextPassage() {
        if (passagePool == null || passagePool.isEmpty()) return;
        crossfadeToPassage((poolIndex + 1) % passagePool.size());
    }

    private void crossfadeToPassage(int newIndex) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(cardPassage, "alpha", 1f, 0f);
        fadeOut.setDuration(160);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                poolIndex = newIndex;
                showCurrentPassage();
                ObjectAnimator.ofFloat(cardPassage, "alpha", 0f, 1f)
                        .setDuration(220).start();
            }
        });
        fadeOut.start();
    }

    // ── Reveal helpers ────────────────────────────────────────────────────────

    private void toggleRomaji() {
        if (!romajiVisible) {
            revealView(textRomaji);
            revealView(textSource);
            btnRomaji.setText(R.string.btn_hide_romaji);
            romajiVisible = true;
        } else {
            hideView(textRomaji);
            if (!englishVisible) hideView(textSource);
            btnRomaji.setText(R.string.btn_show_romaji);
            romajiVisible = false;
        }
    }

    private void toggleEnglish() {
        if (!englishVisible) {
            revealView(textEnglish);
            revealView(textSource);
            btnEnglish.setText(R.string.btn_hide_english);
            englishVisible = true;
        } else {
            hideView(textEnglish);
            if (!romajiVisible) hideView(textSource);
            btnEnglish.setText(R.string.btn_show_english);
            englishVisible = false;
        }
    }

    private void revealView(View v) {
        v.setAlpha(0f);
        v.setVisibility(View.VISIBLE);
        v.animate().alpha(1f).setDuration(250).start();
    }

    private void hideView(View v) {
        v.animate().alpha(0f).setDuration(150)
                .withEndAction(() -> v.setVisibility(View.GONE)).start();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String scriptLabel() {
        switch (currentScript) {
            case HIRAGANA: return "Hiragana Reading";
            case KATAKANA: return "Katakana Reading";
            case MIXED:    return "Mixed Kana Reading";
            case KANJI:    return "Kanji Reading";
            default:       return "Reading";
        }
    }

    private String difficultyLabel(ReadingPassage.Difficulty d) {
        switch (d) {
            case BEGINNER:     return "Beginner";
            case INTERMEDIATE: return "Intermediate";
            case ADVANCED:     return "Advanced";
            default:           return "";
        }
    }

    private int difficultyBackground(ReadingPassage.Difficulty d) {
        switch (d) {
            case BEGINNER:     return R.drawable.badge_beginner;
            case INTERMEDIATE: return R.drawable.badge_intermediate;
            case ADVANCED:     return R.drawable.badge_advanced;
            default:           return R.drawable.badge_beginner;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
