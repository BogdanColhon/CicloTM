package com.example.ciclotm;

import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FurturiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FurturiFragment extends Fragment {
    private ArrayList<furturiPost> postsList;
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

    public FurturiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FurturiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FurturiFragment newInstance(String param1, String param2) {
        FurturiFragment fragment = new FurturiFragment();
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
        View view = inflater.inflate(R.layout.fragment_furturi, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.furturiRView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        postsList = new ArrayList<>();
        setPostInfo();
        recyclerView.setAdapter(new furturiRecycleViewAdapter(getContext(), postsList));
        return view;
    }

    private void setPostInfo() {
        postsList.add(new furturiPost("02.12.2021", "Iulius Mall", "Cursiera Triban neagra", ResourcesCompat.getDrawable(getResources(), R.drawable.bike2, null)));
        postsList.add(new furturiPost("20.11.2021", "Calea Martirilor nr 42 SC A", "Bicicleta de oras Diamant, neagra", ResourcesCompat.getDrawable(getResources(), R.drawable.bike1, null)));


    }
}