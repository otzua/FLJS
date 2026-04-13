package com.pikamander2.japanesequizz;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class QuestionAdapter extends BaseAdapter {
    private final QuizActivity mContext;
    private final Question question;  // was static — fixed to be an instance field

    public ArrayList<Button> buttons = new ArrayList<>();

    public QuestionAdapter(Context c, Question q) {
        mContext = (QuizActivity) c;
        question = q;
    }

    public void changeAnswers() {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setText(question.getAnswer(i));
        }
    }

    public void makeButtons() {
        buttons.clear();
        for (int i = 0; i < getCount(); i++) {
            MaterialButton newButton = new MaterialButton(mContext, null, com.google.android.material.R.attr.materialButtonOutlinedStyle);
            newButton.setOnClickListener(mContext.answerOnClick);
            newButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, mContext.getResources().getDimension(R.dimen.button_font_size));
            newButton.setCornerRadius((int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 16, mContext.getResources().getDisplayMetrics()));
            newButton.setStrokeColorResource(R.color.colorCardStroke);
            newButton.setStrokeWidth((int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 2, mContext.getResources().getDisplayMetrics()));
            newButton.setTextColor(mContext.getResources().getColor(R.color.colorOnSurface, mContext.getTheme()));
            newButton.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.colorSurface, mContext.getTheme()));

            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            int margin = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 6, mContext.getResources().getDisplayMetrics());
            params.setMargins(margin, margin, margin, margin);
            newButton.setLayoutParams(params);

            int padding = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 16, mContext.getResources().getDisplayMetrics());
            newButton.setPadding(padding, padding, padding, padding);
            newButton.setMinHeight((int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 56, mContext.getResources().getDisplayMetrics()));

            buttons.add(newButton);
        }

        changeAnswers();
    }

    public int getCount() {
        return mContext.numAnswerChoices;
    }

    public Object getItem(int position) {
        if (position >= 0 && position < buttons.size()) {
            return buttons.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (buttons.isEmpty()) {
            makeButtons();
        }
        return buttons.get(position);
    }
}
