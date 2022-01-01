package com.example.ciclotm;

import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeneralFragment extends Fragment {

    private ArrayList<generalPost> postsList;
    private RecyclerView recyclerView;
    generalRecycleViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GeneralFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TureFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GeneralFragment newInstance(String param1, String param2) {
        GeneralFragment fragment = new GeneralFragment();
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
        View view = inflater.inflate(R.layout.fragment_general, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.generalRView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        postsList = new ArrayList<>();
        setPostInfo();
        recyclerView.setAdapter(new generalRecycleViewAdapter(getContext(), postsList));
        return view;
    }

    public static TureFragment newInstance() {
        return new TureFragment();
    }

    private void setAdapter() {
        generalRecycleViewAdapter adapter = new generalRecycleViewAdapter(getContext(), postsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setPostInfo() {
        postsList.add(new generalPost("Accident 1", "Data 1.12.2021 ora 19.00", ResourcesCompat.getDrawable(getResources(), R.drawable.user_photo, null)));
        postsList.add(new generalPost("Accident 2", "Plecare la ora 9.00",ResourcesCompat.getDrawable(getResources(), R.drawable.user_photo, null)));
        postsList.add(new generalPost("Accident 3", "Plecare la ora 12:00",ResourcesCompat.getDrawable(getResources(), R.drawable.user_photo, null)));

    }
}