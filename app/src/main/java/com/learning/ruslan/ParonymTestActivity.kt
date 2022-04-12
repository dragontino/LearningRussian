package com.learning.ruslan
//
//import androidx.constraintlayout.widget.ConstraintLayout
//import android.annotation.SuppressLint
//import android.os.Bundle
//import android.graphics.Typeface
//import android.content.res.ColorStateList
//import android.text.Spannable
//import androidx.constraintlayout.widget.ConstraintSet
//import android.text.SpannableString
//import android.text.style.ForegroundColorSpan
//import androidx.appcompat.app.AppCompatActivity
//import com.learning.ruslan.ui.SettingsActivity
//import android.os.Build
//import android.content.Intent
//import com.learning.ruslan.ui.LibraryActivity
//import androidx.annotation.RequiresApi
//import android.graphics.Color
//import android.graphics.Point
//import android.view.*
//import android.widget.*
//import java.util.*
//
//class ParonymTestActivity : AppCompatActivity(), View.OnClickListener {
//    private var btnStart: Button? = null
//    private var btnBreak: Button? = null
//    private var buttons: Array<Button?>
//    private var textViewStart: TextView? = null
//    private var textViewBalls: TextView? = null
//    private var questions = 0
//    private var corr_index = 0
//    private var balls = 0
//    private var count_ans = 0
//    private val MAX_QUESTIONS = 74
//    private var constraintLayout: ConstraintLayout? = null
//    private var tvParams: ConstraintLayout.LayoutParams? = null
//    private var random: Random? = null
//    private var word: WordViewModel? = null
//    private var support: Support? = null
//    private var menu: Menu? = null
//    private var fontColor = Color.BLACK
//    private var letColor = Color.MAGENTA
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_paronym_test)
//        word = WordViewModel.Companion.get(this)
//        support = Support.Companion.get(this)
//        btnStart = findViewById(R.id.btnStartTest)
//        btnBreak = findViewById(R.id.btnBreakTest)
//        textViewStart = findViewById(R.id.textViewStartTest)
//        textViewBalls = findViewById(R.id.textViewBalls)
//        constraintLayout = findViewById(R.id.clMain)
//        buttons = createButtons()
//        random = Random()
//        val display = windowManager.defaultDisplay
//        val size = Point()
//        display.getSize(size)
//        val ScreenHeight = size.y
//        support.setQuestions(support.getQuestions() % MAX_QUESTIONS)
//        btnBreak.setOnClickListener(this)
//        btnStart.setOnClickListener(this)
//        tvParams = textViewStart.getLayoutParams() as ConstraintLayout.LayoutParams
//        textViewStart.setTypeface(Typeface.createFromAsset(assets, "fonts/verdana.ttf"))
//    }
//
//    override fun onResume() {
//        super.onResume()
//        updateUI()
//    }
//
//    private fun updateUI() {
//        questions = support?.questions
//        when (support.getTheme()) {
//            Support.Companion.THEME_LIGHT -> {
//                fontColor = Color.BLACK
//                letColor = Color.MAGENTA
//                textViewBalls!!.setTextColor(Support.Companion.score_color_light)
//                for (btn in buttons) {
//                    btn!!.setBackgroundResource(R.drawable.button_style_2)
//                    btn.setTextColor(Color.WHITE)
//                }
//            }
//            Support.Companion.THEME_NIGHT -> {
//                fontColor = Color.WHITE
//                letColor = Support.Companion.color_magenta2
//                textViewBalls!!.setTextColor(Support.Companion.score_color_night)
//                for (btn in buttons) {
//                    btn!!.setBackgroundResource(R.drawable.button_style)
//                    btn.setTextColor(Color.BLACK)
//                }
//            }
//        }
//        btnStart!!.backgroundTintList = ColorStateList.valueOf(letColor)
//        textViewStart!!.setTextColor(fontColor)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.fragment_menu, menu)
//        this.menu = menu
//        updateUI()
//        return true
//    }
//
//    @SuppressLint("NonConstantResourceId")
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.menu_item_settings -> {
//                startActivity(Intent(this, SettingsActivity::class.java))
//                true
//            }
//            R.id.menu_item_list_words -> {
//                startActivity(LibraryActivity.Companion.getIntent(this, WordViewModel.Companion.ParonymId))
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    private fun createButtons(): Array<Button?> {
//        val buttons = arrayOfNulls<Button>(4)
//        for (i in 0..3) {
//            val btnParams = ConstraintLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//            btnParams.topToTop = ConstraintSet.PARENT_ID
//            btnParams.startToStart = ConstraintSet.PARENT_ID
//            btnParams.endToEnd = ConstraintSet.PARENT_ID
//            btnParams.bottomToBottom = ConstraintSet.PARENT_ID
//            val btn = Button(this)
//            btn.setOnClickListener(this)
//            btn.isAllCaps = false
//            btn.setTypeface(Typeface.createFromAsset(assets, "fonts/xarrovv.otf"), Typeface.BOLD)
//            btn.textSize = 25f
//            btn.id = 100 + i
//            btn.visibility = View.INVISIBLE
//            constraintLayout!!.addView(btn, btnParams)
//            buttons[i] = btn
//        }
//        return buttons
//    }
//
//    //предложения в будущем будут браться из теории.
//    @get:SuppressLint("NewApi")
//    private val newWord:
//
//    //Заполнение массива
//
//    //Этот массив отслеживает, какие слова уже были записаны на кнопках, а какие - нет
//    //false - слова еще не были использованы, true - были
//            Int
//        private get() {
//            val phrases =
//                HashMap<String, List<List<String>>>() //предложения в будущем будут браться из теории.
//            val correct_index: Int
//            val index: Int
//            val countButtons: Int
//            val word: String
//
//            //Заполнение массива
//            phrases["абонемент"] = java.util.List.of(
//                java.util.List.of("абонемент в театр",
//                    "абонемент на цикл лекций",
//                    "межбиблиотечный абонемент",
//                    "абонемент на кинофестиваль",
//                    "абонемент в бассейн"),
//                java.util.List.of("абонент"))
//            phrases["абонент"] = java.util.List.of(
//                java.util.List.of("абонент библиотеки",
//                    "абонент телефонной сети",
//                    "абонент временно недоступен"),
//                java.util.List.of("абонемент"))
//            phrases["абонентов"] = java.util.List.of(
//                java.util.List.of("жалобы от абонентов"),
//                java.util.List.of("абонементов"))
//            phrases["артистический"] = java.util.List.of(
//                java.util.List.of("артистический путь"),
//                java.util.List.of("артистичный"))
//            phrases["артистическая"] = java.util.List.of(
//                java.util.List.of("артистическая среда",
//                    "артистическая карьера",
//                    "артистическая уборная"),
//                java.util.List.of("артистичная"))
//            phrases["артистическое"] = java.util.List.of(java.util.List.of("артистическое кафе"),
//                java.util.List.of("артистичное"))
//            phrases["артистичный"] = java.util.List.of(
//                java.util.List.of("артистичный человек", "артистичный до мозга костей"),
//                java.util.List.of("артистический"))
//            phrases["артистичное"] = java.util.List.of(
//                java.util.List.of("артистичное исполнение"),
//                java.util.List.of("артистическое"))
//            phrases["бедная"] = java.util.List.of(
//                java.util.List.of("бедная обстановка", "бедная одежда"),
//                java.util.List.of("бедственная"))
//            phrases["бедный"] = java.util.List.of(
//                java.util.List.of("бедный человек",
//                    "бедный ужин",
//                    "бедный дом",
//                    "мой бедный мальчик"),
//                java.util.List.of("бедственный"))
//            phrases["бедственное"] = java.util.List.of(
//                java.util.List.of("бедственное положение"),
//                java.util.List.of("бедное"))
//            phrases["безответная"] = java.util.List.of(
//                java.util.List.of("безответная любовь"),
//                java.util.List.of("безответственная"))
//            phrases["безответственный"] = java.util.List.of(
//                java.util.List.of("безответственный поступок"),
//                java.util.List.of("безответный"))
//            phrases["безответственное"] = java.util.List.of(
//                java.util.List.of("безответственное лицо",
//                    "безотвественное отношение",
//                    "безответственное поведение"),
//                java.util.List.of("безответное"))
//            phrases["болотистые"] = java.util.List.of(
//                java.util.List.of("болотистые земли"),
//                java.util.List.of("болотные"))
//            phrases["болотистая"] = java.util.List.of(
//                java.util.List.of("болотистая местность", "болотистая почва"),
//                java.util.List.of("болотная"))
//            phrases["болотные"] = java.util.List.of(
//                java.util.List.of("болотные сапоги", "болотные травы"),
//                java.util.List.of("болотистые"))
//            phrases["болотная"] = java.util.List.of(
//                java.util.List.of("болотная птица"),
//                java.util.List.of("болотистая"))
//            phrases["болотный"] = java.util.List.of(
//                java.util.List.of("болотный мох"),
//                java.util.List.of("болотистый"))
//            phrases["благодарная"] = java.util.List.of(
//                java.util.List.of("благодарная тема"),
//                java.util.List.of("благодарственная"))
//            phrases["благодарный"] = java.util.List.of(
//                java.util.List.of("благодарный пациент",
//                    "благодарный слушатель",
//                    "благодарный ученик",
//                    "благодарный взгляд",
//                    "благодарный материал"),
//                java.util.List.of("благодарственный"))
//            phrases["благодарственное"] = java.util.List.of(
//                java.util.List.of("благодарственное письмо"),
//                java.util.List.of("благодарное"))
//            phrases["благодарственная"] = java.util.List.of(
//                java.util.List.of("благодарственная телеграмма"),
//                java.util.List.of("благодарная"))
//            phrases["благодарственный"] = java.util.List.of(
//                java.util.List.of("благодарственный молебен"),
//                java.util.List.of("благодарный"))
//            phrases["благотворительная"] = java.util.List.of(
//                java.util.List.of("благотворительная акция", "благотворительная лотерея"),
//                java.util.List.of("благотворная"))
//            phrases["благотворительный"] = java.util.List.of(
//                java.util.List.of("благотворительный спектакль", "благотворительный фонд"),
//                java.util.List.of("благотворный"))
//            phrases["благотворное"] = java.util.List.of(
//                java.util.List.of("благотворное влияние"),
//                java.util.List.of("благотворительное"))
//            phrases["благотворная"] = java.util.List.of(
//                java.util.List.of("благотворная прохлада", "благотворная влага"),
//                java.util.List.of("благотворительная"))
//            phrases["бывшая"] = java.util.List.of(
//                java.util.List.of("бывшая школа"),
//                java.util.List.of("былая"))
//            phrases["бывший"] = java.util.List.of(
//                java.util.List.of("бывший клуб", "бывший врач", "бывший директор"),
//                java.util.List.of("былой"))
//            phrases["былые"] = java.util.List.of(
//                java.util.List.of("былые годы"),
//                java.util.List.of("бывшие"))
//            phrases["былая"] = java.util.List.of(
//                java.util.List.of("былая сила", "былая печаль", "былая слава"),
//                java.util.List.of("бывшая"))
//            phrases["былое"] = java.util.List.of(
//                java.util.List.of("былое счастье", "былое уважение"),
//                java.util.List.of("бывшее"))
//            phrases["былой"] = java.util.List.of(
//                java.util.List.of("былой страх"),
//                java.util.List.of("бывший"))
//            phrases["вдох"] = java.util.List.of(
//                java.util.List.of("глубокий вдох", "сделать вдох", "вдох всей грудью"),
//                java.util.List.of("вздох"))
//            phrases["вздох"] = java.util.List.of(
//                java.util.List.of("вздох облегчения", "тяжёлый вздох"),
//                java.util.List.of("вдох"))
//            phrases["вздохом"] = java.util.List.of(
//                java.util.List.of("сказать со вздохом"),
//                java.util.List.of("вдохом"))
//            phrases["вековые"] = java.util.List.of(
//                java.util.List.of("вековые дубы", "вековые традиции", "вековые обычаи"),
//                java.util.List.of("вечные"))
//            phrases["вековая"] = java.util.List.of(
//                java.util.List.of("вековая роща", "вековая пыль", "вековая грязь"),
//                java.util.List.of("вечная"))
//            phrases["вечная"] = java.util.List.of(
//                java.util.List.of("вечная проблема", "вечная мерзлота"),
//                java.util.List.of("вековая"))
//            phrases["вечные"] = java.util.List.of(
//                java.util.List.of("вечные человеческие ценности", "вечные вопросы"),
//                java.util.List.of("вековые"))
//            val keys = phrases.keys.toTypedArray()
//            index = random!!.nextInt(keys.size)
//            word = keys[index]
//            val list_phr = Objects.requireNonNull(
//                phrases[word])[0]
//            val list_words = Objects.requireNonNull(
//                phrases[word])[1]
//
//            //Этот массив отслеживает, какие слова уже были записаны на кнопках, а какие - нет
//            //false - слова еще не были использованы, true - были
//            val words = BooleanArray(list_words.size)
//            Arrays.fill(words, false)
//            countButtons = list_words.size + 1
//            correct_index = random!!.nextInt(countButtons)
//            textViewStart!!.append(
//                getPhrase(
//                    list_phr[random!!.nextInt(list_phr.size)],
//                    word
//                )
//            )
//            for (j in 0 until countButtons) {
//                if (j == correct_index) buttons[j]!!.text = word else {
//                    var k: Int
//                    do k = random!!.nextInt(list_words.size) while (words[k])
//                    words[k] = true
//                    buttons[j]!!.text = list_words[k]
//                }
//                val btnParams = buttons[j]!!.layoutParams as ConstraintLayout.LayoutParams
//                if (countButtons % 2 == 1 && j == countButtons - 1) btnParams.horizontalBias =
//                    0.5.toFloat() else btnParams.horizontalBias = (0.1 + 0.8 * (j % 2)).toFloat()
//                btnParams.verticalBias = (0.65 + 0.2 * (j / 2)).toFloat()
//                buttons[j]!!.layoutParams = btnParams
//                buttons[j]!!.visibility = View.VISIBLE
//            }
//            return correct_index
//        }
//
//    /**
//     *
//     * @param phrase - sentence, that includes keyword
//     * @param keyword - word to delete
//     * @return phrase without keyword
//     */
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private fun getPhrase(phrase: String, keyword: String): Spannable? {
//        if (!phrase.contains(keyword)) return null
//        val text = phrase.split(" ").toTypedArray()
//        for (i in text.indices) {
//            if (text[i] == keyword) text[i] = "..."
//        }
//        val textColor: Int
//        textColor = if (support.getTheme() == Support.Companion.THEME_LIGHT) Color.rgb(217,
//            92,
//            2) else Color.rgb(219, 130, 66)
//        val spannable: Spannable = SpannableString(java.lang.String.join(" ", *text))
//        spannable.setSpan(ForegroundColorSpan(textColor), 0,
//            java.lang.String.join(" ", *text).length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        return spannable
//    }
//
//    @SuppressLint("SetTextI18n")
//    private fun EndGame() {
//        btnBreak!!.visibility = View.GONE
//        textViewBalls!!.text = "Поздравляем, вы набрали $balls очков из $questions"
//        if (balls == questions) textViewBalls!!.append(getString(R.string.great_results))
//        balls = 0
//        count_ans = 0
//        textViewStart!!.textSize = 30f
//        tvParams!!.horizontalBias = 0.5.toFloat()
//        tvParams!!.verticalBias = 0.617.toFloat()
//        for (button in buttons) button!!.visibility = View.INVISIBLE
//        textViewStart!!.text = "Старт"
//        btnStart!!.visibility = View.VISIBLE
//    }
//
//    @SuppressLint("NonConstantResourceId", "SetTextI18n")
//    override fun onClick(v: View) {
//        when (v.id) {
//            R.id.btnStartTest -> {
//                btnStart!!.visibility = View.INVISIBLE
//                btnBreak!!.visibility = View.VISIBLE
//                textViewBalls!!.visibility = View.VISIBLE
//                textViewStart!!.textSize = 20f
//                tvParams!!.horizontalBias = 0.5.toFloat()
//                tvParams!!.verticalBias = 0.5.toFloat()
//                textViewStart!!.layoutParams = tvParams
//                textViewStart!!.setText(R.string.paronym_test_text)
//                textViewBalls!!.text = "баллы: 0/0"
//                balls = 0
//                corr_index = newWord
//            }
//            R.id.btnBreakTest -> EndGame()
//            100, 101, 102, 103 -> {
//                if (v === buttons[corr_index]) {
//                    Toast.makeText(this, "Верно!", Toast.LENGTH_SHORT).show()
//                    balls++
//                } else Toast.makeText(this, "Ошибка!", Toast.LENGTH_SHORT).show()
//                count_ans++
//                if (count_ans == questions) EndGame() else {
//                    textViewStart!!.setText(R.string.paronym_test_text)
//                    textViewBalls!!.text = "баллы: $balls/$count_ans"
//                    corr_index = newWord
//                }
//            }
//        }
//    }
//}