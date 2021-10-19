package com.learning.ruslan.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.learning.ruslan.R;
import com.learning.ruslan.Support;
import com.learning.ruslan.Word;
import com.learning.ruslan.ui.SectionsPagerAdapter;

public class DoubleFragmentActivity extends AppCompatActivity {

    private static final String ActivityId = "ActivityId";

    private Support support;
    private Menu menu;
    private TabLayout tabs;
    private int typeId;

    public static Intent newIntent(Context context, int typeId) {
        Intent intent = new Intent(context, DoubleFragmentActivity.class);
        intent.putExtra(ActivityId, typeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment /*activity_drawer*/);

        support = Support.get(this);
        tabs = findViewById(R.id.tabs);
        typeId = getIntent().getIntExtra(ActivityId, Word.AssentId);
        ViewPager viewPager = findViewById(R.id.view_pager);

//        ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                this,
//                R.layout.spinner_item,
//                R.id.task,
//                getResources().getStringArray(R.array.spinnerItems)
//        );
//        spinner.setAdapter(adapter);

        updateUI();

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(
                this,
                getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                typeId
        );

        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, R.string.nav_app_bar_navigate_up_description, R.string.nav_app_bar_open_drawer_description
//        );
//
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_menu, menu);
        this.menu = menu;
        updateUI();
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                startActivity(SettingsActivity.newIntent(this, typeId));
                return true;
            case R.id.menu_item_list_words:
                startActivity(LibraryActivity.newIntent(this, typeId));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {

        switch (support.getTheme()) {
            case Support.THEME_LIGHT:
                tabs.setBackgroundColor(Color.WHITE);
                tabs.setTabTextColors(ColorStateList.valueOf(Color.BLACK));
                tabs.setSelectedTabIndicatorColor(Color.MAGENTA);
                break;
            case Support.THEME_NIGHT:
                tabs.setBackgroundColor(Color.BLACK);
                tabs.setTabTextColors(ColorStateList.valueOf(Color.WHITE));
                tabs.setSelectedTabIndicatorColor(Support.color_magenta2);
                break;
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                menu != null)
            support.setThemeToScreen(
                    getWindow(),
                    getSupportActionBar(),
                    this.menu,
                    new int[]{R.id.menu_item_settings, R.id.menu_item_list_words}
            );
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//        int index = Word.SUBJECTS.indexOf(typeId) + 1;
//
//        if (position == 0)
//            finish();
//        else if (position != index) {
//            Intent intent = newIntent(this, Word.SUBJECTS.get(position - 1));
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        }
//    }

//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//        Toast.makeText(this, "Вы ничего не выбрали!", Toast.LENGTH_SHORT).show();
//    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//        if (item.getItemId() == R.id.mainActivity) {
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        }
//        else if (item.getItemId() == R.id.assent) {
//            Intent intent = newIntent(this, Word.AssentId);
//            startActivity(intent);
//        }
//        else if (item.getItemId() == R.id.suffix) {
//            Intent intent = newIntent(this, Word.SuffixId);
//            startActivity(intent);
//        }
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
//
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//
//        if (drawer.isDrawerOpen(GravityCompat.START))
//            drawer.closeDrawer(GravityCompat.START);
//        else
//            super.onBackPressed();
//    }
}