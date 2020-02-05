package com.flatplay.mymemories.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.flatplay.mymemories.R;
import com.flatplay.mymemories.db.DBContract;
import com.flatplay.mymemories.db.DBHelper;
import com.flatplay.mymemories.ui.addEvents.AddEventsActivity;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WriteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private FloatingActionButton addEventfab;
    private CompactCalendarView compactCalendarView;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM -yyyy", Locale.getDefault());
    private String date;
    private ActionBar actionBar;
    private AppCompatActivity activity;
    private ListView eventListView;
    private EventCursorAdapter eventCursorAdapter;
    private String[] selectionArgs;
    private TextView textView, dateView;
    private static final String DATE_FORMAT_4 = "dd/MM/yyyy";
    private ImageButton calendar;
    private boolean STATE = true;
    private DBHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dbHelper = new DBHelper(getContext());

        //toolbar
        activity = (AppCompatActivity) getActivity();
        actionBar = activity.getSupportActionBar();


        View root = inflater.inflate(R.layout.fragment_write, container, false);
        textView = root.findViewById(R.id.text_home);
        dateView = root.findViewById(R.id.date_view);


        //fab
        addEventfab = root.findViewById(R.id.add_event_fab);
        addEventfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEventsActivity.class);
                intent.putExtra("DATE", date);
                startActivity(intent);
                getActivity().finish();
            }
        });


        //calendar
        compactCalendarView = root.findViewById(R.id.compactcalendar_view);

        compactCalendarView.setUseThreeLetterAbbreviation(true);

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                date = getDate(dateClicked.getTime());
                Log.e("date", Arrays.toString(selectionArgs));
                selectionArgs = new String[]{date};
                callLoader();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                dateView.setText(simpleDateFormat.format(firstDayOfNewMonth));
                callLoader();
            }
        });

        updateEventInCalendar();

        //hide and show calendar
        calendar = root.findViewById(R.id.calendar);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (STATE) {
                    compactCalendarView.hideCalendarWithAnimation();
                    STATE = false;
                } else {
                    compactCalendarView.showCalendarWithAnimation();
                    STATE = true;
                }
            }
        });


        //list view
        eventListView = root.findViewById(R.id.event_list_view);
        eventCursorAdapter = new EventCursorAdapter(getActivity(), null);
        eventListView.setAdapter(eventCursorAdapter);
        date = getCurrentFullDate();
        selectionArgs = new String[]{date};
        Log.e("date", Arrays.toString(selectionArgs));
        dateView.setText(date);
        callLoader(); //load Events





        return root;
    }


    private void callLoader() {
        getActivity().getSupportLoaderManager().restartLoader(0, null, this);
        textView.setText("No Memories");
        eventListView.setEmptyView(textView);

    }

    private String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    public static String getCurrentFullDate() {
        Calendar calendar;
        SimpleDateFormat dateFormat;
        String date;
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat(DATE_FORMAT_4);
        date = dateFormat.format(calendar.getTime());
        return date;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String selection = DBContract.event.COLUMN_EVENT_DATE + " =?";
        String[] pro = {
                DBContract.event._ID,
                DBContract.event.COLUMN_EVENT_DATE,
                DBContract.event.COLUMN_EVENT_SUBJECT,
                DBContract.event.COLUMN_EVENT_TITLE,
                DBContract.event.COLUMN_EVENT_STATUS

        };


        return new CursorLoader(getActivity(),
                DBContract.event.CONTENT_URI,
                pro,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        eventCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        eventCursorAdapter.swapCursor(null);

    }

    private void updateEventInCalendar() {
        updateByYear();
        updateByMonth();
        updateByDay();
    }


    private void updateByYear(){
        ArrayList<ArrayList<String>> arrayList1 = new ArrayList<>();
        ArrayList<String> arrayList2 = new ArrayList<>();
        arrayList1 = dbHelper.getEventByType("YEAR");
        Event event = null;
        for (int i = 0; i < arrayList1.size(); i++) {
            Log.e("year", "" + arrayList2);
            arrayList2 = arrayList1.get(i);
            event = new Event(Color.RED, getTimeMillis(arrayList2.get(0)));
            compactCalendarView.addEvent(event);
        }
    }


    private void updateByMonth() {
        ArrayList<ArrayList<String>> arrayList1 = new ArrayList<>();
        ArrayList<String> arrayList2 = new ArrayList<>();
        arrayList1 = dbHelper.getEventByType("MONTH");
        Event event = null;
        for (int i = 0; i < arrayList1.size(); i++) {
            Log.e("year", "" + arrayList2);
            arrayList2 = arrayList1.get(i);
            event = new Event(Color.GREEN, getTimeMillis(arrayList2.get(0)));
            compactCalendarView.addEvent(event);
        }

    }

    private void updateByDay() {
        ArrayList<ArrayList<String>> arrayList1 = new ArrayList<>();
        ArrayList<String> arrayList2 = new ArrayList<>();
        arrayList1 = dbHelper.getEventByType("DAY");
        Event event = null;
        for (int i = 0; i < arrayList1.size(); i++) {
            Log.e("year", "" + arrayList2);
            arrayList2 = arrayList1.get(i);
            event = new Event(Color.BLUE, getTimeMillis(arrayList2.get(0)));
            compactCalendarView.addEvent(event);
        }
    }

    private long getTimeMillis(String dueDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = sdf.parse(dueDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }


}