package com.dgp2.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

import java.io.IOException;

public class PantallaCarga extends AppCompatActivity {

    VideoView videocarga;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_carga);
        videocarga=(VideoView) findViewById(R.id.videocarga);

        String path = "android.resource://" + getPackageName() + "/" + R.raw.videoinicio;
        videocarga.setVideoURI(Uri.parse(path));

       /* mp=MediaPlayer.create(this, R.raw.fondo);

        mp.start();*/
        videocarga.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
             /*   mp.stop();
                try {
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                videocarga.stopPlayback();
                videocarga.seekTo(0);
                //mp.seekTo(0);
                Intent intent = new Intent(PantallaCarga.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }
}
