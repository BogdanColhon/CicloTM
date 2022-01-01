package com.example.ciclotm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void clickedLogin(View view) {
        startActivity(new Intent(MainActivity.this, MenuActivity.class));
    }
    public void clickedRegister(View view) {
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }
}