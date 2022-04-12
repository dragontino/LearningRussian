package com.learning.ruslan
//
//import androidx.constraintlayout.widget.ConstraintLayout
//import android.annotation.SuppressLint
//import android.os.Bundle
//import android.content.res.ColorStateList
//import android.text.Spannable
//import android.text.SpannableString
//import android.text.style.ForegroundColorSpan
//import androidx.appcompat.app.AppCompatActivity
//import com.learning.ruslan.ui.SettingsActivity
//import android.content.Intent
//import com.learning.ruslan.ui.LibraryActivity
//import android.graphics.Color
//import android.graphics.Point
//import android.os.Handler
//import android.view.*
//import android.widget.*
//import java.util.*
//
//class SuffixActivity : AppCompatActivity(), View.OnClickListener {
//    var btn_start: Button? = null
//    var btn_break: Button? = null
//    var textViewStart: TextView? = null
//    var tvParams: ConstraintLayout.LayoutParams? = null
//    private var words = arrayOf("скоропортЯщиеся", "доносЯтся (звуки) — доносить, 2 спр",
//        "переловЯт (рыбаки) — ловить, 2 спр", "моЮщиеся — мыться, 1 спр",
//        "дорогостоЯщая — стоить 2 спр", "выкрикиваЮщая — выкрикивать, 1 спряжение",
//        "посылаЕмый — посылать, 1спр", "нарушаЕт (водитель) — нарушать, 1 спр",
//        "оклеиваЕмый — оклеивать, 2 спр", "(дуб) спилИтся — спилиться, 2 спр",
//        "борЕтся (народ) — бороться, 1 спр", "усвоИшь — усвоить, 2 спр",
//        "вышлЕт (бабушка) — выслать, 1 спр", "бросаЕшь — бросать, 1 спр",
//        "красИт (маляр) — красить, 2 спр", "встретИшь — встретить, 2 спр",
//        "внемлЮщий — внимать, 1 спр", "пригреваЕмый — проигрывать, 1 спр",
//        "капризничаЕт — капризничать, 1 спр", "независИмый — зависеть, 2 спр, искл",
//        "строИтся (дом) — строиться, 2 спр", "подозреваЕмый — подозревать, 1 спр",
//        "трудЯтся (электрики) — трудиться, 2 спр", "бросаЮт (игроки) — бросать, 1 спр",
//        "лелеЮщая — лелеять, 1 спр", "парЯт (облака) — парить, 2 спр",
//        "катИтся (шар) — катИлся, 2 спр", "видИмый — видеть, 2 спр, искл",
//        "летЯщий — лететь, ударение", "поставЯт (архитекторы) — поставить, 2 спр",
//        "плачЕт (малыш) — плакать, 1 спр", "бросИшь — бросить, 2 спр",
//        "волнуЮщийся — волноваться, 1 спр", "усвоЯт (ученики) — усвоить, 2 спр",
//        "кудахчЕт (курица) — кудахтать, 1 спр", "прогреваЕмое — прогревать, 1 спр",
//        "брюзжАщий — брюзжать, 1 спр", "запеваЮт (пионеры) — запевать, 1 спр",
//        "(меня) тревожАт (мысли) — тревожить, 2 спр", "дышАщий — дышать, 2 спр, искл",
//        "скачУщий — скакать, 1 спр", "ненавидЯщий — ненавидеть, 2 спр, искл",
//        "слышАщий — слышать, 2 спр, искл", "дружАт (соседи) — дружить, 2 спр",
//        "реЕт (флаг) — реть, 1 спр", "зреЕт (зерно) — зреть, 1 спр", "кружАщий — кружить, 2 спр",
//        "заносЯт (грузчики) — заносить, 2 спр", "сушИт — сушить, 2 спр", "рисуЮт — рисовать, 1 спр",
//        "наклеИвший — наклеить, 2 спр", "предъявИт (прокурор) — предъявить, 2 спр",
//        "рассмотрИшь — смотреть, 2 спр, искл", "вынуждаЕшь — вынуждать, 1 спр",
//        "задремлЕшь — задремать, 1 спр", "задрапируЕшь — задрапировать, 1 спр",
//        "зависИмый — зависеть, 2 спр, искл", "нянчАщая — нянчить, 2 спр",
//        "произносИшь — прозносить, 2 спр", "рокочЕт (мотор) — рокотать, 1 спр",
//        "преобразуЕмый — преобразовать, 1 спр", "(боец) служИт — служить, 2 спр",
//        "дремлЕшь — дремать, 1 спр", "лечИшь — лечить, 2 спр", "перекинЕшь — перекинуть, 1 спр",
//        "вычитаЕмые — вычитать, 1 спр", "сломлЕнный — сломить, 2 спр",
//        "(он) разыгрываЕт — разыгрывать, 1 спр", "(страны) граничАт — граничить, 2 спр",
//        "шепчУщий (на ухо) — шептать, 1 спр", "строИтся (школа) — строить, 2 спр",
//        "засмотрИшься — смотреть, 2 спр, искл", "дышАщий — дышать, 2 спр, искл",
//        "щекочУщий — щекотать, 1 спр", "вышлЕт — выслать, 1 спр",
//        "приходИтся (братом) — приходиться, 2 спр", "лечАщий — лечить, 2 спр, искл", "знаЮщий",
//        "трудЯтся (рабочие)", "выстоЯвшая (армия)", "колеблЕшься", "выделИшь", "независИмый",
//        "мыслИмый", "нарушаЕмый", "предполагаЕмый", "невидИмый", "непромокаЕмый", "произносИшь",
//        "щиплЕшь", "замечЕнный", "смеркаЕтся", "(тени) мерещУтся", "(печка) топИтся",
//        "(родители) отпустЯт", "(друзья) выручАт", "движИмый", "(крот) роЕт",
//        "сверИвший (накануне)", "нарушИвший (права)", "закручиваЕмый", "(земля) вертИтся",
//        "зависИмый", "(птичка) вылетИт", "задержИшься", "оперируЕмый — оперировать, 1 спр",
//        "выгорИшь — горит, 2 спр, ударение", "раскручЕнный— раскрутить, 2 спр",
//        "предвидИшь", "(печка) топИтся", "построИшь", "(можно) надеЯться", "задержАвшийся",
//        "радуЮщийся", "расклеИшь", "наметИть (план)", "прополЕшь", "упрощаЕшь", "(он) расстелЕт",
//        "заправИшь", "(они) мелЮт (зерно)", "ссорЯщийся,", "не успокоЯтся (активисты)",
//        "(она) доедаЕт", "чистЯщий", "поражаЮщий", "навеЕт (ветер)",
//        "посвящаЕтся (стихотворение)", "дорогостоЯщая", "подводЯщая", "пенЯщееся", "навеЯть",
//        "ненавидЯщий", "мучАвшийся", "(матросы) задраЯт", "разбавИвший", "маЮщийся",
//        "вываливаЮщийся", "(враг) не воротИтся", "отчаЯнный", "затеЯла", "выздоровЕть", "отчаЯться",
//        "развеЯв - развеЯть", "(друг) уступИт", "(слово) молвИтся", "(учитель) проверИт",
//        "ляжЕшь (пораньше)", "пенЯщееся", "разбавлЕнный", "(они) стелЮт (скатерть)",
//        "овеЯнный", "верИвший", "вылетИшь", "мелЮщий (кофе)", "(огородники) полЮт (грядки)",
//        "выздоровЕвший (юноша)", "выспИшься", "скачУщий (конь) — скакать, 1 спр",
//        "(они) дышАт — дышать, 2 спр, искл", "трепещЕм (от страха) — трепещать, 1 спр",
//        "колеблЕмые (ветром) — колебать, 1 спр", "поношЕнное (платье) — поносить, 2 спр",
//        "зависИмый — зависеть, 2 спр, искл", "потерЯнный (багаж) — потерять, 1 спр",
//        "(флаги) реЮт", "взлелеЯвший", "занимаЮщаяся (заря)", "выгорИшь", "расстроИвшись",
//        "предвидЕвший", "незыблЕмый", "пышУщий (здоровьем)", "(они) выложАт", "противоречАщий",
//        "выслушАвший", "обездвижЕнный – от «движити»", "натерпИшься", "щебечУщий",
//        "(они) клокочУт", "умоЕшься", "видИмый", "клеИшь", "будоражИвший (воображение)",
//        "расстроИвшись", "повадИшься", "колышУщиеся (травы)", "(они) лечАт", "взлелеЯвший",
//        "верЯщий (на слово)", "немыслЕмый", "застроЕнный", "таЮщие (снега)", "(они) видЯтся",
//        "(вы) устанЕте", "колеблЕмый", "выскочИм", "(вы) вытерпИте", "(они) обидЯтся", "повеЯвший",
//        "дремлЮщий", "(они) гонЯтся (друг за другом)", "установлЕнный", "неуправляЕмый",
//        "(они) шепчУтся", "машУщий (крыльями)", "(вы) останЕтесь", "встретИвшись", "настоЯнный",
//        "лелеЕмый", "(пациенты) лечАтся", "маячАщий (вдали лес)", "зависИшь", "подстрижЕнный",
//        "борЕшься", "воспеваЕмый", "проедЕшься", "необитаЕмый", "вертИшься", "обнаружЕнный",
//        "дорогостоЯщий", "вылинЯвшая (ткань)", "(ничего не) значАщий", "(автомобили) движУтся",
//        "выплачиваЕмый", "обезумЕвший (от страха)", "овеЯнный (прохладой)", "настроИв (радио)",
//        "(кони) бродЯт (по лугу)", "дремлЮщий (в поезде)", "вскочИшь", "(вы) портИте (изделие)",
//        "выкачЕнный (из гаража велосипед)", "грохочУщий", "устроИвший (праздник)",
//        "мучИмый (сомнениями)", "(он) клевещЕт", "движИмый", "тешУщий (себя надеждами)",
//        "обвенчАнные", "(вы слишком) торопИтесь", "посеЕшь (пшеницу)", "скроЕнный (костюм)",
//        "контролируЕмая (ситуация)", "выслушАнный (ответ)", "машУщий (руками)",
//        "молЯщийся (в церкви)", "рассеЯвший (сомнения)", "(они) высмеЮт (зло)", "орошаЮщий (землю)",
//        "колеблЕмый (волной)", "правИвший (лошадьми)", "проверЯвший (ответы)", "(всадник) скачЕт",
//        "обтекаЕмая (форма)", "(дворник) выметЕт (мусор)", "служАщий (делу)",
//        "задержАнный (преступник)", "завещАнный (дом)", "распорЕшь (швы)", "уполномочЕнный",
//        "слышИмый", "опасаЕшься", "потратИвший")
//    var words2 = arrayOf("брезжУщий\n", "зыблЮщийся\n", "зиждУщийся\n", "внемлЮщий\n",
//        "приемлЕмый\n", "неотъемлЕмый\n", "незыблЕмый\n", "движИмый – от «движити»")
//    var isChecked = false
//    var pause = 0
//    var arrayList: ArrayList<Int>? = null
//    var random: Random? = null
//    var mHandler: Handler? = null
//    var posX = 0.0
//    var posY = 0.0
//    var isWord = true
//    var word: WordViewModel? = null
//    var support: Support? = null
//    private var menu: Menu? = null
//    var fontColor = Color.BLACK
//    var letColor = Color.MAGENTA
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_suffix)
//        word = WordViewModel.Companion.get(this)
//        support = Support.Companion.get(this)
//        btn_start = findViewById(R.id.btn_start)
//        btn_break = findViewById(R.id.btn_break)
//        textViewStart = findViewById(R.id.start)
//        tvParams = textViewStart.getLayoutParams() as ConstraintLayout.LayoutParams
//        val display = windowManager.defaultDisplay
//        val size = Point()
//        display.getSize(size)
//        arrayList = ArrayList()
//        random = Random()
//        btn_start.setOnClickListener(this)
//        btn_break.setOnClickListener(this)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        updateUI()
//    }
//
//    private fun updateUI() {
//        isChecked = support.getChecked()
//        pause = support.getPause()
//        when (support.getTheme()) {
//            Support.Companion.THEME_LIGHT -> {
//                fontColor = Color.BLACK
//                letColor = Color.MAGENTA
//            }
//            Support.Companion.THEME_NIGHT -> {
//                fontColor = Color.WHITE
//                letColor = Support.Companion.color_magenta2
//            }
//        }
//        btn_start!!.backgroundTintList = ColorStateList.valueOf(letColor)
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
//                startActivity(LibraryActivity.Companion.getIntent(this, WordViewModel.Companion.SuffixId))
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    @SuppressLint("NonConstantResourceId")
//    override fun onClick(v: View) {
//        when (v.id) {
//            R.id.btn_start -> {
//                btn_start!!.visibility = View.INVISIBLE
//                btn_break!!.visibility = View.VISIBLE
//                tvParams!!.verticalBias = 0.5.toFloat()
//                textViewStart!!.layoutParams = tvParams
//                textViewStart!!.text = ""
//                if (isChecked) {
//                    mHandler = Handler()
//                    mHandler!!.post(mUpdate)
//                } else {
//                    textViewStart!!.setOnClickListener(this)
//                    if (isWord) {
//                        for (elem in words2) {
//                            val spannable: Spannable = SpannableString(elem)
//                            var i = 0
//                            while (i < elem.length) {
//                                if (Character.isUpperCase(elem[i])) {
//                                    spannable.setSpan(ForegroundColorSpan(letColor),
//                                        i,
//                                        i + 1,
//                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//                                    break
//                                }
//                                i++
//                            }
//                            textViewStart!!.append(spannable)
//                        }
//                        isWord = false
//                    } else {
//                        val spannable = newWord
//                        textViewStart!!.text = spannable
//                        posX = Math.random()
//                        posY = Math.random()
//                        tvParams!!.horizontalBias = posX.toFloat()
//                        tvParams!!.verticalBias = posY.toFloat()
//                        textViewStart!!.layoutParams = tvParams
//                    }
//                }
//            }
//            R.id.btn_break -> {
//                btn_start!!.visibility = View.VISIBLE
//                tvParams!!.horizontalBias = 0.5.toFloat()
//                tvParams!!.verticalBias = 0.617.toFloat()
//                textViewStart!!.layoutParams = tvParams
//                if (isChecked) mHandler!!.removeCallbacks(mUpdate)
//                textViewStart!!.text = "Старт"
//                btn_break!!.visibility = View.INVISIBLE
//                isWord = true
//            }
//            R.id.start -> {
//                val spannable = newWord
//                textViewStart!!.text = spannable
//                posX = Math.random()
//                posY = Math.random()
//                tvParams!!.horizontalBias = posX.toFloat()
//                tvParams!!.verticalBias = posY.toFloat()
//                textViewStart!!.layoutParams = tvParams
//            }
//        }
//    }
//
//    private val newWord: Spannable
//        private get() {
//            var index: Int
//            val words = words
//            val text: String
//            if (arrayList!!.size == words.size) arrayList!!.clear()
//            do index = random!!.nextInt(words.size) while (arrayList!!.contains(index))
//            arrayList!!.add(index)
//            val spannable: Spannable = SpannableString(words[index])
//            text = words[index]
//            for (elem in 0 until text.length) if (Character.isUpperCase(text[elem])) {
//                spannable.setSpan(ForegroundColorSpan(letColor),
//                    elem,
//                    elem + 1,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//                break
//            }
//            return spannable
//        }
//    private val mUpdate: Runnable = object : Runnable {
//        override fun run() {
//            if (isWord) {
//                for (elem in words2) {
//                    val spannable: Spannable = SpannableString(elem)
//                    for (i in 0 until elem.length) if (Character.isUpperCase(elem[i])) {
//                        spannable.setSpan(ForegroundColorSpan(Color.MAGENTA),
//                            i,
//                            i + 1,
//                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//                        break
//                    }
//                    textViewStart!!.append(spannable)
//                }
//                mHandler!!.postDelayed(this, 6000)
//                isWord = false
//            } else {
//                val spannable = newWord
//                textViewStart!!.text = spannable
//                mHandler!!.postDelayed(this, pause.toLong())
//                posX = Math.random()
//                posY = Math.random()
//                tvParams!!.horizontalBias = posX.toFloat()
//                tvParams!!.verticalBias = posY.toFloat()
//                textViewStart!!.layoutParams = tvParams
//            }
//        }
//    }
//}