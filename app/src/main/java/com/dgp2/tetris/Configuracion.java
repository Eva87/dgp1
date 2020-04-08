package com.dgp2.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

public class Configuracion extends AppCompatActivity {

    long TiempoDeEspera;
    private String username;
    MediaPlayer mp;
    int posmu=0, musicon;
    ImageButton mus;
    boolean vuelta=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mus = findViewById(R.id.musicaconfiguracion);

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

        mus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
                SharedPreferences.Editor myEditor = preferences.edit();
                int val = preferences.getInt("Music", 0);
                if (val == 0) {
                    mp.start();
                    mus.setBackground(getDrawable(R.drawable.corcheaon));
                    myEditor.putInt("Music", 1);
                } else {
                    mus.setBackground(getDrawable(R.drawable.corcheaoff));
                    myEditor.putInt("Music", 0);
                    mp.stop();
                    try {
                        mp.prepare();
                        mp.seekTo(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    };
                }
                myEditor.commit();
            }
        });
    }

    public void volveratrasconfiguracion (View view){
        Intent intent = new Intent(Configuracion.this, MainActivity.class);
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

    public void cambiarcolordelaspiezas (View view){
        Intent intent = new Intent(Configuracion.this, Cambiocolorpiezas.class);
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
            Intent intent = new Intent(Configuracion.this, MainActivity.class);
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