package com.example.ciclotm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ciclotm.Models.Bike;
import com.example.ciclotm.Models.Report;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ExpandedFurturiPostActivity extends AppCompatActivity {

    DatabaseReference reference;
    FirebaseUser user;
    String currentUser;
    private int bikesFound;
    private Report clicked_report;
    private TextView usernameTextView;
    private TextView publishDateTextView;
    private TextView theftDateTextView;
    private TextView addressTextView;
    private TextView bikeBrandTextView;
    private TextView bikeModelTextView;
    private TextView bikeTypeTextView;
    private TextView bikeColorTextView;
    private TextView bikeDescriptionTextView;
    private TextView thiefDescriptionTextView;
    private ImageView userImage;
    private ImageView placeImage;
    private ImageView bikeImage;
    private Button contactOwnerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_furturi_post);

        getSupportActionBar().setTitle("Raport");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        usernameTextView = findViewById(R.id.upperTextView);
        publishDateTextView = findViewById(R.id.lowerTextView);
        userImage = findViewById(R.id.user_photo);

        theftDateTextView = findViewById(R.id.theftDateTextView);
        addressTextView = findViewById(R.id.theftAddressTextView);
        placeImage = findViewById(R.id.placeImage);

        bikeBrandTextView = findViewById(R.id.theftBikeBrandTextView);
        bikeModelTextView = findViewById(R.id.theftBikeModelTextView);
        bikeTypeTextView = findViewById(R.id.theftBikeTypeTextView);
        bikeColorTextView = findViewById(R.id.theftBikeColorTextView);
        bikeDescriptionTextView = findViewById(R.id.theftBikeDescriptionTextView);
        bikeImage = findViewById(R.id.bikeImage);

        contactOwnerButton = findViewById(R.id.expandedFurturiPostContactButton);

        thiefDescriptionTextView = findViewById(R.id.thiefDescriptionTextView);

        Intent intent = getIntent();
        clicked_report = (Report) intent.getSerializableExtra("clicked_report");

        user = FirebaseAuth.getInstance().getCurrentUser();
        currentUser = user.getUid();



        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
        reference.child(clicked_report.getUser_id()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String firstname = userProfile.getFirstName();
                    String lastname = userProfile.getLastName();
                    String userImageUrl = userProfile.getProfileImageUrl();
                    Glide.with(getApplicationContext()).load(userImageUrl).into(userImage);
                    usernameTextView.setText(firstname + " " + lastname);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        SimpleDateFormat df = new SimpleDateFormat("HH:mm  dd/MM/yyyy", Locale.getDefault());
        String output = df.format(clicked_report.getPublishDate());
        publishDateTextView.setText(output);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(clicked_report.getStolenDate());
        int month = calendar.get(Calendar.MONTH) + 1;
        theftDateTextView.setText(calendar.get(Calendar.DAY_OF_MONTH)
                + "/" + month
                + "/" + calendar.get(Calendar.YEAR));
        addressTextView.setText(clicked_report.getAddress());
        String locationImageUrl = clicked_report.getLocationImageUrl();
        if (!locationImageUrl.equals(""))
            Glide.with(this).load(locationImageUrl).into(placeImage);


        bikeBrandTextView.setText(clicked_report.getBike_brand());
        bikeTypeTextView.setText(clicked_report.getBike_model());
        bikeColorTextView.setText(clicked_report.getBike_color());
        bikeDescriptionTextView.setText(clicked_report.getBike_description());
        String bikeImageUrl = clicked_report.getBikeImageUrl();
        System.out.println(bikeImageUrl);
        if (!bikeImageUrl.equals(""))
            Glide.with(this).load(bikeImageUrl).into(bikeImage);


        thiefDescriptionTextView.setText(clicked_report.getThief_description());

        contactOwnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                intent1.setData(Uri.parse("tel:" + clicked_report.getUser_phone()));
                startActivity(intent1);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_furt_gasit_menu, menu);
        if(currentUser.equals(clicked_report.getUser_id())){
            System.out.println(currentUser);
            System.out.println(clicked_report.getUser_id());
            menu.getItem(0).setVisible(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.found:
                Toast.makeText(ExpandedFurturiPostActivity.this,"Raport eliminat",Toast.LENGTH_SHORT).show();
                DatabaseReference PostReference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference()
                        .child("furturiPosts").child(String.valueOf(clicked_report.getPublishDate()));
                PostReference.child("status").setValue(1);

                DatabaseReference LocalPostsReference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference()
                        .child("Users").child(currentUser).child("Furturi").child(String.valueOf(clicked_report.getPublishDate()));
                LocalPostsReference.child("status").setValue(1);
                finish();
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