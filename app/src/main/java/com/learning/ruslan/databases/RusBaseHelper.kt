package com.learning.ruslan.databases
//
//import android.content.Context
//import android.database.sqlite.SQLiteDatabase
//import android.database.sqlite.SQLiteOpenHelper
//import com.learning.ruslan.databases.RusDbSchema.*
//
//class RusBaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {
//    override fun onCreate(db: SQLiteDatabase) {
//        db.execSQL("create table " + AssentTable.NAME + "(" +
//                AssentTable.Cols.ID + " integer primary key autoincrement, " +
//                AssentTable.Cols.WORD + " text, " +
//                AssentTable.Cols.POSITION + " integer, " +
//                AssentTable.Cols.CHECKED + " integer)")
//        db.execSQL("create table " + SupportTable.NAME + "(" +
//                SupportTable.Cols.ID + " integer primary key autoincrement, " +
//                SupportTable.Cols.CHECKED + " integer, " +
//                SupportTable.Cols.PAUSE + " integer, " +
//                SupportTable.Cols.THEME + " text, " +
//                SupportTable.Cols.QUESTIONS + " integer, " +
//                SupportTable.Cols.LANGUAGE + " text)")
//        db.execSQL("create table " + SuffixTable.NAME + "(" +
//                SuffixTable.Cols.ID + " integer primary key autoincrement, " +
//                SuffixTable.Cols.WORD + " text, " +
//                SuffixTable.Cols.POSITION + " integer, " +
//                SuffixTable.Cols.ALTERNATIVE + " text)")
//        db.execSQL("create table " + ParonymTable.NAME + "(" +
//                ParonymTable.Cols.ID + " integer primary key autoincrement, " +
//                ParonymTable.Cols.WORD + " text, " +
//                ParonymTable.Cols.VARIANTS + " text, " +
//                ParonymTable.Cols.ALTERNATIVE + " text)")
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
//
//    companion object {
//        private const val VERSION = 4
//        private const val DATABASE_NAME = "RusBase.db"
//    }
//}