package com.example.ciclotm.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ciclotm.Views.FurturiFragment;
import com.example.ciclotm.Views.GeneralFragment;
import com.example.ciclotm.Views.TureFragment;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {

    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    ArrayList<String> fragmentTitle;

    public ViewPagerAdapter(@NonNull Fragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 1:
                return new TureFragment();
            case 2:
                return new FurturiFragment();
            default:
                return new GeneralFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
