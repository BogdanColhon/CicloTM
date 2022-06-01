package com.example.ciclotm.Views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.ciclotm.Models.Objects.Report;
import com.example.ciclotm.Models.Users.User;
import com.example.ciclotm.R;
import com.example.ciclotm.ViewModels.ExpandedFurturiPostViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ExpandedFurturiPostActivity extends AppCompatActivity {

    private FirebaseUser user;
    private String currentUser;
    private Calendar calendar;
    private Report clicked_report;
    private TextView usernameTextView;
    private TextView publishDateTextView;
    private TextView theftDateTextView;
    private TextView addressTextView;
    private TextView bikeBrandTextView;
    private TextView bikeTypeTextView;
    private TextView bikeColorTextView;
    private TextView bikeDescriptionTextView;
    private TextView thiefDescriptionTextView;
    private ImageView userImage;
    private ImageView placeImage;
    private ImageView bikeImage;
    private Button contactOwnerButton;
    private ExpandedFurturiPostViewModel mExpandedFurturiPostViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_furturi_post);

        initActionBar();
        initLayout();

        calendar = Calendar.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        currentUser = user.getUid();

        setLocalData();

        mExpandedFurturiPostViewModel = ViewModelProviders.of(this).get(ExpandedFurturiPostViewModel.class);

        initUserProfile();

        contactOwnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                intent1.setData(Uri.parse("tel:" + clicked_report.getUser_phone()));
                startActivity(intent1);
            }
        });


    }

    private void initActionBar() {
        getSupportActionBar().setTitle("Raport");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initLayout() {
        Intent intent = getIntent();
        clicked_report = (Report) intent.getSerializableExtra("clicked_report");

        usernameTextView = findViewById(R.id.upperTextView);
        publishDateTextView = findViewById(R.id.lowerTextView);
        userImage = findViewById(R.id.user_photo);

        theftDateTextView = findViewById(R.id.theftDateTextView);
        addressTextView = findViewById(R.id.theftAddressTextView);
        placeImage = findViewById(R.id.placeImage);

        bikeBrandTextView = findViewById(R.id.theftBikeBrandTextView);
        bikeTypeTextView = findViewById(R.id.theftBikeTypeTextView);
        bikeColorTextView = findViewById(R.id.theftBikeColorTextView);
        bikeDescriptionTextView = findViewById(R.id.theftBikeDescriptionTextView);
        bikeImage = findViewById(R.id.bikeImage);

        contactOwnerButton = findViewById(R.id.expandedFurturiPostContactButton);

        thiefDescriptionTextView = findViewById(R.id.thiefDescriptionTextView);


    }

    private void initUserProfile() {

        mExpandedFurturiPostViewModel.init(clicked_report);
        mExpandedFurturiPostViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                String firstname = user.getFirstName();
                String lastname = user.getLastName();
                String userImageUrl = user.getProfileImageUrl();
                Glide.with(getApplicationContext()).load(userImageUrl).into(userImage);
                usernameTextView.setText(firstname + " " + lastname);
            }
        });
    }

    private void setLocalData() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm  dd/MM/yyyy", Locale.getDefault());
        String output = df.format(clicked_report.getPublishDate());
        publishDateTextView.setText(output);

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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_furt_gasit_menu, menu);
        if (currentUser.equals(clicked_report.getUser_id())) {
            menu.getItem(0).setVisible(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.found:
                mExpandedFurturiPostViewModel.updateStatus(clicked_report, currentUser);
                Toast.makeText(ExpandedFurturiPostActivity.this, "Modificări efectuate", Toast.LENGTH_SHORT).show();
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