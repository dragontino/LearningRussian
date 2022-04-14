package com.learning.ruslan.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import com.learning.ruslan.*
import java.text.SimpleDateFormat
import java.util.*


interface Theme {
    /**
     * метод, который вызывается при обновлении темы
     */
    fun updateTheme()

    /**
     * метод, который возвращает listener для времени рассвета
     */
    fun startTimeListener(calendar: Calendar): View.OnClickListener

    /**
     * метод, который возвращает listener для времени заката
     */
    fun endTimeListener(calendar: Calendar): View.OnClickListener
}

@SuppressLint("InflateParams")
class ThemeBottomFragment(activity: AppCompatActivity, private val theme: Theme):
    BottomFragment(activity) {

    private val timeLayout: LinearLayout by lazy {
        layoutInflater
            .inflate(R.layout.auto_theme_times, null, false) as LinearLayout
    }

    init {
        val listener = View.OnClickListener {
            settings.theme = Themes.values()[it.id]
            if (settings.theme == Themes.AUTO)
                timeLayout.show()
            else
                dismiss()

            theme.updateTheme()
            updateColors()
        }

        activity.resources.getStringArray(R.array.themes).forEach {
            addView(
                image = R.drawable.radio_button_checked,
                imageBound = ImageBounds.RIGHT,
                text = it,
                listener = listener
            )
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timeLayout.update()
        addView(timeLayout)
        updateColors()
    }


    private fun LinearLayout.update() = if (settings.theme != Themes.AUTO) {
        hide()
    }
    else {
        show()
        val header = findViewById<TextView>(R.id.times_header)
        header.setTextColor(settings.fontColor)
        setBackgroundColor(settings.backgroundColor)
        updateTimes(this)
    }


    override fun updateColors(@ColorInt highlightColor: Int) {
        super.updateColors(highlightColor)

        for (index in Themes.values().indices) {
            if (index == settings.getIndexTheme())
                this[index].setDrawables()
            else
                this[index].removeDrawables()
        }

        timeLayout.update()
    }


    private fun updateTimes(root: LinearLayout) {
        val calendarPair = settings.times

        val startTime = getStringFromCalendar(calendarPair[0])
        val endTime = getStringFromCalendar(calendarPair[1])

        val start = root.findViewById<TextView>(R.id.theme_layout_start_time)
        val end = root.findViewById<TextView>(R.id.theme_layout_end_time)

        start.setOnClickListener(theme.startTimeListener(calendarPair[0]))
        end.setOnClickListener(theme.endTimeListener(calendarPair[1]))

        start.text = startTime
        end.text = endTime

        settings.fontColor.let {
            start.textColor = it
            end.textColor = it
        }
        start.setBackgroundResource(settings.textViewBackgroundRes)
        end.setBackgroundResource(settings.textViewBackgroundRes)
    }

    private fun getStringFromCalendar(calendar: Calendar): String {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return format.format(calendar.time)
    }

    override fun onDestroyView() {
        removeView(timeLayout)
        super.onDestroyView()
    }
}