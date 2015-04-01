package ua.kaganovych.notes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import ua.kaganovych.notes.provider.notes.NotesColumns;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private NoteCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView)findViewById(R.id.listView);

        getSupportLoaderManager().initLoader(0, null, this);

        String projection[] = {NotesColumns._ID, NotesColumns.DESCRIPTION};
        Cursor cursor = getContentResolver().query(NotesColumns.CONTENT_URI, projection, null, null, null);
        if (cursor != null) {
            cursor.setNotificationUri(getContentResolver(), NotesColumns.CONTENT_URI);
            getContentResolver().notifyChange(NotesColumns.CONTENT_URI, null);
        }
        mAdapter = new NoteCursorAdapter(this, cursor, 0);
        listView.setAdapter(mAdapter);
        if (cursor != null) {
            cursor.close();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addNote:
                startActivity(new Intent(this, NoteDetailed.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = NotesColumns.ALL_COLUMNS;
        CursorLoader cursorLoader = new CursorLoader(this, NotesColumns.CONTENT_URI, projection, null, null, null);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.changeCursor(null);
    }
}
