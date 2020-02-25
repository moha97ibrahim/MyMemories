package com.flatplay.mymemories.ui.more;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.flatplay.mymemories.R;
import com.flatplay.mymemories.ui.SplashAndLogIn.SplashScreenActivity;
import com.flatplay.mymemories.utility.AlarmReceiver;
import com.flatplay.mymemories.utility.TimePickerFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class MoreFragment extends Fragment implements TimePickerDialog.OnTimeSetListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private TextView logoutBtn, timeToRemind, alarmTextView;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TimePicker alarmTimePicker;
    //private static AlarmActivity inst;
    private Switch remindMeOnOff;
    private Calendar calendar;
    private int currentHour, currentMinute;
    private String amPm;
    private TimePickerDialog timePickerDialog;
    private int hour,min;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_more, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        timeToRemind = root.findViewById(R.id.time_to_remind);
        timeToRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        timeToRemind.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                        hour = hourOfDay;
                        min = minutes;
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
                Log.d("MyActivity", "Alarm On");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE,min );
                Intent myIntent = new Intent(getActivity(), AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, myIntent, 0);
                alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
            }
        });


//        alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
//        remindMeOnOff = root.findViewById(R.id.remind_Switch);
//        remindMeOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//
//                } else {
//                    alarmManager.cancel(pendingIntent);
//                    setAlarmText("");
//                    Log.d("MyActivity", "Alarm Off");
//                }
//            }
//        });


        logoutBtn = root.findViewById(R.id.logOut_btn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent i = new Intent(getActivity(), SplashScreenActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        return root;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        timeToRemind.setText("Hour: " + hourOfDay + " Minute: " + minute);
    }
}