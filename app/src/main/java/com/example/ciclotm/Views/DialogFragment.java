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
import com.example.ciclotm.ViewModels.DialogFragmentViewModel;

public class DialogFragment extends androidx.fragment.app.DialogFragment {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button addButton;
    private EditText placeNameEditText;
    private String address;
    private Double markerLat;
    private Double markerLng;
    private String markerCat;
    private DialogFragmentViewModel mDialogFragmentViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.custom_point_of_interest_dialog, null);

        initActionBar(view);

        mDialogFragmentViewModel = ViewModelProviders.of(this).get(DialogFragmentViewModel.class);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val1, val2;
                Double val3, val4;
                val1 = placeNameEditText.getText().toString().trim();
                val2 = markerCat;
                val3 = markerLat;
                val4 = markerLng;

                if (val1.isEmpty()) {
                    placeNameEditText.setError("Câmp obligatoriu!");
                    placeNameEditText.requestFocus();
                    return;
                }

                mDialogFragmentViewModel.setMarker(val1, val2, val3, val4);
                dismiss();
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

    private void initActionBar(View view) {
        radioGroup = view.findViewById(R.id.radioGroup);
        addButton = view.findViewById(R.id.addButton);
        placeNameEditText = view.findViewById(R.id.placeName);
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

