package com.learning.ruslan.ui

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.learning.ruslan.*
import com.learning.ruslan.databinding.ActivitySettingsBinding
import com.learning.ruslan.settings.*
import com.learning.ruslan.task.TaskType
import java.util.*

class SettingsActivity : AppCompatActivity(), View.OnClickListener, Theme {

    companion object {
        private const val MIN_QUESTIONS = 10
        private const val TASK_TYPE = "SettingsActivity.typeId"

        fun getIntent(context: Context?, type: TaskType) =
            createIntent<SettingsActivity>(context) {
                putExtra(TASK_TYPE, type)
            }
    }

    private lateinit var binding: ActivitySettingsBinding

    private lateinit var settings: SettingsViewModel

    private lateinit var aboutFragment: BottomFragment
    private lateinit var themeBottomFragment: ThemeBottomFragment
    private lateinit var languageBottomFragment: BottomFragment

    private var menu: Menu? = null

    private var isSettingsChanged = false
    private val exitToast: Toast by lazy {
        Toast.makeText(this, R.string.settings_saved, Toast.LENGTH_SHORT)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySettingsBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        settings = SettingsViewModel.getInstance(this)
//        settings.observeSettings(this) {
//            updateTheme()
//        }

        aboutFragment = BottomFragment(this)
        aboutFragment.setHeading(
            "О приложении",
            fontPath = "fonts/segoesc.ttf",
            headSize = 29f
        )
        aboutFragment.addView(
            null,
            R.string.version,
            "fonts/xarrovv.otf",
            Typeface.ITALIC,
            textSize = 20f
        ) {}

        // TODO: 07.04.2022 сделать картинки
        val feedbackFragment = BottomFragment(this)
        feedbackFragment.setHeading(
            getString(R.string.feedback),
            fontPath = "fonts/xarrovv.otf",
            fontStyle = Typeface.BOLD,
            headSize = 26f
        )
        val feedbackListener = View.OnClickListener {
            val address = when (it.id) {
                0 -> "https://vk.com/cepetroff"
                1 -> "https://t.me/cepetroff"
                2 -> "mailto:petrovsd2002@gmail.com"
                else -> return@OnClickListener
            }

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(address))
            startActivity(intent)
        }
        val strings = intArrayOf(
            R.string.vk,
            R.string.telegram,
            R.string.mail
        )

        feedbackFragment.addViews(null, strings, feedbackListener)


        aboutFragment.addView(
            null,
            R.string.feedback,
            "fonts/xarrovv.otf",
            fontStyle = Typeface.BOLD,
            textSize = 22f
        ) {
            aboutFragment.dismiss()
            feedbackFragment.show(supportFragmentManager)
        }


        themeBottomFragment = ThemeBottomFragment(this, this)
        languageBottomFragment = BottomFragment(this)

        updateTheme()

        languageBottomFragment.addView(
            Languages.RUSSIAN.ui,
            "fonts/xarrovv.otf",
            Typeface.BOLD
        ) {
            settings.baseSettings.language = Languages.RUSSIAN
        }

//        if (settings.questions > maxQuestions)
//            settings.questions = maxQuestions

        binding.run {

            autoUpdate.isChecked = settings.baseSettings.isChecked

            switchTheme.setOnClickListener(this@SettingsActivity)
            buttonLanguage.setOnClickListener(this@SettingsActivity)

            switchChecked(settings.baseSettings.isChecked)
            editPause.txt = settings.baseSettings.pause.toString()

            seekPause.progress = settings.baseSettings.pause
//        seekQuestions.max = maxQuestions
            editQuestions.txt = settings.baseSettings.questions.toString()
            seekQuestions.progress = settings.baseSettings.questions

            buttonLanguage.setText(R.string.button_language_text)
            buttonLanguage.background = ContextCompat
                .getDrawable(this@SettingsActivity, R.drawable.button_style_light)


            buttonLanguage.setTypeface(Typeface.createFromAsset(assets, "fonts/xarrovv.otf"),
                Typeface.BOLD)

            seekPause.doOnProgressChanged { _, progress, _ ->
                editPause.txt = progress.toString()
                settings.updatePause(progress)
                isSettingsChanged = true
            }


            editPause.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    val text = binding.editPause.text.toString()

                    when {
                        text.isEmpty() -> Toast.makeText(
                            this@SettingsActivity,
                            R.string.please_set_value,
                            Toast.LENGTH_SHORT
                        ).show()

                        text.toInt() !in SettingsViewModel.minTime until SettingsViewModel.maxTime -> {
                            Toast.makeText(
                                this@SettingsActivity,
                                getString(R.string.lim_of_time,
                                    SettingsViewModel.minTime,
                                    SettingsViewModel.maxTime),
                                Toast.LENGTH_LONG
                            ).show()
                            editPause.clear()
                        }
                        else -> {
                            seekPause.setProgress(editPause.text.toString().toInt(), true)
                            settings.updatePause(editPause.text.toString().toInt())
                            isSettingsChanged = true
                        }
                    }
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }

