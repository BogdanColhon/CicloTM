package com.example.ciclotm.Admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.ciclotm.AdminMenuActivity2;
import com.example.ciclotm.Views.DotariObligatoriiActivity;
import com.example.ciclotm.Adapters.GridAdapter;
import com.example.ciclotm.Views.ImbunatatireaCirculatieiActivity;
import com.example.ciclotm.R;
import com.example.ciclotm.Views.ReguliCirculatieActivity;
import com.example.ciclotm.Views.SemneCirculatieActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminRulesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminRulesFragment extends Fragment {
    private GridAdapter adapter;
    private GridView gridView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminRulesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminRulesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminRulesFragment newInstance(String param1, String param2) {
        AdminRulesFragment fragment = new AdminRulesFragment();
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
        String [] titles = {"Reguli de circulație","Semne de circulație","Echipare obligatorie","Îmbunătățirea siguranței"};
        int[] images= {R.drawable.rules_attention_sign,R.drawable.rules_traffic_signs,R.drawable.rules_echipare_obligatorie,R.drawable.rules_improve_safety};
        adapter = new GridAdapter(getContext(),titles,images);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0: startActivity(new Intent(getActivity(), ReguliCirculatieActivity.class));
                        break;
                    case 1: startActivity(new Intent(getActivity(), SemneCirculatieActivity.class));
                        break;
                    case 2: startActivity(new Intent(getActivity(), DotariObligatoriiActivity.class));
                        break;
                    case 3: startActivity(new Intent(getActivity(), ImbunatatireaCirculatieiActivity.class));
                        break;

                }

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AdminMenuActivity2) getActivity())
                .setActionBarTitle("Legislație");
    }
}