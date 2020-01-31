package com.flatplay.mymemories.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.flatplay.mymemories.R;
import com.flatplay.mymemories.ui.addEvents.AddEventsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;

public class WriteFragment extends Fragment {

    private WriteViewModel writeViewModel;
    private FloatingActionButton addEventfab;

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

        addEventfab = root.findViewById(R.id.add_event_fab);
        addEventfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEventsActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}