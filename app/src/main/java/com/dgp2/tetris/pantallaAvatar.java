package com.dgp2.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

public class pantallaAvatar extends AppCompatActivity {

    private Intent intent=null;
    int posmu=0, musicon;
    MediaPlayer mp;
    int tp;
    String nombre;
    private long tiempoesperavolver;
    private String username;
    long TiempoDeEspera;
    boolean vuelta=false;

    private ImageButton botonavatar1,botonavatar2,botonavatar3,botonavatar4,botonavatar5,botonavatar6,botonavatar7,botonavatar8,botonavatar9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_avatar);

        botonavatar1=findViewById(R.id.avatar1);
        botonavatar2=findViewById(R.id.avatar2);
        botonavatar3=findViewById(R.id.avatar3);
        botonavatar4=findViewById(R.id.avatar4);
        botonavatar5=findViewById(R.id.avatar5);
        botonavatar6=findViewById(R.id.avatar6);
        botonavatar7=findViewById(R.id.avatar7);
        botonavatar8=findViewById(R.id.avatar8);
        botonavatar9=findViewById(R.id.avatar9);

        mp=MediaPlayer.create(this, R.raw.musfondo);

        Bundle datos = this.getIntent().getExtras();
        SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = preferences.edit();
        musicon = preferences.getInt("Music", 0);

        if(datos != null) {
            tp = datos.getInt("TipoPieza");
            posmu = datos.getInt("posicionmusica");
            nombre=datos.getString("Nombre");
            vuelta=true;
        }
        else{
            tp=0;
            nombre="Anonimo";
        }

        if (musicon == 1) {
            if(posmu>0){
                mp.seekTo(posmu);
            }
            mp.start();
        }

        botonavatar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(pantallaAvatar.this, PantallaJuego.class);
                intent.putExtra("TipoPieza", tp);
                intent.putExtra("Nombre", nombre);
                intent.putExtra("Music", 0);
                intent.putExtra("avatar", 1);
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
        });

        botonavatar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(pantallaAvatar.this, PantallaJuego.class);
                intent.putExtra("TipoPieza", tp);
                intent.putExtra("Nombre", nombre);
                intent.putExtra("Music", 0);
                intent.putExtra("avatar", 2);
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
        });

        botonavatar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(pantallaAvatar.this, PantallaJuego.class);
                intent.putExtra("TipoPieza", tp);
                intent.putExtra("Nombre", nombre);
                intent.putExtra("Music", 0);
                intent.putExtra("avatar", 3);
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
        });

        botonavatar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(pantallaAvatar.this, PantallaJuego.class);
                intent.putExtra("TipoPieza", tp);
                intent.putExtra("Nombre", nombre);
                intent.putExtra("Music", 0);
                intent.putExtra("avatar", 4);
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
        });

        botonavatar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(pantallaAvatar.this, PantallaJuego.class);
                intent.putExtra("TipoPieza", tp);
                intent.putExtra("Nombre", nombre);
                intent.putExtra("Music", 0);
                intent.putExtra("avatar", 5);
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
        });

        botonavatar6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(pantallaAvatar.this, PantallaJuego.class);
                intent.putExtra("TipoPieza", tp);
                intent.putExtra("Nombre", nombre);
                intent.putExtra("Music", 0);
                intent.putExtra("avatar", 6);
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
        });

        botonavatar7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(pantallaAvatar.this, PantallaJuego.class);
                intent.putExtra("TipoPieza", tp);
                intent.putExtra("Nombre", nombre);
                intent.putExtra("Music", 0);
                intent.putExtra("avatar", 7);
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
        });

        botonavatar8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(pantallaAvatar.this, PantallaJuego.class);
                intent.putExtra("TipoPieza", tp);
                intent.putExtra("Nombre", nombre);
                intent.putExtra("Music", 0);
                intent.putExtra("avatar", 8);
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
        });

        botonavatar9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(pantallaAvatar.this, PantallaJuego.class);
                intent.putExtra("TipoPieza", tp);
                intent.putExtra("Nombre", nombre);
                intent.putExtra("Music", 0);
                intent.putExtra("avatar", 9);
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
        });
    }

    public void volveratrasavatar (View view){
        Intent intent = new Intent(pantallaAvatar.this, MainActivity.class);
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
            Intent intent = new Intent(pantallaAvatar.this, MainActivity.class);
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
        if(!vuelta)
            mp.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
        vuelta=false;
    }
}