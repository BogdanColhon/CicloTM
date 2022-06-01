package com.example.ciclotm.Views;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.ciclotm.Adapters.GridAdapter;
import com.example.ciclotm.Admin.AdminDotariObligatoriiActivity;
import com.example.ciclotm.Admin.AdminReguliCirculatieActivity;
import com.example.ciclotm.MainActivity;
import com.example.ciclotm.R;

public class RulesFragment extends Fragment {

    private GridAdapter adapter;
    private GridView gridView;

    public RulesFragment() {
        // Required empty public constructor
    }

    public static RulesFragment newInstance(String param1, String param2) {
        RulesFragment fragment = new RulesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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