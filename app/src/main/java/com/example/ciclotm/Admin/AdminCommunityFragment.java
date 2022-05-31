package com.example.ciclotm.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ciclotm.AdminMenuActivity2;
import com.example.ciclotm.R;
import com.example.ciclotm.Adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminCommunityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminCommunityFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    ViewPagerAdapter vpAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminCommunityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminCommunityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminCommunityFragment newInstance(String param1, String param2) {
        AdminCommunityFragment fragment = new AdminCommunityFragment();
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
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        String[] tabTtiles = {"General", "Ture", "Furturi"};
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager2) view.findViewById(R.id.viewPager);
        vpAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(vpAdapter);
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(tabTtiles[position]);
                    }
                }).attach();


        return view;
    }

    public void onResume() {
        super.onResume();

        // Set title bar
        ((AdminMenuActivity2) getActivity())
                .setActionBarTitle("Comunitate");

    }
}