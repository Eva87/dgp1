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
    private int puntos;
    private String nombre;
    int tp;
    int val[]= new int[10];
    String posnom[]= new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle datos = this.getIntent().getExtras();

        if(datos != null) {
            tp = datos.getInt("TipoPiezas");
            puntos=datos.getInt("Puntuacion");
            nombre=datos.getString("Nombrevuelta");
        }
        else{
            tp=1;
        }

        initViews();



        SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = preferences.edit();

        int posicion=0;
        for (int i=0;i<10;i++){
            val[i] = preferences.getInt("punt"+i, 0);
            if(puntos>val[i]){
                posicion=i;
            }
            posnom[i] = preferences.getString("nom"+i, "");
            if(posnom[i].isEmpty()){
                posnom[i]= "padawan";
            }
        }
        for (int i=9;i>posicion;i++){
            val[i] =val[i-1];
        }
        val[posicion]=puntos;
        for (int i=0;i<10;i++){
            myEditor.putInt("punt"+i, val[i]);
            myEditor.putString("nom"+i,  posnom[i]);
        }
        myEditor.commit();


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
        intent.putExtra("Nombre", String.valueOf(recogernombre.getText().toString().trim()));
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
