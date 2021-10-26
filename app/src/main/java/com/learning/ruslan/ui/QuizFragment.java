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
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import com.learning.ruslan.R;
import com.learning.ruslan.Support;
import com.learning.ruslan.Word;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class QuizFragment extends Fragment implements View.OnClickListener, Chronometer.OnChronometerTickListener {

    private static final String ActivityId = "QuizFragment.ActivityId";

    private final static int MAX_TIME = 13;

    private Button button_start;
    private Button button_break;
    private Button button_pause;
    private Button [] buttons;
    private TextView textView, textViewBalls;
    private ConstraintLayout.LayoutParams tvParams;
    private ConstraintLayout constraintLayout;
    private Chronometer chronometer;
    private Random random;
    private Date date;
    private String phrase;

    private int time = MAX_TIME, corr_index, score, numb_of_question = 0;
    private int questions;
    private long wrongTime = 0;
    private int count_variants = 0;
    private int ScreenWidth;
    private int ScreenHeight;
    private boolean hand = false;
    private boolean onPause = false;

    private Handler mHandler;
    private Word word;
    private Support support;
    private int typeId;

    private int fontColor = Color.BLACK;
    private int letColor = Color.MAGENTA;


    public static QuizFragment newInstance(int typeId) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putInt(ActivityId, typeId);
        fragment.setArguments(args);

        return fragment;
    }

    private QuizFragment(){}


    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        word = Word.get(getActivity());
        support = Support.get(getActivity());
        typeId = requireArguments().getInt(ActivityId, Word.AssentId);

        setHasOptionsMenu(true);

        Display display = requireActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ScreenWidth = size.x;
        ScreenHeight = size.y;

        //support.setQuestions(support.getQuestions() % word.getWordCount(Word.AssentId));
        random = new Random();

        word.uploadWords(typeId);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quiz, container, false);

        button_start = v.findViewById(R.id.btn_start);
        chronometer = v.findViewById(R.id.chronometer);
        button_break = v.findViewById(R.id.btn_break);
        button_pause = v.findViewById(R.id.button_pause);
        textView = v.findViewById(R.id.textView_start);
        textViewBalls = v.findViewById(R.id.textViewBalls);
        constraintLayout = v.findViewById(R.id.clMain);

        ConstraintLayout.LayoutParams break_params =
                (ConstraintLayout.LayoutParams) button_break.getLayoutParams();
        ConstraintLayout.LayoutParams pause_Params =
                (ConstraintLayout.LayoutParams) button_pause.getLayoutParams();

        pause_Params.verticalBias = (break_params.height + 10) / (float) ScreenHeight;
        button_pause.setLayoutParams(pause_Params);

        tvParams = (ConstraintLayout.LayoutParams) textView.getLayoutParams();
        textView.setTypeface(Typeface.createFromAsset(
                requireActivity().getAssets(), "fonts/verdana.ttf"));

        button_start.setOnClickListener(this);
        button_break.setOnClickListener(this);
        button_pause.setOnClickListener(this);
        chronometer.setOnChronometerTickListener(this);

        buttons = createButtons();

        updateUI();

        return v;
    }

    private void updateUI() {

        questions = support.getQuestions();

        switch (support.getTheme()) {
            case Support.THEME_LIGHT:
                fontColor = Color.BLACK;
                letColor = Color.MAGENTA;

                textViewBalls.setTextColor(Support.score_color_light);
                button_break.setBackgroundTintList(
                        ColorStateList.valueOf(Support.color_red_light));
                button_pause.setBackgroundTintList(
                        ColorStateList.valueOf(Support.color_red_light));

                for (Button btn: buttons) {
                    btn.setBackgroundResource(R.drawable.button_style_2);
                    btn.setTextColor(Color.WHITE);
                }
                break;
            case Support.THEME_NIGHT:
                fontColor = Color.WHITE;
                letColor = Support.color_magenta2;

                textViewBalls.setTextColor(Support.score_color_night);
                button_break.setBackgroundTintList(
                        ColorStateList.valueOf(Support.color_red_night));
                button_pause.setBackgroundTintList(
                        ColorStateList.valueOf(Support.color_red_night));

                for (Button btn: buttons) {
                    btn.setBackgroundResource(R.drawable.button_style);
                    btn.setTextColor(Color.BLACK);
                }
                break;
        }

        button_start.setBackgroundTintList(
                ColorStateList.valueOf(letColor));
        textView.setTextColor(fontColor);
        chronometer.setTextColor(fontColor);
    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @SuppressLint({"SetTextI18n", "NewApi", "NonConstantResourceId", "StringFormatMatches"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                StartGame();
                break;

            case 100:
            case 101:
            case 102:
            case 103:

                if (v == buttons[corr_index]) {
                    score++;
                    StartGame();
                }
                else {
                    date = new Date();
                    wrongTime = date.getTime();
                    hand = true;
                    mHandler = new Handler();
                    mHandler.post(mUpdate);

                    for (Button btn : buttons) btn.setVisibility(View.INVISIBLE);

                    setChronometerOnPause(true, View.INVISIBLE);
                }
                break;

            case R.id.btn_break:
                if (hand)
                    handlerIsStopped();
                EndGame();
                break;

            case R.id.button_pause:
                button_pause.setVisibility(View.INVISIBLE);
                button_start.setVisibility(View.VISIBLE);
                onPause = true;

                count_variants = 0;
                for (Button btn : buttons)
                    if (btn.getVisibility() == View.VISIBLE) {
                        btn.setVisibility(View.INVISIBLE);
                        count_variants++;
                    }

                textView.setText(R.string.btn_continue);
                updateTextView(false);
                setChronometerOnPause(true, View.VISIBLE);

                break;
        }
    }

    @SuppressLint("StringFormatMatches")
    private void StartGame() {

        if (numb_of_question == questions) {
            EndGame();
            return;
        }

        updateTextView(true);
        updateButtons();

        if (onPause)
            onPause = false;
        else {
            time = getNewTime(numb_of_question);
            wrongTime = 0;
        }

        setChronometerOnPause(false, View.VISIBLE);

        button_start.setVisibility(View.INVISIBLE);
        button_break.setVisibility(View.VISIBLE);
        button_pause.setVisibility(View.VISIBLE);
    }


    private final Runnable mUpdate = new Runnable() {
        @Override
        public void run() {
            String text = textView.getText().toString();
            mHandler.postDelayed(this, 250);

            CharSequence newText = "";
            if (text.equals("")) {
                newText = buttons[corr_index].getText();
                if (typeId == Word.ParonymId)
                    newText = Word.concat(textView.getText().toString(), newText, letColor);
            }

            textView.setText(newText);

            textView.setTextSize(50);
            textView.setTypeface(Typeface.createFromAsset(
                    requireActivity().getAssets(), "fonts/xarrovv.otf"), Typeface.BOLD);

            date = new Date();

            if (date.getTime() - wrongTime >= 3000) {
                handlerIsStopped();
            }
        }
    };



    private void updateButtons() {

        if (onPause) {
            for (int i = 0; i < count_variants; i++)
                buttons[i].setVisibility(View.VISIBLE);

            count_variants = 0;
        }
        else {
            ArrayList<Spannable> words = word.getRandomWords(typeId, letColor);
            if (words == null) {
                Toast.makeText(getContext(), "слов нет...", Toast.LENGTH_SHORT).show();
                return;
            }

            if (typeId == Word.ParonymId) {
                textView.append("\n");
                textView.append(words.get(0));
                phrase = words.get(0).toString();
                words.remove(0);
            }

            Spannable rightWord = words.get(0);
            words.remove(0);
            if (words.size() == 2)
                this.corr_index = 1;
            else
                this.corr_index = random.nextInt(words.size());

            words.add(this.corr_index, rightWord);

            count_variants = words.size();
            if (count_variants > 4) count_variants = 4;

            int elem;
            for (elem = 0; elem < count_variants; elem++) {

                buttons[elem].setVisibility(View.VISIBLE);

                buttons[elem].setText(words.get(elem));

                ConstraintLayout.LayoutParams buttonParams = (ConstraintLayout.LayoutParams) buttons[elem].getLayoutParams();

                if (words.size() % 2 == 1 && elem == words.size() - 1)
                    buttonParams.horizontalBias = (float) 0.5;
                else
                    buttonParams.horizontalBias = (float) (0.1 + 0.8 * (elem % 2));

                buttonParams.verticalBias = (float) (0.65 + 0.2 * (elem / 2));
                buttons[elem].setLayoutParams(buttonParams);
            }

            for (elem = words.size(); elem < 4; ) buttons[elem++].setVisibility(View.INVISIBLE);
        }
    }


    private Button[] createButtons() {
        Button[] buttons = new Button[4];

        for (int i = 0; i < 4; i ++) {
            ConstraintLayout.LayoutParams btnParams = new ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            btnParams.topToTop = ConstraintSet.PARENT_ID;
            btnParams.startToStart = ConstraintSet.PARENT_ID;
            btnParams.endToEnd = ConstraintSet.PARENT_ID;
            btnParams.bottomToBottom = ConstraintSet.PARENT_ID;

            Button btn = new Button(getActivity());
            btn.setOnClickListener(this);
            btn.setAllCaps(false);
            btn.setTypeface(Typeface.createFromAsset(
                    requireActivity().getAssets(),"fonts/xarrovv.otf"), Typeface.BOLD);
            btn.setTextSize(25);
            btn.setMaxWidth((int) (0.5 * ScreenWidth) + 1);
            btn.setId(100 + i);
            btn.setVisibility(View.INVISIBLE);

            constraintLayout.addView(btn, btnParams);
            buttons[i] = btn;
        }

        return buttons;
    }



    private int getNewTime(int count_questions) {
        if (count_questions == 0) return 13;

        int y = 50 / count_questions + 3;
        return Math.min(y, MAX_TIME);
    }



    private String getRightEnding(int score) {
        String word;

        if (score % 10 == 1 && score % 100 != 11) word = getString(R.string.one_point);
        else if (score % 10 >= 2 && score % 10 <= 4 && score % 100 != 12 && score % 100 != 13
                && score % 100 != 14) word = getString(R.string.two_points);
        else word = getString(R.string.ten_points);

        return word;
    }


    private void EndGame() {
        setChronometerOnPause(true, View.INVISIBLE);
        this.time = MAX_TIME;
        this.wrongTime = 0;
        this.onPause = false;
        updateTextView(false);

        this.textView.setText(R.string.btn_start);

        this.textViewBalls.setText(getString(R.string.end_game, score, getRightEnding(score), questions));

        if (score == questions) this.textViewBalls.append(getString(R.string.great_results));

        this.score = 0;
        this.numb_of_question = 0;

        this.button_break.setVisibility(View.INVISIBLE);
        this.button_pause.setVisibility(View.INVISIBLE);

        for (Button button : buttons) button.setVisibility(View.INVISIBLE);

        button_start.setVisibility(View.VISIBLE);
    }

    private void updateTextView(boolean isGame) {
        if (isGame) {
            this.textView.setTextSize(20);
            this.tvParams.horizontalBias = (float) 0.5;
            this.tvParams.verticalBias = (float) 0.5;

            this.textView.setText(getRightText());

            if (!onPause) {
                @SuppressLint("StringFormatMatches")
                String text = getString(R.string.score,
                        ++numb_of_question, questions, score, numb_of_question - 1);


                textViewBalls.setText(text);
                textViewBalls.setVisibility(View.VISIBLE);
            }
        }
        else {
            this.textView.setTextSize(30);
            this.textView.setTypeface(Typeface.createFromAsset(
                    requireActivity().getAssets(),"fonts/verdana.ttf"), Typeface.NORMAL);
            this.tvParams.horizontalBias = (float) 0.5;
            this.tvParams.verticalBias = (float) 0.617;
        }

        this.textView.setLayoutParams(tvParams);
    }

    private void handlerIsStopped() {
        hand = false;
        mHandler.removeCallbacks(mUpdate);
        textView.setTypeface(Typeface.createFromAsset(
                requireActivity().getAssets(),"fonts/verdana.ttf"), Typeface.NORMAL);
        StartGame();
    }

    @SuppressLint("StringFormatMatches")
    private void setChronometerOnPause(boolean pause, int visibility) {
        if (pause)
            chronometer.stop();
        else {
            chronometer.setText(String.valueOf(time));
            chronometer.start();
        }

        chronometer.setVisibility(visibility);
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {
        if (time == 0) {
            Toast.makeText(getActivity(), getString(R.string.time_is_over), Toast.LENGTH_SHORT).show();
            EndGame();
        }

        chronometer.setText(String.valueOf(time--));
    }

    @StringRes
    private int getRightText() {
        switch (typeId) {
            case Word.ParonymId:
                return R.string.paronym_test_text;
            case Word.SuffixId:
                return R.string.suffix_test_text;
            default:
                return R.string.assent_test_text;
        }
    }
}
