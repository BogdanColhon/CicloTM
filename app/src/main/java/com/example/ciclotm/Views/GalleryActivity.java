package com.example.ciclotm.Views;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ciclotm.Models.Objects.Photo;
import com.example.ciclotm.R;
import com.example.ciclotm.ViewModels.GalleryActivityViewModel;
import com.example.ciclotm.Adapters.galleryRecycleViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    private ArrayList<Photo> items = new ArrayList<>();
    private RecyclerView recyclerView;
    private galleryRecycleViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseUser user;
    private String userID;
    private GalleryActivityViewModel mGalleryActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        initActionBar();

        mGalleryActivityViewModel = ViewModelProviders.of(this).get(GalleryActivityViewModel.class);
        mGalleryActivityViewModel.init(userID);
        mGalleryActivityViewModel.getPhotoList().observe(this, new Observer<ArrayList<Photo>>() {
            @Override
            public void onChanged(ArrayList<Photo> photos) {
                adapter.updatePhotoList(photos);
            }
        });

        setRecycleView();
    }

    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Galerie");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void setRecycleView() {
        recyclerView = (RecyclerView) findViewById(R.id.galleryRView);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new galleryRecycleViewAdapter(getApplicationContext(), items);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }


}