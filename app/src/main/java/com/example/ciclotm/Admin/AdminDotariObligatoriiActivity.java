package com.example.ciclotm.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ciclotm.R;
import com.example.ciclotm.ReguliCirculatie.CategorieReguli;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminDotariObligatoriiActivity extends AppCompatActivity {

    private ImageView dotariImage;
    private EditText dotariText;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dotari_obligatorii);

        dotariImage = (ImageView) findViewById(R.id.imageView6);
        dotariText = (EditText) findViewById(R.id.adminDotariObligatoriiEditText);

        initActionBar();
        fetchDotariObligatorii();
    }

    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.echipare_obligatorie_action_bar);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void fetchDotariObligatorii() {
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Legislație").child("Dotări_Obligatorii");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String regula = snapshot.getValue(String.class);
                dotariText.setText(regula);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.admin_rules_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Legislație").child("Dotări_Obligatorii").child("regulă");
                reference.setValue(dotariText.getText().toString());
                Toast.makeText(AdminDotariObligatoriiActivity.this,"Modificări salvate",Toast.LENGTH_SHORT).show();
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