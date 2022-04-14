package com.learning.ruslan.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.learning.ruslan.settings.Settings
import com.learning.ruslan.settings.SettingsDao
import com.learning.ruslan.task.*

@Database(
    entities = [Assent::class, Suffix::class, Paronym::class, Settings::class, SteadyExpression::class],
    version = 2,
    exportSchema = false
)
abstract class MainDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null

        private val MIGRATION_1_2 = Migration(1, 2) {
            it.execSQL(
                "ALTER TABLE SettingsTable ADD COLUMN showingButtonBackground INTEGER NOT NULL DEFAULT 1"
            )
        }

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
                    .addMigrations(MIGRATION_1_2)
                    .allowMainThreadQueries()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}