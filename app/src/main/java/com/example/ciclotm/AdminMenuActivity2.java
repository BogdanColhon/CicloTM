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
    ProgressDialog TempDialog;
    CountDownTimer mCountDownTimer;
    int i =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu2);
        //terminator=this;
        TempDialog = new ProgressDialog(AdminMenuActivity2.this);
        TempDialog.setMessage("Wait");
        TempDialog.setCancelable(false);
        TempDialog.setProgress(i);
        TempDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        TempDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        TempDialog.show();
        mCountDownTimer = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TempDialog.setMessage("wait");
            }

            @Override
            public void onFinish() {
                TempDialog.dismiss();
            }
        }.start();
        BottomNavigationView bottomNavigationView2 = findViewById(R.id.bottomNavigationView2);
        NavController navController = Navigation.findNavController(this,  R.id.fragment2);
        NavigationUI.setupWithNavController(bottomNavigationView2, navController);
    }
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}