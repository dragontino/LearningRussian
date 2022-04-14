package com.learning.ruslan.ui

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.learning.ruslan.createIntent
import com.learning.ruslan.databinding.ActivityParonymBinding
import com.learning.ruslan.databinding.ListItemLibraryBinding
import com.learning.ruslan.getStringExtra
import com.learning.ruslan.settings.SettingsViewModel
import com.learning.ruslan.task.Paronym
import com.learning.ruslan.task.TaskType
import com.learning.ruslan.task.TaskViewModel

// TODO: 08.04.2022 переделать во фрагмент
class ParonymActivity : AppCompatActivity() {

    companion object {
        private const val KEY_WORD = "KEY_WORD"
        fun getIntent(context: Context, word: String) =
            createIntent<ParonymActivity>(context) { putExtra(KEY_WORD, word) }
    }

    private lateinit var binding: ActivityParonymBinding
    private lateinit var settings: SettingsViewModel
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var paronym: Paronym


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityParonymBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        settings = SettingsViewModel.getInstance(this)
        taskViewModel = TaskViewModel.getInstance(this)

        val word = intent.getStringExtra(KEY_WORD, "")
        paronym = taskViewModel.getTask(TaskType.Paronym, word) as Paronym

        title = word

        binding.paronymRecyclerView.adapter = ParonymAdapter()

        settings.updateThemeInScreen(window, supportActionBar)
    }




    private inner class ParonymAdapter : RecyclerView.Adapter<ParonymHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParonymHolder {
            val itemBinding = ListItemLibraryBinding.inflate(layoutInflater, parent, false)
            return ParonymHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: ParonymHolder, position: Int) {
            val phrase = taskViewModel.paintParonym(paronym, settings.highlightColor, position)
            holder.bindParonym(phrase)
        }

        override fun getItemCount() =
            paronym.arrayVariants.size
    }


    private inner class ParonymHolder(private val itemBinding: ListItemLibraryBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bindParonym(phrase: Spannable) = itemBinding.run {
            textView.text = phrase
            textView.setTextColor(settings.fontColor)
            textView.setBackgroundColor(settings.backgroundColor)
        }
    }
}