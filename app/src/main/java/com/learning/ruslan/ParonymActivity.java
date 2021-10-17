package com.learning.ruslan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.learning.ruslan.activities.LibraryActivity;
import com.learning.ruslan.activities.SettingsActivity;

public class ParonymActivity extends AppCompatActivity {

    private Button btnStart, btnBreak;
    private TextView textViewStart;
    private boolean isChecked;
    private int pause, fontColor = Color.BLACK;
    private int letColor = Color.MAGENTA;
    private String[] phrases;
    private Support support;
    private Word word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paronym);

        support = Support.get(this);
        word = Word.get(this);

        phrases = new String[]{}; // тут нужно придумать, как слова будут показываться пользователю (уже придумал!)


        btnStart = findViewById(R.id.buttonStart);
        btnBreak = findViewById(R.id.buttonBreak);
        textViewStart = findViewById(R.id.textViewStart);

        textViewStart.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/verdana.ttf"));


        isChecked = support.getChecked();
        pause = support.getPause();


        switch (support.getTheme()) {
            case Support.THEME_LIGHT:
                fontColor = Color.BLACK;
                letColor = Color.MAGENTA;
                break;
            case Support.THEME_NIGHT:
                fontColor = Color.WHITE;
                letColor = Support.color_magenta2;
                break;
        }
        btnStart.setBackgroundTintList(ColorStateList.valueOf(letColor));
        textViewStart.setTextColor(fontColor);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.menu_item_list_words:
                startActivity(LibraryActivity.newIntent(this, Word.ParonymId));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}