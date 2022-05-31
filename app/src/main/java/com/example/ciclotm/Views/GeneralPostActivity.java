package com.example.ciclotm.Views;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.ciclotm.R;
import com.example.ciclotm.ViewModels.GeneralPostViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Date;

public class GeneralPostActivity extends AppCompatActivity {

    public static Activity terminator;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private FirebaseUser user;
    private GeneralPostViewModel mGeneralPostViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_post);

        terminator  = this;
        mGeneralPostViewModel = ViewModelProviders.of(this).get(GeneralPostViewModel.class);
        initActionBar();
        initLayout();

    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("General");
        actionBar.setDisplayHomeAsUpEnabled(true);

    }
    private void initLayout() {
        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
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
                String description = descriptionEditText.getText().toString();
                Date currentTime = Calendar.getInstance().getTime();

                user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();

                mGeneralPostViewModel.postData(title,description,currentTime,uid);
                Toast.makeText(GeneralPostActivity.this, "Postare adaugată", Toast.LENGTH_SHORT).show();
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