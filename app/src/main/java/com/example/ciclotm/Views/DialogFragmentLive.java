package com.example.ciclotm.Views;

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
import androidx.lifecycle.ViewModelProviders;

import com.example.ciclotm.R;
import com.example.ciclotm.ViewModels.DialogFragmentLiveViewModel;

public class DialogFragmentLive extends androidx.fragment.app.DialogFragment {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button addButton;
    private EditText descriptionEditText;
    private Double markerLat;
    private Double markerLng;
    private String markerCat;
    private DialogFragmentLiveViewModel mDialogFragmentLiveViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.custom_live_event_dialog, null);

        initLayout(view);

        mDialogFragmentLiveViewModel = ViewModelProviders.of(this).get(DialogFragmentLiveViewModel.class);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val1, val2;
                Double val3, val4;
                val1 = markerCat;
                val2 = descriptionEditText.getText().toString().trim();
                val3 = markerLat;
                val4 = markerLng;

                mDialogFragmentLiveViewModel.setMarker(val1, val2, val3, val4);
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

    private void initLayout(View view) {
        radioGroup = view.findViewById(R.id.radioGroup);
        addButton = view.findViewById(R.id.addButton);
        descriptionEditText = view.findViewById(R.id.liveEventDescriptionEditText);
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
