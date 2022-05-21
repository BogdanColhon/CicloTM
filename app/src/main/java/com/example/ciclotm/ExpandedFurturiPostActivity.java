package com.example.ciclotm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ciclotm.Models.Report;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ExpandedFurturiPostActivity extends AppCompatActivity {

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
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_furturi_post);

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
        Report clicked_report= (Report) intent.getSerializableExtra("clicked_report");
        System.out.println(clicked_report.getUser_id());

        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
        reference.child(clicked_report.getUser_id()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String firstname = userProfile.getFirstName();
                    String lastname = userProfile.getLastName();
                    String userImageUrl = userProfile.getProfileImageUrl();

                    Picasso.get().load(userImageUrl).into(userImage);
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
        if(!locationImageUrl.equals(""))
        Picasso.get().load(locationImageUrl).into(placeImage);


        bikeBrandTextView.setText(clicked_report.getBike_brand());
        bikeTypeTextView.setText(clicked_report.getBike_model());
        bikeColorTextView.setText(clicked_report.getBike_color());
        bikeDescriptionTextView.setText(clicked_report.getBike_description());
        String bikeImageUrl = clicked_report.getBikeImageUrl();
        System.out.println(bikeImageUrl);
        if(!bikeImageUrl.equals(""))
        Picasso.get().load(bikeImageUrl).into(bikeImage);

        thiefDescriptionTextView.setText(clicked_report.getThief_description());

        contactOwnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                intent1.setData(Uri.parse("tel:0756082029"));
                startActivity(intent1);
            }
        });


    }
}