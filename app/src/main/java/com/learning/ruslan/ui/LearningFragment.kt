package com.learning.ruslan.ui

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.learning.ruslan.*
import com.learning.ruslan.databinding.FragmentLearningBinding
import com.learning.ruslan.settings.SettingsViewModel
import com.learning.ruslan.task.TaskType
import com.learning.ruslan.task.TaskViewModel

class LearningFragment private constructor(): Fragment(), View.OnClickListener {

    companion object {
        private const val ActivityId = "LearningFragment.ActivityId"

        fun newInstance(type: TaskType): LearningFragment {
            val fragment = LearningFragment()
            val args = Bundle()
            args.putSerializable(ActivityId, type)
            fragment.arguments = args
            return fragment
        }

        fun Bundle?.getType(key: String) =
            this?.getSerializable(key) as TaskType?
    }


    private fun TextView.setTypeface(fontPath: String, textStyle: Int? = null) {
        val tf = Typeface.createFromAsset(activity?.assets, fontPath)
        if (textStyle == null)
            this.typeface = tf
        else
            setTypeface(tf, textStyle)
    }



    class Word {
        var text = ""
        var posX = 0.0
        var posY = 0.0

        fun clear() {
            text = ""
            posX = 0.0
            posY = 0.0
        }
    }

    enum class GameState {
        PLAYED,
        PAUSED,
        STOPPED
    }



    private lateinit var binding: FragmentLearningBinding

    private lateinit var textViewParams: ConstraintLayout.LayoutParams

    private var currentState = GameState.STOPPED
    private var mHandler: Handler? = null

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var settings: SettingsViewModel

    private val word = Word()

    private lateinit var type: TaskType


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentLearningBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        taskViewModel = TaskViewModel.getInstance(this)
        settings = SettingsViewModel.getInstance(this)
        type = arguments.getType(ActivityId) ?: TaskType.Assent

//        settings.observeSettings(this)

        textViewParams = binding.textView.layoutParams as ConstraintLayout.LayoutParams

        for (btn in arrayOf(binding.play, binding.stop, binding.pause))
            btn.setOnClickListener(this)
    }



    private fun updateUI() {
        binding.run {
            stop.imageTint = settings.redColor
            pause.imageTint = settings.redColor

            stop.backgroundTint = settings.backgroundColor
            pause.backgroundTint = settings.backgroundColor

            play.backgroundTint = settings.highlightColor
            textView.setTextColor(settings.fontColor)
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onStop() {
        endGame()
        super.onStop()
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.pause -> binding.run {

                pause.hide()
                play.show()

                word.text = textView.text.toString()
                textView.setText(R.string.btn_continue)

                updateTextView(GameState.PAUSED)

                if (settings.baseSettings.isChecked)
                    mHandler?.removeCallbacks(mUpdate)
                else
                    textView.isEnabled = false
            }
            R.id.play -> {

                binding.play.hide()
                binding.pause.show()
                binding.stop.show()

                updateTextView(GameState.PLAYED)

                mHandler =
                    if (settings.baseSettings.isChecked)
                        activity?.mainLooper?.let { Handler(it) }
                    else {
                        binding.textView.setOnClickListener(this)
                        binding.textView.isEnabled = true
                        null
                    }

                if (word.text.isEmpty()) {
                    if (settings.baseSettings.isChecked)
                        mHandler?.post(mUpdate)
                    else
                        updateWord()
                }
                else {
                    binding.textView.text = taskViewModel
                        .paintString(word.text, null, settings.highlightColor)
                    mHandler?.postDelayed(mUpdate, settings.baseSettings.pause.toLong())
                    word.clear()
                }
            }
            R.id.stop -> endGame()
            R.id.text_view -> updateWord()
        }
    }

    private fun endGame() {
        binding.run {
            play.show()
            stop.hide()
            pause.hide()

            if (currentState == GameState.PLAYED && settings.baseSettings.isChecked)
                mHandler?.removeCallbacks(mUpdate)

            updateTextView(GameState.STOPPED)

            textView.setText(R.string.btn_start)
        }
        word.clear()
    }

    private fun updateWord() {
        val spannable = taskViewModel.getRandomWord(type, settings.highlightColor)
        binding.textView.text = spannable
        updateTextView(GameState.PLAYED)
    }

    private val mUpdate = object: Runnable {
        override fun run() {
            mHandler?.postDelayed(this, settings.baseSettings.pause.toLong())
            updateWord()
        }
    }

    // обновление textView, если isGame, то выбираются рандомные координаты
    private fun updateTextView(newState: GameState) {

        if (newState == GameState.PLAYED) {

            if (currentState != GameState.PAUSED) {
                word.posX = Math.random()
                word.posY = Math.random()
            }

            binding.textView.setTypeface("fonts/xarrovv.otf", Typeface.BOLD)

            binding.textView.textSize = 45f

            textViewParams.horizontalBias = word.posX.toFloat()
            textViewParams.verticalBias = word.posY.toFloat()
        }
        else {
            binding.textView.setTypeface("fonts/verdana.ttf", Typeface.NORMAL)

            binding.textView.textSize = 30f

            word.posX = textViewParams.horizontalBias.toDouble()
            word.posY = textViewParams.verticalBias.toDouble()


            textViewParams.horizontalBias = 0.5f
            textViewParams.verticalBias = 0.578f
        }

        binding.textView.layoutParams = textViewParams
        currentState = newState
    }
}