package com.example.ciclotm.Views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.ciclotm.Models.Legislation.CategorieReguli;
import com.example.ciclotm.R;
import com.example.ciclotm.Adapters.CategorieReguliAdapter;
import com.example.ciclotm.ViewModels.ReguliCirculatieViewModel;
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
    private ReguliCirculatieViewModel mReguliCirculatieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reguli_circulatie);

        initActionBar();

        mReguliCirculatieViewModel = ViewModelProviders.of(this).get(ReguliCirculatieViewModel.class);
        mReguliCirculatieViewModel.init();
        mReguliCirculatieViewModel.getReguliCirculatie().observe(this, new Observer<ArrayList<CategorieReguli>>() {
            @Override
            public void onChanged(ArrayList<CategorieReguli> categorieReguli) {
                adapter.updateReguliCirculatieList(categorieReguli);
            }
        });

        setRecycleView();
    }

    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.reguli_circulatie_action_bar);
        actionBar.setDisplayHomeAsUpEnabled(true);
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