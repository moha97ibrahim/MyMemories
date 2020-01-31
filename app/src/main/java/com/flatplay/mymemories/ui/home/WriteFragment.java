package com.flatplay.mymemories.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.flatplay.mymemories.R;
import com.flatplay.mymemories.ui.addEvents.AddEventsActivity;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WriteFragment extends Fragment {

    private WriteViewModel writeViewModel;
    private FloatingActionButton addEventfab;
    private CompactCalendarView compactCalendarView;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM -yyyy", Locale.getDefault());
    private String date;
    private ActionBar actionBar;
    private AppCompatActivity activity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        writeViewModel =
                ViewModelProviders.of(this).get(WriteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_write, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        writeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //fab
        addEventfab = root.findViewById(R.id.add_event_fab);
        addEventfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEventsActivity.class);
                intent.putExtra("DATE",date);
                startActivity(intent);
                getActivity().finish();
            }
        });

        //calendar
        compactCalendarView = root.findViewById(R.id.compactcalendar_view);
        //toolbar
        activity = (AppCompatActivity) getActivity();
        actionBar = activity.getSupportActionBar();
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                date = getDate(dateClicked.getTime());
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(simpleDateFormat.format(firstDayOfNewMonth));
            }
        });

        return root;
    }

    private String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}