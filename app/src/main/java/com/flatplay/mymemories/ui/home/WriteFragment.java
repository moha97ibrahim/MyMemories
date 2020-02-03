package com.flatplay.mymemories.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import com.flatplay.mymemories.ui.addEvents.AddEventsActivity;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
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
                DBContract.event.COLUMN_EVENT_TITLE

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
}