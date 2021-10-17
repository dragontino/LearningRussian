package com.learning.ruslan.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.learning.ruslan.R;
import com.learning.ruslan.Support;

public class LanguageActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LanguageAdapter mAdapter;
    private Support support;

    private final String[] countries = new String[] {"\uD83C\uDDF7\uD83C\uDDFA Русский",
            "\uD83C\uDDEC\uD83C\uDDE7 English"};

    private int backgroundColor;
    private int fontColor = Color.BLACK;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        mRecyclerView = findViewById(R.id.language_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        support = Support.get(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new LanguageAdapter();
            mRecyclerView.setAdapter(mAdapter);
        }
        else
            mAdapter.notifyDataSetChanged();

        switch (support.getTheme()) {
            case Support.THEME_LIGHT:
                fontColor = Color.BLACK;
                backgroundColor = Color.WHITE;
                break;
            case Support.THEME_NIGHT:
                fontColor = Color.WHITE;
                backgroundColor = getColor(R.color.black2);
                break;
        }

        getWindow().getDecorView().setBackgroundColor(backgroundColor);
        setTitle(getWord(getTitle().toString(), fontColor));
    }


    private SpannableString getWord(String word, int color) {
        SpannableString string = new SpannableString(word);
        string.setSpan(
                new ForegroundColorSpan(color),
                0,
                word.length(),
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        return string;
    }





    private class LanguageAdapter extends RecyclerView.Adapter<LanguageHolder> {

        @NonNull
        @Override
        public LanguageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater()
                    .inflate(R.layout.list_item_language, parent, false);

            return new LanguageHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LanguageHolder holder, int position) {
            String language = countries[position];
            holder.bindLanguage(language, position);
        }

        @Override
        public int getItemCount() {
            return Support.languages.length;
        }
    }




    private class LanguageHolder extends RecyclerView.ViewHolder {

        private final TextView mTextView;
        private int position;

        public LanguageHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.list_item_language_textView);
            mTextView.setOnClickListener(this::onClick);
            mTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/xarrovv.otf"), Typeface.BOLD);
            mTextView.setTextColor(fontColor);
            mTextView.setBackgroundColor(backgroundColor);
        }

        public void bindLanguage(String language, int position) {
            this.position = position;
            mTextView.setText(language);
        }

        private void onClick(View view) {
            Toast.makeText(getApplicationContext(), "Выбран язык " + mTextView.getText(), Toast.LENGTH_SHORT).show();
            support.setLanguage(Support.languages[position]);
            finish();
        }
    }
}