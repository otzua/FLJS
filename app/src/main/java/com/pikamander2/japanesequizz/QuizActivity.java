package com.pikamander2.japanesequizz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import java.text.DecimalFormat;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {
    TextView textViewRomaji;
    TextView textViewScore;
    TextView textViewCorrect;
    TextView textViewCorrectAnswer;
    TextView textViewStreak;
    ProgressBar progressBar;

    int numCorrect = 0;
    int numWrong = 0;
    int currentStreak = 0;
    int bestSessionStreak = 0;
    int quizID;
    int numAnswerChoices = 4;
    int totalQuestions = 50;
    int questionsAnswered = 0;

    Random random = new Random();
    ExpandableHeightGridView gridViewQuestions;
    String currentAnswer = "";
    String currentQuestion = "";
    Question question;
    QuestionAdapter questionAdapter;

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        Intent intent = getIntent();
        quizID = intent.getIntExtra(MainActivity.EXTRA_QUIZ_ID, 1);
        question = new Question(quizID, this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        int valueFound = Integer.parseInt(prefs.getString("choices_list", "4"));
        if (valueFound == 2 || valueFound == 4 || valueFound == 6 || valueFound == 8) {
            numAnswerChoices = valueFound;
        }

        totalQuestions = Integer.parseInt(prefs.getString("count_list", "50"));

        questionAdapter = new QuestionAdapter(this, question);

        textViewRomaji = findViewById(R.id.textViewRomaji);
        textViewScore = findViewById(R.id.textViewScore);
        textViewCorrect = findViewById(R.id.textViewCorrect);
        textViewCorrectAnswer = findViewById(R.id.textViewCorrectAnswer);
        textViewStreak = findViewById(R.id.textViewStreak);
        progressBar = findViewById(R.id.progressBar);

        gridViewQuestions = findViewById(R.id.gridViewQuestions);

        Button buttonFlipQuestions = findViewById(R.id.buttonFlipQuestions);
        buttonFlipQuestions.setOnClickListener(v -> {
            question.switchRomajiFirst();
            switchQuestionAndAnswers();
        });

        progressBar.setMax(totalQuestions);
        progressBar.setProgress(0);

        updateScore();

        for (int i = 0; i < questionAdapter.buttons.size(); i++) {
            questionAdapter.buttons.get(i).setOnClickListener(answerOnClick);
        }

        gridViewQuestions.setAdapter(questionAdapter);
        gridViewQuestions.setExpanded(true);

        switchQuestion();
    }

    public View.OnClickListener answerOnClick = view -> guess(view);

    public void switchQuestionAndAnswers() {
        switchQuestion();
        questionAdapter.changeAnswers();
        questionAdapter.notifyDataSetChanged();
    }

    public void guess(View view) {
        Button tempButton = (Button) view;
        questionsAnswered++;
        progressBar.setProgress(questionsAnswered);

        // Compare as strings to avoid CharSequence/String equality pitfalls
        if (tempButton.getText().toString().equals(currentAnswer)) {
            numCorrect++;
            currentStreak++;
            if (currentStreak > bestSessionStreak) {
                bestSessionStreak = currentStreak;
            }

            textViewCorrect.setText("Correct!");
            textViewCorrect.setTextColor(Color.parseColor("#4CAF50"));
            textViewRomaji.startAnimation(AnimationUtils.loadAnimation(this, R.anim.correct_pulse));
        } else {
            numWrong++;
            currentStreak = 0;

            textViewCorrect.setText("Incorrect");
            textViewCorrect.setTextColor(Color.parseColor("#F44336"));
            textViewRomaji.startAnimation(AnimationUtils.loadAnimation(this, R.anim.incorrect_shake));
        }

        textViewStreak.setText("Streak: " + currentStreak);
        textViewCorrectAnswer.setText(currentQuestion + " = " + currentAnswer);
        updateScore();

        // Check if quiz is complete
        if (questionsAnswered >= totalQuestions) {
            handler.postDelayed(this::finishQuiz, 800);
            return;
        }

        // Brief delay before next question for visual feedback
        handler.postDelayed(this::switchQuestionAndAnswers, 300);
    }

    private void finishQuiz() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        int globalBestStreak = prefs.getInt("best_streak", 0);
        if (bestSessionStreak > globalBestStreak) {
            editor.putInt("best_streak", bestSessionStreak);
        }

        int totalAnsweredAll = prefs.getInt("total_answered", 0);
        editor.putInt("total_answered", totalAnsweredAll + questionsAnswered);
        editor.apply();

        Intent intent = new Intent(this, QuizSummaryActivity.class);
        intent.putExtra("num_correct", numCorrect);
        intent.putExtra("num_wrong", numWrong);
        intent.putExtra("best_streak", bestSessionStreak);
        intent.putExtra("total_questions", totalQuestions);
        intent.putExtra("quiz_id", quizID);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private String getPercentCorrect() {
        if ((numCorrect + numWrong) == 0) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#");
        return df.format((100.0 / (numCorrect + numWrong)) * numCorrect);
    }

    public void updateScore() {
        textViewScore.setText(numCorrect + "/" + questionsAnswered + " (" + getPercentCorrect() + "%)");
    }

    public void switchQuestion() {
        question.shuffleQuestions();

        // Clamp answer choice index to the actual list size to prevent IndexOutOfBoundsException
        int listSize = question.getListSize();
        int safeChoiceCount = Math.min(numAnswerChoices, listSize);
        int randNum = safeChoiceCount > 0 ? random.nextInt(safeChoiceCount) : 0;

        currentQuestion = question.getQuestion(randNum);
        currentAnswer = question.getAnswer(randNum);

        question.setCurrentAnswer(randNum);

        textViewRomaji.setText(currentQuestion);
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
