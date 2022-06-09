package com.example.ciclotm.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import com.example.ciclotm.R;

public class LoadingScreenActivity extends AppCompatActivity {

    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        videoView = findViewById(R.id.videoView);
        Uri video = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.ciclotm_loading_screen_video);
        videoView.setVideoURI(video);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                startActivity(new Intent(LoadingScreenActivity.this, MainActivity.class));
                finish();
            }
        });

        videoView.start();

    }

    private void startNextActivity(){
        if(isFinishing())
            return ;
        startActivity(new Intent(LoadingScreenActivity.this,MainActivity.class));
        finish();
    }
}