            seekQuestions.doOnProgressChanged { _, progress, _ ->
                editQuestions.txt = progress.toString()
                settings.updateQuestions(progress)
                isSettingsChanged = true
            }


            editQuestions.setOnKeyListener { _: View?, keyCode: Int, event: KeyEvent ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    val text = editQuestions.text.toString()

                    when {
                        text.isEmpty() -> Toast.makeText(
                            this@SettingsActivity,
                            R.string.please_set_value,
                            Toast.LENGTH_SHORT
                        ).show()
                        text.toInt() !in 10 until seekQuestions.max -> {
                            Toast.makeText(
                                this@SettingsActivity,
                                getString(R.string.lim_of_questions,
                                    MIN_QUESTIONS,
                                    seekQuestions.max),
                                Toast.LENGTH_LONG
                            ).show()
                            editQuestions.clear()
                        }
                        else -> {
                            seekQuestions.setProgress(editQuestions.text.toString().toInt(), true)
                            settings.updateQuestions(editQuestions.text.toString().toInt())
                            isSettingsChanged = true
                        }
                    }
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }


            autoUpdate.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                switchChecked(isChecked)
                isSettingsChanged = true
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.settings_menu, menu)
        this.menu = menu
        settings.drawMenuItems(menu, R.id.menu_item_info)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        if (item.itemId == R.id.menu_item_info) {
            aboutFragment.show(supportFragmentManager)
            true
        }
        else super.onOptionsItemSelected(item)


    private fun switchChecked(isChecked: Boolean) = binding.run {
        settings.updateChecked(isChecked)

        if (isChecked) {
            autoUpdate.setText(R.string.switch_checked)
            pauseHeader.show()
            editPause.show()
            seekPause.show()
            pauseQuestionsDivider.root.show()
            editPause.txt = settings.baseSettings.pause.toString()
            seekPause.progress = settings.baseSettings.pause
        } else {
            autoUpdate.setText(R.string.switch_unchecked)
            pauseHeader.hide()
            editPause.hide()
            seekPause.hide()
            pauseQuestionsDivider.root.hide()
        }
    }

    private val currentThemeText: String
        get() {
            val themes = resources.getStringArray(R.array.themes)
            val position = settings.getIndexTheme()

            return getString(R.string.switchThemeText,
                themes[position].lowercase(Locale.getDefault())
            )
        }


    override fun updateTheme() {
        settings.updateThemeInScreen(window, supportActionBar)
        settings.drawMenuItems(menu, R.id.menu_item_info)

        binding.run {
            switchTheme.text = currentThemeText

            settings.fontColor.let {
                autoUpdate.textColor = it
                pauseHeader.textColor = it
                questionsHeader.textColor = it
                buttonLanguage.textColor = it
                editPause.textColor = it
                editPause.backgroundTint = it
                editQuestions.textColor = it
                editQuestions.backgroundTint = it
                switchTheme.textColor = it
            }

            seekPause.thumbTintList = ColorStateList.valueOf(settings.highlightColor)
            seekQuestions.thumbTintList = ColorStateList.valueOf(settings.highlightColor)

            seekPause.progressTintList = ColorStateList.valueOf(settings.highlightColor)

            for (header in arrayOf(pauseHeader, questionsHeader, buttonLanguage))
                header.alpha = settings.textAlpha
        }
    }


    override fun startTimeListener(calendar: Calendar) = View.OnClickListener {
        startLauncher.launch(calendar)
    }

    override fun endTimeListener(calendar: Calendar) = View.OnClickListener {
        endLauncher.launch(calendar)
    }



    private val startLauncher = registerForActivityResult(
        TimePickerActivity.Companion.TimePickerActivityContract(R.string.start_time)) {
        if (it != null) {
            settings.setStartTime(it)
            themeBottomFragment.updateColors()
            updateTheme()
        }
    }


    private val endLauncher = registerForActivityResult(
        TimePickerActivity.Companion.TimePickerActivityContract(R.string.end_time)) {
        if (it != null) {
            settings.setEndTime(it)
            themeBottomFragment.updateColors()
            updateTheme()
        }
    }



    override fun onStop() {
        super.onStop()
        if (isSettingsChanged) {
            exitToast.show()
            isSettingsChanged = false
        }
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_language -> languageBottomFragment.show(supportFragmentManager)
            R.id.switch_theme -> themeBottomFragment.show(supportFragmentManager)
        }
    }
}