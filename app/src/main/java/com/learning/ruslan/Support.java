package com.learning.ruslan;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import androidx.annotation.IdRes;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;

import com.learning.ruslan.databases.RusBaseHelper;
import com.learning.ruslan.databases.RusDbSchema.SupportTable;
import com.learning.ruslan.databases.SupportCursorWrapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Support {

    //theme constants
    public static final String THEME_LIGHT = "light";
    public static final String THEME_NIGHT = "night";

    //constants for time in learning fragment
    public static final int MIN_TIME = 100;
    public static final int MAX_TIME = 10000;

    //languages
    public static final String RUSSIAN = "rus";
    public static final String ENGLISH = "eng";

    public static final String[] languages = new String[] {RUSSIAN, ENGLISH};

    //colors
    public static final int color_magenta2 = Color.rgb(255, 0, 170);
    public static final int color_red_night = Color.rgb(247, 124, 124);
    public static final int color_red_light = Color.rgb(243, 10, 10);
    public static final int score_color_light = Color.rgb(34, 140, 89);
    public static final int score_color_night = Color.rgb(75, 242, 161);

    private static final Set<String> THEMES =
            Collections.unmodifiableSet(new HashSet<>(Arrays.asList(THEME_LIGHT, THEME_NIGHT)));

    private static Support sSupport;
    private final SQLiteDatabase mDatabase;
    private Settings mSettings;
    private final Context mContext;



    private void setLightScreen(Window window, @Nullable ActionBar actionBar) {
        window.getDecorView().setBackgroundColor(Color.WHITE);
        window.setStatusBarColor(Color.WHITE);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (actionBar != null)
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
    }

    private void setDarkScreen(Window window, @Nullable ActionBar actionBar) {
        window.getDecorView().setBackgroundColor(Color.BLACK);
        window.setStatusBarColor(Color.BLACK);
        window.getDecorView().setSystemUiVisibility(0);
        if (actionBar != null)
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setThemeToScreen(
            Window window,
            @Nullable ActionBar actionBar,
            Menu menu,
            @NonNull @IdRes int[] menuItems)
    {

        int iconColor;
        SpannableString titleText;

        if (getTheme().equals(THEME_LIGHT)) {
            setLightScreen(window, actionBar);
            iconColor = Color.BLACK;
        }
        else {
            setDarkScreen(window, actionBar);
            iconColor = Color.WHITE;
        }


        titleText = new SpannableString(Objects.requireNonNull(actionBar).getTitle());

        titleText.setSpan(
                new ForegroundColorSpan(iconColor),
                0,
                titleText.length(),
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            titleText.setSpan(
                    new TypefaceSpan(Typeface.DEFAULT_BOLD),
                    0,
                    titleText.length(),
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }

        titleText.setSpan(
                new TypefaceSpan("font/verdana.ttf"),
                0,
                titleText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        actionBar.setTitle(titleText);
//        if (isUpArrow) {
//
//            Drawable upArrow = ContextCompat.getDrawable(mContext, R.drawable.ic_menu_up);
//            Objects.requireNonNull(upArrow).setColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP);
//
//            actionBar.setHomeAsUpIndicator(upArrow);
//        }

        for (int id : menuItems) {
            MenuItem item = menu.findItem(id);

            SpannableString itemTitle = new SpannableString(item.getTitle());
            itemTitle.setSpan(
                    new ForegroundColorSpan(iconColor),
                    0,
                    itemTitle.length(),
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            item.setTitle(itemTitle);
            item.setIconTintList(ColorStateList.valueOf(iconColor));
        }
    }





    public static Support get(Context context) {
        if (sSupport == null)
            sSupport = new Support(context);

        return sSupport;
    }

    private Support(Context context) {
        this.mContext = context.getApplicationContext();
        this.mDatabase = new RusBaseHelper(mContext).getWritableDatabase();
        mSettings = new Settings();

        SupportCursorWrapper cursor = querySupports(null, null);

        if (cursor.getCount() == 0)
            createSupportTable();

        cursor.moveToFirst();
        mSettings = cursor.getSettings();
        cursor.close();
    }

    public void setChecked(boolean isChecked) {
        this.mSettings.setChecked(isChecked);
        updateSupport(mSettings);
    }

    public boolean getChecked() {
        return mSettings.isChecked();
    }

    public void setPause(
            @IntRange(from = MIN_TIME, to = MAX_TIME) int pause) {
        this.mSettings.setPause(pause);
        updateSupport(mSettings);
    }

    public int getPause() {
        return mSettings.getPause();
    }

    public void setTheme(String theme) {
        if (THEMES.contains(theme)) {
            this.mSettings.setTheme(theme);
            updateSupport(mSettings);
        }
    }

    public String getTheme() {
        return mSettings.getTheme();
    }

    public void setQuestions(int questions) {
        if (questions < 10) return;
        this.mSettings.setQuestions(questions);
        updateSupport(mSettings);
    }

    public int getQuestions() {
        return mSettings.getQuestions();
    }

    public void setLanguage(String language) {
        this.mSettings.setLanguage(language);
        updateSupport(mSettings);
    }

    public String getLanguage() {
        return mSettings.getLanguage();
    }



    private ContentValues getContentValues(Settings settings) {
        ContentValues values = new ContentValues();
        values.put(SupportTable.Cols.CHECKED, settings.isChecked() ? 1 : 0);
        values.put(SupportTable.Cols.PAUSE, settings.getPause());
        values.put(SupportTable.Cols.THEME, settings.getTheme());
        values.put(SupportTable.Cols.QUESTIONS, settings.getQuestions());
        values.put(SupportTable.Cols.LANGUAGE, settings.getLanguage());

        return values;
    }

    private void createSupportTable() {
        ContentValues values = getContentValues(mSettings);
        mDatabase.insert(SupportTable.NAME, null, values);
    }

    private void updateSupport(Settings settings) {
        String stringId = String.valueOf(settings.getId());
        ContentValues values = getContentValues(settings);

        mDatabase.update(
                SupportTable.NAME,
                values,
                SupportTable.Cols.ID + " = ?",
                new String[] { stringId });
    }

    private SupportCursorWrapper querySupports(String whereClause, String[] whereArgs) {

        Cursor cursor = mDatabase.query(
                SupportTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new SupportCursorWrapper(cursor);
    }
}
