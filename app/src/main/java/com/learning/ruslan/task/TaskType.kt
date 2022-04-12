package com.learning.ruslan.task

import androidx.annotation.StringRes
import com.learning.ruslan.R

enum class TaskType(@StringRes val title: Int) {

    Assent(R.string.assent_label),
    Suffix(R.string.suffix_label),
    Paronym(R.string.paronym_label),
    SteadyExpression(R.string.steady_expression_label)
}