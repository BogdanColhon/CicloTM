package com.example.ciclotm.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ciclotm.Adapters.myFurturiRecyclerViewAdapter;
import com.example.ciclotm.Adapters.myTureRecyclerViewAdapter;
import com.example.ciclotm.Adapters.mygeneralRecyclerViewAdapter;
import com.example.ciclotm.Models.Objects.Report;
import com.example.ciclotm.Models.Posts.generalPost;
import com.example.ciclotm.Models.Posts.turePost;
import com.example.ciclotm.R;
import com.example.ciclotm.ViewModels.CommunityPostsViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class CommunityPostsActivity extends AppCompatActivity implements mygeneralRecyclerViewAdapter.OnPostListener, myTureRecyclerViewAdapter.OnPostListener, myFurturiRecyclerViewAdapter.OnPostListener {

    private ArrayList<generalPost> myGeneralPosts = new ArrayList<>();
    private ArrayList<turePost> myTurePosts = new ArrayList<>();
    private ArrayList<Report> myFurturiPosts = new ArrayList<>();
    private RecyclerView generalRecyclerView;
    private RecyclerView tureRecyclerView;
    private RecyclerView furturiRecyclerView;
    private LinearLayout generalLayout;
    private LinearLayout tureLayout;
    private LinearLayout furturiLayout;
    private mygeneralRecyclerViewAdapter generalAdapter;
    private myTureRecyclerViewAdapter tureAdapter;
    private myFurturiRecyclerViewAdapter furturiAdapter;
    private RecyclerView.LayoutManager layoutManager1;
    private RecyclerView.LayoutManager layoutManager2;
    private RecyclerView.LayoutManager layoutManager3;

    private FirebaseUser user;
    private CommunityPostsViewModel mCommunityPostsViewModel;

    private String owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_posts);

        initActionBar();
        initLayout();

        user = FirebaseAuth.getInstance().getCurrentUser();
        owner = user.getUid();

        mCommunityPostsViewModel = ViewModelProviders.of(this).get(CommunityPostsViewModel.class);
        mCommunityPostsViewModel.init(owner);

        mCommunityPostsViewModel.getGeneralPosts().observe(this, new Observer<ArrayList<generalPost>>() {
            @Override
            public void onChanged(ArrayList<generalPost> generalPosts) {
                if (generalPosts.size() > 0) {
                    generalLayout.setVisibility(View.VISIBLE);
                    generalAdapter.updateGeneralPosts(generalPosts);
                }
            }
        });

        mCommunityPostsViewModel.getTurePosts().observe(this, new Observer<ArrayList<turePost>>() {
            @Override
            public void onChanged(ArrayList<turePost> turePosts) {
                if (turePosts.size() > 0) {
                    tureLayout.setVisibility(View.VISIBLE);
                    tureAdapter.updateTurePosts(turePosts);
                }
            }
        });

        mCommunityPostsViewModel.getReportPosts().observe(this, new Observer<ArrayList<Report>>() {
            @Override
            public void onChanged(ArrayList<Report> reportPosts) {
                if (reportPosts.size() > 0) {
                    furturiLayout.setVisibility(View.VISIBLE);
                    furturiAdapter.updateReportPosts(reportPosts);
                }
            }
        });

        setRecyclerViews();
    }


    private void initActionBar() {
        getSupportActionBar().setTitle("Postări comunitate");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initLayout() {
        generalRecyclerView = (RecyclerView) findViewById(R.id.myGeneralCommunityPostsRView);
        tureRecyclerView = (RecyclerView) findViewById(R.id.myTureCommunityPostsRView);
        furturiRecyclerView = (RecyclerView) findViewById(R.id.myFurturiCommunityPostsRView);
        generalLayout = (LinearLayout) findViewById(R.id.myGeneralPostsLinearLayout);
        tureLayout = (LinearLayout) findViewById(R.id.myTurePostsLinearLayout);
        furturiLayout = (LinearLayout) findViewById(R.id.myFurturiPostsLinearLayout);

        layoutManager1 = new LinearLayoutManager(CommunityPostsActivity.this);
        layoutManager2 = new LinearLayoutManager(CommunityPostsActivity.this);
        layoutManager3 = new LinearLayoutManager(CommunityPostsActivity.this);

        generalLayout.setVisibility(View.GONE);
        tureLayout.setVisibility(View.GONE);
        furturiLayout.setVisibility(View.GONE);

    }

    private void setRecyclerViews() {
        generalRecyclerView.setLayoutManager(layoutManager1);
        tureRecyclerView.setLayoutManager(layoutManager2);
        furturiRecyclerView.setLayoutManager(layoutManager3);

        generalAdapter = new mygeneralRecyclerViewAdapter(CommunityPostsActivity.this, mCommunityPostsViewModel.getGeneralPosts().getValue(), this);
        tureAdapter = new myTureRecyclerViewAdapter(CommunityPostsActivity.this, mCommunityPostsViewModel.getTurePosts().getValue(), this);
        furturiAdapter = new myFurturiRecyclerViewAdapter(CommunityPostsActivity.this, mCommunityPostsViewModel.getReportPosts().getValue(), this);

        generalRecyclerView.setAdapter(generalAdapter);
        tureRecyclerView.setAdapter(tureAdapter);
        furturiRecyclerView.setAdapter(furturiAdapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onPostClick(int position) {
        Intent intent = new Intent(CommunityPostsActivity.this, ExpandedGeneralPostActivity.class);
        intent.putExtra("clicked_post", mCommunityPostsViewModel.getGeneralPosts().getValue().get(position));
        startActivity(intent);
    }

    @Override
    public void onPostTureClick(int position) {
        Intent intent = new Intent(CommunityPostsActivity.this, ExpandedTurePostActivity.class);
        intent.putExtra("clicked_post", mCommunityPostsViewModel.getTurePosts().getValue().get(position));
        startActivity(intent);
    }

    @Override
    public void onPostFurturiClick(int position) {
        Intent intent = new Intent(CommunityPostsActivity.this, ExpandedFurturiPostActivity.class);
        intent.putExtra("clicked_report", mCommunityPostsViewModel.getReportPosts().getValue().get(position));
        startActivity(intent);
    }

    @Override
    public void OnDeleteClick(int position) {
        removeGeneralItem(position);
    }

    @Override
    public void OnDeleteTureClick(int position) {
        removeTureItem(position);
    }

    @Override
    public void OnDeleteFurturiClick(int position) {
        removeFurturiItem(position);
    }

    public void removeGeneralItem(int position) {
        mCommunityPostsViewModel.removeGeneralPost(mCommunityPostsViewModel.getGeneralPosts().getValue().get(position), owner);
        generalAdapter.notifyItemRemoved(position);
    }

    public void removeTureItem(int position) {
        mCommunityPostsViewModel.removeTurePost(mCommunityPostsViewModel.getTurePosts().getValue().get(position), owner);
        tureAdapter.notifyItemRemoved(position);
    }

    public void removeFurturiItem(int position) {
        mCommunityPostsViewModel.removeReportPost(mCommunityPostsViewModel.getReportPosts().getValue().get(position), owner);
        furturiAdapter.notifyItemRemoved(position);
    }
}