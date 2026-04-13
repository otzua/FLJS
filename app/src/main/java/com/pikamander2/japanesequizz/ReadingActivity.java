package com.pikamander2.japanesequizz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReadingActivity extends AppCompatActivity {

    public static final String EXTRA_SCRIPT = "com.pikamander2.japanesequizz.READING_SCRIPT";

    private ReadingPassage.Script currentScript;
    private List<ReadingPassage>  passagePool = new ArrayList<>();
    private int                   poolIndex   = 0;
    private final Random random = new Random();

    // Whether the user has revealed each layer
    private boolean romajiVisible  = false;
    private boolean englishVisible = false;

    // Online vs offline source tracking
    private boolean showingOnlinePassages = false;

    // Views — passage card
    private TextView  textPassage;
    private TextView  textRomaji;
    private TextView  textEnglish;
    private TextView  textSource;
    private TextView  textDifficulty;
    private TextView  textCounter;
    private View      cardPassage;

    // Controls
    private Button      btnRomaji;
    private Button      btnEnglish;
    private Button      btnNext;
    private Button      btnFetchOnline;
    private ProgressBar progressFetch;
    private TextView    textFetchStatus;
    private ChipGroup   chipGroupDifficulty;

    private ReadingPassage.Difficulty selectedDifficulty = null; // null = show all

    private PassageFetcher fetcher;

    // ── Lifecycle ─────────────────────────────────────────────────────────────

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
        setupButtonListeners();

        fetcher = new PassageFetcher();

        // Load offline passages immediately so the screen is never empty
        loadOfflinePassages();
        showCurrentPassage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fetcher != null) fetcher.shutdown();
    }

    // ── View binding ──────────────────────────────────────────────────────────

    private void bindViews() {
        cardPassage         = findViewById(R.id.cardPassage);
        textPassage         = findViewById(R.id.textPassage);
        textRomaji          = findViewById(R.id.textRomaji);
        textEnglish         = findViewById(R.id.textEnglish);
        textSource          = findViewById(R.id.textSource);
        textDifficulty      = findViewById(R.id.textDifficulty);
        textCounter         = findViewById(R.id.textCounter);
        btnRomaji           = findViewById(R.id.btnRomaji);
        btnEnglish          = findViewById(R.id.btnEnglish);
        btnNext             = findViewById(R.id.btnNext);
        btnFetchOnline      = findViewById(R.id.btnFetchOnline);
        progressFetch       = findViewById(R.id.progressFetch);
        textFetchStatus     = findViewById(R.id.textFetchStatus);
        chipGroupDifficulty = findViewById(R.id.chipGroupDifficulty);
    }

    // ── Setup ─────────────────────────────────────────────────────────────────

    private void setupDifficultyChips() {
        chipGroupDifficulty.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                selectedDifficulty = null;
            } else {
                int id = checkedIds.get(0);
                if (id == R.id.chipBeginner)          selectedDifficulty = ReadingPassage.Difficulty.BEGINNER;
                else if (id == R.id.chipIntermediate) selectedDifficulty = ReadingPassage.Difficulty.INTERMEDIATE;
                else if (id == R.id.chipAdvanced)     selectedDifficulty = ReadingPassage.Difficulty.ADVANCED;
                else                                  selectedDifficulty = null;
            }
            // Re-filter the current pool (don't refetch online)
            applyDifficultyFilter();
            showCurrentPassage();
        });

        // Default: All levels
        Chip chipAll = findViewById(R.id.chipAll);
        chipAll.setChecked(true);
    }

    private void setupButtonListeners() {
        btnRomaji.setOnClickListener(v  -> toggleRomaji());
        btnEnglish.setOnClickListener(v -> toggleEnglish());
        btnNext.setOnClickListener(v    -> nextPassage());
        btnFetchOnline.setOnClickListener(v -> startOnlineFetch());
    }

    // ── Offline library ───────────────────────────────────────────────────────

    private void loadOfflinePassages() {
        passagePool = ReadingLibrary.getShuffled(currentScript, random);
        poolIndex   = 0;
        showingOnlinePassages = false;
        applyDifficultyFilter();
    }

    /**
     * Filter passagePool by selectedDifficulty in-place.
     * When filtering, we re-load from the full library (offline) or the last
     * fetched online batch, then apply the filter.
     */
    private void applyDifficultyFilter() {
        if (selectedDifficulty == null) return;
        List<ReadingPassage> filtered = new ArrayList<>();
        for (ReadingPassage p : passagePool) {
            if (p.difficulty == selectedDifficulty) filtered.add(p);
        }
        // If filter leaves nothing, keep the full pool rather than show "no results"
        if (!filtered.isEmpty()) {
            passagePool = filtered;
        }
        poolIndex = 0;
    }

    // ── Online fetch ──────────────────────────────────────────────────────────

    private void startOnlineFetch() {
        // Disable the button and show the spinner
        btnFetchOnline.setEnabled(false);
        btnFetchOnline.setText(R.string.btn_fetching);
        progressFetch.setVisibility(View.VISIBLE);
        textFetchStatus.setVisibility(View.VISIBLE);
        textFetchStatus.setText(R.string.fetch_status_loading);

        fetcher.fetch(currentScript, new PassageFetcher.FetchCallback() {
            @Override
            public void onSuccess(List<ReadingPassage> passages) {
                showingOnlinePassages = true;

                // Merge with offline passages so the pool stays rich
                List<ReadingPassage> merged = new ArrayList<>(passages);
                merged.addAll(ReadingLibrary.getShuffled(currentScript, random));
                passagePool = merged;
                poolIndex   = 0;

                // Apply any active difficulty filter
                if (selectedDifficulty != null) applyDifficultyFilter();

                progressFetch.setVisibility(View.GONE);
                textFetchStatus.setText(getString(R.string.fetch_status_done,
                        passages.size()));

                btnFetchOnline.setEnabled(true);
                btnFetchOnline.setText(R.string.btn_fetch_online);

                // Crossfade to the first new passage
                crossfadeToPassage(0);
            }

            @Override
            public void onError(String message) {
                progressFetch.setVisibility(View.GONE);
                textFetchStatus.setText(R.string.fetch_status_error);
                btnFetchOnline.setEnabled(true);
                btnFetchOnline.setText(R.string.btn_fetch_online);

                Toast.makeText(ReadingActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    // ── Passage display ───────────────────────────────────────────────────────

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

        // Reset reveal state on every new passage
        romajiVisible  = false;
        englishVisible = false;
        textRomaji.setVisibility(View.GONE);
        textEnglish.setVisibility(View.GONE);
        textSource.setVisibility(View.GONE);

        btnRomaji.setText(R.string.btn_show_romaji);
        btnEnglish.setText(R.string.btn_show_english);

        ReadingPassage passage = passagePool.get(poolIndex);

        textPassage.setText(passage.japanese);
        textRomaji.setText(passage.romaji);
        textEnglish.setText(passage.english);
        textSource.setText(getString(R.string.source_label, passage.source));

        // Difficulty badge
        textDifficulty.setVisibility(View.VISIBLE);
        textDifficulty.setText(difficultyLabel(passage.difficulty));
        textDifficulty.setBackgroundResource(difficultyBackground(passage.difficulty));

        // Counter — show ★ when pool contains online passages
        String counterText = (poolIndex + 1) + " / " + passagePool.size();
        if (showingOnlinePassages) counterText += " ★";
        textCounter.setVisibility(View.VISIBLE);
        textCounter.setText(counterText);
    }

    private void nextPassage() {
        if (passagePool == null || passagePool.isEmpty()) return;
        crossfadeToPassage((poolIndex + 1) % passagePool.size());
    }

    private void crossfadeToPassage(int newIndex) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(cardPassage, "alpha", 1f, 0f);
        fadeOut.setDuration(160);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                poolIndex = newIndex;
                showCurrentPassage();
                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(cardPassage, "alpha", 0f, 1f);
                fadeIn.setDuration(220);
                fadeIn.start();
            }
        });
        fadeOut.start();
    }

    // ── Reveal / hide helpers ─────────────────────────────────────────────────

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

    private void revealView(View view) {
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate().alpha(1f).setDuration(250).start();
    }

    private void hideView(View view) {
        view.animate().alpha(0f).setDuration(150)
                .withEndAction(() -> view.setVisibility(View.GONE))
                .start();
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
