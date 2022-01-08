package com.example.ciclotm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ciclotm.Models.Bike;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class BicyclePostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText nick_nameEditText;
    private EditText brandEditText;
    private EditText yearEditText;
    private EditText weightEditText;
    private EditText descriptionEditText;
    private ImageView bikeImageView;
    private Spinner modelSpinner;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bicycle_post);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Bicicletă");
        actionBar.setDisplayHomeAsUpEnabled(true);

        nick_nameEditText = (EditText) findViewById(R.id.nick_nameEditText);
        brandEditText = (EditText) findViewById(R.id.brandEditText);
        yearEditText = (EditText) findViewById(R.id.yearEditText);
        weightEditText = (EditText) findViewById(R.id.weightEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);

        bikeImageView = (ImageView) findViewById(R.id.bike_image);
        modelSpinner = (Spinner) findViewById(R.id.modelSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(BicyclePostActivity.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.bike_models));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(adapter);
        modelSpinner.setOnItemSelectedListener(this);
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
                String nick_name=nick_nameEditText.getText().toString();
                String brand = brandEditText.getText().toString();
                String weight= weightEditText.getText().toString();
                String year = yearEditText.getText().toString();
                String description = descriptionEditText.getText().toString();

                user = FirebaseAuth.getInstance().getCurrentUser();
                reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
                String owner = user.getUid();
                Bike bike = new Bike(nick_name,type,brand,"model",weight,year,description,owner);

                FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users").child(owner).child("BikeCollection").child(nick_name)
                        .setValue(bike).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(BicyclePostActivity.this, "Bicicletă adaugată", Toast.LENGTH_SHORT).show();
                            BicyclesActivity.terminator.finish();
                            Intent myIntent = new Intent(BicyclePostActivity.this,BicyclesActivity.class);
                            startActivity(myIntent);
                            finish();
                        }
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
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}