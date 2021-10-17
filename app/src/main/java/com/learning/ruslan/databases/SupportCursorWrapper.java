package com.learning.ruslan.databases;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.learning.ruslan.Settings;
import com.learning.ruslan.databases.RusDbSchema.SupportTable.Cols;

public class SupportCursorWrapper extends CursorWrapper {

    public SupportCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Settings getSettings() {
        Settings settings = new Settings();

        settings.set(
                getInt(getColumnIndex(Cols.CHECKED)) == 1,
                getInt(getColumnIndex(Cols.PAUSE)),
                getString(getColumnIndex(Cols.THEME)),
                getInt(getColumnIndex(Cols.QUESTIONS)),
                getString(getColumnIndex(Cols.LANGUAGE))
        );

        settings.setId(getInt(getColumnIndex(Cols.ID)));

        return settings;
    }
}
