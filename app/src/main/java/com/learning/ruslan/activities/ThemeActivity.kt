package com.learning.ruslan.activities
//
//import android.widget.TextView
//import com.learning.ruslan.Support
//import android.os.Bundle
//import android.view.ViewGroup
//import com.learning.ruslan.R
//import android.graphics.Typeface
//import android.widget.Toast
//import android.text.SpannableString
//import android.text.style.ForegroundColorSpan
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.RecyclerView
//import androidx.recyclerview.widget.LinearLayoutManager
//import android.graphics.Color
//import android.view.View
//
//class ThemeActivity : AppCompatActivity() {
//    private var mRecyclerView: RecyclerView? = null
//    private var mAdapter: ThemeAdapter? = null
//    private var support: Support? = null
//    private val themes = arrayOf("Светлая",
//        "Тёмная")
//    private var backgroundColor = 0
//    private var fontColor = Color.BLACK
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.switch_theme_field)
//        mRecyclerView = findViewById(R.id.theme_recycler_view)
//        mRecyclerView.setLayoutManager(LinearLayoutManager(this))
//        support = Support.Companion.get(this)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        updateUI()
//    }
//
//    private fun updateUI() {
//        if (mAdapter == null) {
//            mAdapter = ThemeAdapter()
//            mRecyclerView!!.adapter = mAdapter
//        } else mAdapter!!.notifyDataSetChanged()
//        when (support.getTheme()) {
//            Support.Companion.THEME_LIGHT -> {
//                fontColor = Color.BLACK
//                backgroundColor = Color.WHITE
//            }
//            Support.Companion.THEME_NIGHT -> {
//                fontColor = Color.WHITE
//                backgroundColor = getColor(R.color.background_dark)
//            }
//        }
//        window.decorView.setBackgroundColor(backgroundColor)
//        title = getWord(title.toString(), fontColor)
//    }
//
//    private fun getWord(word: String, color: Int): SpannableString {
//        val string = SpannableString(word)
//        string.setSpan(
//            ForegroundColorSpan(color),
//            0,
//            word.length,
//            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        return string
//    }
//
//    private inner class ThemeAdapter : RecyclerView.Adapter<ThemeHolder>() {
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeHolder {
//            val view = layoutInflater
//                .inflate(R.layout.list_item_language, parent, false)
//            return ThemeHolder(view)
//        }
//
//        override fun onBindViewHolder(holder: ThemeHolder, position: Int) {
//            val theme = themes[position]
//            holder.bindTheme(theme, position)
//        }
//
//        override fun getItemCount(): Int {
//            return themes.size
//        }
//    }
//
//    private inner class ThemeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val mTextView: TextView
//        private var position = 0
//        fun bindTheme(language: String?, position: Int) {
//            this.position = position
//            mTextView.text = language
//        }
//
//        private fun onClick(view: View) {
//            Toast.makeText(applicationContext, "Выбрана тема " + mTextView.text, Toast.LENGTH_SHORT)
//                .show()
//            //            if (position == 0)
////                support.setTheme(Support.THEME_LIGHT);
////            else
////                support.setTheme(Support.THEME_NIGHT);
//            support!!.updateTheme()
//            finish()
//        }
//
//        init {
//            mTextView = itemView.findViewById(R.id.list_item_language_textView)
//            mTextView.setOnClickListener { view: View -> onClick(view) }
//            mTextView.setTypeface(Typeface.createFromAsset(assets, "fonts/xarrovv.otf"),
//                Typeface.BOLD)
//            mTextView.setTextColor(fontColor)
//            mTextView.setBackgroundColor(backgroundColor)
//        }
//    }
//}