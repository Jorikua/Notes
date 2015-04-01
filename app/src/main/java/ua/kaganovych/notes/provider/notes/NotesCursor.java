package ua.kaganovych.notes.provider.notes;

import java.util.Date;

import android.database.Cursor;

import ua.kaganovych.notes.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code notes} table.
 */
public class NotesCursor extends AbstractCursor implements NotesModel {
    public NotesCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(NotesColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code description} value.
     * Can be {@code null}.
     */
    public String getDescription() {
        String res = getStringOrNull(NotesColumns.DESCRIPTION);
        return res;
    }

    /**
     * Get the {@code image} value.
     * Can be {@code null}.
     */
    public String getImage() {
        String res = getStringOrNull(NotesColumns.IMAGE);
        return res;
    }
}
