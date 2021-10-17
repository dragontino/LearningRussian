package com.learning.ruslan.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.learning.ruslan.R;
import com.learning.ruslan.Support;
import com.learning.ruslan.Word;

import static com.learning.ruslan.Support.MAX_TIME;
import static com.learning.ruslan.Support.MIN_TIME;
import static com.learning.ruslan.Support.THEME_NIGHT;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private SwitchCompat switchCompat;
    private TextView switchTheme;
    private TextView textView, textView2;
    private EditText editText, editText2;
    private Button buttonLanguage;
    private SeekBar seekBar, seekBar2;
    private Support support;
    private Menu menu;
    private boolean isSettingsChanged = false;
    private String theme;
    private Toast exitToast;

    private int MAX_QUESTIONS;
    private static final int MIN_QUESTIONS = 10;
    private static final String INTENT_NAME = "SettingsActivity.typeId";


    public static Intent newIntent(Context context, int typeId) {
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.putExtra(INTENT_NAME, typeId);
        return intent;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"SetTextI18n", "StringFormatMatches"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        exitToast = Toast.makeText(this, R.string.settings_saved, Toast.LENGTH_SHORT);

        switchCompat = findViewById(R.id.switchCompat);
        textView = findViewById(R.id.textView1);
        editText = findViewById(R.id.editTextNumber);
        seekBar = findViewById(R.id.seekBar);
        switchTheme = findViewById(R.id.switchTheme);
        editText2 = findViewById(R.id.editTextNumber2);
        seekBar2 = findViewById(R.id.seekBar2);
        textView2 = findViewById(R.id.textView2);
        buttonLanguage = findViewById(R.id.buttonLanguage);

        Word word = Word.get(this);
        MAX_QUESTIONS = word.getWordCount(Word.AssentId);

        support = Support.get(this);

        if (support.getQuestions() > MAX_QUESTIONS) support.setQuestions(MAX_QUESTIONS);

        theme = support.getTheme();

        buttonLanguage.setOnClickListener(this);
        switchTheme.setOnClickListener(this);

        switchChecked(support.getChecked());
        editText.setText(String.valueOf(support.getPause()));
        seekBar.setMax(MAX_TIME);
        seekBar.setMin(MIN_TIME);
        seekBar.setProgress(support.getPause());
        seekBar2.setMax(MAX_QUESTIONS);
        editText2.setText(String.valueOf(support.getQuestions()));
        seekBar2.setProgress(support.getQuestions());

        buttonLanguage.setText(R.string.button_language_text);
        buttonLanguage.setBackground(ContextCompat.getDrawable(this, R.drawable.button_style));

        textView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf"));
        switchCompat.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf"));
        buttonLanguage.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/xarrovv.otf"), Typeface.BOLD);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editText.setText(progress + "");
                support.setPause(progress);
                isSettingsChanged = true;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        editText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                String text = editText.getText().toString();

                if (text.isEmpty())
                    Toast.makeText(
                            this,
                            R.string.please_set_value,
                            Toast.LENGTH_SHORT).show();

                else if (Integer.parseInt(text) < MIN_TIME || Integer.parseInt(text) > MAX_TIME) {
                    Toast.makeText(
                            this,
                            getString(R.string.lim_of_time, MIN_TIME, MAX_TIME),
                            Toast.LENGTH_LONG).show();
                    editText.setText("");

                } else {
                    seekBar.setProgress(Integer.parseInt(editText.getText().toString()), true);
                    support.setPause(Integer.parseInt(editText.getText().toString()));
                    isSettingsChanged = true;
                }

                return true;
            }
            return false;
        });


        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editText2.setText(progress + "");
                support.setQuestions(progress);
                isSettingsChanged = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });



        editText2.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                String text = editText2.getText().toString();

                if (text.isEmpty())
                    Toast.makeText(this, R.string.please_set_value, Toast.LENGTH_SHORT).show();

                else if (Integer.parseInt(text) < 10 || Integer.parseInt(text) > MAX_QUESTIONS) {
                    Toast.makeText(this,
                            getString(R.string.lim_of_questions, MIN_QUESTIONS, MAX_QUESTIONS),
                            Toast.LENGTH_LONG).show();
                    editText2.setText("");

                } else {
                    seekBar2.setProgress(Integer.parseInt(editText2.getText().toString()), true);
                    support.setQuestions(Integer.parseInt(editText2.getText().toString()));
                    isSettingsChanged = true;
                }
                return true;
            }
            return false;
        });



        if (switchCompat != null) switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            switchChecked(isChecked);
            isSettingsChanged = true;
        });
    }




    private void switchChecked(boolean isChecked) {

        support.setChecked(isChecked);
        if (isChecked) {
            switchCompat.setText(R.string.switch_checked);
            textView.setVisibility(View.VISIBLE);
            editText.setVisibility(View.VISIBLE);
            seekBar.setVisibility(View.VISIBLE);
            editText.setText(String.valueOf(support.getPause()));
            seekBar.setProgress(support.getPause());
        }
        else {
            switchCompat.setText(R.string.switch_unchecked);
            textView.setVisibility(View.GONE);
            editText.setVisibility(View.GONE);
            seekBar.setVisibility(View.GONE);
        }
    }


    private void updateTheme() {

        if (!theme.equals(support.getTheme()))
            isSettingsChanged = true;

        theme = support.getTheme();

        if (theme.equals(THEME_NIGHT))
            changeTextColor(Color.WHITE, R.style.SwitchNight);
        else
            changeTextColor(Color.BLACK, R.style.SwitchLight);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                menu != null) {
            support.setThemeToScreen(
                    getWindow(),
                    getSupportActionBar(),
                    menu,
                    new int[]{R.id.menu_item_info}
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTheme();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isSettingsChanged) {
            save_data();
            exitToast.show();
            isSettingsChanged = false;
        }
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        exitToast.cancel();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        this.menu = menu;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            updateTheme();
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_item_info) {
            Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("ResourceType")
    private void changeTextColor(int newColor, @StyleRes int switchTheme) {
        this.switchCompat.setTextColor(newColor);
        //this.switchCompat.setBackgroundResource(switchTheme);

        switchCompat.setScrollBarStyle(switchTheme);

        this.textView.setTextColor(newColor);
        this.textView2.setTextColor(newColor);
        this.buttonLanguage.setTextColor(newColor);

        if (newColor == Color.BLACK) {
            this.textView.setAlpha((float) 0.5);
            this.textView2.setAlpha((float) 0.5);

            this.buttonLanguage.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            this.buttonLanguage.setAlpha((float) 0.8);
        }
        else {
            this.textView.setAlpha(1);
            this.textView2.setAlpha(1);

            this.buttonLanguage.setBackgroundTintList(ColorStateList.valueOf(R.color.black2));
            buttonLanguage.setAlpha(1);
        }

        this.editText.setTextColor(newColor);
        this.editText.setBackgroundTintList(ColorStateList.valueOf(newColor));

        this.editText2.setTextColor(newColor);
        this.editText2.setBackgroundTintList(ColorStateList.valueOf(newColor));

        this.switchTheme.setTextColor(newColor);
        this.seekBar.getProgressDrawable().setColorFilter(newColor, PorterDuff.Mode.MULTIPLY);
        this.seekBar2.getProgressDrawable().setColorFilter(newColor, PorterDuff.Mode.MULTIPLY);
    }


    //buttonSave and textViewMode methods, they worked if buttonSave or textViewMode is clicked
    //Ахахаха, наебал
    private void save_data() {
        String text1 = editText.getText().toString();
        String text2 = editText2.getText().toString();

        if (!text1.isEmpty() && !text2.isEmpty()) {
            if (Integer.parseInt(text1) >= MIN_TIME && Integer.parseInt(text1) <= MAX_TIME &&
                    Integer.parseInt(text2) >= 10 && Integer.parseInt(text2) <= MAX_QUESTIONS) {
                seekBar.setProgress(Integer.parseInt(text1));
                support.setPause(Integer.parseInt(text1));

                seekBar2.setProgress(Integer.parseInt(text2));
                support.setQuestions(Integer.parseInt(text2));
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLanguage:
                startActivity(new Intent(this, LanguageActivity.class));
                break;
            case R.id.switchTheme:
                startActivity(new Intent(this, ThemeActivity.class));
                break;
        }
    }
}