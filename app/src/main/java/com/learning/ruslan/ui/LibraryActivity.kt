package com.learning.ruslan.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.learning.ruslan.*
import com.learning.ruslan.databinding.ActivityLibraryBinding
import com.learning.ruslan.databinding.ListItemLibraryBinding
import com.learning.ruslan.settings.SettingsViewModel
import com.learning.ruslan.task.TaskType
import com.learning.ruslan.task.TaskViewModel

class LibraryActivity : AppCompatActivity() {

    companion object {
        private const val ActivityId = "LibraryId"

        fun getIntent(context: Context, type: TaskType) = createIntent<LibraryActivity>(context) {
            putExtra(ActivityId, type)
        }
    }


    private lateinit var binding: ActivityLibraryBinding
    private var adapter: LibraryAdapter? = null

    private lateinit var settings: SettingsViewModel
    private lateinit var taskViewModel: TaskViewModel

    private var searchResults: List<Spannable> = ArrayList()

    private lateinit var searchView: SearchView

    private lateinit var typeId: TaskType

    private var menu: Menu? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLibraryBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        settings = SettingsViewModel.getInstance(this)
        taskViewModel = TaskViewModel.getInstance(this)
        typeId = intent.getSerializableExtra(ActivityId) as TaskType
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.library_menu, menu)
        this.menu = menu
        settings.drawMenuItems(menu, R.id.menu_item_settings, R.id.menu_item_search)

        searchView = menu.findItem(R.id.menu_item_search).actionView as SearchView
        drawSearchView()

        searchView.setOnCloseListener {
            searchResults = ArrayList()
            false
        }

        searchView.setOnQueryTextListener(
            object: SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String) = false

                @SuppressLint("NotifyDataSetChanged")
                override fun onQueryTextChange(newText: String): Boolean {
                    searchResults = taskViewModel.searchWords(typeId, newText, settings.highlightColor)
                    adapter?.notifyDataSetChanged()
                    return true
                }
            })
        return true
    }

    @SuppressLint("NonConstantResourceId")
    override fun onOptionsItemSelected(item: MenuItem) =
        if (item.itemId == R.id.menu_item_settings) {
            startActivity(SettingsActivity.getIntent(this, typeId))
            true
        }
        else super.onOptionsItemSelected(item)


    override fun onResume() {
        super.onResume()
        updateUI()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUI() {
        settings.updateThemeInScreen(window, supportActionBar)
        settings.drawMenuItems(menu, R.id.menu_item_settings, R.id.menu_item_search)

        if (adapter == null) {
            adapter = LibraryAdapter()
            binding.libraryRecyclerView.adapter = adapter
        } else {
            adapter?.notifyDataSetChanged()
        }
    }

    private fun drawSearchView() {
        val searchText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        val searchImage = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_button)
        val clearView = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)

        searchText.setTextColor(settings.fontColor)
        searchText.setHintTextColor(getColor(android.R.color.darker_gray))
        searchText.backgroundTint = settings.fontColor
        searchImage.imageTint = settings.highlightColor
        clearView.imageTint = settings.highlightColor
    }





    private inner class LibraryAdapter : RecyclerView.Adapter<LibraryHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryHolder {
            val binding = ListItemLibraryBinding.inflate(layoutInflater, parent, false)
            return LibraryHolder(binding)
        }

        override fun onBindViewHolder(holder: LibraryHolder, position: Int) {
            val word = if (searchResults.isEmpty())
                taskViewModel.getWord(typeId, position, settings.highlightColor)
            else
                searchResults[position]

            holder.bindWord(word)
        }

        override fun getItemCount() = if (searchResults.isEmpty())
            taskViewModel.getWordsCount(typeId)
        else
            searchResults.size
    }

    private inner class LibraryHolder(private val itemBinding: ListItemLibraryBinding):
        RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {

        init {
            if (typeId == TaskType.Paronym) {
                itemBinding.root.setOnClickListener(this)
                itemBinding.textView.setTextIsSelectable(false)
            }
            else itemBinding.textView.setTextIsSelectable(true)
        }

        fun bindWord(word: Spannable) {
            itemBinding.run {
                textView.text = word
                textView.setTextColor(settings.fontColor)
                textView.setBackgroundColor(settings.backgroundColor)
            }
        }

        override fun onClick(v: View) {
            val intent = ParonymActivity.getIntent(
                this@LibraryActivity,
                itemBinding.textView.text.toString()
            )
            startActivity(intent)
        }
    }
}