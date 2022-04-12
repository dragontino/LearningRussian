package com.learning.ruslan.settings

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SettingsDao {

    @Insert(entity = Settings::class, onConflict = OnConflictStrategy.REPLACE)
    fun addSettings(settings: Settings)

    @Query("UPDATE SettingsTable SET themeString = :theme")
    fun updateTheme(theme: String)

    @Query("UPDATE SettingsTable SET isChecked = :isChecked")
    fun updateChecked(isChecked: Boolean)

    @Query("UPDATE SettingsTable SET pause = :pause")
    fun updatePause(pause: Int)

    @Query("UPDATE SettingsTable SET questions = :questions")
    fun updateQuestions(questions: Int)

    @Query("UPDATE SettingsTable SET languageString = :language")
    fun updateLanguages(language: String)

    @Query("SELECT * FROM SettingsTable")
    fun getSettings(): Settings? //LiveData<Settings?>

    @Query("SELECT COUNT(themeString) FROM SettingsTable")
    fun getCount(): Int
}