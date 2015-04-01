package ua.kaganovych.notes.provider.notes;

import java.util.Date;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import ua.kaganovych.notes.provider.base.AbstractSelection;

/**
 * Selection for the {@code notes} table.
 */
public class NotesSelection extends AbstractSelection<NotesSelection> {
    @Override
    protected Uri baseUri() {
        return NotesColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @param sortOrder How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort
     *            order, which may be unordered.
     * @return A {@code NotesCursor} object, which is positioned before the first entry, or null.
     */
    public NotesCursor query(ContentResolver contentResolver, String[] projection, String sortOrder) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), sortOrder);
        if (cursor == null) return null;
        return new NotesCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null)}.
     */
    public NotesCursor query(ContentResolver contentResolver, String[] projection) {
        return query(contentResolver, projection, null);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null, null)}.
     */
    public NotesCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null, null);
    }


    public NotesSelection id(long... value) {
        addEquals("notes." + NotesColumns._ID, toObjectArray(value));
        return this;
    }

    public NotesSelection description(String... value) {
        addEquals(NotesColumns.DESCRIPTION, value);
        return this;
    }

    public NotesSelection descriptionNot(String... value) {
        addNotEquals(NotesColumns.DESCRIPTION, value);
        return this;
    }

    public NotesSelection descriptionLike(String... value) {
        addLike(NotesColumns.DESCRIPTION, value);
        return this;
    }

    public NotesSelection descriptionContains(String... value) {
        addContains(NotesColumns.DESCRIPTION, value);
        return this;
    }

    public NotesSelection descriptionStartsWith(String... value) {
        addStartsWith(NotesColumns.DESCRIPTION, value);
        return this;
    }

    public NotesSelection descriptionEndsWith(String... value) {
        addEndsWith(NotesColumns.DESCRIPTION, value);
        return this;
    }

    public NotesSelection image(String... value) {
        addEquals(NotesColumns.IMAGE, value);
        return this;
    }

    public NotesSelection imageNot(String... value) {
        addNotEquals(NotesColumns.IMAGE, value);
        return this;
    }

    public NotesSelection imageLike(String... value) {
        addLike(NotesColumns.IMAGE, value);
        return this;
    }

    public NotesSelection imageContains(String... value) {
        addContains(NotesColumns.IMAGE, value);
        return this;
    }

    public NotesSelection imageStartsWith(String... value) {
        addStartsWith(NotesColumns.IMAGE, value);
        return this;
    }

    public NotesSelection imageEndsWith(String... value) {
        addEndsWith(NotesColumns.IMAGE, value);
        return this;
    }
}
