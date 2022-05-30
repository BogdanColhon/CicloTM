package com.example.ciclotm.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ciclotm.R;
import com.example.ciclotm.Models.Legislation.CategorieReguli;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AdminReguliCirculatieActivity extends AppCompatActivity implements adminReguliCirculatieRecyclerViewAdapter.OnPostListener {


    private ArrayList<CategorieReguli> rulesList = new ArrayList<>();
    private RecyclerView recyclerView;
    adminReguliCirculatieRecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Date currentTime = Calendar.getInstance().getTime();
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reguli_circulatie);

        initActionBar();
        fetchReguliCirculatie();
        setRecycleView();
    }

    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.reguli_circulatie_action_bar);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void fetchReguliCirculatie() {
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Legislație").child("Reguli_Circulatie");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CategorieReguli regula = snapshot.getValue(CategorieReguli.class);
                rulesList.add(0, regula);
                adapter.notifyDataSetChanged();
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

    public void setRecycleView() {
        recyclerView = (RecyclerView) findViewById(R.id.rulesRecycleView);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new adminReguliCirculatieRecyclerViewAdapter(getApplicationContext(), rulesList, this);
        recyclerView.setAdapter(adapter);


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
                CategorieReguli regula = new CategorieReguli(currentTime, "-", "-");
                reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Legislație").child("Reguli_Circulatie");
                reference.child(currentTime.toString()).setValue(regula);
                rulesList.add(regula);
                adapter.notifyDataSetChanged();
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
    public void onPostSave(int position, String category, String rules) {
        DatabaseReference RuleReference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference()
                .child("Legislație").child("Reguli_Circulatie");
        HashMap hashMap = new HashMap();
        hashMap.put("category", category);
        hashMap.put("rules", rules);
        RuleReference.child(rulesList.get(position).getPublishDate().toString()).updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminReguliCirculatieActivity.this, "Modificări efectuate", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    @Override
    public void onPostDelete(int position) {
        DatabaseReference RuleReference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference()
                .child("Legislație").child("Reguli_Circulatie").child(rulesList.get(position).getPublishDate().toString());
        RuleReference.removeValue();
        adapter.notifyItemRemoved(position);

    }
}