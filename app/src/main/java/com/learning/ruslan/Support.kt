package com.learning.ruslan
//
//import android.content.ContentValues
//import android.content.Context
//import android.content.res.ColorStateList
//import android.database.sqlite.SQLiteDatabase
//import android.graphics.Color
//import android.graphics.Typeface
//import android.graphics.drawable.ColorDrawable
//import android.os.Build
//import android.text.SpannableString
//import android.text.Spanned
//import android.text.style.ForegroundColorSpan
//import android.text.style.TypefaceSpan
//import android.view.*
//import androidx.annotation.IdRes
//import androidx.annotation.RequiresApi
//import androidx.appcompat.app.ActionBar
//import com.learning.ruslan.settings.Settings
//import java.util.*
//
//class Support private constructor(context: Context) {
//    private val mDatabase: SQLiteDatabase
//    private var mSettings: Settings
//    private val mContext = context.applicationContext
//
//    private fun setLightScreen(window: Window, actionBar: ActionBar?) {
//        window.decorView.setBackgroundColor(Color.WHITE)
//        window.statusBarColor = Color.WHITE
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//        actionBar?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
//    }
//
//    private fun setDarkScreen(window: Window, actionBar: ActionBar?) {
//        window.decorView.setBackgroundColor(Color.BLACK)
//        window.statusBarColor = Color.BLACK
//        window.decorView.systemUiVisibility = 0
//        actionBar?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    fun setThemeToScreen(
//        window: Window,
//        actionBar: ActionBar?,
//        menu: Menu?,
//        @IdRes menuItems: IntArray?
//    ) {
//        val iconColor = if (theme == THEME_LIGHT) {
//            setLightScreen(window, actionBar)
//            Color.BLACK
//        } else {
//            setDarkScreen(window, actionBar)
//            Color.WHITE
//        }
//        val titleText = SpannableString(Objects.requireNonNull(actionBar).title)
//        titleText.setSpan(
//            ForegroundColorSpan(iconColor),
//            0,
//            titleText.length,
//            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        titleText.setSpan(
//            TypefaceSpan(Typeface.DEFAULT_BOLD),
//            0,
//            titleText.length,
//            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        titleText.setSpan(
//            TypefaceSpan("font/verdana.ttf"),
//            0,
//            titleText.length,
//            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        actionBar?.title = titleText
//        //        if (isUpArrow) {
////
////            Drawable upArrow = ContextCompat.getDrawable(mContext, R.drawable.ic_menu_up);
////            Objects.requireNonNull(upArrow).setColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP);
////
////            actionBar.setHomeAsUpIndicator(upArrow);
////        }
//        if (menu == null || menuItems == null) return
//        for (id in menuItems) {
//            val item = menu.findItem(id)
//            val itemTitle = SpannableString(item.title)
//            itemTitle.setSpan(
//                ForegroundColorSpan(iconColor),
//                0,
//                itemTitle.length,
//                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//            item.title = itemTitle
//            item.iconTintList = ColorStateList.valueOf(iconColor)
//        }
//    }
//
//    var checked: Boolean
//        get() = mSettings.isChecked
//        set(isChecked) {
//            mSettings.isChecked = isChecked
//            updateSupport(mSettings)
//        }
//    var pause: Int
//        get() = mSettings.pause
//        set(pause) {
//            mSettings.pause = pause
//            updateSupport(mSettings)
//        }
//
//    fun setTheme(theme: String) {
//        if (THEMES.contains(theme)) {
//            mSettings.themeString = theme
//            updateSupport(mSettings)
//        }
//    }
//
//    val theme: String
//        get() = mSettings.themeString
//
//    fun updateTheme() {
//        if (theme == THEME_LIGHT) setTheme(THEME_NIGHT) else setTheme(THEME_LIGHT)
//    }
//
//    var questions: Int
//        get() = mSettings.questions
//        set(questions) {
//            if (questions < 10) return
//            mSettings.questions = questions
//            updateSupport(mSettings)
//        }
//
//    fun setLanguage(position: Int) {
//        mSettings.languageString = languages[position]
//        updateSupport(mSettings)
//    }
//
//    private fun getContentValues(settings: Settings): ContentValues {
//        val values = ContentValues()
//        values.put(SupportTable.Cols.CHECKED, if (settings.isChecked) 1 else 0)
//        values.put(SupportTable.Cols.PAUSE, settings.pause)
//        values.put(SupportTable.Cols.THEME, settings.themeString)
//        values.put(SupportTable.Cols.QUESTIONS, settings.questions)
//        values.put(SupportTable.Cols.LANGUAGE, settings.languageString)
//        return values
//    }
//
//    private fun createSupportTable() {
//        val values = getContentValues(mSettings)
//        mDatabase.insert(SupportTable.NAME, null, values)
//    }
//
//    private fun updateSupport(settings: Settings) {
//        val stringId = settings.id.toString()
//        val values = getContentValues(settings)
//        mDatabase.update(
//            SupportTable.NAME,
//            values,
//            SupportTable.Cols.ID + " = ?", arrayOf(stringId))
//    }
//
//    companion object {
//
//        //colors
//        val color_magenta2 = Color.rgb(255, 0, 170)
//        val color_red_night = Color.rgb(247, 124, 124)
//        val color_red_light = Color.rgb(243, 10, 10)
//        val score_color_light = Color.rgb(34, 140, 89)
//        val score_color_night = Color.rgb(75, 242, 161)
//
//    }
//
//    init {
//        mDatabase = RusBaseHelper(mContext).writableDatabase
//        mSettings = Settings()
//        val cursor = querySupports(null, null)
//        if (cursor.count == 0) createSupportTable()
//        cursor.moveToFirst()
//        mSettings = cursor.settings
//        cursor.close()
//    }
//}