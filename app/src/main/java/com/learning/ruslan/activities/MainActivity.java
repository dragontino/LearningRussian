package com.learning.ruslan.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.ruslan.R;
import com.learning.ruslan.Support;
import com.learning.ruslan.Word;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;
    private Support support;
    private TextView textView_version;

    private Menu menu;

    private int fontColor;
    private int backgroundColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;

        mRecyclerView = findViewById(R.id.main_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        textView_version = findViewById(R.id.textView_version);
        textView_version.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        textView_version.setText(getString(R.string.version, " "));


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ConstraintLayout.LayoutParams recyclerParams =
                    (ConstraintLayout.LayoutParams) mRecyclerView.getLayoutParams();

            recyclerParams.matchConstraintMaxHeight = screenHeight - 350;
        }

        support = Support.get(this);

        textView_version.setTextColor(fontColor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.fragment_menu, menu);

        MenuItem library = menu.findItem(R.id.menu_item_list_words);
        library.setVisible(false);
        updateMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_settings)
            startActivity(SettingsActivity.newIntent(this, Word.AssentId));
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new TaskAdapter();
            mRecyclerView.setAdapter(mAdapter);
        }
        else
            mAdapter.notifyDataSetChanged();


        switch (support.getTheme()) {
            case Support.THEME_LIGHT:
                backgroundColor = Color.WHITE;
                fontColor = Color.BLACK;
                break;
            case Support.THEME_NIGHT:
                backgroundColor = Color.BLACK;
                fontColor = Color.WHITE;
                break;
        }

        updateMenu();
        textView_version.setTextColor(fontColor);
    }

    private void updateMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
        menu != null)
            support.setThemeToScreen(
                    getWindow(),
                    getSupportActionBar(),
                    this.menu,
                    new int[]{R.id.menu_item_settings}
            );
    }






    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View view = inflater
                    .inflate(R.layout.list_item_main, parent, false);

            return new TaskHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            String title = getString(Word.TITLES[position]);
            int typeId = Word.SUBJECTS.get(position);
            holder.bindTask(title, typeId);
        }

        @Override
        public int getItemCount() {
            return Word.SUBJECTS.size();
        }
    }


    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mTextView;
        private int typeId;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.list_item_main_textView);
            itemView.setOnClickListener(this);
        }

        public void bindTask(String title, int typeId) {
            mTextView.setText(title);
            mTextView.setTextColor(fontColor);
            mTextView.setBackgroundColor(backgroundColor);
            this.typeId = typeId;
        }

        @Override
        public void onClick(View v) {
            Intent intent = DoubleFragmentActivity
                    .newIntent(getApplicationContext(), typeId);
            startActivity(intent);
        }
    }
}