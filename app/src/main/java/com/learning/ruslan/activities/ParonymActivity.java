package com.learning.ruslan.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.ruslan.R;
import com.learning.ruslan.Support;
import com.learning.ruslan.Word;

public class ParonymActivity extends AppCompatActivity {

    private static final String KEY_POS = "KEY_POS";
    private static final String KEY_WORD = "KEY_WORD";

    private int fontColor = Color.BLACK;
    private int letColor = Color.MAGENTA;
    private Word word;
    private int position_paronym;
    private int backgroundColor;

    public static Intent newIntent(Context context, String word, int position) {
        Intent intent = new Intent(context, ParonymActivity.class);
        intent.putExtra(KEY_WORD, word);
        intent.putExtra(KEY_POS, position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paronym);

        RecyclerView mRecyclerView = findViewById(R.id.paronym_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Support support = Support.get(this);
        word = Word.get(this);
        position_paronym = getIntent().getIntExtra(KEY_POS, 0);
        String title = getIntent().getStringExtra(KEY_WORD);
        setTitle(title);

        mRecyclerView.setAdapter(new ParonymAdapter());

        switch (support.getTheme()) {
            case Support.THEME_LIGHT:
                backgroundColor = Color.WHITE;
                fontColor = Color.BLACK;
                letColor = Color.MAGENTA;
                break;
            case Support.THEME_NIGHT:
                backgroundColor = Color.BLACK;
                fontColor = Color.WHITE;
                letColor = Support.color_magenta2;
                break;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            support.setThemeToScreen(
                    getWindow(),
                    getSupportActionBar(),
                    null,
                    null
            );
        }
    }




    private class ParonymAdapter extends RecyclerView.Adapter<ParonymHolder> {

        @NonNull
        @Override
        public ParonymHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.list_item_library, parent, false);

            return new ParonymHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ParonymHolder holder, int position) {
            SpannableString phrase = word.getParonymVariant(position_paronym, position, letColor);
            holder.bindParonym(phrase);
        }

        @Override
        public int getItemCount() {
            return word.getParonymPhraseCount(position_paronym);
        }
    }




    private class ParonymHolder extends RecyclerView.ViewHolder {

        private final TextView textView;

        public ParonymHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.list_item_library_textView);
            textView.setTextIsSelectable(true);
        }

        public void bindParonym(SpannableString phrase) {
            textView.setText(phrase);
            textView.setTextColor(fontColor);
            textView.setBackgroundColor(backgroundColor);
        }
    }
}