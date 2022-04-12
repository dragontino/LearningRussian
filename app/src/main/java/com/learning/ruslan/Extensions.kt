package com.learning.ruslan

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.toSpannable
import androidx.core.widget.TextViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

fun TextView?.isEmpty() =
    this == null ||text.isEmpty()

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.isShow() =
    visibility == View.VISIBLE

var EditText.txt: String
    get() = text.toString()
    set(value) = setText(value)

fun TextView.clear() {
    text = ""
}


fun Int.isEven() =
    this % 2 == 0

fun Int.isOdd() =
    this % 2 == 1



var View.backgroundTint get() = Color.WHITE
    set(@ColorInt value) {
        backgroundTintList = ColorStateList.valueOf(value)
    }


var ImageView.imageTint get() = Color.WHITE
    set(@ColorInt value) {
        imageTintList = ColorStateList.valueOf(value)
    }


var TextView.textColor
    get() = Color.BLACK
    set(value) = setTextColor(value)



fun TextView.setCompoundDrawableColor(color: Int) =
    TextViewCompat.setCompoundDrawableTintList(this, ColorStateList.valueOf(color))



internal fun Bundle?.getString(key: String, defaultValue: String = "") =
    this?.getString(key) ?: defaultValue

internal fun Intent?.getStringExtra(key: String, defaultValue: String) =
    this?.getStringExtra(key) ?: defaultValue


inline fun <reified T: AppCompatActivity>createIntent(context: Context?, block: Intent.() -> Unit) =
    Intent(context, T::class.java).apply(block)


inline fun SeekBar.doOnProgressChanged(crossinline action: (seekBar: SeekBar?, progress: Int, fromUser: Boolean) -> Unit) {
    val onSeekBarChaListener = object: SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) =
            action(seekBar, progress, fromUser)

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    }

    setOnSeekBarChangeListener(onSeekBarChaListener)
}

fun TabLayout.setUpWithViewPager(
    viewPager2: ViewPager2,
    onConfigurationStrategy: TabLayoutMediator.TabConfigurationStrategy,
) =
    TabLayoutMediator(this, viewPager2, onConfigurationStrategy).attach()



inline fun <T, R> List<T>.editElements(crossinline function: (T) -> R): List<R> {
    val exitList = ArrayList<R>()
    forEach { exitList += function(it) }
    return exitList
}

fun <E> MutableList<E>.add(vararg elements: E) =
    addAll(elements)

fun TextView.append(vararg text: CharSequence) = text.forEach {
    append(it)
}


inline fun String.edit(index: Int, action: (Char) -> Char): String {
    val builder = StringBuilder(this)
    builder[index] = action(builder[index])
    return builder.toString()
}


fun List<String>.draw(@ColorInt color: Int, startIndex: Int = 0, lastIndex: Int = size): Spannable {
    val span = ForegroundColorSpan(color)
    val result = SpannableStringBuilder()

    for (i in indices) {
        if (i in startIndex until lastIndex)
            result.append(this[i], span, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        else
            result.append(this[i])

        if (i != this.lastIndex)
            result.append(" ")
    }

    return result.toSpannable()
}