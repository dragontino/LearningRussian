package com.learning.ruslan.databases
//
//import android.database.Cursor
//import android.database.CursorWrapper
//import com.learning.ruslan.task.Paronym
//import com.learning.ruslan.task.Suffix
//import com.learning.ruslan.task.Task
//import com.learning.ruslan.WordViewModel
//import com.learning.ruslan.databases.RusDbSchema.*
//import com.learning.ruslan.task.Assent
//
//class TaskCursorWrapper(cursor: Cursor?) : CursorWrapper(cursor) {
//    val assent: Assent
//        get() = Assent(
//            getString(getColumnIndex(AssentTable.Cols.WORD)),
//            getInt(getColumnIndex(AssentTable.Cols.POSITION)),
//            getInt(getColumnIndex(AssentTable.Cols.CHECKED)) == 1,
//            getInt(getColumnIndex(AssentTable.Cols.ID))
//        )
//    val suffix: Suffix
//        get() = Suffix(
//            getString(getColumnIndex(SuffixTable.Cols.WORD)),
//            getInt(getColumnIndex(SuffixTable.Cols.POSITION)),
//            getString(getColumnIndex(SuffixTable.Cols.ALTERNATIVE)))
//    val paronym: Paronym
//        get() = Paronym(
//            getString(getColumnIndex(ParonymTable.Cols.WORD)),
//            getString(getColumnIndex(ParonymTable.Cols.VARIANTS)),
//            getString(getColumnIndex(ParonymTable.Cols.ALTERNATIVE))
//        )
//
//    fun getTask(typeId: Int): Task {
//        return when (typeId) {
//            WordViewModel.Companion.SuffixId -> suffix
//            WordViewModel.Companion.ParonymId -> paronym
//            else -> assent
//        }
//    }
//}