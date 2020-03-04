package com.dgp2.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ImageButton botonplay,botonranking, botonajustes, botonayuda;
    private Intent intent=null;
    private EditText recogernombre;
    private long tiempoesperavolver;
    private String username;
    int tp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = preferences.edit();
        Bundle datos = this.getIntent().getExtras();

        if(datos != null) {
            tp = datos.getInt("TipoPiezas");

        }
        else{
            tp=1;
        }

        initViews();


      /*  botonranking.setVisibility(View.INVISIBLE);
        botonajustes.setVisibility(View.INVISIBLE);*/



        botonranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                intent = new Intent(MainActivity.this, Ranking.class);
                startActivity(intent);
                finish();
            }
        });

        botonajustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(MainActivity.this, Configuracion.class);
                startActivity(intent);
                finish();

            }
        });

        botonayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(MainActivity.this, ayuda.class);
                startActivity(intent);
                finish();

            }
        });
    }

    public void iniciarJuego(View view){


       /* SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = preferences.edit();
        myEditor.putInt("TipoPieza", tp);
        myEditor.commit();*/


        intent = new Intent(MainActivity.this, PantallaJuego.class);
        intent.putExtra("TipoPieza", tp);
        intent.putExtra("Nombre", String.valueOf(recogernombre));
        startActivity(intent);
        finish();
    }

    private void initViews(){
        botonplay = findViewById(R.id.botonPlay);

        botonranking=findViewById(R.id.ranking);
        botonajustes=findViewById(R.id.configuracion);
        botonayuda=findViewById(R.id.ayuda);
        recogernombre=findViewById(R.id.nombrejugador);
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
