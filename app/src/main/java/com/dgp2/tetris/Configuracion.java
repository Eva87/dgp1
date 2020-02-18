package com.dgp2.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Configuracion extends AppCompatActivity {

    long TiempoDeEspera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
    }




    public void volveratrasconfiguracion (View view){

        Intent intent = new Intent(Configuracion.this, MainActivity.class);
        startActivity(intent);
        finish();
    }



    @Override
    public void onBackPressed() {
        if (TiempoDeEspera + 1500 > System.currentTimeMillis()) {
            Intent intent = new Intent(Configuracion.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Pulsa nuevamente para volver", Toast.LENGTH_SHORT).show();
        }

        TiempoDeEspera = System.currentTimeMillis();
    }

}
