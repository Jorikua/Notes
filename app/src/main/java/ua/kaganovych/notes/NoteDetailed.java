package ua.kaganovych.notes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.Calendar;

import ua.kaganovych.notes.provider.notes.NotesColumns;

public class NoteDetailed extends ActionBarActivity {

    private EditText mDescription;
    private Uri noteUri;
    private Cursor mCursor;
    private long mTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detailed);

        mDescription = (EditText) findViewById(R.id.description);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();

        noteUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(MainActivity.PARCELABLE_DATA);

        Calendar calendar = Calendar.getInstance();
        mTime = calendar.getTimeInMillis();

        if (extras != null) {
            noteUri = extras.getParcelable(MainActivity.PARCELABLE_DATA);
            String[] projection = NotesColumns.ALL_COLUMNS;
            mCursor = getContentResolver().query(noteUri, projection, null, null, null);
            if (mCursor != null) {
                mCursor.moveToFirst();
                mDescription.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(NotesColumns.DESCRIPTION)));
            }
        }
    }

    private void saveNote() {
        String description = mDescription.getText().toString();
        if (description.length() == 0) {
            return;
        }

        ContentValues cv = new ContentValues();
        cv.put(NotesColumns.DESCRIPTION, mDescription.getText().toString());
        cv.put(NotesColumns.DATE, mTime);
        if (noteUri == null) {
            getContentResolver().insert(NotesColumns.CONTENT_URI, cv);
        } else if (mCursor != null) {
            mCursor.moveToFirst();
            String currentDesc = mCursor.getString(mCursor.getColumnIndexOrThrow(NotesColumns.DESCRIPTION));
            if (!description.equals(currentDesc)) {
                getContentResolver().update(noteUri, cv, null, null);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note_detailed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                saveNote();
                return true;
            case R.id.delete:
                if (noteUri == null) {
                    finish();
                    return false;
                }
                getContentResolver().delete(noteUri, null, null);
                finish();
                return true;
            case R.id.save:
                saveNote();
                finish();
                return true;
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mDescription.getText().toString());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share text to..."));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        saveNote();
        outState.putParcelable(MainActivity.PARCELABLE_DATA, noteUri);
    }
}
