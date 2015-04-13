package ua.kaganovych.notes;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
        holder.mDateIndex = cursor.getColumnIndex(NotesColumns.DATE);

        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder)view.getTag();
        holder.mLabel.setText(cursor.getString(holder.mLabelIndex));
        long time = cursor.getLong(holder.mDateIndex);
        holder.mDate.setText(formatDate(time));
    }

    static {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        sDateToFormat = new Date();
        sDateTodayZero = new Date(calendar.getTimeInMillis());

        calendar.setTimeInMillis(calendar.getTimeInMillis() - DateUtils.DAY_IN_MILLIS);
        sDateYesterdayZero = new Date(calendar.getTimeInMillis());
        calendar.setTimeInMillis(calendar.getTimeInMillis() - DateUtils.WEEK_IN_MILLIS + DateUtils.DAY_IN_MILLIS*2);
        sDateLastWeekZero = new Date(calendar.getTimeInMillis());

        sDateFormatToday = new SimpleDateFormat("HH:mm", Locale.getDefault());
        sDateFormatNote = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sDateFormatLastWeek = new SimpleDateFormat("cccc", Locale.getDefault());
    }

    private static Date sDateToFormat;
    private static Date sDateTodayZero;
    private static Date sDateYesterdayZero;
    private static Date sDateLastWeekZero;
    private static SimpleDateFormat sDateFormatToday;
    private static SimpleDateFormat sDateFormatNote;
    private static SimpleDateFormat sDateFormatLastWeek;

    public static String formatDate(long time) {
        sDateToFormat.setTime(time);
        if (sDateToFormat.after(sDateTodayZero))
            return sDateFormatToday.format(sDateToFormat);
        else if (sDateToFormat.after(sDateYesterdayZero))
            return "Yesterday";
        else if (sDateToFormat.after(sDateLastWeekZero))
            return sDateFormatLastWeek.format(sDateToFormat);
        else
            return sDateFormatNote.format(sDateToFormat);
    }
}
