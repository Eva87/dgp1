package com.dgp2.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

public class ayuda extends AppCompatActivity {

    long TiempoDeEspera;
    MediaPlayer mp;
    int posmu=0, musicon;
    private String username;
    boolean vuelta=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mp=MediaPlayer.create(this, R.raw.musfondo);
        Bundle datos = this.getIntent().getExtras();
        SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = preferences.edit();
        musicon = preferences.getInt("Music", 0);
        if(datos != null) {
            posmu = datos.getInt("posicionmusica");
            vuelta=true;
        }

        if (musicon == 1) {
            if(posmu>0){
                mp.seekTo(posmu);
            }
            mp.start();
        }
    }

    public void volveratrasayuda (View view){
        Intent intent = new Intent(ayuda.this, MainActivity.class);
        intent.putExtra("posicionmusica", mp.getCurrentPosition());
        mp.stop();
        try {
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.seekTo(0);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (TiempoDeEspera + 1500 > System.currentTimeMillis()) {
            Intent intent = new Intent(ayuda.this, MainActivity.class);
            intent.putExtra("posicionmusica", mp.getCurrentPosition());
            mp.stop();
            try {
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.seekTo(0);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Pulsa nuevamente para volver", Toast.LENGTH_SHORT).show();
        }
        TiempoDeEspera = System.currentTimeMillis();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mp.stop();
        vuelta=false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!vuelta)
            mp.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mp.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
        vuelta=false;
    }
}