package com.dgp2.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ImageButton botonplay,botonranking, botonajustes, botonayuda;
    private Intent intent=null;
    private long tiempoesperavolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initViews();

        botonranking.setVisibility(View.INVISIBLE);
        botonajustes.setVisibility(View.INVISIBLE);



        botonranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        botonajustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
        intent = new Intent(MainActivity.this, PantallaJuego.class);
        startActivity(intent);
        finish();
    }

    private void initViews(){
        botonplay = findViewById(R.id.botonPlay);

        botonranking=findViewById(R.id.ranking);
        botonajustes=findViewById(R.id.configuracion);
        botonayuda=findViewById(R.id.ayuda);
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
