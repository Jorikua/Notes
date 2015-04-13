package ua.kaganovych.notes;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import ua.kaganovych.notes.provider.notes.NotesColumns;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private NoteCursorAdapter mAdapter;
    private ListView mListView;
    private String mQuery;
    private Cursor mCursor;
    
    public static final String PARCELABLE_URI_DATA = "ua.kaganovych.notes.data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listView);
        mListView.setEmptyView(findViewById(R.id.empty));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NoteDetailed.class));
            }
        });

        registerForContextMenu(mListView);


        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater inflater = actionMode.getMenuInflater();
                inflater.inflate(R.menu.note_list_item_context, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_item_delete_note:
                        long[] checkedIds = mListView.getCheckedItemIds();
                        for (long id : checkedIds) {
                            Uri uri = Uri.parse(NotesColumns.CONTENT_URI + "/" + id);
                            getContentResolver().delete(uri, null, null);
                        }
                        actionMode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, NoteDetailed.class);
                Uri noteUri = Uri.parse(NotesColumns.CONTENT_URI + "/" + id);
                intent.putExtra(PARCELABLE_URI_DATA, noteUri);
                startActivity(intent);
            }
        });

        fillData();
    }

    private void fillData() {

        getSupportLoaderManager().initLoader(0, null, this);

        String[] projection = NotesColumns.ALL_COLUMNS;
        mCursor = getContentResolver().query(NotesColumns.CONTENT_URI, projection, null, null, null);
        mAdapter = new NoteCursorAdapter(this, mCursor, 0);
        mListView.setAdapter(mAdapter);

        mListView.setFocusable(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.note_list_item_context, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    mQuery = "";
                } else {
                    mQuery = newText;
                }
                getSupportLoaderManager().restartLoader(0, null, MainActivity.this);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = NotesColumns.ALL_COLUMNS;
        String selection = null;
        String[] selectionArgs = null;
        if (mQuery != null) {
            selection = NotesColumns.DESCRIPTION + " like ? ";
            selectionArgs = new String[] {"%" + mQuery + "%"};
        }
        CursorLoader cursorLoader = new CursorLoader(this, NotesColumns.CONTENT_URI, projection, selection, selectionArgs, NotesColumns.DATE + " DESC");
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
