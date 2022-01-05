package com.example.ciclotm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class ReportStolenBikeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Bitmap captureImage;
    TextView dateReportTextView;
    EditText addressReportEditText;
    EditText brandReportEditText;
    EditText colorReportEditText;
    EditText bikeDescriptionEditText;
    EditText thiefDescriptionEditText;
    ImageView locationReportImageView;
    ImageView stolenBikeReportImageView;
    Spinner modelReportSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_stolen_bike);
        modelReportSpinner = (Spinner) findViewById(R.id.modelReportSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReportStolenBikeActivity.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.bike_models));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelReportSpinner.setAdapter(adapter);
        modelReportSpinner.setOnItemSelectedListener(this);


        locationReportImageView = (ImageView) findViewById(R.id.locationReportImageView);
        stolenBikeReportImageView = (ImageView) findViewById(R.id.stolenBikeImageView);

        dateReportTextView = (TextView) findViewById(R.id.dateReportTextView);
        brandReportEditText = (EditText) findViewById(R.id.brandReportEditText);
        colorReportEditText = (EditText) findViewById(R.id.colorReportEditText);
        addressReportEditText = (EditText) findViewById(R.id.addressReportEditText);
        bikeDescriptionEditText = (EditText) findViewById(R.id.bikeDescriptionReportEditText);
        thiefDescriptionEditText = (EditText) findViewById(R.id.thiefDescriptionReportEditText);

        Intent intent = getIntent();
        String full_address = intent.getStringExtra("location");
        addressReportEditText.setText(full_address);

        locationReportImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ContextCompat.checkSelfPermission(ReportStolenBikeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ReportStolenBikeActivity.this,
                                new String[]{
                                        Manifest.permission.CAMERA
                                }, 100);
                    }
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,100);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        stolenBikeReportImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ContextCompat.checkSelfPermission(ReportStolenBikeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ReportStolenBikeActivity.this,
                                new String[]{
                                        Manifest.permission.CAMERA
                                }, 101);
                    }
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,101);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        dateReportTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog(ReportStolenBikeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dateReportTextView.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                dpd.show();


            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (data != null) {
                captureImage = (Bitmap) data.getExtras().get("data");
                locationReportImageView.setImageBitmap(captureImage);
            }
        }
        if (requestCode == 101) {
            if (data != null) {
                captureImage = (Bitmap) data.getExtras().get("data");
                stolenBikeReportImageView.setImageBitmap(captureImage);
            }
        }

    }
}