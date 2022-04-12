package com.learning.ruslan.ui

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Chronometer.OnChronometerTickListener
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.learning.ruslan.*
import com.learning.ruslan.databinding.FragmentQuizBinding
import com.learning.ruslan.settings.SettingsViewModel
import com.learning.ruslan.settings.SettingsViewModel.Companion.reverseColor
import com.learning.ruslan.task.TaskType
import com.learning.ruslan.task.TaskViewModel
import com.learning.ruslan.ui.LearningFragment.Companion.getType
import java.util.*
import kotlin.random.Random

class QuizFragment : Fragment(), View.OnClickListener,
    OnChronometerTickListener {

    companion object {
        private const val ActivityId = "QuizFragment.ActivityId"
        private const val MAX_TIME = 13

        fun newInstance(type: TaskType): QuizFragment {
            val fragment = QuizFragment()
            val args = Bundle()
            args.putSerializable(ActivityId, type)
            fragment.arguments = args
            return fragment
        }
    }


    private lateinit var binding: FragmentQuizBinding

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var settings: SettingsViewModel

    private lateinit var answerButtons: ArrayList<Button>


    private lateinit var textViewParams: ConstraintLayout.LayoutParams
    private val random = Random.Default
    private lateinit var date: Date

    /**
     * текущее время в хронометре (показывает, сколько осталось времени на ответ)
     */
    private var currentTime = MAX_TIME
    private var rightIndex = 0
    private var score = 0
    private var currentQuestion = 0
    private var wrongTime: Long = 0
    private var countVariants = 0

    private var handlerIsRunning = false
    private var onPause = false

    private var handler: Handler? = null

    private lateinit var typeId: TaskType


    private fun TextView.setTypeface(fontPath: String, textStyle: Int? = null) {
        val tf = Typeface.createFromAsset(activity?.assets, fontPath)
        if (textStyle == null)
            this.typeface = tf
        else
            setTypeface(tf, textStyle)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        activity?.let {
            taskViewModel = TaskViewModel.getInstance(it)
            settings = SettingsViewModel.getInstance(it)
        }

        // TODO: 11.04.2022 проверить работоспособность. Не работает :(
//        settings.observeSettings(this)

        typeId = arguments?.getType(ActivityId) ?: TaskType.Assent

        textViewParams = binding.textView.layoutParams as ConstraintLayout.LayoutParams

        for (btn in arrayOf(binding.play, binding.pause, binding.stop))
            btn.setOnClickListener(this)

        binding.run {
            answerButtons = arrayListOf(firstAnswer, secondAnswer, thirdAnswer, fourthAnswer)
            chronometer.onChronometerTickListener = this@QuizFragment
        }

        answerButtons.forEach { it.setOnClickListener(this) }
    }

    private fun updateUI() {
        binding.run {
            play.backgroundTint = settings.highlightColor
            textView.setTextColor(settings.fontColor)
            chronometer.setTextColor(settings.fontColor)

            score.setTextColor(settings.scoreColor)

            stop.imageTint = settings.redColor
            pause.imageTint = settings.redColor

            stop.backgroundTint = settings.backgroundColor
            pause.backgroundTint = settings.backgroundColor

            for (btn in answerButtons) {
                btn.setBackgroundResource(settings.buttonBackgroundRes)
                btn.setTextColor(settings.fontColor.reverseColor())
            }
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


    override fun onClick(v: View) {
        when (v.id) {
            R.id.play -> startGame()

            in answerButtons.editElements { it.id } ->
                if (v === answerButtons[rightIndex]) {
                    score++
                    startGame()
                }
                else {
                    date = Date()
                    wrongTime = date.time
                    handlerIsRunning = true
                    activity?.let {
                        handler = Handler(it.mainLooper)
                    }
                    handler?.post(mUpdate)

                    for (btn in answerButtons)
                        btn.hide()

                    setChronometerOnPause(true, View.INVISIBLE)
                }
            R.id.stop -> {
                if (handlerIsRunning)
                    stopHandler()
                endGame()
            }
            R.id.pause -> {
                binding.pause.hide()
                binding.play.show()
                onPause = true
                countVariants = 0

                for (btn in answerButtons) if (btn.isShow()) {
                    btn.hide()
                    countVariants++
                }

                binding.textView.setText(R.string.btn_continue)
                updateTextView(false)
                setChronometerOnPause(true, View.VISIBLE)
            }
        }
    }

    @SuppressLint("StringFormatMatches")
    private fun startGame() {
        if (currentQuestion == settings.baseSettings.questions) {
            endGame()
            return
        }

        updateTextView(true)
        updateButtons()

        if (onPause)
            onPause = false
        else {
            currentTime = getNextTime(currentQuestion)
            wrongTime = 0
        }
        setChronometerOnPause(false, View.VISIBLE)

        binding.play.hide()
        binding.stop.show()
        binding.pause.show()
    }

    private val mUpdate: Runnable = object: Runnable {
        override fun run() {

            val startText = binding.textView.text.toString()
            var newText: CharSequence = ""

            handler?.postDelayed(this, 250)

            if (startText == "") {
                newText = answerButtons[rightIndex].text

                if (typeId == TaskType.Paronym)
                    newText = taskViewModel.concat(
                        binding.textView.text.toString(),
                        newText.toString(),
                        settings.highlightColor
                    )
            }

            binding.textView.apply {
                text = newText
                textSize = 50f
                setTypeface("fonts/xarrovv.otf", Typeface.BOLD)
            }

            date = Date()
            if (date.time - wrongTime >= 3000) {
                stopHandler()
            }
        }
    }

    private fun updateButtons() {
        if (onPause) {

            for (i in 0 until countVariants)
                answerButtons[i].show()
            countVariants = 0
        }
        else {
            val wordsPair = taskViewModel.getRandomArrayWords(typeId, settings.highlightColor)

            if (wordsPair == null) {
                binding.textView.clear()
                Toast.makeText(context, "слов нет...", Toast.LENGTH_SHORT).show()
                return
            }

            if (typeId == TaskType.Paronym) {
                binding.textView.append("\n", wordsPair.first)
                wordsPair.first = wordsPair.second[0]
                wordsPair.second.removeAt(0)
            }

            val rightWord = wordsPair.first

            rightIndex =
                if (wordsPair.second.size == 2) 1
                else random.nextInt(wordsPair.second.size)

            wordsPair.second.add(rightIndex, rightWord)
            countVariants = wordsPair.second.size

            if (countVariants > 4) countVariants = 4

            for (elem in 0 until countVariants) {
                val button = answerButtons[elem]

                button.show()
                button.text = wordsPair.second[elem]

                val params = button.layoutParams as GridLayout.LayoutParams

//                if (wordsPair.second.size.isOdd() && elem == wordsPair.second.lastIndex)
//                    params.horizontalBias = 0.5f
//                else
//                    params.horizontalBias = (0.1 + 0.8 * (elem % 2)).toFloat()
//                params.verticalBias = (0.65 + 0.2 * (elem / 2)).toFloat()
//
//                button.layoutParams = params
            }

            var elem = wordsPair.second.size
            while (elem < 4) {
                answerButtons[elem++].hide()
            }
        }
    }

//    private fun createButtons(): Array<Button> {
//        val buttons = arrayOfNulls<Button>(4)
//        for (i in 0..3) {
//            val btnParams = ConstraintLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//            btnParams.topToTop = ConstraintSet.PARENT_ID
//            btnParams.startToStart = ConstraintSet.PARENT_ID
//            btnParams.endToEnd = ConstraintSet.PARENT_ID
//            btnParams.bottomToBottom = ConstraintSet.PARENT_ID
//            val btn = Button(activity)
//            btn.setOnClickListener(this)
//            btn.isAllCaps = false
//            btn.setTypeface("fonts/xarrovv.otf", Typeface.BOLD)
//            btn.textSize = 25f
//            btn.maxWidth = (0.5 * screenWidth).toInt() + 1
//            btn.id = 100 + i
//            btn.visibility = View.INVISIBLE
//            constraintLayout!!.addView(btn, btnParams)
//            buttons[i] = btn
//        }
//        return buttons
//    }

    /**
     * возвращает время, которое будет на ответ в следующий раз
     */
    private fun getNextTime(countQuestions: Int): Int {
        if (countQuestions == 0) return 13
        val y = 50 / countQuestions + 3
        return y.coerceAtMost(MAX_TIME)
    }



    private fun getRightEnding(score: Int) =
        if (score % 10 == 1 && score % 100 != 11)
            getString(R.string.one_point)
        else if (score % 10 in 2..4 && score % 100 !in 12..14)
            getString(R.string.two_points)
        else
            getString(R.string.ten_points)



    private fun endGame() {
        setChronometerOnPause(true, View.INVISIBLE)
        currentTime = MAX_TIME
        wrongTime = 0
        onPause = false

        updateTextView(false)
        binding.textView.setText(R.string.btn_start)
        binding.score.text = getString(
            R.string.end_game,
            score,
            getRightEnding(score),
            settings.baseSettings.questions
        )

        if (score == settings.baseSettings.questions)
            binding.score.append(getString(R.string.great_results))

        score = 0
        currentQuestion = 0
        binding.stop.hide()
        binding.pause.hide()

        for (button in answerButtons)
            button.hide()

        binding.play.show()
    }

    private fun updateTextView(isGame: Boolean) {
        if (isGame) {
            binding.run {
                textView.textSize = 20f
                textViewParams.horizontalBias = 0.5f
                textViewParams.verticalBias = 0.5f

                textView.setText(taskText)
            }

            if (!onPause) {

                val text = getString(
                    R.string.score,
                    ++currentQuestion,
                    settings.baseSettings.questions,
                    score,
                    currentQuestion - 1
                )

                binding.score.text = text
                binding.score.show()
            }

        } else {
            binding.textView.textSize = 30f
            binding.textView.setTypeface("fonts/verdana.ttf", Typeface.NORMAL)
            textViewParams.horizontalBias = 0.5.toFloat()
            textViewParams.verticalBias = 0.47.toFloat()
        }
        binding.textView.layoutParams = textViewParams
    }

    private fun stopHandler() {
        handlerIsRunning = false

        handler?.removeCallbacks(mUpdate)
        binding.textView.setTypeface("fonts/verdana.ttf", Typeface.NORMAL)

        startGame()
    }

    @SuppressLint("StringFormatMatches")
    private fun setChronometerOnPause(pause: Boolean, visibility: Int) {
        if (pause)
            binding.chronometer.stop()
        else {
            binding.chronometer.text = currentTime.toString()
            binding.chronometer.start()
        }

        binding.chronometer.visibility = visibility
    }



    override fun onChronometerTick(chronometer: Chronometer) {
        if (currentTime == 0) {
            Toast.makeText(activity, getString(R.string.time_is_over), Toast.LENGTH_SHORT).show()
            endGame()
        }

        chronometer.text = currentTime--.toString()
    }

    @get:StringRes
    private val taskText: Int
        get() = when (typeId) {
            TaskType.Assent -> R.string.assent_test_text
            TaskType.Suffix -> R.string.suffix_test_text
            TaskType.Paronym, TaskType.SteadyExpression -> R.string.paronym_test_text
        }
}