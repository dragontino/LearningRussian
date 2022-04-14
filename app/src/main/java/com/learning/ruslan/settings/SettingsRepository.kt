package com.learning.ruslan.settings

class SettingsRepository(private val settingsDao: SettingsDao) {

    fun checkSettings() {
        if (settingsDao.getCount() == 0)
            settingsDao.addSettings(Settings())
    }

    fun updateTheme(theme: String) =
        settingsDao.updateTheme(theme)

    fun updateChecked(isChecked: Boolean) =
        settingsDao.updateChecked(isChecked)

    fun updatePause(pause: Int) =
        settingsDao.updatePause(pause)

    fun updateQuestions(questions: Int) =
        settingsDao.updateQuestions(questions)

    fun updateLanguages(language: String) =
        settingsDao.updateLanguages(language)

    fun updateShowingButtonBackground(showingButtonBackground: Boolean) =
        settingsDao.updateShowingButtonBackground(showingButtonBackground)

//    fun getLiveSettings() = settingsDao.getSettings()

    fun getSettings() =
        settingsDao.getSettings() ?: Settings()

//    fun observeSettings(owner: LifecycleOwner, observer: Observer<Settings?>) =
//        settingsDao.getSettings().observe(owner, observer)
}