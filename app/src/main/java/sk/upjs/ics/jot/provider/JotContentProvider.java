package sk.upjs.ics.jot.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import static sk.upjs.ics.jot.Constants.ALL_COLUMNS;
import static sk.upjs.ics.jot.Constants.NO_CONTENT_OBSERVER;
import static sk.upjs.ics.jot.Constants.NO_GROUP_BY;
import static sk.upjs.ics.jot.Constants.NO_HAVING;
import static sk.upjs.ics.jot.Constants.NO_NULL_COLUMN_HACK;
import static sk.upjs.ics.jot.Constants.NO_SELECTION;
import static sk.upjs.ics.jot.Constants.NO_SELECTION_ARGS;
import static sk.upjs.ics.jot.Constants.NO_SORT_ORDER;

public class JotContentProvider extends ContentProvider {

    private DatabaseOpenHelper databaseOpenHelper;

    @Override
    public boolean onCreate() {
        databaseOpenHelper = new DatabaseOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = databaseOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(JotContract.Note.TABLE_NAME, ALL_COLUMNS, NO_SELECTION, NO_SELECTION_ARGS, NO_GROUP_BY, NO_HAVING, NO_SORT_ORDER);
        cursor.setNotificationUri(getContext().getContentResolver(), JotContract.Note.CONTENT_URI);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();

        long id = db.insert(JotContract.Note.TABLE_NAME, NO_NULL_COLUMN_HACK, values);

        getContext().getContentResolver().notifyChange(JotContract.Note.CONTENT_URI, NO_CONTENT_OBSERVER);

        return Uri.withAppendedPath(JotContract.Note.CONTENT_URI, String.valueOf(id));
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String id = uri.getLastPathSegment();
        SQLiteDatabase db = databaseOpenHelper.getWritableDatabase();

        String[] whereArgs = { id };

        int affectedRows = db.delete(JotContract.Note.TABLE_NAME, JotContract.Note._ID + "=?", whereArgs);

        getContext().getContentResolver().notifyChange(JotContract.Note.CONTENT_URI, NO_CONTENT_OBSERVER);

        return affectedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

}
