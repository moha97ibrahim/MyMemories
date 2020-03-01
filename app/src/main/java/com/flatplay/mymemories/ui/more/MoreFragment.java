package com.flatplay.mymemories.ui.more;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.flatplay.mymemories.R;
import com.flatplay.mymemories.ui.SplashAndLogIn.SplashScreenActivity;
import com.flatplay.mymemories.utility.Alarm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Objects;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class MoreFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private TextView logoutBtn, timeToRemind;
    public static final String NOTIFICATION_CHANNEL_ID = "channel_id";
    public static final String CHANNEL_NAME = "Notification Channel";
    private int importance = NotificationManager.IMPORTANCE_DEFAULT;
    private NotificationChannel notificationChannel;
    public static final int NOTIFICATION_ID = 101;
    int hour, min;
    private TimePickerDialog timePickerDialog;
    private Switch reminderOnOff;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Calendar calendar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String SETTING = "SETTING", ON = "on", OFF = "off";
    private String REMIND_STATUS = "";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_more, container, false);

        reminderOnOff = root.findViewById(R.id.remind_Switch);
        timeToRemind = root.findViewById(R.id.time_to_remind);
        sharedPreferences = getContext().getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        REMIND_STATUS = sharedPreferences.getString(SETTING, null);
        if (Objects.equals(REMIND_STATUS, ON)) {
            reminderOnOff.setChecked(true);
        }
        String DATE = sharedPreferences.getString("DATE", null);
        if (DATE != null) {
            timeToRemind.setText(DATE);
        }


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        calendar = Calendar.getInstance();
        Intent intent = new Intent(getActivity(), Alarm.class);
        alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

        timeToRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        Log.e("adfsd", "" + hourOfDay + ":" + minutes);
                        //createNotification();
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minutes);
                        timeToRemind.setText(hourOfDay + ":" + minutes);
                        reminderOnOff.setChecked(false);
                        editor.putString("DATE", "" + hourOfDay + ":" + minutes);
                        editor.apply();
                    }
                }, hour, min, false);
                timePickerDialog.show();
            }
        });


        reminderOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reminderOnOff.isChecked()) {
                    Toast.makeText(getContext(), "Remainder ON", Toast.LENGTH_SHORT).show();
                    editor.putString(SETTING, ON);
                    editor.apply();
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24, pendingIntent);

                } else {
                    alarmManager.cancel(pendingIntent);
                    Toast.makeText(getContext(), "Remainder OFF", Toast.LENGTH_SHORT).show();
                    editor.putString(SETTING, OFF);
                    editor.apply();

                }
            }
        });

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



}
    