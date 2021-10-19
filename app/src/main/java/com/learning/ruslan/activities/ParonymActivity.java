package com.learning.ruslan.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.ruslan.Paronym;
import com.learning.ruslan.R;
import com.learning.ruslan.Support;
import com.learning.ruslan.Word;

public class ParonymActivity extends AppCompatActivity {

    private static final String KEY = "KEY";

    private int fontColor = Color.BLACK;
    private int letColor = Color.MAGENTA;
    private Word word;
    private int position;
    private int ResId;

    public static Intent newIntent(Context context, int position) {
        Intent intent = new Intent(context, ParonymActivity.class);
        intent.putExtra(KEY, position);
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
        position = getIntent().getIntExtra(KEY, 0);

        SpannableString[] phrases = word.getParonymVariants(position, letColor);
        mRecyclerView.setAdapter(new ParonymAdapter(phrases));

        switch (support.getTheme()) {
            case Support.THEME_LIGHT:
                fontColor = Color.BLACK;
                letColor = Color.MAGENTA;
                ResId = R.drawable.edittext_style;
                break;
            case Support.THEME_NIGHT:
                ResId = R.drawable.edittext_style_2;
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

        private final SpannableString[] phrases;

        public ParonymAdapter(SpannableString[] phrases) {
            this.phrases = phrases;
        }

        @NonNull
        @Override
        public ParonymHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.list_item_library, parent, false);

            return new ParonymHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ParonymHolder holder, int position) {
            holder.bindParonym(phrases[position]);
        }

        @Override
        public int getItemCount() {
            return phrases.length;
        }
    }




    private class ParonymHolder extends RecyclerView.ViewHolder {

        private final TextView textView;

        public ParonymHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.list_item_library_textView);
        }

        public void bindParonym(SpannableString phrase) {
            textView.setText(phrase);
            textView.setTextColor(fontColor);
            textView.setBackgroundResource(ResId);
        }
    }
}