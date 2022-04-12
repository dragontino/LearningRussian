package com.learning.ruslan.activities
//
//import android.widget.TextView
//import com.learning.ruslan.Support
//import android.os.Bundle
//import com.learning.ruslan.R
//import android.graphics.Typeface
//import androidx.fragment.app.FragmentActivity
//import android.text.method.LinkMovementMethod
//import android.graphics.Color
//
//class AboutActivity : FragmentActivity() {
//    private var support: Support? = null
//    private var textViews: Array<TextView>
//    private var fontColor = Color.BLACK
//    private var textLinkColor = 0
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_about)
//        support = Support[this]
//        textViews = arrayOf(
//            findViewById(R.id.textView_title),
//            findViewById(R.id.app_version),
//            findViewById(R.id.textView_vk),
//            findViewById(R.id.textView_email))
//        textViews[0].setTypeface(Typeface.createFromAsset(assets, "fonts/segoesc.ttf"))
//        textViews[1].setTypeface(Typeface.createFromAsset(assets, "fonts/xarrovv.otf"),
//            Typeface.ITALIC)
//        textViews[2].setTypeface(Typeface.createFromAsset(assets, "fonts/xarrovv.otf"))
//        textViews[3].setTypeface(Typeface.createFromAsset(assets, "fonts/xarrovv.otf"),
//            Typeface.BOLD)
//        textViews[1].text = getString(R.string.version, "\n")
//        textViews[2].movementMethod = LinkMovementMethod.getInstance()
//        textViews[3].movementMethod = LinkMovementMethod.getInstance()
//        textLinkColor = R.color.purple_200_light
//        when (support.getTheme()) {
//            Support.Companion.THEME_LIGHT -> {
//                fontColor = Color.BLACK
//                textLinkColor = R.color.purple_200_light
//                window.decorView.setBackgroundColor(getColor(R.color.white))
//            }
//            Support.Companion.THEME_NIGHT -> {
//                fontColor = Color.WHITE
//                textLinkColor = R.color.purple_200
//                window.decorView.setBackgroundColor(getColor(R.color.background_dark))
//            }
//        }
//        for (tv in textViews) {
//            tv.setTextColor(fontColor)
//            tv.setLinkTextColor(getColor(textLinkColor))
//        }
//    }
//}