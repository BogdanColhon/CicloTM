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

import com.example.ciclotm.Models.PointOfInterestMarker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class DialogFragment extends androidx.fragment.app.DialogFragment {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button addButton;
    private EditText placeNameEditText;
    private String address;
    private Double markerLat;
    private Double markerLng;
    private String markerCat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.custom_point_of_interest_dialog, null);
        radioGroup = view.findViewById(R.id.radioGroup);
        addButton = view.findViewById(R.id.addButton);
        placeNameEditText = view.findViewById(R.id.placeName);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val1, val2;
                Double val3, val4;
                val1 = placeNameEditText.getText().toString().trim();
                val2 = markerCat;
                val3 = markerLat;
                val4 = markerLng;

                PointOfInterestMarker marker = new PointOfInterestMarker(val1, val2, val3, val4);

                FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("PointsOfInterestMarkers").child(val1)
                        .setValue(marker).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        }
                    }
                });
            }
        });
        view.findViewById(R.id.serviceRadioButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkButton(v);
            }
        });
        view.findViewById(R.id.storeRadioButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkButton(v);
            }
        });
        view.findViewById(R.id.coffeRadioButton).setOnClickListener(new View.OnClickListener() {
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
        address = args.getString("address");

    }

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = v.findViewById(radioId);
        markerCat = radioButton.getText().toString();

    }


}

