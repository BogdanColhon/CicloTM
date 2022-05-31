package com.example.ciclotm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.ciclotm.Models.Objects.Report;
import com.example.ciclotm.Models.Posts.generalPost;
import com.example.ciclotm.Models.Posts.turePost;
import com.example.ciclotm.Views.ExpandedGeneralPostActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    mygeneralRecyclerViewAdapter generalAdapter;
    myTureRecyclerViewAdapter tureAdapter;
    myFurturiRecyclerViewAdapter furturiAdapter;
    RecyclerView.LayoutManager layoutManager1;
    RecyclerView.LayoutManager layoutManager2;
    RecyclerView.LayoutManager layoutManager3;

    FirebaseUser user;
    DatabaseReference reference;
    private DatabaseReference PostReference;
    private DatabaseReference LocalPostsReference;

    String owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_posts);
        getSupportActionBar().setTitle("Postări comunitate");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        generalRecyclerView = (RecyclerView) findViewById(R.id.myGeneralCommunityPostsRView);
        tureRecyclerView = (RecyclerView) findViewById(R.id.myTureCommunityPostsRView);
        furturiRecyclerView = (RecyclerView) findViewById(R.id.myFurturiCommunityPostsRView);

        generalLayout = (LinearLayout) findViewById(R.id.myGeneralPostsLinearLayout);
        tureLayout = (LinearLayout) findViewById(R.id.myTurePostsLinearLayout);
        furturiLayout = (LinearLayout) findViewById(R.id.myFurturiPostsLinearLayout);

        layoutManager1 = new LinearLayoutManager(CommunityPostsActivity.this);
        layoutManager2 = new LinearLayoutManager(CommunityPostsActivity.this);
        layoutManager3 = new LinearLayoutManager(CommunityPostsActivity.this);

        generalRecyclerView.setLayoutManager(layoutManager1);
        tureRecyclerView.setLayoutManager(layoutManager2);
        furturiRecyclerView.setLayoutManager(layoutManager3);

        generalAdapter = new mygeneralRecyclerViewAdapter(CommunityPostsActivity.this, myGeneralPosts, this);
        tureAdapter = new myTureRecyclerViewAdapter(CommunityPostsActivity.this, myTurePosts, this);
        furturiAdapter = new myFurturiRecyclerViewAdapter(CommunityPostsActivity.this, myFurturiPosts, this);

        generalRecyclerView.setAdapter(generalAdapter);
        tureRecyclerView.setAdapter(tureAdapter);
        furturiRecyclerView.setAdapter(furturiAdapter);

        generalLayout.setVisibility(View.GONE);
        tureLayout.setVisibility(View.GONE);
        furturiLayout.setVisibility(View.GONE);

        fetchMyGeneralPosts();
        fetchMyTurePosts();
        fetchMyFurturiPosts();

    }

    private void fetchMyGeneralPosts() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
        owner = user.getUid();
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users").child(owner).child("CommunityPosts").child("MyGeneralPosts");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                generalPost myGeneralPost = snapshot.getValue(generalPost.class);
                if (myGeneralPost != null) {
                    generalLayout.setVisibility(View.VISIBLE);
                    myGeneralPosts.add(0, myGeneralPost);
                    generalAdapter.notifyDataSetChanged();
                }

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

    private void fetchMyTurePosts() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
        owner = user.getUid();
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users").child(owner).child("CommunityPosts").child("MyTurePosts");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                turePost myTurePost = snapshot.getValue(turePost.class);
                if (myTurePost != null) {
                    tureLayout.setVisibility(View.VISIBLE);
                    myTurePosts.add(0, myTurePost);
                    tureAdapter.notifyDataSetChanged();
                }
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

    private void fetchMyFurturiPosts() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
        owner = user.getUid();
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users").child(owner).child("Furturi");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Report myFurturiPost = snapshot.getValue(Report.class);
                if (myFurturiPost != null) {
                    if (myFurturiPost.getStatus() == 0) {
                        furturiLayout.setVisibility(View.VISIBLE);
                        myFurturiPosts.add(0, myFurturiPost);
                        furturiAdapter.notifyDataSetChanged();
                    }
                }
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
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onPostClick(int position) {
        System.out.println("pressed item " + position);
        Intent intent = new Intent(CommunityPostsActivity.this, ExpandedGeneralPostActivity.class);
        intent.putExtra("clicked_post", myGeneralPosts.get(position));
        startActivity(intent);
    }

    @Override
    public void onPostTureClick(int position) {
        Intent intent = new Intent(CommunityPostsActivity.this, ExpandedTurePostActivity.class);
        intent.putExtra("clicked_post", myTurePosts.get(position));
        startActivity(intent);
    }

    @Override
    public void onPostFurturiClick(int position) {
        Intent intent = new Intent(CommunityPostsActivity.this, ExpandedFurturiPostActivity.class);
        intent.putExtra("clicked_report", myFurturiPosts.get(position));
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
        System.out.println("Removed position: " + position);
        PostReference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference()
                .child("GeneralPosts").child(String.valueOf(myGeneralPosts.get(position).getDate()));
        PostReference.removeValue();
        LocalPostsReference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference()
                .child("Users").child(owner).child("CommunityPosts").child("MyGeneralPosts").child(String.valueOf(myGeneralPosts.get(position).getDate()));
        LocalPostsReference.removeValue();
        myGeneralPosts.remove(position);
        generalAdapter.notifyItemRemoved(position);
    }

    public void removeTureItem(int position) {
        System.out.println("Removed position: " + position);
        PostReference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference()
                .child("TurePosts").child(String.valueOf(myTurePosts.get(position).getDate()));
        PostReference.removeValue();
        LocalPostsReference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference()
                .child("Users").child(owner).child("CommunityPosts").child("MyTurePosts").child(String.valueOf(myTurePosts.get(position).getDate()));
        LocalPostsReference.removeValue();
        myTurePosts.remove(position);
        tureAdapter.notifyItemRemoved(position);
    }

    public void removeFurturiItem(int position) {
        System.out.println("Removed position: " + position);
        PostReference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference()
                .child("furturiPosts").child(String.valueOf(myFurturiPosts.get(position).getPublishDate()));
        PostReference.removeValue();
        LocalPostsReference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference()
                .child("Users").child(owner).child("Furturi").child(String.valueOf(myFurturiPosts.get(position).getPublishDate()));
        LocalPostsReference.removeValue();
        myFurturiPosts.remove(position);
        furturiAdapter.notifyItemRemoved(position);
    }
}