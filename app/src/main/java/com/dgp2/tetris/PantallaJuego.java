package com.dgp2.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class PantallaJuego extends AppCompatActivity {

    private ImageButton izq;
    private ImageButton der;
    private ImageButton abajo;
    private ImageButton girar;
    private TextView puntos;
    private Intent intent=null;
    private long tiempoesperavolver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_juego);
        initViews();
    }



    public void moverizq(View view){


    }
    public void moverder(View view){


    }
    public void moverabajo(View view){


    }
    public void giropieza(View view){


    }

    private void initViews(){
        izq = findViewById(R.id.Izq);
        der = findViewById(R.id.Der);
        abajo = findViewById(R.id.Abajo);
        girar = findViewById(R.id.Giro);
        puntos = findViewById(R.id.puntos);
    }

    public void onBackPressed(){
        if(tiempoesperavolver+1600>System.currentTimeMillis()){
            finish();
        }
        else{
            Toast.makeText(this, "Pulsa nuevamente para salir", Toast.LENGTH_SHORT).show();
        }
        tiempoesperavolver =System.currentTimeMillis();
    }

}
