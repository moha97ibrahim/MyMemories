package com.flatplay.mymemories.ui.showEvent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.flatplay.mymemories.R;
import com.flatplay.mymemories.db.DBHelper;
import com.flatplay.mymemories.ui.addEvents.AddEventsActivity;

import java.util.ArrayList;

public class ShowEventActivity extends AppCompatActivity {

    private String EVENT_ID;
    private TextView editBtn, showTilte, showDate, showContent;
    private DBHelper dbHelper;
    private ArrayList<String> arrayList1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showevent);
        dbHelper = new DBHelper(getApplicationContext());


        EVENT_ID = getIntent().getStringExtra("id");

        showTilte = findViewById(R.id.show_title);
        showDate = findViewById(R.id.show_date);
        showContent = findViewById(R.id.show_content);
        updateUI();

        //editbtn
        editBtn = findViewById(R.id.edit_btn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShowEventActivity.this, AddEventsActivity.class);
                i.putExtra("id", EVENT_ID);
                i.putExtra("edit", "edit");
                startActivity(i);
            }
        });


    }

    private void updateUI() {
        arrayList1 = dbHelper.getEvent(EVENT_ID);
        showDate.setText(arrayList1.get(0));
        showTilte.setText(arrayList1.get(1));
        showContent.setText(arrayList1.get(3));
    }
}
