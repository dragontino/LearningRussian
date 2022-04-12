package com.learning.ruslan

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.learning.ruslan.settings.Settings
import com.learning.ruslan.settings.SettingsDao
import com.learning.ruslan.task.*

@Database(
    entities = [Assent::class, Suffix::class, Paronym::class, Settings::class, SteadyExpression::class],
    version = 1,
    exportSchema = false
)
abstract class MainDatabase : RoomDatabase() {

    abstract fun taskDao() : TaskDao
    abstract fun settingsDao() : SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getDatabase(context: Context): MainDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null)
                return tempInstance

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDatabase::class.java,
                    "RusBase"
                )
                    .allowMainThreadQueries()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}