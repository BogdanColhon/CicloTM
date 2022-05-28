package com.example.ciclotm;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.ciclotm.Admin.AdminDotariObligatoriiActivity;
import com.example.ciclotm.Admin.AdminReguliCirculatieActivity;
import com.example.ciclotm.ReguliCirculatie.ReguliCirculatieActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RulesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RulesFragment extends Fragment {

    FirebaseUser user;
    DatabaseReference reference;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private GridAdapter adapter;
    private GridView gridView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RulesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RulesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RulesFragment newInstance(String param1, String param2) {
        RulesFragment fragment = new RulesFragment();
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
        View view = inflater.inflate(R.layout.fragment_rules, container, false);

        gridView = (GridView) view.findViewById(R.id.gridView);
        String[] titles = {"Reguli de circulație", "Semne de circulație", "Echipare obligatorie", "Îmbunătățirea siguranței"};
        int[] images = {R.drawable.rules_attention_sign, R.drawable.rules_traffic_signs, R.drawable.rules_echipare_obligatorie, R.drawable.rules_improve_safety};
        adapter = new GridAdapter(getContext(), titles, images);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (MainActivity.role.equals("0"))
                            startActivity(new Intent(getActivity(), ReguliCirculatieActivity.class));
                        else
                            startActivity(new Intent(getActivity(), AdminReguliCirculatieActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), SemneCirculatieActivity.class));
                        break;
                    case 2:
                        if (MainActivity.role.equals("0"))
                            startActivity(new Intent(getActivity(), DotariObligatoriiActivity.class));
                        else
                            startActivity(new Intent(getActivity(), AdminDotariObligatoriiActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(getActivity(), ImbunatatireaCirculatieiActivity.class));
                        break;

                }

            }
        });
        return view;


    }

    public void onResume() {
        super.onResume();

        // Set title bar
        if (MainActivity.role.equals("0"))
            ((MenuActivity) getActivity()).setActionBarTitle("Legislație");
        else {
            ((AdminMenuActivity2) getActivity()).setActionBarTitle("Legislație");
        }

    }
}