package com.learning.ruslan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.learning.ruslan.activities.LibraryActivity;
import com.learning.ruslan.activities.SettingsActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ParonymTestActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnStart, btnBreak;
    private Button[] buttons;
    private TextView textViewStart, textViewBalls;
    private int questions, corr_index, balls, count_ans = 0;
    private final int MAX_QUESTIONS = 74;
    private ConstraintLayout constraintLayout;
    private ConstraintLayout.LayoutParams tvParams;
    private Random random;
    private Word word;
    private Support support;
    private Menu menu;

    private int fontColor = Color.BLACK;
    private int letColor = Color.MAGENTA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paronym_test);

        word = Word.get(this);
        support = Support.get(this);

        btnStart = findViewById(R.id.btnStartTest);
        btnBreak = findViewById(R.id.btnBreakTest);
        textViewStart = findViewById(R.id.textViewStartTest);
        textViewBalls = findViewById(R.id.textViewBalls);
        constraintLayout = findViewById(R.id.clMain);
        buttons = createButtons();
        random = new Random();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int ScreenHeight = size.y;

        support.setQuestions(support.getQuestions() % MAX_QUESTIONS);

        btnBreak.setOnClickListener(this);
        btnStart.setOnClickListener(this);

        tvParams = (ConstraintLayout.LayoutParams) textViewStart.getLayoutParams();

        textViewStart.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/verdana.ttf"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {

        questions = support.getQuestions();

        switch (support.getTheme()) {
            case Support.THEME_LIGHT:
                fontColor = Color.BLACK;
                letColor = Color.MAGENTA;
                textViewBalls.setTextColor(Support.score_color_light);
                for (Button btn: buttons) {
                    btn.setBackgroundResource(R.drawable.button_style_2);
                    btn.setTextColor(Color.WHITE);
                }
                break;
            case Support.THEME_NIGHT:
                fontColor = Color.WHITE;
                letColor = Support.color_magenta2;
                textViewBalls.setTextColor(Support.score_color_night);

                for (Button btn: buttons) {
                    btn.setBackgroundResource(R.drawable.button_style);
                    btn.setTextColor(Color.BLACK);
                }
                break;
        }
        btnStart.setBackgroundTintList(ColorStateList.valueOf(letColor));
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
                startActivity(LibraryActivity.newIntent(this, Word.ParonymId));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private Button[] createButtons() {
        Button[] buttons = new Button[4];

        for (int i = 0; i < 4; i ++) {
            ConstraintLayout.LayoutParams btnParams = new ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            btnParams.topToTop = ConstraintSet.PARENT_ID;
            btnParams.startToStart = ConstraintSet.PARENT_ID;
            btnParams.endToEnd = ConstraintSet.PARENT_ID;
            btnParams.bottomToBottom = ConstraintSet.PARENT_ID;

            Button btn = new Button(this);
            btn.setOnClickListener(this);
            btn.setAllCaps(false);
            btn.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/xarrovv.otf"), Typeface.BOLD);
            btn.setTextSize(25);
            btn.setId(100 + i);
            btn.setVisibility(View.INVISIBLE);

            constraintLayout.addView(btn, btnParams);
            buttons[i] = btn;
        }

        return buttons;
    }



    @SuppressLint("NewApi")
    private int getNewWord() {

        HashMap<String, List<List<String>>> phrases = new HashMap<>();//предложения в будущем будут браться из теории.
        int correct_index, index, countButtons;
        String word;

        //Заполнение массива
        phrases.put("абонемент", List.of(
                List.of("абонемент в театр", "абонемент на цикл лекций", "межбиблиотечный абонемент",
                        "абонемент на кинофестиваль", "абонемент в бассейн"),
                List.of("абонент")));
        phrases.put("абонент", List.of(
                List.of("абонент библиотеки", "абонент телефонной сети", "абонент временно недоступен"),
                List.of("абонемент")));
        phrases.put("абонентов", List.of(
                List.of("жалобы от абонентов"),
                List.of("абонементов")));
        phrases.put("артистический", List.of(
                List.of("артистический путь"),
                List.of("артистичный")));
        phrases.put("артистическая", List.of(
                List.of("артистическая среда", "артистическая карьера", "артистическая уборная"),
                List.of("артистичная")));
        phrases.put("артистическое",
                List.of(List.of("артистическое кафе"),
                List.of("артистичное")));
        phrases.put("артистичный", List.of(
                List.of("артистичный человек", "артистичный до мозга костей"),
                List.of("артистический")));
        phrases.put("артистичное", List.of(
                List.of("артистичное исполнение"),
                List.of("артистическое")));
        phrases.put("бедная", List.of(
                List.of("бедная обстановка", "бедная одежда"),
                List.of("бедственная")));
        phrases.put("бедный", List.of(
                List.of("бедный человек", "бедный ужин", "бедный дом", "мой бедный мальчик"),
                List.of("бедственный")));
        phrases.put("бедственное", List.of(
                List.of("бедственное положение"),
                List.of("бедное")));
        phrases.put("безответная", List.of(
                List.of("безответная любовь"),
                List.of("безответственная")));
        phrases.put("безответственный", List.of(
                List.of("безответственный поступок"),
                List.of("безответный")));
        phrases.put("безответственное", List.of(
                List.of("безответственное лицо", "безотвественное отношение", "безответственное поведение"),
                List.of("безответное")));
        phrases.put("болотистые", List.of(
                List.of("болотистые земли"),
                List.of("болотные")));
        phrases.put("болотистая", List.of(
                List.of("болотистая местность", "болотистая почва"),
                List.of("болотная")));
        phrases.put("болотные", List.of(
                List.of("болотные сапоги", "болотные травы"),
                List.of("болотистые")));
        phrases.put("болотная", List.of(
                List.of("болотная птица"),
                List.of("болотистая")));
        phrases.put("болотный", List.of(
                List.of("болотный мох"),
                List.of("болотистый")));
        phrases.put("благодарная", List.of(
                List.of("благодарная тема"),
                List.of("благодарственная")));
        phrases.put("благодарный", List.of(
                List.of("благодарный пациент", "благодарный слушатель", "благодарный ученик", "благодарный взгляд", "благодарный материал"),
                List.of("благодарственный")));
        phrases.put("благодарственное", List.of(
                List.of("благодарственное письмо"),
                List.of("благодарное")));
        phrases.put("благодарственная", List.of(
                List.of("благодарственная телеграмма"),
                List.of("благодарная")));
        phrases.put("благодарственный", List.of(
                List.of("благодарственный молебен"),
                List.of("благодарный")));
        phrases.put("благотворительная", List.of(
                List.of("благотворительная акция", "благотворительная лотерея"),
                List.of("благотворная")));
        phrases.put("благотворительный", List.of(
                List.of("благотворительный спектакль", "благотворительный фонд"),
                List.of("благотворный")));
        phrases.put("благотворное", List.of(
                List.of("благотворное влияние"),
                List.of("благотворительное")));
        phrases.put("благотворная", List.of(
                List.of("благотворная прохлада", "благотворная влага"),
                List.of("благотворительная")));
        phrases.put("бывшая", List.of(
                List.of("бывшая школа"),
                List.of("былая")));
        phrases.put("бывший", List.of(
                List.of("бывший клуб", "бывший врач", "бывший директор"),
                List.of("былой")));
        phrases.put("былые", List.of(
                List.of("былые годы"),
                List.of("бывшие")));
        phrases.put("былая", List.of(
                List.of("былая сила", "былая печаль", "былая слава"),
                List.of("бывшая")));
        phrases.put("былое", List.of(
                List.of("былое счастье" ,"былое уважение"),
                List.of("бывшее")));
        phrases.put("былой", List.of(
                List.of("былой страх"),
                List.of("бывший")));
        phrases.put("вдох", List.of(
                List.of("глубокий вдох", "сделать вдох", "вдох всей грудью"),
                List.of("вздох")));
        phrases.put("вздох", List.of(
                List.of("вздох облегчения", "тяжёлый вздох"),
                List.of("вдох")));
        phrases.put("вздохом", List.of(
                List.of("сказать со вздохом"),
                List.of("вдохом")));
        phrases.put("вековые", List.of(
                List.of("вековые дубы", "вековые традиции", "вековые обычаи"),
                List.of("вечные")));
        phrases.put("вековая", List.of(
                List.of("вековая роща", "вековая пыль", "вековая грязь"),
                List.of("вечная")));
        phrases.put("вечная", List.of(
                List.of("вечная проблема", "вечная мерзлота"),
                List.of("вековая")));
        phrases.put("вечные", List.of(
                List.of("вечные человеческие ценности", "вечные вопросы"),
                List.of("вековые")));



        String[] keys = phrases.keySet().toArray(new String[0]);

        index = random.nextInt(keys.length);
        word = keys[index];

        List<String> list_phr = Objects.requireNonNull(phrases.get(word)).get(0);
        List<String> list_words = Objects.requireNonNull(phrases.get(word)).get(1);

        //Этот массив отслеживает, какие слова уже были записаны на кнопках, а какие - нет
        //false - слова еще не были использованы, true - были
        boolean[] words = new boolean[list_words.size()];
        Arrays.fill(words, false);

        countButtons = list_words.size() + 1;
        correct_index = random.nextInt(countButtons);

        textViewStart.append(
                getPhrase(
                        list_phr.get(random.nextInt(list_phr.size())),
                        word
                )
        );


        for (int j = 0; j < countButtons; j ++) {

            if (j == correct_index)
                buttons[j].setText(word);
            else {
                int k;
                do k = random.nextInt(list_words.size());
                while (words[k]);

                words[k] = true;
                buttons[j].setText(list_words.get(k));
            }

            ConstraintLayout.LayoutParams btnParams = (ConstraintLayout.LayoutParams) buttons[j].getLayoutParams();

            if (countButtons % 2 == 1 && j == countButtons - 1)
                btnParams.horizontalBias = (float) 0.5;
            else
                btnParams.horizontalBias = (float) (0.1 + 0.8 * (j % 2));

            btnParams.verticalBias = (float) (0.65 + 0.2 * (j / 2));

            buttons[j].setLayoutParams(btnParams);
            buttons[j].setVisibility(View.VISIBLE);
        }
        return correct_index;
    }

    /**
     *
     * @param phrase - sentence, that includes keyword
     * @param keyword - word to delete
     * @return phrase without keyword
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Spannable getPhrase(String phrase, String keyword) {
        if (!phrase.contains(keyword)) return null;

        String [] text = phrase.split(" ");

        for (int i = 0; i < text.length; i ++) {
            if (text[i].equals(keyword))
                text[i] = "...";
        }

        int textColor;

        if (support.getTheme().equals(Support.THEME_LIGHT))
            textColor = Color.rgb(217, 92, 2);
        else
            textColor = Color.rgb(219, 130, 66);

        Spannable spannable = new SpannableString(String.join(" ", text));
        spannable.setSpan(new ForegroundColorSpan(textColor), 0,
                String.join(" ", text).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
    }


    @SuppressLint("SetTextI18n")
    private void EndGame() {
        this.btnBreak.setVisibility(View.GONE);

        this.textViewBalls.setText("Поздравляем, вы набрали " + balls + " очков из " + questions);

        if (balls == questions) this.textViewBalls.append(getString(R.string.great_results));

        this.balls = 0;
        this.count_ans = 0;

        this.textViewStart.setTextSize(30);
        this.tvParams.horizontalBias = (float) 0.5;
        this.tvParams.verticalBias = (float) 0.617;

        for (Button button : buttons) button.setVisibility(View.INVISIBLE);

        textViewStart.setText("Старт");
        btnStart.setVisibility(View.VISIBLE);
    }





    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartTest:
                btnStart.setVisibility(View.INVISIBLE);
                btnBreak.setVisibility(View.VISIBLE);
                textViewBalls.setVisibility(View.VISIBLE);

                textViewStart.setTextSize(20);
                tvParams.horizontalBias = (float) 0.5;
                tvParams.verticalBias = (float) 0.5;
                textViewStart.setLayoutParams(tvParams);

                textViewStart.setText(R.string.paronym_test_text);
                textViewBalls.setText("баллы: 0/0");
                balls = 0;
                corr_index = getNewWord();

                break;
            case R.id.btnBreakTest:
                EndGame();
                break;
            case 100:
            case 101:
            case 102:
            case 103:
                if (v == buttons[corr_index]) {
                    Toast.makeText(this, "Верно!", Toast.LENGTH_SHORT).show();
                    balls++;
                } else
                    Toast.makeText(this, "Ошибка!", Toast.LENGTH_SHORT).show();

                count_ans++;

                if (count_ans == questions)
                    EndGame();
                else {
                    textViewStart.setText(R.string.paronym_test_text);
                    textViewBalls.setText("баллы: " + balls + "/" + count_ans);
                    corr_index = getNewWord();
                }

                break;
        }
    }
}