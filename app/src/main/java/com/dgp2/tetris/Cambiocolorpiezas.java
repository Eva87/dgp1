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

public class Cambiocolorpiezas extends AppCompatActivity {
    Intent intent;
    private ImageButton boton1, boton2, boton3, boton4, boton5, boton6;
    long TiempoDeEspera;
    private String username;
    MediaPlayer mp;
    int posmu, musicon;
    boolean vuelta=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiocolorpiezas);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mp=MediaPlayer.create(this, R.raw.musfondo);
        Bundle datos = this.getIntent().getExtras();
        SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = preferences.edit();
        musicon = preferences.getInt("Music", 0);
        if(datos != null) {
            posmu = datos.getInt("posicionmusica");
            if (musicon == 1) {
                if(posmu>0){
                    mp.seekTo(posmu);
                }
                mp.start();
            }
            vuelta=true;
        }
        boton1 = findViewById(R.id.color1);
        boton2 = findViewById(R.id.color2);
        boton3 = findViewById(R.id.color3);
        boton4 = findViewById(R.id.color4);
        boton5 = findViewById(R.id.color5);
        boton6 = findViewById(R.id.color6);

        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Cambiocolorpiezas.this, MainActivity.class);
                intent.putExtra("posicionmusica", mp.getCurrentPosition());
                SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
                SharedPreferences.Editor myEditor = preferences.edit();
                myEditor.putInt("colorfichas", 1);
                myEditor.commit();
                mp.stop();
                try {
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.seekTo(0);
                intent.putExtra("TipoPiezas", 1);
                startActivity(intent);
                finish();
            }
        });

        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Cambiocolorpiezas.this, MainActivity.class);
                intent.putExtra("posicionmusica", mp.getCurrentPosition());
                SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
                SharedPreferences.Editor myEditor = preferences.edit();
                myEditor.putInt("colorfichas", 2);
                myEditor.commit();
                mp.stop();
                try {
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.seekTo(0);
                intent.putExtra("TipoPiezas", 2);
                startActivity(intent);
                finish();
            }
        });

        boton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Cambiocolorpiezas.this, MainActivity.class);
                intent.putExtra("posicionmusica", mp.getCurrentPosition());
                SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
                SharedPreferences.Editor myEditor = preferences.edit();
                myEditor.putInt("colorfichas", 3);
                myEditor.commit();
                mp.stop();
                try {
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.seekTo(0);
                intent.putExtra("TipoPiezas", 3);
                startActivity(intent);
                finish();
            }
        });

        boton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Cambiocolorpiezas.this, MainActivity.class);
                intent.putExtra("posicionmusica", mp.getCurrentPosition());
                SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
                SharedPreferences.Editor myEditor = preferences.edit();
                myEditor.putInt("colorfichas", 4);
                myEditor.commit();
                mp.stop();
                try {
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.seekTo(0);
                intent.putExtra("TipoPiezas", 4);
                startActivity(intent);
                finish();
            }
        });

        boton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Cambiocolorpiezas.this, MainActivity.class);
                intent.putExtra("posicionmusica", mp.getCurrentPosition());
                SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
                SharedPreferences.Editor myEditor = preferences.edit();
                myEditor.putInt("colorfichas", 5);
                myEditor.commit();
                mp.stop();
                try {
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.seekTo(0);
                intent.putExtra("TipoPiezas", 5);
                startActivity(intent);
                finish();
            }
        });

        boton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Cambiocolorpiezas.this, MainActivity.class);
                intent.putExtra("posicionmusica", mp.getCurrentPosition());
                SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
                SharedPreferences.Editor myEditor = preferences.edit();
                myEditor.putInt("colorfichas", 6);
                myEditor.commit();
                mp.stop();
                try {
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.seekTo(0);
                intent.putExtra("TipoPiezas", 6);
                startActivity(intent);
                finish();
            }
        });
    }

    public void volveratrascolorpiezas (View view){
        Intent intent = new Intent(Cambiocolorpiezas.this, MainActivity.class);
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
            Intent intent = new Intent(Cambiocolorpiezas.this, MainActivity.class);
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