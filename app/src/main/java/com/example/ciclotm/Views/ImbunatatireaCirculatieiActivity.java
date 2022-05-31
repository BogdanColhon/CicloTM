package com.example.ciclotm.Views;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.ciclotm.Models.Legislation.ImbunatatireCirculatieItem;
import com.example.ciclotm.R;
import com.example.ciclotm.Adapters.imbunatatireCirculatieRecycleViewAdapter;

import java.util.ArrayList;

public class ImbunatatireaCirculatieiActivity extends AppCompatActivity {

    private ArrayList<ImbunatatireCirculatieItem> items = new ArrayList<>();
    private RecyclerView recyclerView;
    imbunatatireCirculatieRecycleViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imbunatatirea_circulatiei);

        initActionBar();
        initData();
        setRecycleView();
    }

    public void initActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.siguranta_action_bar);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void initData(){
        items.add(new ImbunatatireCirculatieItem("Mersul pe bicicletă noaptea",getString(R.string.rules_bicicleta_noaptea),R.drawable.bike_night));
        items.add(new ImbunatatireCirculatieItem("Mersul pe bicicletă pe timp de ploaie",getString(R.string.rules_bicicleta_ploaie),R.drawable.bike_rain));
        items.add(new ImbunatatireCirculatieItem("Mersul pe bicicletă iarna",getString(R.string.rules_bicicleta_iarna),R.drawable.bike_snow));
        items.add(new ImbunatatireCirculatieItem("Mersul cu bicicleta în zone aglomerate",getString(R.string.rules_bicicleta_aglomeratie),R.drawable.bike_traffic));
    }

    public void setRecycleView(){
        recyclerView = (RecyclerView) findViewById(R.id.ImbunatatireRView);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new imbunatatireCirculatieRecycleViewAdapter(getApplicationContext(), items);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }
}