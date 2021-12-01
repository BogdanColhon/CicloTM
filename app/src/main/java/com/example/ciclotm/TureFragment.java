package com.example.ciclotm;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TureFragment extends Fragment {

    private ArrayList<Post> postsList;
    private RecyclerView recyclerView;
    recycleViewAdapter adapter;
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
        recyclerView=(RecyclerView) view.findViewById(R.id.tureRView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        postsList=new ArrayList<>();
        setPostInfo();
        //postsList.add(new Post("Accident","S-a produs un acciden.."));
        //postsList.add(new Post("Unealta","Are cineva o cheie.."));
        //postsList.add(new Post("Competitie","X-bike 2021 este o comp.."));
        recyclerView.setAdapter(new recycleViewAdapter(getContext(),postsList));
        return view;
    }

    public static TureFragment newInstance(){
        return new TureFragment();
    }

    private void setAdapter(){
        recycleViewAdapter adapter = new recycleViewAdapter(getContext(),postsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
    private void  setPostInfo(){
        postsList.add(new Post("Tura 50km","Data 1.12.2021 ora 19.00"));
        postsList.add(new Post("Tura Granita Serbia","Plecare la ora 9.00"));
        postsList.add(new Post("Tura Padure Giroc","Plecare la ora 12:00"));

    }
}