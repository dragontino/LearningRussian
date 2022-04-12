package com.learning.ruslan
//
//import android.app.Application
//import android.text.Spannable
//import com.learning.ruslan.databases.RusDbSchema.AssentTable
//import com.learning.ruslan.databases.RusDbSchema.SuffixTable
//import com.learning.ruslan.databases.RusDbSchema.ParonymTable
//import android.database.CursorWrapper
//import android.text.SpannableString
//import com.learning.ruslan.databases.TaskCursorWrapper
//import android.text.style.ForegroundColorSpan
//import android.text.Spanned
//import android.text.SpannableStringBuilder
//import android.util.Log
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.ViewModelStoreOwner
//import com.learning.ruslan.task.*
//import java.util.*
//
//class WordViewModel private constructor(application: Application): AndroidViewModel(application) {
//
//    companion object {
//        private const val TAG = "Word"
//
//        @Volatile
//        private var INSTANCE: WordViewModel? = null
//
//        fun getInstance(owner: ViewModelStoreOwner): WordViewModel {
//            val temp = INSTANCE
//            if (temp != null)
//                return temp
//
//            synchronized(this) {
//                val instance = ViewModelProvider(owner)[WordViewModel::class.java]
//
//                INSTANCE = instance
//                return instance
//            }
//        }
//
//        //вставляет в фразу слово на место многоточия
//        fun concat(phrase: String, word: String, color: Int): String {
//            val builder = SpannableStringBuilder()
//            val arr = phrase.split(" ")
//            for (s in arr) {
//                if (s == "...") {
//                    val string = SpannableString(word)
//                    string.setSpan(
//                        ForegroundColorSpan(color),
//                        0,
//                        word.length,
//                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//                    )
//
//                    builder.append(string)
//                }
//                else builder.append(s)
//
//                builder.append(" ")
//            }
//            Log.d(TAG, builder.toString())
//
//            return builder.toString()
//        }
//    }
//
//    private val random = Random()
//
//    private val assentWords = arrayOf("аэропОрты", "бАнты", "сбрить бОроду", "много бухгАлтеров",
//        "вероисповЕдание", "газопровОд",
//        "граждАнство", "дефИс", "дешевИзна", "диспансЕр", "договорЕнность", "докумЕнт",
//        "досУг", "еретИк", "жалюзИ", "знАчимость", "Иксы", "каталОг",
//        "квартАл", "киломЕтр", "нет кОнусов", "корЫсть", "крАны", "кремЕнь",
//        "лЕкторы", "лЕкторов", "лыжнЯ", "мЕстностей", "мусоропровОд", "намЕрение",
//        "нарОст", "нЕдруг", "недУг", "некролОг", "нефтепровОд", "много новостЕй",
//        "нОгтя", "обеспЕчение", "отзЫв (посла)", "Отзыв (на книгу)", "Отрочество", "партЕр",
//        "портфЕль", "пОручни", "придАное", "призЫв", "процЕнт", "свЕкла",
//        "бедные сирОты", "созЫв", "собирать срЕдства", "стАтуя", "столЯр", "тамОжня",
//        "тОрты", "тОртов", "цемЕнт", "цепОчка", "шерстяные шАрфы", "шофЕр",
//        "щавЕль", "экспЕрт", "она вернА", "давнИшний", "знАчимый", "красИвее",
//        "красИвейший", "кУхонный", "она ловкА", "мозаИчный", "оптОвый", "она прозорлИва",
//        "слИвовый", "баловАть", "баловАться", "бралА", "бралАсь", "взялА",
//        "взялАсь", "включИть", "включИшь", "включИт", "включИм", "влилАсь",
//        "ворвалАсь", "воспринялА", "воссоздалА", "вручИть", "вручИшь", "вручИт",
//        "гналА", "гналАсь", "добралА", "добралАсь", "дождалАсь", "дозвонИться",
//        "он дозвонИтся", "они дозвонЯтся", "дозИровать", "ждалА", "жилОсь", "закУпорить",
//        "занЯть", "зАнял", "занялА", "зАняло", "зАняли", "заперлА",
//        "заперлАсь на ключ", "звалА", "звонИть", "звонИшь", "звонИт", "звонИм",
//        "исключИть", "исключИт", "клАла", "клЕить", "крАлась", "кренИться",
//        "кренИтся", "кровоточИть", "лгалА", "лилА", "лилАсь", "навралА",
//        "наделИть", "наделИт", "надорвалАсь", "назвалАсь", "накренИться", "накренИтся",
//        "налилА", "нарвалА", "насорИть", "насорИт", "начАть", "нАчал",
//        "началА", "нАчали", "обзвонИть", "обзвонИт", "облегчИть", "облегчИт",
//        "облилАсь", "обнялАсь", "обогналА", "ободралА", "ободрИть", "ободрИт",
//        "ободрИться", "ободрИшься", "обострИть", "одолжИть", "одолжИт", "озлОбить",
//        "оклЕить", "окружИть", "окружИт", "опломбировАть", "опОшлить", "опОшлят",
//        "освЕдомиться", "освЕдомишься", "отбылА", "отдалА", "откУпорить", "откУпорил",
//        "отозвалА", "отозвалАсь", "перезвонИть", "перезвонИт", "перелилА", "плодоносИть",
//        "повторИть", "повторИт", "позвалА", "позвонИть", "позвонИшь", "позвонИт",
//        "полилА", "положИть", "положИл", "понялА", "послАла", "премировАть",
//        "прибЫть", "прИбыл", "прибылА", "прИбыло", "принУдить", "принЯть",
//        "прИнял", "прИняли", "рвалА", "сверлИть", "сверлИшь", "сверлИт",
//        "снялА", "создалА", "сорвалА", "убралА", "убыстрИть", "углубИть",
//        "укрепИть", "укрепИт", "чЕрпать", "щемИть", "щемИт", "щЕлкать",
//        "балОванный", "включЕнный", "включЕн", "довезЕнный", "зАгнутый", "занятА",
//        "зАпертый", "запертА", "заселЕнный", "заселЕн", "заселенА", "избалОванный",
//        "изОгнутый", "кормЯщий", "кровоточАщий", "молЯщий", "наделЕнный", "нажИвший",
//        "налитА", "налИвший", "нанЯвшийся", "начАвший", "нАчатый", "низведЕнный",
//        "низведЕн", "ободренА", "ободрЕнный", "ободрЕн", "обострЕнный", "отключЕнный",
//        "отключЕн", "повторЕнный", "поделЕнный", "понЯвший", "прИнятый", "приручЕнный",
//        "приручЕн", "прожИвший", "разОгнутый", "снятА", "сОгнутый", "созданА",
//        "балУясь", "закУпорив", "исчЕрпав", "начАв", "начАвшись", "отдАв",
//        "поднЯв", "понЯв", "прибЫв", "вОвремя", "добелА", "докраснА",
//        "дОверху", "донЕльзя", "дОнизу", "дОсуха", "завИдно", "зАгодя",
//        "зАсветло", "зАтемно", "Исстари", "красИвее", "навЕрх", "надОлго",
//        "ненадОлго", "тОтчас")
//
//    /*нужно записать альтернативные буквы:
//    у - а
//    ю - я
//    я - ю, е
//    и - е
//    е - и
//    а - у, ю
//    */
//    private val suffixWords = arrayOf("брезжУщий", "зыблЮщийся", "зиждУщийся", "внемлЮщий",
//        "приемлЕмый", "неотъемлЕмый", "незыблЕмый", "движИмый – от «движити»",
//        "скоропортЯщиеся", "доносЯтся (звуки) — доносить, 2 спр",
//        "переловЯт (рыбаки) — ловить, 2 спр", "моЮщиеся — мыться, 1 спр",
//        "дорогостоЯщая — стоить 2 спр", "выкрикиваЮщая — выкрикивать, 1 спряжение",
//        "посылаЕмый — посылать, 1 спр", "нарушаЕт (водитель) — нарушать, 1 спр",
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
//
//
//    private val paronymWords = arrayOf(
//        "абонемент:абонемент в театр|абонемент на цикл лекций|межбиблиотечный абонемент|" +
//                "абонемент на кинофестиваль|абонемент в бассейн:абонент",
//        "абонент:абонент библиотеки|абонент телефонной сети|абонент временно недоступен:абонемент",
//        "абонентов:жалобы от абонентов:абонементов",
//        "артистический:артистический путь:артистичный",
//        "артистическая:артистическая среда|артистическая карьера|артистическая уборная:артистичная",
//        "артистическое:артистическое кафе:артистичное",
//        "артистичный:артистичный человек|артистичный до мозга костей:артистический",
//        "артистичное:артистичное исполнение:артистическое",
//        "бедная:бедная обстановка|бедная одежда:бедственная",
//        "бедный:бедный человек|бедный ужин|бедный дом|мой бедный мальчик:бедственный",
//        "бедственное:бедственное положение:бедное",
//        "безответная:безответная любовь:безответственная",
//        "безответственный:безответственный поступок:безответный",
//        "безответственное:безответственное лицо|безотвественное отношение|безответственное поведение:безответное",
//        "болотистые:болотистые земли:болотные",
//        "болотистая:болотистая местность|болотистая почва:болотная",
//        "болотные:болотные сапоги|болотные травы:болотистые",
//        "болотная:болотная птица:болотистая",
//        "болотный:болотный мох:болотистый",
//        "благодарная:благодарная тема:благодарственная",
//        "благодарный:благодарный пациент|благодарный слушатель|благодарный ученик|благодарный взгляд|благодарный материал:благодарственный",
//        "благодарственное:благодарственное письмо:благодарное",
//        "благодарственная:благодарственная телеграмма:благодарная",
//        "благодарственный:благодарственный молебен:благодарный",
//        "благотворительная:благотворительная акция|благотворительная лотерея:благотворная",
//        "благотворительный:благотворительный спектакль|благотворительный фонд:благотворный",
//        "благотворное:благотворное влияние:благотворительное",
//        "благотворная:благотворная прохлада|благотворная влага:благотворительная",
//        "бывшая:бывшая школа:былая",
//        "бывший:бывший клуб|бывший врач|бывший директор:былой",
//        "былые:былые годы:бывшие",
//        "былая:былая сила|былая печаль|былая слава:бывшая",
//        "былое:былое счастье |былое уважение:бывшее",
//        "былой:былой страх:бывший",
//        "вдох:глубокий вдох|сделать вдох|вдох всей грудью:вздох",
//        "вздох:вздох облегчения|тяжёлый вздох:вдох",
//        "вздохом:сказать со вздохом:вдохом",
//        "вековые:вековые дубы|вековые традиции|вековые обычаи:вечные",
//        "вековая:вековая роща|вековая пыль|вековая грязь:вечная",
//        "вечная:вечная проблема|вечная мерзлота:вековая",
//        "вечные:вечные человеческие ценности|вечные вопросы:вековые"
//    )
//
//    fun uploadWords(typeId: TaskType) {
//        if (isTableEmpty(typeId)) addWords(typeId)
//    }
//
//    private fun addWords(typeId: TaskType) {
//        val words = when(typeId) {
//            TaskType.Assent -> assentWords
//            TaskType.Suffix -> suffixWords
//            TaskType.Paronym -> paronymWords
//            TaskType.SteadyExpression ->
//        }
//
//
//        if (typeId != TaskType.Paronym)
//            quickSort(words, 0, words.size - 1)
//
//        for (word in words) {
//            val pos = if (typeId == TaskType.Paronym) 0
//            else findUpperChar(word)
//
//            if (pos < 0) return
//
//            val task = when (typeId) {
//                TaskType.Suffix -> Suffix(word.lowercase(Locale.getDefault()), pos, "")
//                TaskType.Paronym -> {
//                    val splitWord = word.split(":").toTypedArray()
//                    Paronym(splitWord)
//                }
//                else -> Assent(
//                    word = word.lowercase(Locale.getDefault()),
//                    position = pos,
//                    isChecked = false
//                )
//            }
//        }
//    }
//
//    //возвращает индекс прописной буквы в слове
//    private fun findUpperChar(word: String): Int {
//        var pos = -1
//        for (i in word.indices) if (Character.isUpperCase(word[i])) {
//            pos = i
//            break
//        }
//        return pos
//    }
//
//    //Возвращает позицию первой буквы слова в словосочетании
//
//    private fun findWordInPhrase(phrase: String, word: String): Int {
//        var pos = 0
//        phrase.split(" ").forEach {
//            if (it == word) return pos
//            pos += it.length + 1
//        }
//        return -1
//    }
//
//    //Возвращает рандомное слово для раздела теория
//    fun getRandomWord(type: TaskType, color: Int): Spannable {
//        uploadWords(type)
//        val index = random.nextInt(getWordCount(type))
//        if (type == TaskType.Paronym) {
//            val paronym = getWordFromTable(type, index) as Paronym? ?: return null
//            val phrase = paronym.randomVariant
//            return getPaintedParonym(phrase, paronym.word, color)
//        }
//        return getWordFromTable(type, index, color)
//    }
//
//    //Возвращает разные варианты написания слова/постановки ударения и т.д.
//    fun getRandomWords(typeId: TaskType, color: Int): ArrayList<Spannable> {
//        val mTask: Task?
//        val returns: ArrayList<Spannable?>
//        uploadWords(typeId)
//        val index = random.nextInt(getWordCount(typeId))
//        mTask = getWordFromTable(typeId, index)
//
//        if (typeId == AssentId)
//            return getRandomAssents(mTask as Assent, color)
//        else if (typeId == ParonymId) {
//            returns = ArrayList()
//            val str = getRightPhrase(mTask as Paronym, color)
//            returns.add(str)
//            returns.add(SpannableString(mTask.getWord()))
//            val list = mTask.getRandomAlternatives(3)
//            list.forEach {
//                returns.add(SpannableString(it))
//            }
//            return returns
//        }
//        return null
//    }
//
//    private fun getRandomAssents(assent: Assent, color: Int): ArrayList<Spannable> {
//        var cursorWrapper: TaskCursorWrapper
//        var index: Int
//
//        //обновление базы данных нужно доделать, а то она не обновляется :)
//        cursorWrapper = queryTasks(
//            AssentTable.NAME,
//            AssentTable.Cols.CHECKED + " = ?", arrayOf("0"))
//        if (cursorWrapper.count == 0) {
//            cursorWrapper = queryTasks(
//                AssentTable.NAME,
//                AssentTable.Cols.CHECKED + " = ?", arrayOf("1"))
//            cursorWrapper.moveToFirst()
//            while (!cursorWrapper.isAfterLast) {
//                cursorWrapper.assent.isChecked = false
//                updateTask(cursorWrapper.assent, AssentId)
//                cursorWrapper.moveToNext()
//            }
//        }
//        getRightText(assent)
//        var indexes = getIndexesOfLetters(
//            assent.word,
//            assent.position
//        )
//        while (assent.isChecked || indexes == null) {
//            index = random.nextInt(getWordCount(AssentId)) + 1
//            assent = getWordFromTable(AssentId, index) as Assent?
//            if (assent == null) break
//            getRightText(assent)
//            indexes = getIndexesOfLetters(
//                assent.word,
//                assent.position
//            )
//        }
//        assent.isChecked = true
//        updateTask(assent, AssentId)
//        val returns = ArrayList<Spannable?>()
//        for (i in indexes!!.indices) {
//            returns.add(getPaintedWord(
//                assent.word.lowercase(),
//                indexes[i],
//                color))
//        }
//        return returns
//    }
//
//    //возвращает очередное слово для библиотеки знаний
//    fun getNextWord(typeId: Int, position: Int, color: Int): Spannable {
//        uploadWords(typeId)
//        return getWordFromTable(typeId, position, color)
//    }
//
//    //Возвращает spannable, сделанный из phrase, в которой word покрашено в color
//    private fun getPaintedParonym(phrase: String, word: String, color: Int): SpannableString {
//        val pos = findWordInPhrase(phrase, word)
//        val string = SpannableString(phrase)
//        if (pos >= 0) string.setSpan(
//            ForegroundColorSpan(color),
//            pos,
//            pos + word.length + 1,
//            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        return string
//    }
//
//    //метод, который вместо слова ставит многоточие в фразе
//    private fun getRightPhrase(paronym: Paronym, color: Int): SpannableString {
//        val builder = SpannableStringBuilder()
//        val phrase = paronym.randomVariant.split(" ")
//        for (s in phrase) {
//            if (s == paronym.word) {
//                val string = SpannableString("...")
//                string.setSpan(
//                    ForegroundColorSpan(color),
//                    0,
//                    string.length,
//                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//                )
//                builder.append(string).append(" ")
//            } else builder.append(s).append(" ")
//        }
//        return SpannableString.valueOf(builder)
//    }
//
//    //возвращает вариант фразы на позиции position из паронима с индексом index_of_paronym
//    fun getParonymVariant(index_of_paronym: Int, position: Int, color: Int): SpannableString {
//        val paronym = getWordFromTable(ParonymId, index_of_paronym) as Paronym
//        val phrase = paronym.getVariant(position)
//        return getPaintedParonym(phrase, paronym.word, color)
//    }
//
//    //возвращает количество слов в таблице в зависимости от typeId
//    fun getWordCount(type: TaskType): Int {
//        val tableName = when (type) {
//            TaskType.Assent -> AssentTable.NAME
//            TaskType.Suffix -> SuffixTable.NAME
//            TaskType.Paronym -> ParonymTable.NAME
//        }
//        val cursor: CursorWrapper = queryTasks(tableName, null, null)
//        return cursor.count
//    }
//
//    fun getParonymPhraseCount(position: Int): Int {
//        val paronym = getWordFromTable(TaskType.Paronym, position) as Paronym
//        return paronym.getArrayVariants().size
//    }
//
//    //проверяет таблицу на пустоту
//    fun isTableEmpty(typeId: TaskType): Boolean {
//        return getWordCount(typeId) == 0
//    }
//
//    //возвращает слово, с прописной буквой на позиции pos (остальные буквы строчные)
//
//    fun toUpperCase(task: Task) {
//        if (task.position < 0 || task.position >= task.word.length) return
//
//        val builder = StringBuilder(task.word)
//
//        builder[task.position] = task.word[task.position].uppercaseChar()
//        task.word = java.lang.String.join("", *text)
//    }
//
//    /**
//     * @param type - определяет таблицу, из которой берутся слова
//     * @param index - индекс в таблице, по которому определяется слово (индекс может быть равным 0)
//     * @param color - цвет прописной буквы
//     * @return возвращает слово из таблицы
//     */
//    private fun getWordFromTable(type: TaskType, index: Int, color: Int): Spannable {
//        val task = getWordFromTable(type, index)
//
//        return if (type == TaskType.Paronym)
//            SpannableString(task.word)
//        else
//            getPaintedWord(task.word, task.position, color) ?: SpannableString(task.word)
//    }
//
//    //индекс должен быть >= 0
//    private fun getWordFromTable(type: TaskType, index: Int): Task {
//        val task: Task
//        val cursorWrapper: TaskCursorWrapper
//        var tableName = AssentTable.NAME
//        var id = AssentTable.Cols.ID
//        when (type) {
//            TaskType.Assent -> {
//                tableName = AssentTable.NAME
//                id = AssentTable.Cols.ID
//            }
//            TaskType.Suffix -> {
//                tableName = SuffixTable.NAME
//                id = SuffixTable.Cols.ID
//            }
//            TaskType.Paronym -> {
//                tableName = ParonymTable.NAME
//                id = ParonymTable.Cols.ID
//            }
//        }
//        cursorWrapper = queryTasks(
//            tableName,
//            "$id = ?", arrayOf((index + 1).toString()))
//        if (cursorWrapper.count == 0) return null
//        cursorWrapper.moveToFirst()
//        task = cursorWrapper.getTask(type)
//        toUpperCase(task)
//        cursorWrapper.close()
//        return task
//    }
//
//    //возвращает слово с буквой на позиции position цвета charColor
//    fun getPaintedWord(word: String, position: Int?, charColor: Int): Spannable? {
//        if (position == null) position = findUpperChar(word)
//        if (position < 0) return null
//        val spannable: Spannable = SpannableString(word)
//        spannable.setSpan(
//            ForegroundColorSpan(charColor),
//            position,
//            position + 1,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        return spannable
//    }
//
//    //находит слово в таблице по заданному
//    fun findWordInTable(typeId: TaskType, word: String, color: Int): List<Spannable> {
//        var task: Task
//        var tableName = AssentTable.NAME
//        var wordColumn = AssentTable.Cols.WORD
//        val results = ArrayList<Spannable>()
//
//        if (word.isEmpty()) return results
//        when (typeId) {
//            AssentId -> {
//                tableName = AssentTable.NAME
//                wordColumn = AssentTable.Cols.WORD
//            }
//            SuffixId -> {
//                tableName = SuffixTable.NAME
//                wordColumn = SuffixTable.Cols.WORD
//            }
//            ParonymId -> {
//                tableName = ParonymTable.NAME
//                wordColumn = ParonymTable.Cols.WORD
//            }
//        }
//        cursorWrapper = queryTasks(
//            tableName,
//            "$wordColumn = ?", arrayOf(word))
//
//        if (cursorWrapper.count == 0) {
//            cursorWrapper = queryTasks(tableName, null, null)
//            cursorWrapper.moveToFirst()
//            while (!cursorWrapper.isAfterLast) {
//                task = cursorWrapper.getTask(typeId)
//                getRightText(task)
//                val diff = diffOfStrings(task.word, word)
//                if (task.word.length == word.length && diff <= 3 ||
//                    diff <= 1
//                ) {
//                    toUpperCase(task)
//                    results.add(getPaintedWord(task.word, task.position, color))
//                }
//                cursorWrapper.moveToNext()
//            }
//        } else {
//            cursorWrapper.moveToFirst()
//            while (!cursorWrapper.isAfterLast) {
//                task = cursorWrapper.getTask(typeId)
//                toUpperCase(task)
//                results.add(getPaintedWord(task.word, task.position, color))
//                cursorWrapper.moveToNext()
//            }
//        }
//        cursorWrapper.close()
//        return results
//    }
//
//    //алгоритм быстрой сортировки слов
//    private fun quickSort(array: Array<String>?, low: Int, high: Int) {
//        if (array == null) return
//        if (array.size == 0) return
//        if (low >= high) return
//        val middle = (low + high) / 2
//        val support = getRightText(array[middle])
//        var i = low
//        var j = high
//        while (i <= j) {
//            while (support!!.compareTo(getRightText(array[i])!!, ignoreCase = true) > 0) i++
//            while (support.compareTo(getRightText(array[j])!!, ignoreCase = true) < 0) j--
//            if (i <= j) {
//                val temp = array[i]
//                array[i] = array[j]
//                array[j] = temp
//                i++
//                j--
//            }
//        }
//        if (low < j) quickSort(array, low, j)
//        if (high > i) quickSort(array, i, high)
//    }
//
//    //считает разницу между строками
//    private fun diffOfStrings(first_string: String, second_string: String): Int {
//        var diff = 0
//        val minLen = first_string.length.coerceAtMost(second_string.length)
//
//        for (i in 0 until minLen) {
//            if (first_string[i] != second_string[i]) diff++
//        }
//        return diff
//    }
//
//    //возвращает массив, содержащий индексы гласных букв
//    private fun getIndexesOfLetters(word: String, indexOfRightLetter: Int): List<Int?>? {
//        var indexes: MutableList<Int?> = ArrayList()
//        if (word.contains("отзыв")) {
//            indexes.add(0)
//            indexes.add(3)
//            return indexes
//        }
//        for (elem in word.indices) {
//            if (word.substring(elem, elem + 1).matches(
//                    Regex("^(?ui:[аеёиоуыэюя]).*")) &&
//                elem != indexOfRightLetter
//            ) indexes.add(elem)
//        }
//        indexes.shuffle()
//        indexes.add(0, indexOfRightLetter)
//        if (indexes.size < 2) return null else if (indexes.size > 4) indexes = indexes.subList(0, 4)
//        return indexes
//    }
//
//    //удаляет в словосочетании слова без прописных букв
//    private fun getRightText(task: Task) {
//        val text = task.word.split(" ")
//
//        if (text.contains("отзыв") || text.size == 1) return
//        var len = 0
//        for (word in text) {
//            if (len + word.length > task.position) {
//                task.word = word
//                task.position -= len
//                break
//            }
//            len += word.length + 1
//        }
//    }
//
//    //тоже самое, что и выше, но без объекта класса Task
//    private fun getRightText(text: String): String {
//        val pos = findUpperChar(text)
//        if (pos < 0) return text
//        val task = Task(text, pos)
//        getRightText(task)
//        return task.word
//    }
//}