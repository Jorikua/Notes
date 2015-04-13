package ua.kaganovych.notes;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;
import java.util.UUID;

import ua.kaganovych.notes.provider.notes.NotesColumns;

public class NoteDetailed extends ActionBarActivity {

    private static final String TAG = "TAG";

    private EditText mDescription;
    private ImageView mImageView;
    private Uri noteUri;
    private Cursor mCursor;
    private MenuItem mDeleteButton;
    private MenuItem mShareButton;
    private boolean b = false;

    public String photoFileName = UUID.randomUUID() + ".jpg";
    private Uri mPhotoUri;
    private String mImagePath;

    private static final int CAPTURE_PHOTO_REQUEST = 100;
    private static final int PICK_PHOTO_FROM_GALLERY_REQUEST = 200;

    private String[] array = {"Camera", "Photo Gallery"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detailed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mImageView = (ImageView) findViewById(R.id.imageView);
        mDescription = (EditText) findViewById(R.id.description);
        mDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateButton(charSequence);
                invalidateOptionsMenu();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPhotoUri = getPhotoUri(photoFileName);
        mImagePath = mPhotoUri.getPath();
        Log.d("TAG", mPhotoUri.getPath());

        Bundle extras = getIntent().getExtras();

        noteUri = (savedInstanceState == null) ? null : (Uri) savedInstanceState.getParcelable(MainActivity.PARCELABLE_URI_DATA);


        if (extras != null) {
            noteUri = extras.getParcelable(MainActivity.PARCELABLE_URI_DATA);
            String[] projection = NotesColumns.ALL_COLUMNS;
            mCursor = getContentResolver().query(noteUri, projection, null, null, null);
            if (mCursor != null) {
                mCursor.moveToFirst();
                mDescription.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(NotesColumns.DESCRIPTION)));
            }
            if (notEmpty(mImagePath)) {
                mImageView.setVisibility(View.VISIBLE);
                Picasso.with(this).load(mPhotoUri).into(mImageView);
            }
        }
    }

    private void saveNote() {
        String description = mDescription.getText().toString();
        if (description.trim().length() == 0) {
            return;
        }

        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTimeInMillis();

        ContentValues cv = new ContentValues();
        cv.put(NotesColumns.DESCRIPTION, mDescription.getText().toString());
        cv.put(NotesColumns.DATE, time);
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
        getMenuInflater().inflate(R.menu.menu_note_detailed, menu);
        mDeleteButton = menu.findItem(R.id.delete);
        mShareButton = menu.findItem(R.id.share);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mDeleteButton.setVisible(b);
        mShareButton.setVisible(b);
        return super.onPrepareOptionsMenu(menu);
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
                return true;
            case R.id.photo:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if (which == 0) {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                            startActivityForResult(cameraIntent, CAPTURE_PHOTO_REQUEST);
                            Log.d("TAG", "Camera");
                        } else if (which == 1) {
                            Intent intent = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, PICK_PHOTO_FROM_GALLERY_REQUEST);
                            Log.d("TAG", "Gallery");
                        }
                    }
                }).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAPTURE_PHOTO_REQUEST:
                if (resultCode == RESULT_OK) {
                    mImageView.setVisibility(View.VISIBLE);
                    Picasso.with(NoteDetailed.this).load(mPhotoUri).into(mImageView);
                    break;
                }
            case PICK_PHOTO_FROM_GALLERY_REQUEST:
                if (data != null) {
                    Uri photoUri = data.getData();
                    mImageView.setVisibility(View.VISIBLE);
                    Picasso.with(NoteDetailed.this).load(photoUri).into(mImageView);
                    break;
                }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        saveNote();
        outState.putParcelable(MainActivity.PARCELABLE_URI_DATA, noteUri);
    }

    private void updateButton(CharSequence s) {
        String text = null;
        if (s != null) {
            text = s.toString();
        }
        if (text != null && text.trim().length() != 0) {
            b = true;
        } else {
            b = false;
        }
    }

    private boolean notEmpty(String v) {
        return v != null && !v.equals("");
    }

    private Uri getPhotoUri(String fileName) {
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getPackageName());
        if (!directory.exists() && !directory.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }
        return Uri.fromFile(new File(directory.getPath() + File.separator + fileName));
    }
}
