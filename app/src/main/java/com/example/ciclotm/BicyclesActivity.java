package com.example.ciclotm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BicyclesActivity extends AppCompatActivity {
    private ImageView left_icon;
    private ImageView right_icon;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bicycles);

        left_icon = findViewById(R.id.left_icon);
        right_icon = findViewById(R.id.right_icon);
        toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Bicilete");

        left_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BicyclesActivity.this, "Left", Toast.LENGTH_SHORT).show();
            }
        });
        right_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BicyclesActivity.this, "Right", Toast.LENGTH_SHORT).show();
                System.out.println("apasat");
            }
        });
    }
}