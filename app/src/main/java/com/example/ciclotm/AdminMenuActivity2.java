package com.example.ciclotm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminMenuActivity2 extends AppCompatActivity {
    int i =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu2);
        //terminator=this;
        BottomNavigationView bottomNavigationView2 = findViewById(R.id.bottomNavigationView2);
        NavController navController = Navigation.findNavController(this,  R.id.fragment2);
        NavigationUI.setupWithNavController(bottomNavigationView2, navController);
    }
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}