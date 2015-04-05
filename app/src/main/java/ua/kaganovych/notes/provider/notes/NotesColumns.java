package ua.kaganovych.notes.provider.notes;

import android.net.Uri;
import android.provider.BaseColumns;

import ua.kaganovych.notes.provider.MyNotesProvider;

/**
 * Columns for the {@code notes} table.
 */
public class NotesColumns implements BaseColumns {
    public static final String TABLE_NAME = "notes";
    public static final Uri CONTENT_URI = Uri.parse(MyNotesProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    public static final String DESCRIPTION = "description";

    public static final String IMAGE = "image";

    public static final String DATE = "date";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            DESCRIPTION,
            IMAGE,
            DATE
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(DESCRIPTION) || c.contains("." + DESCRIPTION)) return true;
            if (c.equals(IMAGE) || c.contains("." + IMAGE)) return true;
            if (c.equals(DATE) || c.contains("." + DATE)) return true;
        }
        return false;
    }

}
