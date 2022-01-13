package com.example.ciclotm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminMenuActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu2);
        BottomNavigationView bottomNavigationView2 = findViewById(R.id.bottomNavigationView2);
        NavController navController = Navigation.findNavController(this,  R.id.fragment2);
        NavigationUI.setupWithNavController(bottomNavigationView2, navController);
    }
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}