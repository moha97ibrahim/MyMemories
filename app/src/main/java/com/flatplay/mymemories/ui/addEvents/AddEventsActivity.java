package com.flatplay.mymemories.ui.addEvents;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flatplay.mymemories.MainActivity;
import com.flatplay.mymemories.R;
import com.flatplay.mymemories.db.DBContract;


public class AddEventsActivity extends AppCompatActivity {
    private EditText eventTitle, eventSubject, eventBody;
    private CheckBox everyDay, monthly, yearly;
    private Button cancel, save;
    private String title, subject, body, date;
    private TextView eventDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addevents);


        eventTitle = findViewById(R.id.event_title);
        eventSubject = findViewById(R.id.event_subject);
        eventBody = findViewById(R.id.event_body);
        everyDay = findViewById(R.id.checkEveryDay);
        monthly = findViewById(R.id.checkEveryMonth);
        yearly = findViewById(R.id.checkEveryYear);
        cancel = findViewById(R.id.btn_cancel);
        save = findViewById(R.id.btn_save);
        eventDate = findViewById(R.id.event_date);


        //toolbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Event");

        Toast.makeText(AddEventsActivity.this, "" + getIntent().getStringExtra("DATE"), Toast.LENGTH_LONG).show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelEvent();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
            }
        });

        everyDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthly.setChecked(false);
                yearly.setChecked(false);
            }
        });

        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                everyDay.setChecked(false);
                yearly.setChecked(false);
            }
        });

        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthly.setChecked(false);
                everyDay.setChecked(false);
            }
        });

    }

    private void cancelEvent() {
        Intent intent = new Intent(AddEventsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveEvent() {
        if (checkRequiredField() && checkEventStatus()) {
            ContentValues values = new ContentValues();
            values.put(DBContract.event.COLUMN_EVENT_DATE, getIntent().getStringExtra("DATE"));
            values.put(DBContract.event.COLUMN_EVENT_TITLE, title);
            values.put(DBContract.event.COLUMN_EVENT_SUBJECT, subject);
            values.put(DBContract.event.COLUMN_EVENT_BODY, body);
            values.put(DBContract.event.COLUMN_EVENT_STATUS, getStatus());
            Log.e("hfdj", String.valueOf(values));
            Uri newUri = getContentResolver().insert(DBContract.event.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, "Profile Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Profile Saved", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddEventsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }


    private String getStatus() {
        if (everyDay.isChecked()) {
            return "DAY";
        } else if (monthly.isChecked()) {
            return "MONTH";
        } else if (yearly.isChecked()) {
            return "YEAR";
        } else {
            return null;
        }
    }


    private boolean checkRequiredField() {
        title = eventTitle.getText().toString().trim();
        subject = eventSubject.getText().toString().trim();
        body = eventBody.getText().toString().trim();
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(subject) || TextUtils.isEmpty(body)) {
            Toast.makeText(AddEventsActivity.this, "Required Field Empty", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }

    }

    private boolean checkEventStatus() {
        if ((yearly.isChecked()) || (monthly.isChecked()) || (everyDay.isChecked())) {
            return true;
        } else {
            Toast.makeText(AddEventsActivity.this, "Choose any event type", Toast.LENGTH_LONG).show();
            return false;
        }
    }

}
