package com.learning.ruslan.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.learning.ruslan.databases.RusDbSchema.AssentTable;
import com.learning.ruslan.databases.RusDbSchema.ParonymTable;
import com.learning.ruslan.databases.RusDbSchema.SuffixTable;
import com.learning.ruslan.databases.RusDbSchema.SupportTable;

public class RusBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 4;
    private static final String DATABASE_NAME = "RusBase.db";


    public RusBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + AssentTable.NAME + "(" +
                AssentTable.Cols.ID + " integer primary key autoincrement, " +
                AssentTable.Cols.WORD + " text, " +
                AssentTable.Cols.POSITION + " integer, " +
                AssentTable.Cols.CHECKED + " integer)");

        db.execSQL("create table " + SupportTable.NAME + "(" +
                SupportTable.Cols.ID + " integer primary key autoincrement, " +
                SupportTable.Cols.CHECKED + " integer, " +
                SupportTable.Cols.PAUSE + " integer, " +
                SupportTable.Cols.THEME + " text, " +
                SupportTable.Cols.QUESTIONS + " integer, " +
                SupportTable.Cols.LANGUAGE + " text)");

        db.execSQL("create table " + SuffixTable.NAME + "(" +
                SuffixTable.Cols.ID + " integer primary key autoincrement, " +
                SuffixTable.Cols.WORD + " text, " +
                SuffixTable.Cols.POSITION + " integer, " +
                SuffixTable.Cols.ALTERNATIVE + " text)");

        db.execSQL("create table " + ParonymTable.NAME + "(" +
                ParonymTable.Cols.ID + " integer primary key autoincrement, " +
                ParonymTable.Cols.WORD + " text, " +
                ParonymTable.Cols.VARIANTS + " text, " +
                ParonymTable.Cols.ALTERNATIVE + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
