package com.example.ciclotm;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ciclotm.Admin.AdminCommunityFragment;
import com.example.ciclotm.Admin.AdminFurturiFragment;
import com.example.ciclotm.Admin.AdminGeneralFragment;
import com.example.ciclotm.Admin.AdminTureFragment;

import java.util.ArrayList;

public class ViewPagerAdapterAdmin extends FragmentStateAdapter {

    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    ArrayList<String> fragmentTitle;

    public ViewPagerAdapterAdmin(@NonNull AdminCommunityFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 1:
                return new AdminTureFragment();
            case 2:
                return new AdminFurturiFragment();
            default:
                return new AdminGeneralFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
