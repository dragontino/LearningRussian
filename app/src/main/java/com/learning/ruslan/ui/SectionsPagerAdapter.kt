package com.learning.ruslan.ui

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.learning.ruslan.R
import com.learning.ruslan.task.TaskType

class SectionsPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val type: TaskType,
    private val tabCount: Int = TAB_TITLES.size,
) : FragmentStateAdapter(fragmentActivity) {

    companion object {
        @StringRes
        val TAB_TITLES = intArrayOf(R.string.tab_1, R.string.tab_2)
    }

    override fun getItemCount(): Int = if (tabCount <= TAB_TITLES.size && tabCount > 0)
        tabCount
    else TAB_TITLES.size

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> LearningFragment.newInstance(type)
        else -> QuizFragment.newInstance(type)
    }
}