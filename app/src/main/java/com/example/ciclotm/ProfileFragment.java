package com.example.ciclotm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ciclotm.Models.Objects.Photo;
import com.example.ciclotm.Models.Users.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ProfileFragment extends Fragment {


    String[] button_names = {"Biciclete", "Statistici", "Ture", "Postări comunitate", "Deconectare"};
    int[] button_icons = {R.drawable.bike_collection_button, R.drawable.stats_bars_button, R.drawable.routes_button, R.drawable.my_community_posts_button, R.drawable.log_out_button};
    ArrayList<profileListViewButton> profile_buttons = new ArrayList<>();
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private Calendar today, birthday;
    private int i = 0;
    TextView text;
    Space space;
    ImageView userProfileImageView;
    ImageView imageView0, imageView1, imageView2, imageView3, imageView4;
    ImageButton openGalleryButton;
    LinearLayout galleryLayout;
    RelativeLayout seeGalleryLayout;
    ArrayList<String> gallery_links = new ArrayList<>();
    ArrayList<ImageView> gallery = new ArrayList<>();
    private StorageReference storageReference;
    private boolean hasPhotos = false;
    final int color = 0x66000000;
    final Drawable dr = new ColorDrawable(color);

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit) {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            intent.putExtra("uId", userID);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("Users");
        userID = user.getUid();

        final TextView nameTextView = (TextView) view.findViewById(R.id.profileFragmentNameTextView);
        final TextView bioTextView = (TextView) view.findViewById(R.id.profileFragmentBioTextView);
        userProfileImageView = (ImageView) view.findViewById(R.id.userProfileImageView);

        text = (TextView) view.findViewById(R.id.profileFragmentGalleryTextView);
        text.setVisibility(View.INVISIBLE);

        space = (Space) view.findViewById(R.id.spacer);
        space.setVisibility(View.INVISIBLE);

        imageView0 = (ImageView) view.findViewById(R.id.imageView0);
        imageView1 = (ImageView) view.findViewById(R.id.imageView1);
        imageView2 = (ImageView) view.findViewById(R.id.imageView2);
        imageView3 = (ImageView) view.findViewById(R.id.imageView3);
        imageView4 = (ImageView) view.findViewById(R.id.imageView4);

        galleryLayout = (LinearLayout) view.findViewById(R.id.fragmentProfileGalleryLayout);
        seeGalleryLayout = (RelativeLayout) view.findViewById(R.id.profileFragmentGalleryRelativeLayout);


        gallery.add(imageView1);
        gallery.add(imageView2);
        gallery.add(imageView3);
        gallery.add(imageView4);


        seeGalleryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), GalleryActivity.class);
                intent.putExtra("uId", userID);
                startActivity(intent);
            }
        });

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), GalleryActivity.class);
                intent.putExtra("uId", userID);
                startActivity(intent);
            }
        });

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String firstname = userProfile.getFirstName().toString();
                    String lastname = userProfile.getLastName().toString();
                    String bio = userProfile.getBio().toString();
                    Date birthDate = userProfile.getBirthDate();

                    birthday = Calendar.getInstance();
                    birthday.setTime(birthDate);
                    today = Calendar.getInstance();
                    int age = today.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
                    if (today.get(Calendar.DAY_OF_YEAR) < birthday.get(Calendar.DAY_OF_YEAR)) {
                        age--;
                    }

                    nameTextView.setText(firstname + " " + lastname + ", " + String.valueOf(age));
                    bioTextView.setText(bio);
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

        reference.child(userID).child("Gallery").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    galleryLayout.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child(userID).child("Gallery").limitToLast(4).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Photo photo = dataSnapshot.getValue(Photo.class);
                if (photo.getPhotoUrl() != null) {
                    gallery_links.add(photo.getPhotoUrl());
                    if (i < 3)
                        Glide.with(getContext()).load(photo.getPhotoUrl()).into(gallery.get(i));
                    if (i == 3) {
                        seeGalleryLayout.setVisibility(View.GONE);
                        imageView1.setForeground(dr);
                        imageView4.setVisibility(View.VISIBLE);
                        text.setVisibility(View.VISIBLE);
                        space.setVisibility(View.VISIBLE);
                        Glide.with(getContext()).load(photo.getPhotoUrl()).into(gallery.get(i));
                    }
                    i++;
                    hasPhotos = true;
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });

        ListView profileListView = (ListView) view.findViewById(R.id.profileListView);
        for (int i = 0; i < button_names.length; i++) {
            profileListViewButton button = new profileListViewButton(button_names[i], button_icons[i]);
            profile_buttons.add(button);
        }
        profileListViewAdapter adapter = new profileListViewAdapter(getContext(), profile_buttons);
        profileListView.setAdapter(adapter);
        profileListView.setClickable(true);
        profileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent i = new Intent(getActivity(), BicyclesActivity.class);
                        startActivity(i);
                        break;
                    case 1:
                        i = new Intent(getActivity(), BikeStatsActivity.class);
                        startActivity(i);
                        break;
                    case 2:
                        i = new Intent(getActivity(), RoutePostsActivity.class);
                        startActivity(i);
                        break;
                    case 3:
                        i = new Intent(getActivity(), CommunityPostsActivity.class);
                        startActivity(i);
                        break;
                    case 4:
                        FirebaseAuth.getInstance().signOut();
                        SharedPreferences preferences = getActivity().getSharedPreferences("credentials", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        i = new Intent(getActivity(), MainActivity.class);
                        startActivity(i);
                        getActivity().finish();
                        break;
                }
            }
        });


        return view;


    }


    public void getUserProfilePhoto(String profileImageUrl) throws IOException {
        if (!profileImageUrl.equals("")) {
            Glide.with(getContext()).load(profileImageUrl).into(userProfileImageView);
        }
    }

    public void onResume() {
        super.onResume();

        // Set title bar
        ((MenuActivity) getActivity())
                .setActionBarTitle("Profil");

    }
}