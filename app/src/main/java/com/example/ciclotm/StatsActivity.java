package com.example.ciclotm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.ciclotm.Models.Report;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class StatsActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private ArrayList<Report> postsList = new ArrayList<>();
    private ArrayList<Report> foundList = new ArrayList<>();
    private TextView totalBikesTextView;
    private TextView topTypeTextView;
    private TextView topColorTextView;
    private TextView totalBikesRecoveredTextView;
    private String topType;
    private String topColor;
    private int topTypeCount=0;
    private int topColorCount=0;
    private HashMap hashMapType;
    private HashMap hashMapColor;
    private int total;
    private int foundTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        getSupportActionBar().setTitle("Statistici");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        hashMapType = new HashMap();
        hashMapType.put("MTB", 0);
        hashMapType.put("Cursieră", 0);
        hashMapType.put("De oraș", 0);
        hashMapType.put("BMX", 0);
        hashMapType.put("Pliabilă", 0);
        hashMapType.put("E-bike", 0);
        hashMapType.put("Fat bike", 0);

        hashMapColor = new HashMap();
        hashMapColor.put("Alb", 0);
        hashMapColor.put("Crem", 0);
        hashMapColor.put("Galben", 0);
        hashMapColor.put("Portocaliu", 0);
        hashMapColor.put("Roșie", 0);
        hashMapColor.put("Roz", 0);
        hashMapColor.put("Mov", 0);
        hashMapColor.put("Verde", 0);
        hashMapColor.put("Turcoaz", 0);
        hashMapColor.put("Albastru", 0);
        hashMapColor.put("Vișiniu", 0);
        hashMapColor.put("Maro", 0);
        hashMapColor.put("Argintiu", 0);
        hashMapColor.put("Gri", 0);
        hashMapColor.put("Negru", 0);

        totalBikesTextView = (TextView) findViewById(R.id.totalBikesTextView);
        totalBikesRecoveredTextView = (TextView) findViewById(R.id.totalBikesRecoveredTextView);
        topTypeTextView = (TextView) findViewById(R.id.typeTopTextView);
        topColorTextView = (TextView) findViewById(R.id.colorTopTextView);

        reference = FirebaseDatabase.getInstance(getResources().getString(R.string.db_instance)).getReference("furturiPosts");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Report newPost = snapshot.getValue(Report.class);
                postsList.add(newPost);
                total = postsList.size();
                if(newPost.getStatus() == 1)
                {
                    foundList.add(newPost);
                    foundTotal = foundList.size();
                }
                totalBikesTextView.setText(String.valueOf(total));
                totalBikesRecoveredTextView.setText(String.valueOf(foundTotal));
                String type = newPost.getBike_model();
                String color = newPost.getBike_color();
                int countType = (int) hashMapType.get(type);
                countType=countType+1;
                int countColor = (int) hashMapColor.get(color);
                countColor=countColor+1;
                if (countType > topTypeCount) {
                    topTypeCount = countType;
                    topType = type;
                }
                hashMapType.put(type, countType);
                float percentageType = (((float) topTypeCount / (float) total) * 100.0f);
                topTypeTextView.setText(topType + " (" + String.valueOf(percentageType) + "%)");

                if (countColor >= topColorCount) {
                    topColorCount = countColor;
                    topColor = color;
                }
                hashMapColor.put(color, countColor);
                float percentageColor = (((float) topColorCount / (float) total) * 100.0f);
                System.out.println(topColorCount + "   "+ total);
                topColorTextView.setText(topColor + " (" + String.valueOf(percentageColor) + "%)");


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }
}