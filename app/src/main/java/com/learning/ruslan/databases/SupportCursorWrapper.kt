package com.learning.ruslan.databases

//import com.learning.ruslan.databases.RusDbSchema.SupportTable
//import android.database.CursorWrapper
//import android.database.Cursor
//import com.learning.ruslan.*
//import com.learning.ruslan.settings.Settings
//
//class SupportCursorWrapper(cursor: Cursor?) : CursorWrapper(cursor) {
//    val settings: Settings
//        get() {
//            val settings = Settings()
//            settings[getInt(getColumnIndex(SupportTable.Cols.CHECKED)) == 1, getInt(getColumnIndex(
//                SupportTable.Cols.PAUSE)), getString(getColumnIndex(SupportTable.Cols.THEME)), getInt(
//                getColumnIndex(
//                    SupportTable.Cols.QUESTIONS))] =
//                getString(getColumnIndex(SupportTable.Cols.LANGUAGE))
//            settings.id = getInt(getColumnIndex(SupportTable.Cols.ID))
//            return settings
//        }
//}