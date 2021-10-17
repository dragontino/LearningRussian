package com.learning.ruslan;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.Nullable;

import com.learning.ruslan.databases.Paronym;
import com.learning.ruslan.databases.RusBaseHelper;
import com.learning.ruslan.databases.RusDbSchema;
import com.learning.ruslan.databases.RusDbSchema.AssentTable;
import com.learning.ruslan.databases.RusDbSchema.ParonymTable;
import com.learning.ruslan.databases.RusDbSchema.SuffixTable;
import com.learning.ruslan.databases.TaskCursorWrapper;

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
            "абонемент", "", "абонемент в театр", "абонемент на цикл лекций", "межбиблиотечный абонемент",
                    "абонемент на кинофестиваль", "абонемент в бассейн", "", "абонент", "\n"
    };

    private static Word sWord;

    public static Word get(Context context) {
        if (sWord == null)
            sWord = new Word(context.getApplicationContext());

        return sWord;
    }

    private Word(Context context) {
        mContext = context.getApplicationContext();
        this.mRandom = new Random();
        this.mDatabase = new RusBaseHelper(mContext).getWritableDatabase();
    }

    private void addWord(int typeId) {

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

            if (typeId == AssentId) {
                Assent mAssent = new Assent(word.toLowerCase(), pos, false);
                values = getContentValues(mAssent);
            }
            else if (typeId == SuffixId) {
                Suffix mSuffix = new Suffix(word.toLowerCase(), pos, "");
                values = getContentValues(mSuffix);
            }
            else {
                Paronym paronym = new Paronym("абонемент", "абонент",
                        new String[]{"абонемент в театр", "абонемент на цикл лекций", "межбиблиотечный абонемент"});
                values = getContentValues(paronym);
            }

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

    //Возвращает рандомное слово для раздела теория
    public Spannable getRandomWord(int typeId, int color) {
        int index;

        if (isTableEmpty(typeId)) {
            addWord(typeId);
        }

        index = mRandom.nextInt(getWordCount(typeId)) + 1;
        return getWordFromTable(typeId, index, color);
    }


    //Возвращает разные варианты написания слова/постановки ударения и т.д.
    public ArrayList<Spannable> getRandomWords(int typeId, int color) {
        TaskCursorWrapper cursorWrapper;
        Assent mAssent;
        ArrayList<Spannable> returns;
        int index;

        if (isTableEmpty(typeId)) {
            addWord(typeId);
        }

        //обновление базы данных нужно доделать, а то она не обновляется :)
        cursorWrapper = queryTasks(
                AssentTable.NAME,
                AssentTable.Cols.CHECKED + " = ?",
                new String[] {"0"}
        );

        if (cursorWrapper.getCount() == 0) {

            cursorWrapper = queryTasks(
                    AssentTable.NAME,
                    AssentTable.Cols.CHECKED + " = ?",
                    new String[] {"1"}
            );

            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                cursorWrapper.getAssent().setChecked(false);
                updateAssent(cursorWrapper.getAssent());
                cursorWrapper.moveToNext();
            }
        }

        List<Integer> indexes;

        do {
            index = mRandom.nextInt(getWordCount(typeId)) + 1;
            cursorWrapper = queryTasks(
                    AssentTable.NAME,
                    AssentTable.Cols.ID + " = ?",
                    new String[] {index + ""}
            );

            cursorWrapper.moveToFirst();
            mAssent = cursorWrapper.getAssent();

            cursorWrapper.close();

            getRightText(mAssent);
            indexes = getIndexesOfLetters(
                    mAssent.getWord(),
                    mAssent.getPosition()
            );
        }
        while (mAssent.isChecked() || indexes == null);

        mAssent.setChecked(true);
        updateAssent(mAssent);

        returns = new ArrayList<>();

        for (int i = 0; i < indexes.size(); i++) {

            Log.d(TAG, mAssent.getWord());
            Log.d(TAG, indexes.get(i).toString() + "\n");

            returns.add(getPaintedWord(
                    mAssent.getWord(),
                    indexes.get(i),
                    color
            ));
        }

        return returns;
    }

    //возвращает очередное слово для библиотеки знаний
    public Spannable getNextWord(int typeId, int position, int color) {

        if (isTableEmpty(typeId)) {
            addWord(typeId);
        }

        if (typeId == AssentId || typeId == SuffixId)
            return getWordFromTable(typeId, position + 1, color);
        return null;
    }

    //возвращает количество слов в таблице в зависимости от typeId
    public int getWordCount(int typeId) {
        String tableName;

        switch (typeId) {
            case SuffixId:
                tableName = SuffixTable.NAME;
                break;
            default:
                tableName = AssentTable.NAME;
                break;
        }

        CursorWrapper cursor = queryTasks(tableName, null, null);
        return cursor.getCount();
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
     * @param index - индекс в таблице, по которому определяется слово
     * @param color - цвет прописной буквы
     * @return возвращает слово из таблицы
     **/
    @Nullable
    private Spannable getWordFromTable(int typeId, @IntRange(from = 1) int index, int color) {

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
        }

        cursorWrapper = queryTasks(
                tableName,
                id + " = ?",
                new String[] { String.valueOf(index) }
        );

        if (cursorWrapper.getCount() == 0)
            return null;

        cursorWrapper.moveToFirst();
        task = cursorWrapper.getTask(typeId);
        toUpperCase(task);
        cursorWrapper.close();

        return getPaintedWord(task.getWord(), task.getPosition(), color);
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

            cursorWrapper.close();
        }
        else {
            cursorWrapper.moveToFirst();

            while (!cursorWrapper.isAfterLast()) {
                task = cursorWrapper.getTask(typeId);
                toUpperCase(task);
                results.add(getPaintedWord(task.getWord(), task.getPosition(), color));
                cursorWrapper.moveToNext();
            }

            cursorWrapper.close();
        }


        
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









    private static ContentValues getContentValues(Assent assent) {
        ContentValues values = new ContentValues();
        values.put(AssentTable.Cols.WORD, assent.getWord());
        values.put(AssentTable.Cols.POSITION, assent.getPosition());
        values.put(AssentTable.Cols.CHECKED, assent.isChecked() ? 1 : 0);

        return values;
    }

    private static ContentValues getContentValues(Suffix suffix) {
        ContentValues values = new ContentValues();
        values.put(SuffixTable.Cols.WORD, suffix.getWord());
        values.put(SuffixTable.Cols.POSITION, suffix.getPosition());
        values.put(SuffixTable.Cols.ALTERNATIVE, suffix.getAlternative());

        return values;
    }

    private ContentValues getContentValues(Paronym paronym) {
        ContentValues values = new ContentValues();
        values.put(ParonymTable.Cols.WORD, paronym.getWord());
        values.put(ParonymTable.Cols.VARIANTS, paronym.getVariants());
        values.put(ParonymTable.Cols.ALTERNATIVE, paronym.getAlternative());

        return values;
    }

    private void updateAssent(Assent assent) {

        String idString = assent.getId() + "";
        ContentValues values = getContentValues(assent);

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