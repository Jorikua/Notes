package ua.kaganovych.notes;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import ua.kaganovych.notes.provider.notes.NotesColumns;

public class NoteCursorAdapter extends CursorAdapter {

    public NoteCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    private static class ViewHolder {
        private TextView mLabel;
        private int mLabelIndex;
        private TextView mDate;
        private int mDateIndex;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_note, viewGroup, false);
        ViewHolder holder = new ViewHolder();
        holder.mLabel = (TextView)view.findViewById(R.id.label);
        holder.mDate = (TextView)view.findViewById(R.id.date);
        holder.mLabelIndex = cursor.getColumnIndex(NotesColumns.DESCRIPTION);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder)view.getTag();
        holder.mLabel.setText(cursor.getString(holder.mLabelIndex));
//        holder.mDate.setText(new SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault()).format(holder.mDate));
        holder.mDate.setText(DateFormat.format("dd.MM.yy HH:mm", new Date()));
    }
}
