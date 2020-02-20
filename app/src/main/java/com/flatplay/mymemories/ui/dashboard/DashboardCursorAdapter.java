package com.flatplay.mymemories.ui.dashboard;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import com.flatplay.mymemories.R;
import com.flatplay.mymemories.db.DBContract;


public class DashboardCursorAdapter extends CursorAdapter {

    private CardView dasheventCartItem;
    private TextView title, subject,date;
    private String eventType, eventDate, oldDate = null;
    private LinearLayout linearLayout;

    public DashboardCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.dashboard_list_view, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        title = view.findViewById(R.id.dash_list_event_title);
        subject = view.findViewById(R.id.dash_list_event_subject);
        linearLayout = view.findViewById(R.id.linear);
        date = view.findViewById(R.id.dash_date_view);



        title.setText(cursor.getString(cursor.getColumnIndex(DBContract.event.COLUMN_EVENT_TITLE)));
        subject.setText(cursor.getString(cursor.getColumnIndex(DBContract.event.COLUMN_EVENT_SUBJECT)));
        eventType = cursor.getString(cursor.getColumnIndex(DBContract.event.COLUMN_EVENT_STATUS));
        eventDate = cursor.getString(cursor.getColumnIndex(DBContract.event.COLUMN_EVENT_DATE));

        if (eventDate.equals(oldDate)){
            linearLayout.setVisibility(View.INVISIBLE);
            date.setText("invisibel");
        }
        else{
            linearLayout.setVisibility(View.VISIBLE);
            oldDate = eventDate;
            date.setText("viible"+eventDate +"="+oldDate);
        }

        //oldDate = eventDate;
        //seaprateByDate(eventDate,oldDate);
        View view1 = view.findViewById(R.id.dash_event_color);
        setEventColor(view1, eventType);


    }

    private void seaprateByDate(String eventDate, String oldDate) {

    }


    private void setEventColor(View view1, String eventType) {
        if (eventType.equals("DAY"))
            view1.setBackgroundColor(Color.BLUE);
        if (eventType.equals("MONTH"))
            view1.setBackgroundColor(Color.GREEN);
        if (eventType.equals("YEAR"))
            view1.setBackgroundColor(Color.RED);
    }
}
