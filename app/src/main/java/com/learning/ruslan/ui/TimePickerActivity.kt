package com.learning.ruslan.ui

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.TimePicker
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.learning.ruslan.R
import com.learning.ruslan.createIntent
import com.learning.ruslan.databinding.DialogTimeBinding
import com.learning.ruslan.settings.SettingsViewModel
import java.util.*

class TimePickerActivity: AppCompatActivity() {

    companion object {
        private const val ARG_TIME = "time"
        private const val ARG_TITLE = "title"
        private const val EXTRA_TIME = "extra_time"

        fun newIntent(context: Context, calendar: Calendar, @StringRes title: Int) =
            createIntent<TimePickerActivity>(context) {
                putExtra(ARG_TIME, calendar)
                putExtra(ARG_TITLE, title)
            }

        fun parseIntent(intent: Intent) =
            intent.getSerializableExtra(EXTRA_TIME) as Calendar


        class TimePickerActivityContract(@StringRes private val title: Int) :
            ActivityResultContract<Calendar, Calendar?>() {

            override fun createIntent(context: Context, input: Calendar) =
                newIntent(context, input, title)

            override fun parseResult(resultCode: Int, intent: Intent?) = when {
                resultCode != RESULT_OK || intent == null -> null
                else -> parseIntent(intent)
            }
        }
    }

    private lateinit var binding: DialogTimeBinding

    private lateinit var timePicker: TimePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DialogTimeBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val settings = SettingsViewModel.getInstance(this)
        val calendar = intent.getSerializableExtra(ARG_TIME) as Calendar
        val title = intent.getIntExtra(ARG_TITLE, R.string.start_time)

        setTitle(title)

        val hours = calendar[Calendar.HOUR_OF_DAY]
        val minutes = calendar[Calendar.MINUTE]

        timePicker = layoutInflater.inflate(
            if (settings.isLightTheme()) R.layout.time_picker_light
            else R.layout.time_picker_dark,
            null,
            false
        ) as TimePicker

        timePicker.setIs24HourView(true)
        timePicker.hour = hours
        timePicker.minute = minutes

        binding.dialogTimePicker.addView(timePicker)

        binding.layoutButtons.setBackgroundColor(settings.backgroundColor)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(settings.highlightColor))

        binding.buttonCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        binding.buttonOk.setOnClickListener {
            calendar[Calendar.HOUR_OF_DAY] = timePicker.hour
            calendar[Calendar.MINUTE] = timePicker.minute

            val intent = Intent()
            intent.putExtra(EXTRA_TIME, calendar)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}