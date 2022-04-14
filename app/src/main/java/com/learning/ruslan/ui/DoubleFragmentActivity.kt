package com.learning.ruslan.ui

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.learning.ruslan.R
import com.learning.ruslan.createIntent
import com.learning.ruslan.databinding.ActivityFragmentBinding
import com.learning.ruslan.setUpWithViewPager
import com.learning.ruslan.settings.SettingsViewModel
import com.learning.ruslan.task.TaskType

class DoubleFragmentActivity : AppCompatActivity() {

    companion object {
        private const val ActivityId = "ActivityId"

        fun getIntent(context: Context, taskType: TaskType) =
            createIntent<DoubleFragmentActivity>(context) {
                putExtra(ActivityId, taskType)
            }
    }

    private lateinit var binding: ActivityFragmentBinding

    private var menu: Menu? = null

    private lateinit var settings: SettingsViewModel
    private lateinit var typeId: TaskType


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFragmentBinding.inflate(layoutInflater) /* activity drawer */

        super.onCreate(savedInstanceState)
        // TODO: 14.02.2022 сделать navigationView и FrameLayout
        setContentView(binding.root)

        settings = SettingsViewModel.getInstance(this)
        typeId = intent.getSerializableExtra(ActivityId) as TaskType
        val viewPager = findViewById<ViewPager2>(R.id.view_pager)

//        settings.observeSettings(this)

        setTitle(typeId.title)

        val sectionsPagerAdapter =
            if (typeId == TaskType.SteadyExpression || typeId == TaskType.Suffix)
                SectionsPagerAdapter(this, typeId, tabCount = 1)
            else
                SectionsPagerAdapter(this, typeId)

        viewPager.adapter = sectionsPagerAdapter
        binding.tabs.setUpWithViewPager(viewPager) { tab, position ->
            tab.text = getString(SectionsPagerAdapter.TAB_TITLES[position])
        }

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.fragment_menu, menu)
        this.menu = menu
        settings.drawMenuItems(menu, R.id.menu_item_library, R.id.menu_item_settings)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menu_item_settings -> {
            startActivity(SettingsActivity.getIntent(this, typeId))
            true
        }
        R.id.menu_item_library -> {
            startActivity(LibraryActivity.getIntent(this, typeId))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    public override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        binding.tabs.setBackgroundColor(settings.backgroundColor)
        binding.tabs.tabTextColors = ColorStateList.valueOf(settings.fontColor)
        binding.tabs.setSelectedTabIndicatorColor(settings.highlightColor)

        settings.updateThemeInScreen(window, supportActionBar)
        settings.drawMenuItems(menu, R.id.menu_item_library, R.id.menu_item_settings)
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