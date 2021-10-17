package com.learning.ruslan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.learning.ruslan.activities.LibraryActivity;
import com.learning.ruslan.activities.SettingsActivity;

import java.util.ArrayList;
import java.util.Random;

public class SuffixActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_start, btn_break;
    TextView textViewStart;
    ConstraintLayout.LayoutParams tvParams;
    String [] words = new String[]{"скоропортЯщиеся", "доносЯтся (звуки) — доносить, 2 спр",
            "переловЯт (рыбаки) — ловить, 2 спр", "моЮщиеся — мыться, 1 спр",
            "дорогостоЯщая — стоить 2 спр", "выкрикиваЮщая — выкрикивать, 1 спряжение",
            "посылаЕмый — посылать, 1спр", "нарушаЕт (водитель) — нарушать, 1 спр",
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

    String [] words2 = new String[]{"брезжУщий\n", "зыблЮщийся\n", "зиждУщийся\n", "внемлЮщий\n",
            "приемлЕмый\n", "неотъемлЕмый\n", "незыблЕмый\n", "движИмый – от «движити»"};
    boolean isChecked;
    int pause;
    ArrayList<Integer> arrayList;
    Random random;
    Handler mHandler;
    double posX, posY;
    boolean isWord = true;
    Word word;
    Support support;
    private Menu menu;
    int fontColor = Color.BLACK;
    int letColor = Color.MAGENTA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suffix);

        word = Word.get(this);
        support = Support.get(this);

        btn_start = findViewById(R.id.btn_start);
        btn_break = findViewById(R.id.btn_break);
        textViewStart = findViewById(R.id.textView_start);
        tvParams = (ConstraintLayout.LayoutParams) textViewStart.getLayoutParams();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        arrayList = new ArrayList<>();
        random = new Random();

        btn_start.setOnClickListener(this);
        btn_break.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {

        isChecked = support.getChecked();
        pause = support.getPause();

        switch (support.getTheme()) {
            case Support.THEME_LIGHT:
                fontColor = Color.BLACK;
                letColor = Color.MAGENTA;
                break;
            case Support.THEME_NIGHT:
                fontColor = Color.WHITE;
                letColor = Support.color_magenta2;
                break;
        }

        btn_start.setBackgroundTintList(ColorStateList.valueOf(letColor));
        textViewStart.setTextColor(fontColor);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_menu, menu);

        this.menu = menu;
        updateUI();

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.menu_item_list_words:
                startActivity(LibraryActivity.newIntent(this, Word.SuffixId));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                btn_start.setVisibility(View.INVISIBLE);
                btn_break.setVisibility(View.VISIBLE);

                tvParams.verticalBias = (float) 0.5;

                textViewStart.setLayoutParams(tvParams);
                textViewStart.setText("");

                if (isChecked) {
                    mHandler = new Handler();
                    mHandler.post(mUpdate);
                }
                else {
                    textViewStart.setOnClickListener(this);

                    if (isWord) {

                        for (String elem : words2) {
                            Spannable spannable = new SpannableString(elem);

                            for (int i = 0; i < elem.length(); i++)
                                if (Character.isUpperCase(elem.charAt(i))) {
                                    spannable.setSpan(new ForegroundColorSpan(letColor), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    break;
                                }
                            textViewStart.append(spannable);
                        }
                        isWord = false;
                    }
                    else {
                        Spannable spannable = getNewWord();

                        textViewStart.setText(spannable);

                        posX = Math.random();
                        posY = Math.random();

                        tvParams.horizontalBias = (float) posX;
                        tvParams.verticalBias = (float) posY;
                        textViewStart.setLayoutParams(tvParams);
                    }
                }
                break;
            case R.id.btn_break:
                btn_start.setVisibility(View.VISIBLE);
                tvParams.horizontalBias = (float) 0.5;
                tvParams.verticalBias = (float) 0.617;

                textViewStart.setLayoutParams(tvParams);

                if (isChecked) mHandler.removeCallbacks(mUpdate);

                textViewStart.setText("Старт");
                btn_break.setVisibility(View.INVISIBLE);
                isWord = true;
                break;
            case R.id.textView_start:
                Spannable spannable = getNewWord();

                textViewStart.setText(spannable);

                posX = Math.random();
                posY = Math.random();

                tvParams.horizontalBias = (float) posX;
                tvParams.verticalBias = (float) posY;
                textViewStart.setLayoutParams(tvParams);
                break;
        }
    }

    private String [] getWords() {
        return this.words;
    }
    private Spannable getNewWord() {
        int index;
        String [] words = getWords();
        String text;

        if (arrayList.size() == words.length) arrayList.clear();

        do index = random.nextInt(words.length);
        while (arrayList.contains(index));

        arrayList.add(index);

        Spannable spannable = new SpannableString(words[index]);

        text = words[index];

        for (int elem = 0; elem < text.length(); elem ++) if (Character.isUpperCase(text.charAt(elem))) {
            spannable.setSpan(new ForegroundColorSpan(letColor), elem, elem + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            break;
        }
        return spannable;
    }

    private final Runnable mUpdate = new Runnable() {
        @Override
        public void run() {
            if (isWord) {
                for (String elem : words2) {
                    Spannable spannable = new SpannableString(elem);

                    for (int i = 0; i < elem.length(); i ++) if (Character.isUpperCase(elem.charAt(i))) {
                        spannable.setSpan(new ForegroundColorSpan(Color.MAGENTA), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        break;
                    }

                    textViewStart.append(spannable);
                }
                mHandler.postDelayed(this, 6000);
                isWord = false;
            }
            else {
                Spannable spannable = getNewWord();

                textViewStart.setText(spannable);
                mHandler.postDelayed(this, pause);

                posX = Math.random();
                posY = Math.random();

                tvParams.horizontalBias = (float) posX;
                tvParams.verticalBias = (float) posY;
                textViewStart.setLayoutParams(tvParams);
            }
        }
    };
}