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
public class TureFragment extends Fragment {

    private ArrayList<turePost> postsList;
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

    public TureFragment() {
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
    public static TureFragment newInstance(String param1, String param2) {
        TureFragment fragment = new TureFragment();
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
        View view = inflater.inflate(R.layout.fragment_ture, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.tureRView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        postsList = new ArrayList<>();
        setPostInfo();
        recyclerView.setAdapter(new tureRecycleViewAdapter(getContext(), postsList));
        return view;
    }

    public static TureFragment newInstance() {
        return new TureFragment();
    }


    private void setPostInfo() {
        postsList.add(new turePost("Distanta - 40", "Ora plecare - 12:00", "Punct de plecare - Giratoriu AEM", "Participanti - 1", ResourcesCompat.getDrawable(getResources(), R.drawable.user_photo, null)));
        postsList.add(new turePost("Distanta - 20", "Ora plecare - 18:00", "Punct de plecare - Stadion", "Participanti - 2", ResourcesCompat.getDrawable(getResources(), R.drawable.user_photo, null)));
        postsList.add(new turePost("Distanta - 15", "Ora plecare - 12:00", "Punct de plecare - Stadion", "Participanti - 1", ResourcesCompat.getDrawable(getResources(), R.drawable.user_photo, null)));

    }
}