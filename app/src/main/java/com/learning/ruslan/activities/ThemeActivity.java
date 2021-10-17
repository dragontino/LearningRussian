package com.learning.ruslan.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.ruslan.R;
import com.learning.ruslan.Support;

public class ThemeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ThemeAdapter mAdapter;
    private Support support;

    private final String[] themes = new String[] {"Светлая",
            "Тёмная", "По расписанию", "Системная"};

    private int backgroundColor;
    private int fontColor = Color.BLACK;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        mRecyclerView = findViewById(R.id.theme_recycler_view);
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
            mAdapter = new ThemeAdapter();
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





    private class ThemeAdapter extends RecyclerView.Adapter<ThemeHolder> {

        @NonNull
        @Override
        public ThemeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater()
                    .inflate(R.layout.list_item_language, parent, false);

            return new ThemeHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ThemeHolder holder, int position) {
            String theme = themes[position];
            holder.bindTheme(theme, position);
        }

        @Override
        public int getItemCount() {
            return themes.length;
        }
    }




    private class ThemeHolder extends RecyclerView.ViewHolder {

        private final TextView mTextView;
        private int position;

        public ThemeHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.list_item_language_textView);
            mTextView.setOnClickListener(this::onClick);
            mTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/xarrovv.otf"), Typeface.BOLD);
            mTextView.setTextColor(fontColor);
            mTextView.setBackgroundColor(backgroundColor);
        }

        public void bindTheme(String language, int position) {
            this.position = position;
            mTextView.setText(language);
        }

        private void onClick(View view) {
            Toast.makeText(getApplicationContext(), "Выбрана тема " + mTextView.getText(), Toast.LENGTH_SHORT).show();
            if (position == 0)
                support.setTheme(Support.THEME_LIGHT);
            else
                support.setTheme(Support.THEME_NIGHT);
            finish();
        }
    }
}