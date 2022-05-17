package com.example.ciclotm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ciclotm.Models.Photo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {


    String[] button_names = {"Biciclete", "Statistici", "Ture", "Postări comunitate", "Deconectare"};
    int[] button_icons = {R.drawable.bike_collection_button, R.drawable.stats_bars_button, R.drawable.routes_button, R.drawable.my_community_posts_button, R.drawable.log_out_button};
    ArrayList<profileListViewButton> profile_buttons = new ArrayList<>();
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private Calendar today, birthday;
    private int i = 0;
    ImageView userProfileImageView;
    ImageView imageView1, imageView2, imageView3;
    ImageButton openGalleryButton;
    ArrayList<String> gallery_links = new ArrayList<>();
    ArrayList<ImageView> gallery = new ArrayList<>();
    private StorageReference storageReference;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        final TextView nameTextView = (TextView) view.findViewById(R.id.profileNameTextView);
        final TextView ageTextView = (TextView) view.findViewById(R.id.profileAgeTextView);
        userProfileImageView = (ImageView) view.findViewById(R.id.userProfileImageView);

        imageView1 = (ImageView) view.findViewById(R.id.imageView1);
        imageView2 = (ImageView) view.findViewById(R.id.imageView2);
        imageView3 = (ImageView) view.findViewById(R.id.imageView3);

        openGalleryButton = (ImageButton) view.findViewById(R.id.galleryImageButton);

        gallery.add(imageView1);
        gallery.add(imageView2);
        gallery.add(imageView3);


        //  Button editProfileButton = (Button) view.findViewById(R.id.editProfileButton);
//        editProfileButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), EditProfileActivity.class);
//                intent.putExtra("uId", userID);
//                startActivity(intent);
//            }
//        });

        openGalleryButton.setOnClickListener(new View.OnClickListener() {
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
                    Date birthDate = userProfile.getBirthDate();

                    birthday = Calendar.getInstance();
                    birthday.setTime(birthDate);
                    today = Calendar.getInstance();
                    int age = today.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
                    if (today.get(Calendar.DAY_OF_YEAR) < birthday.get(Calendar.DAY_OF_YEAR)) {
                        age--;
                    }

                    nameTextView.setText(firstname + " " + lastname);
                    ageTextView.setText(String.valueOf(age) + " ani");
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

        reference.child(userID).child("Gallery").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Photo photo = dataSnapshot.getValue(Photo.class);
                gallery_links.add(0, photo.getPhotoUrl());
                if (i < 3)
                    Picasso.get().load(photo.getPhotoUrl()).resize(300, 300).centerCrop().into(gallery.get(i));
                i++;
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
                        i = new Intent(getActivity(), StatsActivity.class);
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
            Picasso.get().load(profileImageUrl).fit().centerInside().into(userProfileImageView);
        }
    }

    public void onResume() {
        super.onResume();

        // Set title bar
        ((MenuActivity) getActivity())
                .setActionBarTitle("Profil");

    }
}