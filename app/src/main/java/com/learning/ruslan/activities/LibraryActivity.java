package com.learning.ruslan.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.ruslan.R;
import com.learning.ruslan.Support;
import com.learning.ruslan.Word;

import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends AppCompatActivity {

    private static final String ActivityId = "ActivityId";

    private RecyclerView mLibraryRecyclerView;
    private LibraryAdapter mAdapter;
    private Support support;
    private Word word;
    private Menu menu;
    private List<Spannable> search_results;
    private SearchView searchView;

    private int typeId;
    private int fontColor, letterColor;
    private int ResId;


    public static Intent newIntent(Context context, int typeId) {
        Intent intent = new Intent(context, LibraryActivity.class);
        intent.putExtra(ActivityId, typeId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        mLibraryRecyclerView = findViewById(R.id.library_recycler_view);
        mLibraryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        support = Support.get(this);
        word = Word.get(this);
        typeId = getIntent().getIntExtra(ActivityId, Word.AssentId);
        search_results = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.library_menu, menu);
        this.menu = menu;

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search_results = word.findWordInTable(typeId, query, letterColor);
                updateUI();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    search_results = word.findWordInTable(typeId, "", letterColor);
                    updateUI();
                }
                return false;
            }
        });

        updateMenu();
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_settings) {
            startActivity(SettingsActivity.newIntent(this, typeId));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {

//        int index = Word.SUBJECTS.indexOf(typeId);
//        setTitle(Word.TITLES[index]);

        switch (support.getTheme()) {
            case Support.THEME_LIGHT:
                ResId = R.drawable.edittext_style;
                fontColor = Color.BLACK;
                letterColor = Color.MAGENTA;
                break;
            case Support.THEME_NIGHT:
                ResId = R.drawable.edittext_style_2;
                letterColor = Support.color_magenta2;
                fontColor = Color.WHITE;
                break;
        }

        if (mAdapter == null) {
            mAdapter = new LibraryAdapter(word);
            mLibraryRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.notifyDataSetChanged();
        }

        updateMenu();

        if (searchView != null) {
            EditText searchText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
            ImageView clearView = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);

            searchText.setTextColor(fontColor);
            searchText.setBackgroundTintList(ColorStateList.valueOf(fontColor));
            clearView.setBackgroundTintList(ColorStateList.valueOf(fontColor));
        }
    }



    private void updateMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                menu != null) {
            support.setThemeToScreen(
                    getWindow(),
                    getSupportActionBar(),
                    this.menu,
                    new int[]{R.id.menu_item_settings, R.id.menu_item_search}
            );
        }
    }




    private class LibraryAdapter extends RecyclerView.Adapter<LibraryHolder> {

        private final Word word;

        public LibraryAdapter(Word word) {
            this.word = word;
        }

        @NonNull
        @Override
        public LibraryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View view = inflater
                    .inflate(R.layout.list_item_library, parent, false);

            return new LibraryHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LibraryHolder holder, int position) {
            Spannable word;

            if (search_results.size() == 0)
                word = this.word.getNextWord(typeId, position, letterColor);
            else
                word = search_results.get(position);

            holder.bindWord(word);

            holder.setOnClickListener(v ->
                    startActivity(ParonymActivity.newIntent(getApplicationContext(), position)));
        }

        @Override
        public int getItemCount() {
            if (search_results.size() == 0)
                return word.getWordCount(typeId);

            return search_results.size();
        }
    }




    private class LibraryHolder extends RecyclerView.ViewHolder {

        private final TextView mTextView;

        public LibraryHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.list_item_library_textView);
        }

        public void bindWord(Spannable word) {
            mTextView.setText(word);
            mTextView.setTextColor(fontColor);
            mTextView.setBackground(ContextCompat.getDrawable(getApplicationContext(), ResId));
        }

        public void setOnClickListener(View.OnClickListener listener) {
            if (typeId == Word.ParonymId)
                mTextView.setOnClickListener(listener);
        }
    }
}