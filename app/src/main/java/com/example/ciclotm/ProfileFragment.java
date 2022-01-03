package com.example.ciclotm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {


    String[] button_names = {"Biciclete", "Statistici", "Postări comunitate", "Deconectare"};
    ArrayList<profileListViewButton> profile_buttons = new ArrayList<>();
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private Calendar today, birthday;
    ImageView userProfileImageView;
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
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String firstname = userProfile.FirstName;
                    String lastname = userProfile.LastName;
                    Date birthDate = userProfile.BirthDate;

                    birthday = Calendar.getInstance();
                    birthday.setTime(birthDate);
                    today = Calendar.getInstance();
                    int age = today.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
                    if (today.get(Calendar.DAY_OF_YEAR) < birthday.get(Calendar.DAY_OF_YEAR)) {
                        age--;
                    }

                    nameTextView.setText(firstname + " " + lastname);
                    ageTextView.setText(String.valueOf(age));
                    try {
                        getUserProfilePhoto();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        ListView profileListView = (ListView) view.findViewById(R.id.profileListView);
        for (int i = 0; i < button_names.length; i++) {
            profileListViewButton button = new profileListViewButton(button_names[i], ">");
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
                        i = new Intent(getActivity(), CommunityPostsActivity.class);
                        startActivity(i);
                        break;
                    case 3:
                        FirebaseAuth.getInstance().signOut();
                        i = new Intent(getActivity(), MainActivity.class);
                        startActivity(i);
                        getActivity().finish();
                        break;
                }
            }
        });


        return view;
    }
    public void getUserProfilePhoto() throws IOException {
        storageReference = FirebaseStorage.getInstance().getReference().child("UsersProfilePicture/person.png");
        File localFile= File.createTempFile("tempFile","png");
        storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                userProfileImageView.setImageBitmap(bitmap);
            }
        });
    }

    public void onResume() {
        super.onResume();

        // Set title bar
        ((MenuActivity) getActivity())
                .setActionBarTitle("Profil");

    }
}