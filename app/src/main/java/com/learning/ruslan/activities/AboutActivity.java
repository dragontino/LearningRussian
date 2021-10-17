package com.learning.ruslan.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.learning.ruslan.R;
import com.learning.ruslan.Support;

public class AboutActivity extends FragmentActivity {

    private Support support;
    private TextView [] textViews;
    private int fontColor = Color.BLACK;
    private int textLinkColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        support = Support.get(this);

        textViews = new TextView[]{
                findViewById(R.id.textView_title),
                findViewById(R.id.textView_version),
                findViewById(R.id.textView_vk),
                findViewById(R.id.textView_email)};

        textViews[0].setTypeface(Typeface.createFromAsset(getAssets(), "fonts/segoesc.ttf"));
        textViews[1].setTypeface(Typeface.createFromAsset(getAssets(), "fonts/xarrovv.otf"), Typeface.ITALIC);
        textViews[2].setTypeface(Typeface.createFromAsset(getAssets(), "fonts/xarrovv.otf"));
        textViews[3].setTypeface(Typeface.createFromAsset(getAssets(), "fonts/xarrovv.otf"), Typeface.BOLD);

        textViews[1].setText(getString(R.string.version, "\n"));

        textViews[2].setMovementMethod(LinkMovementMethod.getInstance());
        textViews[3].setMovementMethod(LinkMovementMethod.getInstance());

        textLinkColor = R.color.purple_200_light;

        switch (support.getTheme()) {
            case Support.THEME_LIGHT:
                fontColor = Color.BLACK;
                textLinkColor = R.color.purple_200_light;

                getWindow().getDecorView().setBackgroundColor(getColor(R.color.white));
                break;
            case Support.THEME_NIGHT:
                fontColor = Color.WHITE;
                textLinkColor = R.color.purple_200;

                getWindow().getDecorView().setBackgroundColor(getColor(R.color.black2));
                break;
        }

        for (TextView tv : textViews) {
            tv.setTextColor(fontColor);
            tv.setLinkTextColor(getColor(textLinkColor));
        }
    }
}