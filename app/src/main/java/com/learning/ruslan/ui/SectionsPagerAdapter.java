package com.learning.ruslan.ui;

import android.content.Context;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.BaseTransientBottomBar.Duration;
import com.learning.ruslan.R;
import com.learning.ruslan.Word;

import java.util.Objects;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_1, R.string.tab_2};
    private final Context mContext;
    private final int typeId;

    public SectionsPagerAdapter(
            Context context, @NonNull FragmentManager fm, int behavior, int typeId) {
        super(fm, behavior);
        mContext = context;
        this.typeId = typeId;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = LearningFragment.newInstance(typeId);
                break;
            case 1:
                fragment = QuizFragment.newInstance(typeId);
                break;
        }
        return Objects.requireNonNull(fragment);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}
