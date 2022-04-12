package com.learning.ruslan.activities
//
//import android.annotation.SuppressLint
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
//import com.learning.ruslan.settings.SettingsViewModel
//
//class LanguageActivity : AppCompatActivity() {
//
//
//    private lateinit var mRecyclerView: RecyclerView
//    private var mAdapter: LanguageAdapter? = null
//    private lateinit var settingsViewModel: SettingsViewModel
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_language)
//        mRecyclerView = findViewById(R.id.language_recycler_view)
//        mRecyclerView.layoutManager = LinearLayoutManager(this)
//        settingsViewModel = SettingsViewModel.getInstance(this)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        updateUI()
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    private fun updateUI() {
//        if (mAdapter == null) {
//            mAdapter = LanguageAdapter()
//            mRecyclerView.adapter = mAdapter
//        } else mAdapter?.notifyDataSetChanged()
//
//        window.decorView.setBackgroundColor(settingsViewModel.backgroundColor)
//        title = getWord(title.toString(), settingsViewModel.fontColor)
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
//    private inner class LanguageAdapter : RecyclerView.Adapter<LanguageHolder>() {
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageHolder {
//            val view = layoutInflater
//                .inflate(R.layout.list_item_language, parent, false)
//            return LanguageHolder(view)
//        }
//
//        override fun onBindViewHolder(holder: LanguageHolder, position: Int) {
//            val language = countries[position]
//            holder.bindLanguage(language, position)
//        }
//
//        override fun getItemCount(): Int {
//            return Support.Companion.languages.size
//        }
//    }
//
//    private inner class LanguageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val mTextView: TextView
//        private var position = 0
//        fun bindLanguage(language: String?, position: Int) {
//            this.position = position
//            mTextView.text = language
//        }
//
//        private fun onClick(view: View) {
//            Toast.makeText(applicationContext, "Выбран язык " + mTextView.text, Toast.LENGTH_SHORT)
//                .show()
//            settingsViewModel.setLanguage(position)
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