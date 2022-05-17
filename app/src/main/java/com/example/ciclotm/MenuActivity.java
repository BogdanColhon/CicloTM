package com.example.ciclotm;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity {
    public static Activity terminator;
    private MapsFragment mapsFragment;
    private CommunityFragment communityFragment;
    private RecordFragment recordFragment;
    private RulesFragment rulesFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        terminator = this;
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // NavController navController = Navigation.findNavController(this, R.id.fragment);
        // NavigationUI.setupWithNavController(bottomNavigationView, navController);
        mapsFragment = new MapsFragment();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Fragment fragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.mapsFragment:
                        fragment = new MapsFragment();
                        break;

                    case R.id.communityFragment:
                        fragment = new CommunityFragment();
                        break;

                    case R.id.storeFragment:
                        fragment = new RecordFragment();
                        break;

                    case R.id.rulesFragment:
                        fragment = new RulesFragment();
                        break;

                    case R.id.profileFragment:
                        fragment = new ProfileFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
                return true;
            }
        });
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}