package com.learning.ruslan.ui;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.learning.ruslan.R;
import com.learning.ruslan.Support;
import com.learning.ruslan.Word;

public class LearningFragment extends Fragment implements View.OnClickListener {

    private static final String ActivityId = "LearningFragment.ActivityId";

    private Button button_start, button_break, button_pause;
    private ConstraintLayout.LayoutParams textViewParams;
    private TextView textViewStart;
    private boolean isChecked, isGameStarted = false;
    private int pause;
    private int fontColor = Color.BLACK;
    private int letColor = Color.MAGENTA;
    private Handler mHandler;
    private Word word;
    private Support support;
    private String currWord;
    private double currPosX, currPosY;
    private int screenHeight;
    private int typeId;


    public static LearningFragment newInstance(int typeId) {
        LearningFragment fragment = new LearningFragment();
        Bundle args = new Bundle();
        args.putInt(ActivityId, typeId);
        fragment.setArguments(args);

        return fragment;
    }

    private LearningFragment(){}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        word = Word.get(getActivity());
        support = Support.get(getActivity());
        typeId = requireArguments()
                .getInt(ActivityId, Word.AssentId);

        Display display = requireActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenHeight = size.y;

        word.uploadWords(typeId);
    }


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_learning, container, false);

        button_start = v.findViewById(R.id.btn_start);
        textViewStart = v.findViewById(R.id.textView_start);
        button_break = v.findViewById(R.id.btn_break);
        button_pause = v.findViewById(R.id.button_pause);
        textViewParams = (ConstraintLayout.LayoutParams) textViewStart.getLayoutParams();

        currWord = "";
        currPosX = 0;
        currPosY = 0;

        button_start.setOnClickListener(this);
        button_break.setOnClickListener(this);
        button_pause.setOnClickListener(this);

        textViewStart.setTypeface(Typeface.createFromAsset(
                requireActivity().getAssets(), "fonts/verdana.ttf"));

        ConstraintLayout.LayoutParams break_params =
                (ConstraintLayout.LayoutParams) button_break.getLayoutParams();
        ConstraintLayout.LayoutParams pause_params =
                (ConstraintLayout.LayoutParams) button_pause.getLayoutParams();

        pause_params.verticalBias = (break_params.height + 10) / (float) screenHeight;
        button_pause.setLayoutParams(pause_params);

        updateUI();

        return v;
    }

    private void updateUI() {

        isChecked = support.getChecked();
        pause = support.getPause();

        switch (support.getTheme()) {

            case Support.THEME_LIGHT:
                button_break.setBackgroundTintList(
                        ColorStateList.valueOf(Support.color_red_light));
                button_pause.setBackgroundTintList(
                        ColorStateList.valueOf(Support.color_red_light));

                fontColor = Color.BLACK;
                letColor = Color.MAGENTA;
                break;

            case Support.THEME_NIGHT:
                button_break.setBackgroundTintList(
                        ColorStateList.valueOf(Support.color_red_night));
                button_pause.setBackgroundTintList(
                        ColorStateList.valueOf(Support.color_red_night));

                fontColor = Color.WHITE;
                letColor = Support.color_magenta2;
                break;
        }

        button_start.setBackgroundTintList(ColorStateList.valueOf(letColor));
        textViewStart.setTextColor(fontColor);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onStop() {
        super.onStop();
        EndGame();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_pause:
                button_pause.setVisibility(View.INVISIBLE);
                button_start.setVisibility(View.VISIBLE);

                currWord = textViewStart.getText().toString();
                textViewStart.setText(R.string.btn_continue);
                isGameStarted = false;
                updateTextView(false);

                if (isChecked)
                    mHandler.removeCallbacks(mUpdate);
                else {
                    textViewStart.setEnabled(false);
                }
                break;
            case R.id.btn_start:
                button_start.setVisibility(View.INVISIBLE);
                button_pause.setVisibility(View.VISIBLE);
                button_break.setVisibility(View.VISIBLE);
                isGameStarted = true;

                updateTextView(true);

                if (isChecked) {
                    mHandler = new Handler();
                    if (currWord.equals(""))
                        mHandler.post(mUpdate);
                    else {
                        textViewStart.setText(word.getPaintedWord(currWord, null, letColor));
                        mHandler.postDelayed(mUpdate, pause);
                        currWord = "";
                        currPosX = 0;
                        currPosY = 0;
                    }
                }
                else {
                    if (currWord.equals(""))
                        updateWord();
                    else {
                        textViewStart.setText(word.getPaintedWord(currWord, null, letColor));
                        currWord = "";
                        currPosX = 0;
                        currPosY = 0;
                    }

                    textViewStart.setOnClickListener(this);
                    textViewStart.setEnabled(true);
                }
                break;
            case R.id.btn_break:
                EndGame();
                break;
            case R.id.textView_start:
                updateWord();
                break;
        }
    }

    private void EndGame() {

        button_start.setVisibility(View.VISIBLE);
        button_break.setVisibility(View.INVISIBLE);
        button_pause.setVisibility(View.INVISIBLE);

        updateTextView(false);
        textViewStart.setText(R.string.btn_start);
        currWord = "";
        currPosX = 0;
        currPosY = 0;

        if (isGameStarted) {
            isGameStarted = false;
            if (isChecked) mHandler.removeCallbacks(mUpdate);
        }
    }


    private void updateWord() {
        Spannable spannable = word.getRandomWord(typeId, letColor);
        textViewStart.setText(spannable);
        updateTextView(true);
    }


    private final Runnable mUpdate = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(this, pause);
            updateWord();
        }
    };

    private void updateTextView(boolean isGame) {
        double posX, posY;
        if (isGame) {

            if (currPosX == 0 || currPosY == 0) {
                posX = Math.random();
                posY = Math.random();
            }
            else {
                posX = currPosX;
                posY = currPosY;
            }

            textViewStart.setTypeface(Typeface.createFromAsset(
                    requireActivity().getAssets(), "fonts/xarrovv.otf"), Typeface.BOLD);

            if (typeId == Word.AssentId)
                textViewStart.setTextSize(50);
            else
                textViewStart.setTextSize(40);
        }

        else {
            textViewStart.setTypeface(Typeface.createFromAsset(
                    requireActivity().getAssets(),"fonts/verdana.ttf"), Typeface.NORMAL);
            textViewStart.setTextSize(30);

            currPosX = textViewParams.horizontalBias;
            currPosY = textViewParams.verticalBias;

            posX = 0.5;
            posY = 0.617;
        }

        textViewParams.horizontalBias = (float) posX;
        textViewParams.verticalBias = (float) posY;
        textViewStart.setLayoutParams(textViewParams);
    }
}