package sk.upjs.ics.jot;

import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import sk.upjs.ics.jot.provider.JotContentProvider;
import sk.upjs.ics.jot.provider.JotContract;

import static sk.upjs.ics.jot.Constants.DEFAULT_LOADER_ID;
import static sk.upjs.ics.jot.Constants.NO_COOKIE;
import static sk.upjs.ics.jot.Constants.NO_CURSOR;
import static sk.upjs.ics.jot.Constants.NO_FLAGS;
import static sk.upjs.ics.jot.Constants.NO_SELECTION;
import static sk.upjs.ics.jot.Constants.NO_SELECTION_ARGS;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemLongClickListener {
    private GridView notesGridView;

    private SimpleCursorAdapter notesGridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesGridView = (GridView) findViewById(R.id.notesGridView);

        String[] from = { JotContract.Note.DESCRIPTION };
        int[] to = { R.id.cardText };
        notesGridViewAdapter = new SimpleCursorAdapter(this, R.layout.note, NO_CURSOR, from, to, NO_FLAGS);
        notesGridView.setAdapter(notesGridViewAdapter);
        notesGridView.setOnItemLongClickListener(this);

        getLoaderManager().initLoader(DEFAULT_LOADER_ID, Bundle.EMPTY, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id != DEFAULT_LOADER_ID) {
            throw new IllegalStateException("Invalid loader ID " + id);
        }

        CursorLoader cursorLoader = new CursorLoader(this);
        cursorLoader.setUri(JotContract.Note.CONTENT_URI);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursor.setNotificationUri(getContentResolver(), JotContract.Note.CONTENT_URI);
        notesGridViewAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        notesGridViewAdapter.swapCursor(NO_CURSOR);
    }

    public void onFabClick(View view) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(JotContract.Note.DESCRIPTION, "Test this");

        AsyncQueryHandler queryHandler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                Toast.makeText(MainActivity.this, "Note was saved", Toast.LENGTH_LONG).show();
            }
        };
        queryHandler.startInsert(0, NO_COOKIE, JotContract.Note.CONTENT_URI, contentValues);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        AsyncQueryHandler queryHandler = new AsyncQueryHandler(getContentResolver()) {
            // no specific behavior is required after DELETE is completed
        };
        queryHandler.startDelete(0, null, Uri.withAppendedPath(JotContract.Note.CONTENT_URI, String.valueOf(id)), NO_SELECTION, NO_SELECTION_ARGS);

        return true;
    }
}
