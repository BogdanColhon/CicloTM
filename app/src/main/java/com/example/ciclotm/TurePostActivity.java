package com.example.ciclotm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.ciclotm.Models.Posts.turePost;
import com.example.ciclotm.Models.Users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TurePostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText titleEditText;
    private EditText distanceEditText;
    private EditText startTimeEditText;
    private EditText startPointEditText;
    private EditText descriptionEditText;
    private EditText durationEditText;
    private TextView activityDateTextView;
    private Spinner timeUnitSpinner;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String timeUnit;
    private String userImageUrl;
    Calendar calendar = Calendar.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ture_post);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Ture");
        actionBar.setDisplayHomeAsUpEnabled(true);

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
                reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
                String uid = user.getUid();
                List<String> participants = new ArrayList<>(Arrays.asList(uid));
                Date currentTime = Calendar.getInstance().getTime();

                reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userProfile = snapshot.getValue(User.class);
                        if (userProfile != null) {
                            userImageUrl = String.valueOf(userProfile.getProfileImageUrl());
                            turePost post = new turePost(title, distance, duration + " " + timeUnit, startTime, startPoint, 1, description, uid, currentTime, activityDate, userImageUrl,participants);

                            FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("TurePosts").child(String.valueOf(currentTime))
                                    .setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        finish();
                                        Toast.makeText(TurePostActivity.this, "Postare adaugată", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users").child(uid).child("CommunityPosts").child("MyTurePosts").child(String.valueOf(currentTime))
                                    .setValue(post);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

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