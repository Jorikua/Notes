package ua.kaganovych.notes.provider.notes;

import ua.kaganovych.notes.provider.base.BaseModel;

import java.util.Date;

/**
 * Data model for the {@code notes} table.
 */
public interface NotesModel extends BaseModel {

    /**
     * Get the {@code description} value.
     * Can be {@code null}.
     */
    String getDescription();

    /**
     * Get the {@code image} value.
     * Can be {@code null}.
     */
    String getImage();
}
