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

public class DashboardFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private ListView dashEventListView;
    private DashboardCursorAdapter dashboardCursorAdapter;

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
                null,
                null, ""+DBContract.event.COLUMN_EVENT_DATE+" ASC ");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        dashboardCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        dashboardCursorAdapter.swapCursor(null);

    }
}