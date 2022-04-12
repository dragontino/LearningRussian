package com.learning.ruslan.settings

import android.app.Application
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.view.Menu
import android.view.View
import android.view.Window
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.learning.ruslan.MainDatabase
import com.learning.ruslan.Pair
import com.learning.ruslan.R
import com.learning.ruslan.settings.EnumPreferences.*
import java.util.*

class SettingsViewModel(private val mApplication: Application) :
    AndroidViewModel(mApplication) {

    companion object {
        const val minTime = 10
        const val maxTime = 10000

        @Volatile
        private var INSTANCE: SettingsViewModel? = null

        fun getInstance(owner: ViewModelStoreOwner) : SettingsViewModel {
            val temp = INSTANCE

            if (temp != null)
                return temp

            synchronized(this) {
                val instance = ViewModelProvider(owner)[SettingsViewModel::class.java]

                INSTANCE = instance
                return instance
            }
        }

        fun Int.reverseColor() = if (this == Color.WHITE)
            Color.BLACK
        else Color.WHITE
    }

    private val settingsRepository: SettingsRepository
    private val preferences = mApplication
        .getSharedPreferences(APP_PREFERENCES.title, Context.MODE_PRIVATE)
    private var startTime = getDateFromPreferences(
        keyHours = APP_PREFERENCES_START_HOURS,
        keyMinutes = APP_PREFERENCES_START_MINUTES,
        defHours = 7
    )

    private var endTime = getDateFromPreferences(
        keyHours = APP_PREFERENCES_END_HOURS,
        keyMinutes = APP_PREFERENCES_END_MINUTES,
        defHours = 23
    )



    val baseSettings: Settings get() =
        settingsRepository.getSettings()

    //Colors
    @ColorInt var fontColor = Color.BLACK
    @ColorInt var highlightColor = mApplication.getColor(R.color.raspberry)
    @ColorInt var backgroundColor = Color.WHITE
    @ColorInt var redColor = mApplication.getColor(R.color.red_light)
    @ColorInt var scoreColor = mApplication.getColor(R.color.score_light)
    var textAlpha = 0.5f


    // TODO: 11.04.2022 добавить возможность отключать задний фон у кнопок
    @DrawableRes var buttonBackgroundRes = R.drawable.button_style_dark


    init {
        val settingsDao = MainDatabase.getDatabase(mApplication).settingsDao()
        settingsRepository = SettingsRepository(settingsDao)
        settingsRepository.checkSettings()
        updateColors()
    }


    fun updateThemeInScreen(
        window: Window,
        actionBar: ActionBar?
    ) {

//        window.setDecorFitsSystemWindows(true)
//        WindowCompat.setDecorFitsSystemWindows(window, true)

        window.decorView.systemUiVisibility =
            if(isLightTheme()) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            else 0

        window.decorView.setBackgroundColor(backgroundColor)
        window.statusBarColor = backgroundColor
        actionBar?.setBackgroundDrawable(ColorDrawable(backgroundColor))

        val titleText = SpannableString(actionBar?.title)

        titleText.setSpan(
            ForegroundColorSpan(fontColor),
            0,
            titleText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        titleText.setSpan(
            TypefaceSpan(Typeface.create("font/verdana.ttf", Typeface.BOLD)),
            0,
            titleText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        actionBar?.title = titleText
    }


    fun drawMenuItems(menu: Menu?, @IdRes vararg items: Int) = items.forEach {
        if (menu == null)
            return

        val item = menu.findItem(it)
        val itemTitle = SpannableString(item.title)

        itemTitle.setSpan(
            ForegroundColorSpan(backgroundColor),
            0,
            itemTitle.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        item.title = itemTitle
        item.iconTintList = ColorStateList.valueOf(highlightColor)
    }


    var theme: Themes
        get() = baseSettings.theme
        set(value) {
            settingsRepository.updateTheme(value.themeName)
            updateColors()
        }

    fun updateChecked(isChecked: Boolean) =
        settingsRepository.updateChecked(isChecked)

    fun updatePause(pause: Int) =
        settingsRepository.updatePause(pause)

    fun updateQuestions(questions: Int) =
        settingsRepository.updateQuestions(questions)

    fun updateLanguages(language: String) =
        settingsRepository.updateLanguages(language)


    fun getIndexTheme() = Themes.values().indexOf(theme)


    fun isLightTheme() = when(baseSettings.theme) {
        Themes.LIGHT -> true
        Themes.DARK -> false
        Themes.SYSTEM -> {
            val currentNightMode =
                mApplication.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

            currentNightMode == Configuration.UI_MODE_NIGHT_NO
        }
        Themes.AUTO -> {
            val date = Date(System.currentTimeMillis())

            date.after(startTime.time) && date.before(endTime.time) || date == startTime.time
        }
    }

    private fun updateColors() = if (isLightTheme()) {
        fontColor = Color.BLACK
        highlightColor = mApplication.getColor(R.color.raspberry)
        backgroundColor = Color.WHITE
        textAlpha = 0.5f
        redColor = mApplication.getColor(R.color.red_light)
        scoreColor = mApplication.getColor(R.color.score_light)
        buttonBackgroundRes = R.drawable.button_style_dark
    }
    else {
        fontColor = Color.WHITE
        highlightColor = mApplication.getColor(R.color.pink)
        backgroundColor = mApplication.getColor(R.color.background_dark)
        redColor = mApplication.getColor(R.color.red_dark)
        scoreColor = mApplication.getColor(R.color.score_dark)
        textAlpha = 1f
        buttonBackgroundRes = R.drawable.button_style_light
    }



    fun setStartTime(startTime: Calendar) {
        this.startTime = startTime
        setDateToPreferences(
            keyHours = APP_PREFERENCES_START_HOURS,
            keyMinutes = APP_PREFERENCES_START_MINUTES,
            date = startTime
        )
        updateColors()
    }

    fun setEndTime(endTime: Calendar) {
        this.endTime = endTime
        setDateToPreferences(
            keyHours = APP_PREFERENCES_END_HOURS,
            keyMinutes = APP_PREFERENCES_END_MINUTES,
            date = endTime
        )
        updateColors()
    }

    val times get() = Pair(startTime, endTime)


    private fun getDateFromPreferences(
        keyHours: EnumPreferences, defHours: Int, keyMinutes: EnumPreferences): Calendar {

        val hours = preferences.getInt(keyHours.title, defHours)
        val minutes = preferences.getInt(keyMinutes.title, 0)
        val calendar = GregorianCalendar()

        calendar[Calendar.HOUR_OF_DAY] = hours
        calendar[Calendar.MINUTE] = minutes
        return calendar
    }


    private fun setDateToPreferences(
        keyHours: EnumPreferences, keyMinutes: EnumPreferences, date: Calendar) {

        val editor = preferences.edit()
        editor.putInt(keyHours.title, date[Calendar.HOUR_OF_DAY])
        editor.putInt(keyMinutes.title, date[Calendar.MINUTE])
        editor.apply()
    }


//    fun observeSettings(owner: LifecycleOwner, observe: ((Settings) -> Unit)? = null) =
//        settingsRepository.observeSettings(owner) {
//            baseSettings = it ?: Settings()
//            if (observe != null)
//                observe(baseSettings)
//        }
}