package com.dgp2.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

public class PantallaCarga extends AppCompatActivity {

    VideoView videocarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_carga);
        videocarga=(VideoView) findViewById(R.id.videocarga);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.videoinicio;
        videocarga.setVideoURI(Uri.parse(path));
        videocarga.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                videocarga.stopPlayback();
                videocarga.seekTo(0);
                Intent intent = new Intent(PantallaCarga.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }
}
