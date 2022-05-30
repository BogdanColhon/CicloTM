package com.example.ciclotm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ciclotm.Adapters.bicycleRecycleViewAdapter;
import com.example.ciclotm.Models.Objects.Bike;
import com.example.ciclotm.ViewModels.BicyclesActivityViewModel;

import java.util.ArrayList;

public class BicyclesActivity extends AppCompatActivity {

    public static Activity terminator;
    private ArrayList<Bike> bikeList = new ArrayList<>();
    private RecyclerView recyclerView;
    private bicycleRecycleViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private BicyclesActivityViewModel mBicyclesActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bicycles);

        recyclerView = (RecyclerView) findViewById(R.id.bicyclesRecyclerView);

        terminator = this;


        mBicyclesActivityViewModel = ViewModelProviders.of(this).get(BicyclesActivityViewModel.class);
        mBicyclesActivityViewModel.init();
        System.out.println(mBicyclesActivityViewModel.bikesList);
        mBicyclesActivityViewModel.getBikes().observe(this, new Observer<ArrayList<Bike>>() {
            @Override
            public void onChanged(ArrayList<Bike> bikes) {
                adapter.updateBikeList(bikes);
            }
        });

        initActionBar();
        initRecyclerView();
        //fetchBikeCollection();

    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Biciclete");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initRecyclerView() {
        adapter = new bicycleRecycleViewAdapter(BicyclesActivity.this, mBicyclesActivityViewModel.getBikes().getValue());
        layoutManager = new LinearLayoutManager(BicyclesActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

//    private void fetchBikeCollection() {
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
//        String owner = user.getUid();
//        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users").child(owner).child("BikeCollection");
//        reference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Bike bike = snapshot.getValue(Bike.class);
//                if (bike != null) {
//                    bikeList.add(0, bike);
//                    adapter.notifyDataSetChanged();
//                }
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_bar_bicycles, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(BicyclesActivity.this, BicyclePostActivity.class);
                startActivity(intent);
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