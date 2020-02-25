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
        import com.flatplay.mymemories.db.DBHelper;

        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.Date;
        import java.util.Objects;


public class AddEventsActivity extends AppCompatActivity {
    private EditText eventTitle, eventSubject, eventBody;
    private CheckBox everyDay, monthly, yearly;
    private Button cancel, save;
    private String title, subject, body, date;
    private TextView eventDate;
    private DBHelper dbHelper;
    private ArrayList<String> arrayList1 = new ArrayList<>();
    private String UI = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addevents);
        dbHelper = new DBHelper(getApplicationContext());


        eventTitle = findViewById(R.id.event_title);
        eventSubject = findViewById(R.id.event_subject);
        eventBody = findViewById(R.id.event_body);
        everyDay = findViewById(R.id.checkEveryDay);
        monthly = findViewById(R.id.checkEveryMonth);
        yearly = findViewById(R.id.checkEveryYear);
        cancel = findViewById(R.id.btn_cancel);
        save = findViewById(R.id.btn_save);
        eventDate = findViewById(R.id.event_date);

        eventDate.setText(getIntent().getStringExtra("DATE"));
        UI = getIntent().getStringExtra("edit");


        //toolbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Event");
        eventDate.setText(getIntent().getStringExtra("DATE"));


        try {
            if (getIntent().getStringExtra("edit").equals("edit")) {
                actionBar.setTitle("Update Event");
                updateUI(getIntent().getStringExtra("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelEvent();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(UI, "edit"))
                    updateEvent();
                else
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


    private void updateUI(String id) {

        arrayList1 = dbHelper.getEvent(id);

        eventDate.setText(arrayList1.get(0));

        eventTitle.setText(arrayList1.get(1));

        eventSubject.setText(arrayList1.get(2));

        eventBody.setText(arrayList1.get(3));

        switch (arrayList1.get(4)) {
            case "DAY":
                everyDay.setChecked(true);
                break;
            case "MONTH":
                monthly.setChecked(true);
                break;
            case "YEAR":
                yearly.setChecked(true);
                break;
        }

        save.setText("Update");

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
            values.put(DBContract.event.COLUMN_EVENT_TIME_STAMP, getTimeStamp(getIntent().getStringExtra("DATE")));
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

    private String getTimeStamp(String date1) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = sdf.parse(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(date.getTime());
    }

    private void updateEvent() {
        if (checkRequiredField() && checkEventStatus()) {
            ContentValues values = new ContentValues();
            values.put(DBContract.event.COLUMN_EVENT_DATE, arrayList1.get(0));
            values.put(DBContract.event.COLUMN_EVENT_TITLE, title);
            values.put(DBContract.event.COLUMN_EVENT_SUBJECT, subject);
            values.put(DBContract.event.COLUMN_EVENT_BODY, body);
            values.put(DBContract.event.COLUMN_EVENT_STATUS, getStatus());
            String[] id = {getIntent().getStringExtra("id")};
            Log.e("dfsd", "for " + Arrays.toString(id) + values);
            dbHelper.updateEvent(values, id);
            Intent intent = new Intent(AddEventsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
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
