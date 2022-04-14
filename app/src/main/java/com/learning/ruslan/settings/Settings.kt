package com.learning.ruslan.settings

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SettingsTable")
class Settings(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var isChecked: Boolean = true,
    var pause: Int = 1600,
    var questions: Int = 10,
    var themeString: String? = Themes.SYSTEM.themeName,
    var languageString: String? = Languages.RUSSIAN.languageName,
    var showingButtonBackground: Boolean = true) {

    var theme: Themes
        get() =
            Themes.values().find {
                it.themeName == themeString
            } ?: Themes.SYSTEM
        set(value) {
            themeString = value.themeName
        }

    var language: Languages
        get() =
            Languages.values().find {
                it.languageName == languageString
            } ?: Languages.ENGLISH
        set(value) {
            languageString = value.languageName
        }
}