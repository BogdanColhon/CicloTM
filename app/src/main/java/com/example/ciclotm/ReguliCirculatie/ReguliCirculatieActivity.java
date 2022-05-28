package com.example.ciclotm.ReguliCirculatie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;

import com.example.ciclotm.R;
import com.example.ciclotm.turePost;
import com.example.ciclotm.tureRecycleViewAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ReguliCirculatieActivity extends AppCompatActivity {

    private ArrayList<CategorieReguli> rulesList = new ArrayList<>();
    private RecyclerView recyclerView;
    CategorieReguliAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

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
        adapter = new CategorieReguliAdapter(getApplicationContext(), rulesList);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }

}