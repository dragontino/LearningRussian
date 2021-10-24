package com.learning.ruslan;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.Toast;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;

import com.learning.ruslan.databases.RusBaseHelper;
import com.learning.ruslan.databases.RusDbSchema.AssentTable;
import com.learning.ruslan.databases.RusDbSchema.ParonymTable;
import com.learning.ruslan.databases.RusDbSchema.SuffixTable;
import com.learning.ruslan.databases.TaskCursorWrapper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Word {

    public static final int AssentId = 10;
    public static final int SuffixId = 11;
    public static final int ParonymId = 12;

    private static final String TAG = "Word";

    public static final ArrayList<Integer> SUBJECTS =
            new ArrayList<>(Arrays.asList(AssentId, SuffixId, ParonymId));

    public static final int[] TITLES = new int[] {
            R.string.assent_label,
            R.string.suffix_label,
            R.string.paronym_label
    };

    private final Random mRandom;
    private final Context mContext;
    private final SQLiteDatabase mDatabase;
    private final String[] AssentWords = new String[]{"аэропОрты", "бАнты", "сбрить бОроду", "много бухгАлтеров",
            "вероисповЕдание", "газопровОд",
            "граждАнство", "дефИс", "дешевИзна", "диспансЕр", "договорЕнность", "докумЕнт",
            "досУг", "еретИк", "жалюзИ", "знАчимость", "Иксы", "каталОг",
            "квартАл", "киломЕтр", "нет кОнусов", "корЫсть", "крАны", "кремЕнь",
            "лЕкторы", "лЕкторов", "лыжнЯ", "мЕстностей", "мусоропровОд", "намЕрение",
            "нарОст", "нЕдруг", "недУг", "некролОг", "нефтепровОд", "много новостЕй",
            "нОгтя", "обеспЕчение", "отзЫв (посла)", "Отзыв (на книгу)", "Отрочество", "партЕр",
            "портфЕль", "пОручни", "придАное", "призЫв", "процЕнт", "свЕкла",
            "бедные сирОты", "созЫв", "собирать срЕдства", "стАтуя", "столЯр", "тамОжня",
            "тОрты", "тОртов", "цемЕнт", "цепОчка", "шерстяные шАрфы", "шофЕр",
            "щавЕль", "экспЕрт", "она вернА", "давнИшний", "знАчимый", "красИвее",
            "красИвейший", "кУхонный", "она ловкА", "мозаИчный", "оптОвый", "она прозорлИва",
            "слИвовый", "баловАть", "баловАться", "бралА", "бралАсь", "взялА",
            "взялАсь", "включИть", "включИшь", "включИт", "включИм", "влилАсь",
            "ворвалАсь", "воспринялА", "воссоздалА", "вручИть", "вручИшь", "вручИт",
            "гналА", "гналАсь", "добралА", "добралАсь", "дождалАсь", "дозвонИться",
            "он дозвонИтся", "они дозвонЯтся", "дозИровать", "ждалА", "жилОсь", "закУпорить",
            "занЯть", "зАнял", "занялА", "зАняло", "зАняли", "заперлА",
            "заперлАсь на ключ", "звалА", "звонИть", "звонИшь", "звонИт", "звонИм",
            "исключИть", "исключИт", "клАла", "клЕить", "крАлась", "кренИться",
            "кренИтся", "кровоточИть", "лгалА", "лилА", "лилАсь", "навралА",
            "наделИть", "наделИт", "надорвалАсь", "назвалАсь", "накренИться", "накренИтся",
            "налилА", "нарвалА", "насорИть", "насорИт", "начАть", "нАчал",
            "началА", "нАчали", "обзвонИть", "обзвонИт", "облегчИть", "облегчИт",
            "облилАсь", "обнялАсь", "обогналА", "ободралА", "ободрИть", "ободрИт",
            "ободрИться", "ободрИшься", "обострИть", "одолжИть", "одолжИт", "озлОбить",
            "оклЕить", "окружИть", "окружИт", "опломбировАть", "опОшлить", "опОшлят",
            "освЕдомиться", "освЕдомишься", "отбылА", "отдалА", "откУпорить", "откУпорил",
            "отозвалА", "отозвалАсь", "перезвонИть", "перезвонИт", "перелилА", "плодоносИть",
            "повторИть", "повторИт", "позвалА", "позвонИть", "позвонИшь", "позвонИт",
            "полилА", "положИть", "положИл", "понялА", "послАла", "премировАть",
            "прибЫть", "прИбыл", "прибылА", "прИбыло", "принУдить", "принЯть",
            "прИнял", "прИняли", "рвалА", "сверлИть", "сверлИшь", "сверлИт",
            "снялА", "создалА", "сорвалА", "убралА", "убыстрИть", "углубИть",
            "укрепИть", "укрепИт", "чЕрпать", "щемИть", "щемИт", "щЕлкать",
            "балОванный", "включЕнный", "включЕн", "довезЕнный", "зАгнутый", "занятА",
            "зАпертый", "запертА", "заселЕнный", "заселЕн", "заселенА", "избалОванный",
            "изОгнутый", "кормЯщий", "кровоточАщий", "молЯщий", "наделЕнный", "нажИвший",
            "налитА", "налИвший", "нанЯвшийся", "начАвший", "нАчатый", "низведЕнный",
            "низведЕн", "ободренА", "ободрЕнный", "ободрЕн", "обострЕнный", "отключЕнный",
            "отключЕн", "повторЕнный", "поделЕнный", "понЯвший", "прИнятый", "приручЕнный",
            "приручЕн", "прожИвший", "разОгнутый", "снятА", "сОгнутый", "созданА",
            "балУясь", "закУпорив", "исчЕрпав", "начАв", "начАвшись", "отдАв",
            "поднЯв", "понЯв", "прибЫв", "вОвремя", "добелА", "докраснА",
            "дОверху", "донЕльзя", "дОнизу", "дОсуха", "завИдно", "зАгодя",
            "зАсветло", "зАтемно", "Исстари", "красИвее", "навЕрх", "надОлго",
            "ненадОлго", "тОтчас"};

    /*нужно записать альтернативные буквы:
    у - а
    ю - я
    я - ю, е
    и - е
    е - и
    а - у, ю
    */


    private final String [] SuffixWords = new String[]{"брезжУщий", "зыблЮщийся", "зиждУщийся", "внемлЮщий",
            "приемлЕмый", "неотъемлЕмый", "незыблЕмый", "движИмый – от «движити»",
            "скоропортЯщиеся", "доносЯтся (звуки) — доносить, 2 спр",
            "переловЯт (рыбаки) — ловить, 2 спр", "моЮщиеся — мыться, 1 спр",
            "дорогостоЯщая — стоить 2 спр", "выкрикиваЮщая — выкрикивать, 1 спряжение",
            "посылаЕмый — посылать, 1 спр", "нарушаЕт (водитель) — нарушать, 1 спр",
            "оклеиваЕмый — оклеивать, 2 спр", "(дуб) спилИтся — спилиться, 2 спр",
            "борЕтся (народ) — бороться, 1 спр", "усвоИшь — усвоить, 2 спр",
            "вышлЕт (бабушка) — выслать, 1 спр", "бросаЕшь — бросать, 1 спр",
            "красИт (маляр) — красить, 2 спр", "встретИшь — встретить, 2 спр",
            "внемлЮщий — внимать, 1 спр", "пригреваЕмый — проигрывать, 1 спр",
            "капризничаЕт — капризничать, 1 спр", "независИмый — зависеть, 2 спр, искл",
            "строИтся (дом) — строиться, 2 спр", "подозреваЕмый — подозревать, 1 спр",
            "трудЯтся (электрики) — трудиться, 2 спр", "бросаЮт (игроки) — бросать, 1 спр",
            "лелеЮщая — лелеять, 1 спр", "парЯт (облака) — парить, 2 спр",
            "катИтся (шар) — катИлся, 2 спр", "видИмый — видеть, 2 спр, искл",
            "летЯщий — лететь, ударение", "поставЯт (архитекторы) — поставить, 2 спр",
            "плачЕт (малыш) — плакать, 1 спр", "бросИшь — бросить, 2 спр",
            "волнуЮщийся — волноваться, 1 спр", "усвоЯт (ученики) — усвоить, 2 спр",
            "кудахчЕт (курица) — кудахтать, 1 спр", "прогреваЕмое — прогревать, 1 спр",
            "брюзжАщий — брюзжать, 1 спр", "запеваЮт (пионеры) — запевать, 1 спр",
            "(меня) тревожАт (мысли) — тревожить, 2 спр", "дышАщий — дышать, 2 спр, искл",
            "скачУщий — скакать, 1 спр", "ненавидЯщий — ненавидеть, 2 спр, искл",
            "слышАщий — слышать, 2 спр, искл", "дружАт (соседи) — дружить, 2 спр",
            "реЕт (флаг) — реть, 1 спр", "зреЕт (зерно) — зреть, 1 спр", "кружАщий — кружить, 2 спр",
            "заносЯт (грузчики) — заносить, 2 спр", "сушИт — сушить, 2 спр", "рисуЮт — рисовать, 1 спр",
            "наклеИвший — наклеить, 2 спр", "предъявИт (прокурор) — предъявить, 2 спр",
            "рассмотрИшь — смотреть, 2 спр, искл", "вынуждаЕшь — вынуждать, 1 спр",
            "задремлЕшь — задремать, 1 спр", "задрапируЕшь — задрапировать, 1 спр",
            "зависИмый — зависеть, 2 спр, искл", "нянчАщая — нянчить, 2 спр",
            "произносИшь — прозносить, 2 спр", "рокочЕт (мотор) — рокотать, 1 спр",
            "преобразуЕмый — преобразовать, 1 спр", "(боец) служИт — служить, 2 спр",
            "дремлЕшь — дремать, 1 спр", "лечИшь — лечить, 2 спр", "перекинЕшь — перекинуть, 1 спр",
            "вычитаЕмые — вычитать, 1 спр", "сломлЕнный — сломить, 2 спр",
            "(он) разыгрываЕт — разыгрывать, 1 спр", "(страны) граничАт — граничить, 2 спр",
            "шепчУщий (на ухо) — шептать, 1 спр", "строИтся (школа) — строить, 2 спр",
            "засмотрИшься — смотреть, 2 спр, искл", "дышАщий — дышать, 2 спр, искл",
            "щекочУщий — щекотать, 1 спр", "вышлЕт — выслать, 1 спр",
            "приходИтся (братом) — приходиться, 2 спр", "лечАщий — лечить, 2 спр, искл", "знаЮщий",
            "трудЯтся (рабочие)", "выстоЯвшая (армия)", "колеблЕшься", "выделИшь", "независИмый",
            "мыслИмый", "нарушаЕмый", "предполагаЕмый", "невидИмый", "непромокаЕмый", "произносИшь",
            "щиплЕшь", "замечЕнный", "смеркаЕтся", "(тени) мерещУтся", "(печка) топИтся",
            "(родители) отпустЯт", "(друзья) выручАт", "движИмый", "(крот) роЕт",
            "сверИвший (накануне)", "нарушИвший (права)", "закручиваЕмый", "(земля) вертИтся",
            "зависИмый", "(птичка) вылетИт", "задержИшься", "оперируЕмый — оперировать, 1 спр",
            "выгорИшь — горит, 2 спр, ударение", "раскручЕнный— раскрутить, 2 спр",
            "предвидИшь", "(печка) топИтся", "построИшь", "(можно) надеЯться", "задержАвшийся",
            "радуЮщийся", "расклеИшь", "наметИть (план)", "прополЕшь", "упрощаЕшь", "(он) расстелЕт",
            "заправИшь", "(они) мелЮт (зерно)", "ссорЯщийся,", "не успокоЯтся (активисты)",
            "(она) доедаЕт", "чистЯщий", "поражаЮщий", "навеЕт (ветер)",
            "посвящаЕтся (стихотворение)", "дорогостоЯщая", "подводЯщая", "пенЯщееся", "навеЯть",
            "ненавидЯщий", "мучАвшийся", "(матросы) задраЯт", "разбавИвший", "маЮщийся",
            "вываливаЮщийся", "(враг) не воротИтся", "отчаЯнный", "затеЯла", "выздоровЕть", "отчаЯться",
            "развеЯв - развеЯть", "(друг) уступИт", "(слово) молвИтся", "(учитель) проверИт",
            "ляжЕшь (пораньше)", "пенЯщееся", "разбавлЕнный", "(они) стелЮт (скатерть)",
            "овеЯнный", "верИвший", "вылетИшь", "мелЮщий (кофе)", "(огородники) полЮт (грядки)",
            "выздоровЕвший (юноша)", "выспИшься", "скачУщий (конь) — скакать, 1 спр",
            "(они) дышАт — дышать, 2 спр, искл", "трепещЕм (от страха) — трепещать, 1 спр",
            "колеблЕмые (ветром) — колебать, 1 спр", "поношЕнное (платье) — поносить, 2 спр",
            "зависИмый — зависеть, 2 спр, искл", "потерЯнный (багаж) — потерять, 1 спр",
            "(флаги) реЮт", "взлелеЯвший", "занимаЮщаяся (заря)", "выгорИшь", "расстроИвшись",
            "предвидЕвший", "незыблЕмый", "пышУщий (здоровьем)", "(они) выложАт", "противоречАщий",
            "выслушАвший", "обездвижЕнный – от «движити»", "натерпИшься", "щебечУщий",
            "(они) клокочУт", "умоЕшься", "видИмый", "клеИшь", "будоражИвший (воображение)",
            "расстроИвшись", "повадИшься", "колышУщиеся (травы)", "(они) лечАт", "взлелеЯвший",
            "верЯщий (на слово)", "немыслЕмый", "застроЕнный", "таЮщие (снега)", "(они) видЯтся",
            "(вы) устанЕте", "колеблЕмый", "выскочИм", "(вы) вытерпИте", "(они) обидЯтся", "повеЯвший",
            "дремлЮщий", "(они) гонЯтся (друг за другом)", "установлЕнный", "неуправляЕмый",
            "(они) шепчУтся", "машУщий (крыльями)", "(вы) останЕтесь", "встретИвшись", "настоЯнный",
            "лелеЕмый", "(пациенты) лечАтся", "маячАщий (вдали лес)", "зависИшь", "подстрижЕнный",
            "борЕшься", "воспеваЕмый", "проедЕшься", "необитаЕмый", "вертИшься", "обнаружЕнный",
            "дорогостоЯщий", "вылинЯвшая (ткань)", "(ничего не) значАщий", "(автомобили) движУтся",
            "выплачиваЕмый", "обезумЕвший (от страха)", "овеЯнный (прохладой)", "настроИв (радио)",
            "(кони) бродЯт (по лугу)", "дремлЮщий (в поезде)", "вскочИшь", "(вы) портИте (изделие)",
            "выкачЕнный (из гаража велосипед)", "грохочУщий", "устроИвший (праздник)",
            "мучИмый (сомнениями)", "(он) клевещЕт", "движИмый", "тешУщий (себя надеждами)",
            "обвенчАнные", "(вы слишком) торопИтесь", "посеЕшь (пшеницу)", "скроЕнный (костюм)",
            "контролируЕмая (ситуация)", "выслушАнный (ответ)", "машУщий (руками)",
            "молЯщийся (в церкви)", "рассеЯвший (сомнения)", "(они) высмеЮт (зло)", "орошаЮщий (землю)",
            "колеблЕмый (волной)", "правИвший (лошадьми)", "проверЯвший (ответы)", "(всадник) скачЕт",
            "обтекаЕмая (форма)", "(дворник) выметЕт (мусор)", "служАщий (делу)",
            "задержАнный (преступник)", "завещАнный (дом)", "распорЕшь (швы)", "уполномочЕнный",
            "слышИмый", "опасаЕшься", "потратИвший"};

    private final String[] ParonymWords = new String[] {
            "абонемент:абонемент в театр|абонемент на цикл лекций|межбиблиотечный абонемент|" +
                    "абонемент на кинофестиваль|абонемент в бассейн:абонент",
            "абонент:абонент библиотеки|абонент телефонной сети|абонент временно недоступен:абонемент",
            "абонентов:жалобы от абонентов:абонементов"
    };

    private static Word sWord;

    public static Word get(Context context) {
        if (sWord == null)
            sWord = new Word(context.getApplicationContext());

        return sWord;
    }

    @StringRes
    public static int getTitle(int typeId) {
        switch (typeId) {
            case SuffixId:
                return TITLES[1];
            case ParonymId:
                return TITLES[2];
            default:
                return TITLES[0];
        }
    }

    private Word(Context context) {
        mContext = context.getApplicationContext();
        this.mRandom = new Random();
        this.mDatabase = new RusBaseHelper(mContext).getWritableDatabase();
    }

    public void uploadWords(int typeId) {
        if (isTableEmpty(typeId))
            addWords(typeId);
    }

    private void addWords(int typeId) {

        String [] words = AssentWords;
        String NAME = null;

        switch (typeId) {
            case AssentId:
                words = AssentWords;
                NAME = AssentTable.NAME;
                break;
            case SuffixId:
                words = SuffixWords;
                NAME = SuffixTable.NAME;
                break;
            case ParonymId:
                words = ParonymWords;
                NAME = ParonymTable.NAME;
                break;
        }

        if (typeId != ParonymId)
            quickSort(words, 0, words.length - 1);


        for (String word: words) {
            int pos;
            if (typeId == ParonymId)
                pos = 0;
            else
                pos = findUpperChar(word);

            if (pos < 0) return;

            ContentValues values;
            Task task;

            switch (typeId) {
                case SuffixId:
                    task = new Suffix(word.toLowerCase(), pos, "");
                    break;
                case ParonymId:
                    String[] splitWord = word.split(":");
                    task = new Paronym(splitWord);
                    break;
                default:
                    task = new Assent(word.toLowerCase(), pos, false);
                    break;
            }

            values = getContentValues(task, typeId);
            mDatabase.insert(NAME, null, values);
        }
    }

    //возвращает индекс прописной буквы в слове
    private int findUpperChar(String word) {
        int pos = -1;

        for (int i = 0; i < word.length(); i++)
            if (Character.isUpperCase(word.charAt(i))) {
                pos = i;
                break;
            }
        return pos;
    }

    //Возвращает позицию первой буквы слова в словосочетании
    private int findWordInPhrase(String phrase, String word) {
        int pos = 0;

        for (String s : phrase.split(" ")) {
            if (s.equals(word)) return pos;
            pos += s.length();
        }
        return -1;
    }

    @Nullable
    //Возвращает рандомное слово для раздела теория
    public Spannable getRandomWord(int typeId, int color) {
        int index;

        uploadWords(typeId);

        index = mRandom.nextInt(getWordCount(typeId));

        if (typeId == ParonymId) {
            Paronym paronym = (Paronym) getWordFromTable(typeId, index);
            if (paronym == null) return null;

            String phrase = paronym.getRandomVariant();
            return getPaintedParonym(phrase, paronym.getWord(), color);
        }
        return getWordFromTable(typeId, index, color);
    }


    //Возвращает разные варианты написания слова/постановки ударения и т.д.
    @Nullable
    public ArrayList<Spannable> getRandomWords(int typeId, int color) {
        Task mTask;
        ArrayList<Spannable> returns;

        uploadWords(typeId);

        int index = mRandom.nextInt(getWordCount(typeId));
        mTask = getWordFromTable(typeId, index);
        if (mTask == null) return null;

        if (typeId == AssentId) return getRandomAssents((Assent) mTask, color);
        else if (typeId == ParonymId) {
            returns = new ArrayList<>();
            SpannableString str = getRightPhrase((Paronym) mTask, color);
            returns.add(str);
            returns.add(new SpannableString(mTask.getWord()));

            List<String> list = ((Paronym) mTask).getRandomAlternatives(3);
            for (int elem = 0; elem < list.size(); elem++)
                returns.add(new SpannableString(list.get(elem)));
            return returns;
        }
        return null;
    }


    private ArrayList<Spannable> getRandomAssents(Assent assent, int color) {
        TaskCursorWrapper cursorWrapper;
        ArrayList<Spannable> returns;
        int index;

        //обновление базы данных нужно доделать, а то она не обновляется :)
        cursorWrapper = queryTasks(
                AssentTable.NAME,
                AssentTable.Cols.CHECKED + " = ?",
                new String[]{"0"}
        );

        if (cursorWrapper.getCount() == 0) {
            cursorWrapper = queryTasks(
                    AssentTable.NAME,
                    AssentTable.Cols.CHECKED + " = ?",
                    new String[]{"1"}
            );

            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                cursorWrapper.getAssent().setChecked(false);
                updateTask(cursorWrapper.getAssent(), AssentId);
                cursorWrapper.moveToNext();
            }
        }


        getRightText(assent);
        List<Integer> indexes = getIndexesOfLetters(
                assent.getWord(),
                assent.getPosition()
        );

        while (assent.isChecked() || indexes == null) {
            index = mRandom.nextInt(getWordCount(AssentId)) + 1;
            assent = (Assent) getWordFromTable(AssentId, index);
            if (assent == null) break;

            getRightText(assent);
            indexes = getIndexesOfLetters(
                    assent.getWord(),
                    assent.getPosition()
            );
        }

        assent.setChecked(true);
        updateTask(assent, AssentId);

        returns = new ArrayList<>();

        for (int i = 0; i < indexes.size(); i++) {
            returns.add(getPaintedWord(
                    assent.getWord().toLowerCase(),
                    indexes.get(i),
                    color));
        }

        return returns;
    }

    //возвращает очередное слово для библиотеки знаний
    public Spannable getNextWord(int typeId, int position, int color) {

        uploadWords(typeId);
        return getWordFromTable(typeId, position, color);
    }


    //Возвращает spannable, сделанный из phrase, в которой word покрашено в color
    @NonNull
    private SpannableString getPaintedParonym(String phrase, String word, int color) {
        int pos = findWordInPhrase(phrase, word);

        SpannableString string = new SpannableString(phrase);
        if (pos >= 0)
            string.setSpan(
                    new ForegroundColorSpan(color),
                    pos,
                    pos + word.length() + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        return string;
    }

    //метод, который вместо слова ставит многоточие в фразе
    @NotNull
    private SpannableString getRightPhrase(Paronym paronym, int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String[] phrase = paronym.getRandomVariant().split(" ");
        for (String s : phrase) {
            if (s.equals(paronym.getWord())) {
                SpannableString string = new SpannableString("...");
                string.setSpan(
                        new ForegroundColorSpan(color),
                        0,
                        string.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
                builder.append(string).append(" ");
            }
            else builder.append(s).append(" ");
        }

        return SpannableString.valueOf(builder);
    }

    //возвращает вариант фразы на позиции position из паронима с индексом index_of_paronym
    @Nullable
    public SpannableString getParonymVariant(int index_of_paronym, int position, int color) {
        Paronym paronym = (Paronym) getWordFromTable(ParonymId, index_of_paronym);
        if (paronym == null) return null;

        String phrase = paronym.getVariant(position);
        return getPaintedParonym(phrase, paronym.getWord(), color);
    }

    //возвращает количество слов в таблице в зависимости от typeId
    public int getWordCount(int typeId) {
        String tableName;

        switch (typeId) {
            case SuffixId:
                tableName = SuffixTable.NAME;
                break;
            case ParonymId:
                tableName = ParonymTable.NAME;
                break;
            default:
                tableName = AssentTable.NAME;
                break;
        }

        CursorWrapper cursor = queryTasks(tableName, null, null);
        return cursor.getCount();
    }

    public int getParonymPhraseCount(int position) {
        Paronym paronym = (Paronym) getWordFromTable(ParonymId, position);
        if (paronym == null) return 0;

        return paronym.getVariants().length;
    }

    //проверяет таблицу на пустоту
    public boolean isTableEmpty(int typeId) {
        return getWordCount(typeId) == 0;
    }


    //возвращает слово, с прописной буквой на позиции pos (остальные буквы строчные)
    @SuppressLint("NewApi")
    public void toUpperCase(Task task) {
        if (task.getPosition() < 0 || task.getPosition() >= task.getWord().length()) return;

        String[] text = task.getWord().split("");
        text[task.getPosition()] = text[task.getPosition()].toUpperCase();

        task.setWord(String.join("", text));
    }


    /**
     * @param typeId - определяет таблицу, из которой берутся слова
     * @param index - индекс в таблице, по которому определяется слово (индекс может быть равным 0)
     * @param color - цвет прописной буквы
     * @return возвращает слово из таблицы
     **/

    @Nullable
    private Spannable getWordFromTable(int typeId, int index, int color) {

        Task task = getWordFromTable(typeId, index);
        if (task == null) return null;

        if (typeId == ParonymId)
            return new SpannableString(task.getWord());

        return getPaintedWord(task.getWord(), task.getPosition(), color);
    }

    @Nullable
    //индекс должен быть >= 0
    private Task getWordFromTable(int typeId, int index) {
        Task task;
        TaskCursorWrapper cursorWrapper;
        String tableName = AssentTable.NAME;
        String id = AssentTable.Cols.ID;

        switch (typeId) {
            case AssentId:
                tableName = AssentTable.NAME;
                id = AssentTable.Cols.ID;
                break;
            case SuffixId:
                tableName = SuffixTable.NAME;
                id = SuffixTable.Cols.ID;
                break;
            case ParonymId:
                tableName = ParonymTable.NAME;
                id = ParonymTable.Cols.ID;
                break;
        }

        cursorWrapper = queryTasks(
                tableName,
                id + " = ?",
                new String[] { String.valueOf(index + 1) }
        );

        if (cursorWrapper.getCount() == 0)
            return null;

        cursorWrapper.moveToFirst();
        task = cursorWrapper.getTask(typeId);
        toUpperCase(task);
        cursorWrapper.close();

        return task;
    }

    //возвращает слово с буквой на позиции position цвета charColor
    @Nullable
    public Spannable getPaintedWord(String word, Integer position, int charColor) {
        if (position == null)
            position = findUpperChar(word);

        if (position < 0) return null;

        Spannable spannable = new SpannableString(word);
        spannable.setSpan(
                new ForegroundColorSpan(charColor),
                position,
                position + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        return spannable;
    }

    //находит слово в таблице по заданному
    @Nullable
    public List<Spannable> findWordInTable(int typeId, String word, int color) {
        Task task;
        TaskCursorWrapper cursorWrapper;
        String tableName = AssentTable.NAME;
        String wordColumn = AssentTable.Cols.WORD;
        List<Spannable> results = new ArrayList<>();

        if (word.length() == 0)
            return results;

        switch (typeId) {
            case AssentId:
                tableName = AssentTable.NAME;
                wordColumn = AssentTable.Cols.WORD;
                break;
            case SuffixId:
                tableName = SuffixTable.NAME;
                wordColumn = SuffixTable.Cols.WORD;
                break;
            case ParonymId:
                tableName = ParonymTable.NAME;
                wordColumn = ParonymTable.Cols.WORD;
                break;
        }

        cursorWrapper = queryTasks(
                tableName,
                wordColumn + " = ?",
                new String[]{word}
        );

        if (cursorWrapper.getCount() == 0) {
            cursorWrapper = queryTasks(tableName, null, null);

            cursorWrapper.moveToFirst();

            while (!cursorWrapper.isAfterLast()) {
                task = cursorWrapper.getTask(typeId);
                getRightText(task);
                int diff = diffOfStrings(task.getWord(), word);

                if (task.getWord().length() == word.length() && diff <= 3 ||
                        diff <= 1) {
                    toUpperCase(task);
                    results.add(getPaintedWord(task.getWord(), task.getPosition(), color));
                }
                cursorWrapper.moveToNext();
            }
        }
        else {
            cursorWrapper.moveToFirst();

            while (!cursorWrapper.isAfterLast()) {
                task = cursorWrapper.getTask(typeId);
                toUpperCase(task);
                results.add(getPaintedWord(task.getWord(), task.getPosition(), color));
                cursorWrapper.moveToNext();
            }

        }
        cursorWrapper.close();

        return results;
    }


    //алгоритм быстрой сортировки слов
    private void quickSort(String[] array, int low, int high) {

        if (array == null) return;

        if (array.length == 0) return;

        if (low >= high) return;

        int middle = (low + high) / 2;
        String support = getRightText(array[middle]);

        int i = low, j = high;

        while (i <= j) {
            while (support.compareToIgnoreCase(getRightText(array[i])) > 0)
                i++;

            while (support.compareToIgnoreCase(getRightText(array[j])) < 0)
                j--;

            if (i <= j) {
                String temp = array[i];
                array[i] = array[j];
                array[j] = temp;

                i++;
                j--;
            }
        }

        if (low < j)
            quickSort(array, low, j);

        if (high > i)
            quickSort(array, i, high);
    }

    //считает разницу между строками
    private int diffOfStrings(String first_string, String second_string) {
        int diff = 0;
        int min_len = Math.min(first_string.length(), second_string.length());

        for (int i = 0; i < min_len; i++) {
            if (first_string.charAt(i) != second_string.charAt(i))
                diff++;
        }

        return diff;
    }

    //возвращает массив, содержащий индексы гласных букв
    @Nullable
    private List<Integer> getIndexesOfLetters(String word, int indexOfRightLetter) {
        List<Integer> indexes = new ArrayList<>();

        if (word.contains("отзыв")) {
            indexes.add(0);
            indexes.add(3);
            return indexes;
        }

        for (int elem = 0; elem < word.length(); elem++) {
            if (word.substring(elem, elem + 1).matches("^(?ui:[аеёиоуыэюя]).*") &&
                    elem != indexOfRightLetter)
                indexes.add(elem);
        }
        Collections.shuffle(indexes);
        indexes.add(0, indexOfRightLetter);

        if (indexes.size() < 2)
            return null;
        else if (indexes.size() > 4)
            indexes = indexes.subList(0, 4);

        return indexes;
    }

    //удаляет в словосочетании слова без прописных букв
    private void getRightText(Task task) {
        String[] text = task.getWord().split(" ");

        if (Arrays.asList(text).contains("отзыв") || text.length == 1) return;

        int len = 0;
        for (String word : text) {
            if (len + word.length() > task.getPosition()) {
                task.setWord(word);
                task.setPosition(task.getPosition() - len);
                break;
            }
            len += word.length() + 1;
        }
    }

    //тоже самое, что и выше, но без объекта класса Task
    private String getRightText(String text) {
        int pos = findUpperChar(text);
        if (pos < 0) return text;

        Task task = new Task(text, pos);
        getRightText(task);
        return task.getWord();
    }







    @NonNull
    private static ContentValues getContentValues(Task task, int typeId) {

        ContentValues values = new ContentValues();

        switch (typeId) {
            case SuffixId:
                values.put(SuffixTable.Cols.WORD, task.getWord());
                values.put(SuffixTable.Cols.POSITION, task.getPosition());
                values.put(SuffixTable.Cols.ALTERNATIVE, ((Suffix) task).getAlternative());
                break;
            case ParonymId:
                values.put(ParonymTable.Cols.WORD, task.getWord());
                values.put(ParonymTable.Cols.VARIANTS, ((Paronym) task).getStringVariants());
                values.put(ParonymTable.Cols.ALTERNATIVE, ((Paronym) task).getStringAlternatives());
                break;
            default:
                values.put(AssentTable.Cols.WORD, task.getWord());
                values.put(AssentTable.Cols.POSITION, task.getPosition());
                values.put(AssentTable.Cols.CHECKED, ((Assent) task).isChecked() ? 1 : 0);
                break;
        }

        return values;
    }

    private void updateTask(Task task, int typeId) {

        String idString = task.getId() + "";
        ContentValues values = getContentValues(task, typeId);

        mDatabase.update(
                AssentTable.NAME,
                values,
                AssentTable.Cols.ID + " = ?",
                new String[] {idString});
    }

    private TaskCursorWrapper queryTasks(
            String tableName, String whereClause, String[] whereArgs) {

        Cursor cursor;

        cursor = mDatabase.query(
                tableName,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new TaskCursorWrapper(cursor);
    }
}
