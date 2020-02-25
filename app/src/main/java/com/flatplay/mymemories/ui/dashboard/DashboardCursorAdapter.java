package com.flatplay.mymemories.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import com.flatplay.mymemories.R;
import com.flatplay.mymemories.db.DBContract;
import com.flatplay.mymemories.ui.showEvent.ShowEventActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DashboardCursorAdapter extends CursorAdapter {

    private CardView dasheventCartItem;
    private TextView title, subject,date , eventdate,eventmonth;
    private String eventType, eventDate, oldDate = null,timestamap;
    private LinearLayout linearLayout;
    private static final String DATE_FORMAT_4 = "dd";
    private static final String DAY_FORMAT_4 = "MMM";
    private static final String DATE_FORMAT = "EEE, MMM d, ''yy";
    boolean SET_DATE = false;

    public DashboardCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.dashboard_list_view, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        title = view.findViewById(R.id.dash_list_event_title);
        subject = view.findViewById(R.id.dash_list_event_subject);
        linearLayout = view.findViewById(R.id.linear);
        date = view.findViewById(R.id.dash_date_view);
        eventdate = view.findViewById(R.id.dash_event_date);
        eventmonth = view.findViewById(R.id.dash_event_month);




        title.setText(cursor.getString(cursor.getColumnIndex(DBContract.event.COLUMN_EVENT_TITLE)));
        subject.setText(cursor.getString(cursor.getColumnIndex(DBContract.event.COLUMN_EVENT_SUBJECT)));
        eventType = cursor.getString(cursor.getColumnIndex(DBContract.event.COLUMN_EVENT_STATUS));
        eventDate = cursor.getString(cursor.getColumnIndex(DBContract.event.COLUMN_EVENT_DATE));
        timestamap = cursor.getString(cursor.getColumnIndex(DBContract.event.COLUMN_EVENT_TIME_STAMP));

        eventdate.setText(getDate(Long.parseLong(timestamap),DATE_FORMAT_4));
        eventmonth.setText(getDate(Long.parseLong(timestamap),DAY_FORMAT_4));

        if (eventDate.equals(oldDate)){
            linearLayout.setVisibility(View.GONE);
            date.setText("");
        }
        else{
            linearLayout.setVisibility(View.VISIBLE);
            oldDate = eventDate;
            if(getCurrentFullDate().equals(eventDate)){
                date.setText("Today Event");
                SET_DATE=true;
            }else if(SET_DATE){
                date.setText("Tomorrow");
                SET_DATE = false;
            }else {
                date.setText(getDate(Long.parseLong(timestamap),DATE_FORMAT));
            }

        }

        //seaprateByDate(eventDate,oldDate);
        View view1 = view.findViewById(R.id.dash_event_color);
        setEventColor(view1, eventType);


        dasheventCartItem = view.findViewById(R.id.dashboardCardItem);
        dasheventCartItem.setTag(cursor.getPosition());
        dasheventCartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), "" + getItemId((Integer) v.getTag()), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context.getApplicationContext(), ShowEventActivity.class);
                intent.putExtra("id", ""+getItemId((Integer) v.getTag()));
                context.startActivity(intent);
            }
        });

    }


    private void setEventColor(View view1, String eventType) {
        if (eventType.equals("DAY"))
            view1.setBackgroundColor(Color.BLUE);
        if (eventType.equals("MONTH"))
            view1.setBackgroundColor(Color.GREEN);
        if (eventType.equals("YEAR"))
            view1.setBackgroundColor(Color.RED);
    }


    private String getDate(long milliSeconds,String dateformat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateformat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String getCurrentFullDate() {
        Calendar calendar;
        SimpleDateFormat dateFormat;
        String date;
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = dateFormat.format(calendar.getTime());
        return date;
    }

}
