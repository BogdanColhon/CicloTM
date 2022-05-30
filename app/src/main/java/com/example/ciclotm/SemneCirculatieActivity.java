package com.example.ciclotm;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.ciclotm.Models.Legislation.SemnCirculatie;

import java.util.ArrayList;

public class SemneCirculatieActivity extends AppCompatActivity {

    private ArrayList<SemnCirculatie> items = new ArrayList<>();
    private RecyclerView recyclerView;
    private int surse_semne_circulatie[] = {R.drawable.s1_cedeaza, R.drawable.s2_stop, R.drawable.s3_prioritate, R.drawable.s4_sfarsit_prioritate, R.drawable.s5_interzis, R.drawable.s6_interzis_ambele_sensuri, R.drawable.s7_interzis_biciclete, R.drawable.s8_interzis_stanga, R.drawable.s9_interzis_dreapta, R.drawable.s10_interzis_intoarcere, R.drawable.s11_urcare, R.drawable.s18_coborare, R.drawable.s12_ocolire, R.drawable.s13_giratoriu, R.drawable.s14_pista, R.drawable.s15_pista_combinat, R.drawable.s16_trecere, R.drawable.s17_sens_unic};
    semneCirculatieRecycleViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semne_circulatie);

        initActionBar();
        initData();
        setRecycleView();
    }

    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.road_signs_action_bar);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void initData() {

        for (int i = 0; i < surse_semne_circulatie.length; i++) {
            items.add(new SemnCirculatie(getResources().getStringArray(R.array.denumire_semne_circulatie)[i], getResources().getStringArray(R.array.defintie_semne_circulatie)[i], surse_semne_circulatie[i]));
        }

    }

    public void setRecycleView() {
        recyclerView = (RecyclerView) findViewById(R.id.semneCirculatieRView);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new semneCirculatieRecycleViewAdapter(getApplicationContext(), items);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }
}