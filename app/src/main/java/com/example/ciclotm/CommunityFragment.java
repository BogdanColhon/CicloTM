package com.example.ciclotm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ciclotm.Adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class CommunityFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    ViewPagerAdapter vpAdapter;

    public CommunityFragment() {
        // Required empty public constructor
    }

    public static CommunityFragment newInstance() {
        CommunityFragment fragment = new CommunityFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        ((MenuActivity) getActivity()).setActionBarTitle("Comunitate");
    }
}