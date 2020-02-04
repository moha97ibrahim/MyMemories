package com.flatplay.mymemories.ui.showEvent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.flatplay.mymemories.R;
import com.flatplay.mymemories.ui.addEvents.AddEventsActivity;

public class ShowEventActivity extends AppCompatActivity {

    private String EVENT_ID;
    private TextView editBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showevent);

        EVENT_ID = getIntent().getStringExtra("id");
        //editbtn
        editBtn = findViewById(R.id.edit_btn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShowEventActivity.this, AddEventsActivity.class);
                i.putExtra("id",EVENT_ID);
                i.putExtra("edit","edit");
                startActivity(i);
            }
        });


    }
}
