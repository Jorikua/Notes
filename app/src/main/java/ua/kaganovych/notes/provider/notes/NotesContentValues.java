package ua.kaganovych.notes.provider.notes;

import java.util.Date;

import android.content.ContentResolver;
import android.net.Uri;

import ua.kaganovych.notes.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code notes} table.
 */
public class NotesContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return NotesColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver,  NotesSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public NotesContentValues putDescription(String value) {
        mContentValues.put(NotesColumns.DESCRIPTION, value);
        return this;
    }

    public NotesContentValues putDescriptionNull() {
        mContentValues.putNull(NotesColumns.DESCRIPTION);
        return this;
    }

    public NotesContentValues putImage(String value) {
        mContentValues.put(NotesColumns.IMAGE, value);
        return this;
    }

    public NotesContentValues putImageNull() {
        mContentValues.putNull(NotesColumns.IMAGE);
        return this;
    }
}
