package com.example.ciclotm.ReguliCirculatie;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;

import com.example.ciclotm.R;
import com.example.ciclotm.turePost;
import com.example.ciclotm.tureRecycleViewAdapter;
import com.google.firebase.database.DatabaseReference;

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
        initData();
        setRecycleView();
    }

    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.reguli_circulatie_action_bar);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void initData() {
        rulesList.add(new CategorieReguli("Obligații", getString(R.string.rules_obligatii)));
        rulesList.add(new CategorieReguli("Interdicții", getString(R.string.rules_interdictii)));
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