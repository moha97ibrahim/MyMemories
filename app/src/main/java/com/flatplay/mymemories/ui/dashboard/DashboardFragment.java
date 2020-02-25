package com.flatplay.mymemories.ui.dashboard;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.flatplay.mymemories.R;
import com.flatplay.mymemories.db.DBContract;
import com.google.firebase.auth.TwitterAuthCredential;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DashboardFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private ListView dashEventListView;
    private DashboardCursorAdapter dashboardCursorAdapter;
    private static final String DATE_FORMAT_4 = "dd/MM/yyyy";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        TextView textView = root.findViewById(R.id.text_dashboard);

        //listView
        dashEventListView = root.findViewById(R.id.dash_event_list_view);
        dashboardCursorAdapter = new DashboardCursorAdapter(getActivity(),null);
        dashEventListView.setAdapter(dashboardCursorAdapter);
        dashEventListView.setEmptyView(textView);
        callLoader();



        return root;
    }

    private void callLoader() {
        getActivity().getSupportLoaderManager().restartLoader(0, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String selection = DBContract.event.COLUMN_EVENT_TIME_STAMP + " >=?";
        String[] selectionArgs = new String[]{getCurrentFullDate()};

        String[] pro = {
                DBContract.event._ID,
                DBContract.event.COLUMN_EVENT_DATE,
                DBContract.event.COLUMN_EVENT_SUBJECT,
                DBContract.event.COLUMN_EVENT_TITLE,
                DBContract.event.COLUMN_EVENT_STATUS,
                DBContract.event.COLUMN_EVENT_TIME_STAMP

        };


        return new CursorLoader(getActivity(),
                DBContract.event.CONTENT_URI,
                pro,
                selection,
                selectionArgs, ""+DBContract.event.COLUMN_EVENT_TIME_STAMP+" ASC ");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        dashboardCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        dashboardCursorAdapter.swapCursor(null);

    }

    public static String getCurrentFullDate() {
        Calendar calendar;
        SimpleDateFormat dateFormat;
        String date;
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat(DATE_FORMAT_4);
        date = dateFormat.format(calendar.getTime());
        date = String.valueOf(getTimeMillis(date));
        return date;
    }

    private static long getTimeMillis(String dueDate) {
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