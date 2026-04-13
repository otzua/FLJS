package com.pikamander2.japanesequizz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class QuizSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_summary);

        Intent intent = getIntent();
        int numCorrect = intent.getIntExtra("num_correct", 0);
        int numWrong = intent.getIntExtra("num_wrong", 0);
        int bestStreak = intent.getIntExtra("best_streak", 0);
        int totalQuestions = intent.getIntExtra("total_questions", 0);
        int quizId = intent.getIntExtra("quiz_id", 1);

        String quizType;
        switch (quizId) {
            case 1:
                quizType = "Hiragana";
                break;
            case 2:
                quizType = "Katakana";
                break;
            default:
                quizType = "Mixed";
                break;
        }

        int percentage = 0;
        if (totalQuestions > 0) {
            percentage = (int) ((100.0 / totalQuestions) * numCorrect);
        }

        String grade;
        if (percentage >= 95) {
            grade = "S";
        } else if (percentage >= 85) {
            grade = "A";
        } else if (percentage >= 70) {
            grade = "B";
        } else if (percentage >= 55) {
            grade = "C";
        } else {
            grade = "D";
        }

        TextView textGrade = findViewById(R.id.textGrade);
        TextView textQuizType = findViewById(R.id.textQuizType);
        TextView textPercentage = findViewById(R.id.textPercentage);
        TextView textCorrect = findViewById(R.id.textCorrectCount);
        TextView textWrong = findViewById(R.id.textWrongCount);
        TextView textStreak = findViewById(R.id.textBestStreak);
        Button buttonRetry = findViewById(R.id.buttonRetry);
        Button buttonHome = findViewById(R.id.buttonHome);

        textGrade.setText(grade);
        textQuizType.setText(quizType + " Quiz Complete");
        textPercentage.setText(percentage + "%");
        textCorrect.setText(String.valueOf(numCorrect));
        textWrong.setText(String.valueOf(numWrong));
        textStreak.setText(String.valueOf(bestStreak));

        buttonRetry.setOnClickListener(v -> {
            Intent retryIntent = new Intent(this, QuizActivity.class);
            retryIntent.putExtra(MainActivity.EXTRA_QUIZ_ID, quizId);
            startActivity(retryIntent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });

        buttonHome.setOnClickListener(v -> {
            Intent homeIntent = new Intent(this, MainActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homeIntent = new Intent(this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}
