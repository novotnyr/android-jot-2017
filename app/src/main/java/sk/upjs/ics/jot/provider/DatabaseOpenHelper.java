package sk.upjs.ics.jot.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sk.upjs.ics.jot.provider.JotContract;

import static sk.upjs.ics.jot.Constants.DEFAULT_CURSOR_FACTORY;
import static sk.upjs.ics.jot.Constants.NO_NULL_COLUMN_HACK;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "jot";

    public static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, "jot", DEFAULT_CURSOR_FACTORY, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableSql());

        insertSampleEntry(db, "Have fun");
        insertSampleEntry(db, "Code Java");
    }

    private String createTableSql() {
        String sqlTemplate = "CREATE TABLE %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s TEXT,"
                + "%s DATETIME"
                + ")";
        return String.format(sqlTemplate,
                JotContract.Note.TABLE_NAME,
                JotContract.Note._ID,
                JotContract.Note.DESCRIPTION,
                JotContract.Note.TIMESTAMP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // upgrade is not supported
    }

    private void insertSampleEntry(SQLiteDatabase db, String description) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(JotContract.Note.DESCRIPTION, description);
        contentValues.put(JotContract.Note.TIMESTAMP, System.currentTimeMillis() / 1000);
        db.insert(JotContract.Note.TABLE_NAME, NO_NULL_COLUMN_HACK, contentValues);
    }
}
