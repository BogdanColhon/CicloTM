package com.example.ciclotm.Views;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.ciclotm.R;
import com.example.ciclotm.ViewModels.TurePostViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TurePostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static Activity terminator;
    private EditText titleEditText;
    private EditText distanceEditText;
    private EditText startTimeEditText;
    private EditText startPointEditText;
    private EditText descriptionEditText;
    private EditText durationEditText;
    private TextView activityDateTextView;
    private Spinner timeUnitSpinner;
    private FirebaseUser user;
    private String timeUnit;
    Calendar calendar = Calendar.getInstance();
    private TurePostViewModel mTurePostViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ture_post);

        terminator = this;

        initActionBar();
        initLayout();

        mTurePostViewModel = ViewModelProviders.of(this).get(TurePostViewModel.class);


        activityDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog(TurePostActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                activityDateTextView.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                                calendar.set(year, monthOfYear, dayOfMonth);

                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                dpd.show();


            }
        });
    }

    private void initLayout() {
        timeUnitSpinner = (Spinner) findViewById(R.id.timeUnitSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TurePostActivity.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.time_units));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeUnitSpinner.setAdapter(adapter);
        timeUnitSpinner.setOnItemSelectedListener(this);

        titleEditText = findViewById(R.id.rideTitleEditText);
        distanceEditText = findViewById(R.id.distanceEditText);
        startTimeEditText = findViewById(R.id.startTimeEditText);
        startPointEditText = findViewById(R.id.startPointEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        durationEditText = findViewById(R.id.durationEditText);
        activityDateTextView = findViewById(R.id.turePostDateTextView);
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Ture");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        timeUnit = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_bar_bicycles, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                String title = titleEditText.getText().toString();
                String distance = distanceEditText.getText().toString();
                String duration = durationEditText.getText().toString();
                String startTime = startTimeEditText.getText().toString();
                String startPoint = startPointEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                Date activityDate = calendar.getTime();


                user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();

                List<String> participants = new ArrayList<>(Arrays.asList(uid));
                Date currentTime = Calendar.getInstance().getTime();

                mTurePostViewModel.postData(title, distance, duration, timeUnit, startTime, startPoint, 1, description, uid, currentTime, activityDate, participants);
                Toast.makeText(TurePostActivity.this, "Postare adaugată", Toast.LENGTH_SHORT).show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }
}