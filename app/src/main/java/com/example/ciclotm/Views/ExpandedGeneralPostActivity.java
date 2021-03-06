package com.example.ciclotm.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ciclotm.Adapters.generalCommentsRecyclerViewAdapter;
import com.example.ciclotm.Models.Objects.Comment;
import com.example.ciclotm.Models.Posts.generalPost;
import com.example.ciclotm.Models.Users.User;
import com.example.ciclotm.R;
import com.example.ciclotm.ViewModels.ExpandedGeneralPostViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExpandedGeneralPostActivity extends AppCompatActivity implements generalCommentsRecyclerViewAdapter.OnPostListener {

    TextView titlePostTextView;
    TextView datePostTextView;
    TextView contentPostTextView;
    TextView userNamePostTextView;
    EditText commentInputEditText;
    ImageView userProfileImageView;
    ImageView commentUserProfileImageView;
    ImageView sendCommentImageView;
    private Date today;

    private ArrayList<Comment> commentsList = new ArrayList<>();


    private RecyclerView recyclerView;
    private generalCommentsRecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private User userExtra;
    private generalPost post;
    private FirebaseUser user;
    private String userID;
    private DatabaseReference reference;
    private DatabaseReference reference2;
    private DatabaseReference CommentReference;
    private ExpandedGeneralPostViewModel mExpandedGeneralPostViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_general_post);

        today = Calendar.getInstance().getTime();
        initActionBar();
        initLayout();

        mExpandedGeneralPostViewModel = ViewModelProviders.of(this).get(ExpandedGeneralPostViewModel.class);
        mExpandedGeneralPostViewModel.init(post);
        mExpandedGeneralPostViewModel.getComments().observe(this, new Observer<ArrayList<Comment>>() {
            @Override
            public void onChanged(ArrayList<Comment> comments) {
                adapter.updateGeneralCommentList(comments);
            }
        });
        initRecyclerView();
        setLocalData();


        initFirebaseUser();


        userProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                reference2 = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
                reference2.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userProfile = snapshot.getValue(User.class);
                        if (userProfile.getIsAdmin().equals("0")) {
                            Intent intent = new Intent(ExpandedGeneralPostActivity.this, ProfileActivity.class);
                            intent.putExtra("clicked_user", userExtra);
                            startActivity(intent);
                        }
                        if (userProfile.getIsAdmin().equals("1")) {
                            Intent intent = new Intent(ExpandedGeneralPostActivity.this, AdminProfileActivity.class);
                            intent.putExtra("clicked_user", userExtra);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        sendCommentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                today = Calendar.getInstance().getTime();
                String commentContent = commentInputEditText.getText().toString().trim();
                Comment comment = new Comment(commentContent, today, userID);
                FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("GeneralPosts").child(String.valueOf(post.getDate())).child("comments").child(String.valueOf(today))
                        .setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        commentInputEditText.setText("");
                        Toast.makeText(ExpandedGeneralPostActivity.this, "Comentariu ad??ugat", Toast.LENGTH_SHORT).show();
                    }

                });

            }
        });
    }

    private void initActionBar() {
        getSupportActionBar().setTitle("Postare");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initLayout() {
        Intent intent = getIntent();
        post = (generalPost) intent.getSerializableExtra("clicked_post");
        userNamePostTextView = (TextView) findViewById(R.id.upperTextView);
        datePostTextView = (TextView) findViewById(R.id.lowerTextView);
        titlePostTextView = (TextView) findViewById(R.id.titleTextView);
        contentPostTextView = (TextView) findViewById(R.id.contentTextView);
        commentInputEditText = (EditText) findViewById(R.id.expandedGeneralPostCommentInputEditText);
        userProfileImageView = (ImageView) findViewById(R.id.user_photo);
        commentUserProfileImageView = (ImageView) findViewById(R.id.expandedGeneralPostCommentUserImageView);
        sendCommentImageView = (ImageView) findViewById(R.id.expandedGeneralPostSendImageView);
        recyclerView = (RecyclerView) findViewById(R.id.expandedGeneralPostRView);

        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
        reference.child(post.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                userExtra = userProfile;

                if (userProfile != null) {
                    String firstname = userProfile.getFirstName();
                    String lastname = userProfile.getLastName();

                    userNamePostTextView.setText(firstname + " " + lastname);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(ExpandedGeneralPostActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new generalCommentsRecyclerViewAdapter(ExpandedGeneralPostActivity.this, commentsList, this);
        recyclerView.setAdapter(adapter);
    }

    private void setLocalData() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm  dd/MM/yyyy", Locale.getDefault());
        String output = df.format(post.getDate());
        datePostTextView.setText(output);

        titlePostTextView.setText(post.getTitle());
        contentPostTextView.setText(post.getContent());


        String userImageUrl = post.getUserImageUrl();
        Glide.with(this).load(userImageUrl).into(userProfileImageView);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }

    public void initFirebaseUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    try {
                        getUserProfilePhoto(userProfile.getProfileImageUrl());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public void getUserProfilePhoto(String profileImageUrl) throws IOException {
        if (!profileImageUrl.equals("")) {
            Glide.with(this).load(profileImageUrl).into(commentUserProfileImageView);
        }
    }

    @Override
    public void OnDeleteClick(int position) {
        removeCommentItem(position);
    }

    private void removeCommentItem(int position) {
        CommentReference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference()
                .child("GeneralPosts").child(String.valueOf(post.getDate())).child("comments").child(String.valueOf(commentsList.get(position).getDate()));
        CommentReference.removeValue();
        commentsList.remove(position);
        adapter.notifyItemRemoved(position);
    }
}