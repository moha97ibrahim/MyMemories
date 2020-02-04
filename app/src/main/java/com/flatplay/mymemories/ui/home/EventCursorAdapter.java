package com.flatplay.mymemories.ui.home;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.flatplay.mymemories.R;
import com.flatplay.mymemories.db.DBContract;
import com.flatplay.mymemories.ui.showEvent.ShowEventActivity;

public class EventCursorAdapter extends CursorAdapter {
    private CardView eventCartItem;
    private TextView title, subject;


    public EventCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.event_list_view, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        eventCartItem = view.findViewById(R.id.eventCardItem);
        title = view.findViewById(R.id.list_event_title);
        subject = view.findViewById(R.id.list_event_subject);
        title.setText(cursor.getString(cursor.getColumnIndex(DBContract.event.COLUMN_EVENT_TITLE)));
        subject.setText(cursor.getString(cursor.getColumnIndex(DBContract.event.COLUMN_EVENT_SUBJECT)));

        eventCartItem.setTag(cursor.getPosition());
        eventCartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), "" + getItemId((Integer) v.getTag()), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context.getApplicationContext(), ShowEventActivity.class);
                intent.putExtra("id", ""+getItemId((Integer) v.getTag()));
                context.startActivity(intent);
            }
        });

    }
}
