package com.example.ciclotm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ciclotm.Models.LiveEventsMarker;
import com.example.ciclotm.Models.PointOfInterestMarker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class DialogFragmentLive extends androidx.fragment.app.DialogFragment {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button addButton;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private Double markerLat;
    private Double markerLng;
    private String markerCat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.custom_live_event_dialog, null);
        radioGroup = view.findViewById(R.id.radioGroup);
        addButton = view.findViewById(R.id.addButton);
        titleEditText = view.findViewById(R.id.liveEventTitleEditText);
        descriptionEditText = view.findViewById(R.id.liveEventDescriptionEditText);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val1, val2, val3;
                Double val4, val5;
                val1 = titleEditText.getText().toString().trim();
                val2 = descriptionEditText.getText().toString().trim();
                val3 = markerCat;
                val4 = markerLat;
                val5 = markerLng;
                Date currentTime = Calendar.getInstance().getTime();
                Date expiringTime = new Date();
                expiringTime.setTime(System.currentTimeMillis() + (6*60*60*1000));

                if (val1.isEmpty()) {
                    titleEditText.setError("Câmp obligatoriu!");
                    titleEditText.requestFocus();
                    return;
                }

                LiveEventsMarker marker = new LiveEventsMarker(val1,val3,val2,currentTime,expiringTime,val4,val5,0);

                FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("LiveEventsMarkers").child(val1)
                        .setValue(marker).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        }
                    }
                });
                dismiss();
            }
        });
        view.findViewById(R.id.holeRadioButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkButton(v);
            }
        });
        view.findViewById(R.id.brokeGlassRadioButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkButton(v);
            }
        });
        view.findViewById(R.id.iceRadioButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkButton(v);
            }
        });
        view.findViewById(R.id.roadWorkRadioButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkButton(v);
            }
        });
        view.findViewById(R.id.accidentRadioButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkButton(v);
            }
        });
        return view;

    }

    public void putArguments(Bundle args) {
        markerLat = args.getDouble("markerLat");
        markerLng = args.getDouble("markerLng");
    }

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = v.findViewById(radioId);
        markerCat = radioButton.getText().toString();

    }
}
