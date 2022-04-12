package com.learning.ruslan.ui

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.learning.ruslan.R
import com.learning.ruslan.databinding.ActivityMainBinding
import com.learning.ruslan.databinding.ListItemMainBinding
import com.learning.ruslan.settings.SettingsViewModel
import com.learning.ruslan.task.TaskType

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var adapter: TaskAdapter? = null
    private lateinit var settings: SettingsViewModel

    private var menu: Menu? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.appVersion.typeface = Typeface.defaultFromStyle(Typeface.ITALIC)
        binding.appVersion.text = getString(R.string.version)

        settings = SettingsViewModel.getInstance(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        this.menu = menu
        settings.drawMenuItems(menu, R.id.menu_item_settings)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menu_item_settings -> {
            startActivity(SettingsActivity.getIntent(this, TaskType.Assent))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUI() {
        if (adapter == null) {
            adapter = TaskAdapter()
            binding.mainRecyclerView.adapter = adapter
        }
        else adapter?.notifyDataSetChanged()

        settings.updateThemeInScreen(window, supportActionBar)
        settings.drawMenuItems(menu, R.id.menu_item_settings)
        binding.appVersion.setTextColor(settings.fontColor)
    }



    private inner class TaskAdapter : RecyclerView.Adapter<TaskHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
            val itemBinding = ListItemMainBinding.inflate(layoutInflater, parent, false)
            return TaskHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: TaskHolder, position: Int) {
            val taskType = TaskType.values()[position]
            holder.bindTask(taskType)
        }

        override fun getItemCount() =
            TaskType.values().size
    }




    private inner class TaskHolder(private val itemBinding: ListItemMainBinding):
        RecyclerView.ViewHolder(itemBinding.root) {

        private lateinit var taskType: TaskType

        init {
            itemView.setOnClickListener {
                val intent = DoubleFragmentActivity.getIntent(this@MainActivity, taskType)
                startActivity(intent)
            }
        }

        fun bindTask(taskType: TaskType) {
            itemBinding.run {
                textView.text = getString(taskType.title)
                textView.setTextColor(settings.fontColor)
                textView.setBackgroundColor(settings.backgroundColor)
            }
            this.taskType = taskType
        }
    }
}