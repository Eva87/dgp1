package com.dgp2.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class Cambiocolorpiezas extends AppCompatActivity {
    Intent intent;
    private ImageButton boton1, boton2, boton3, boton4, boton5, boton6;
    long TiempoDeEspera;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiocolorpiezas);

        boton1 = findViewById(R.id.color1);
        boton2 = findViewById(R.id.color2);
        boton3 = findViewById(R.id.color3);
        boton4 = findViewById(R.id.color4);
        boton5 = findViewById(R.id.color5);
        boton6 = findViewById(R.id.color6);

        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
                SharedPreferences.Editor myEditor = preferences.edit();
                myEditor.putInt("TipoPiezas", 1);
                myEditor.commit();*/

                intent = new Intent(Cambiocolorpiezas.this, MainActivity.class);
                intent.putExtra("TipoPiezas", 1);
                startActivity(intent);
                finish();

            }
        });

        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
                SharedPreferences.Editor myEditor = preferences.edit();
                myEditor.putInt("TipoPiezas", 2);
                myEditor.commit();*/

                intent = new Intent(Cambiocolorpiezas.this, MainActivity.class);
                intent.putExtra("TipoPiezas", 2);
                startActivity(intent);
                finish();

            }
        });
        boton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
                SharedPreferences.Editor myEditor = preferences.edit();
                myEditor.putInt("TipoPiezas", 3);
                myEditor.commit();*/

                intent = new Intent(Cambiocolorpiezas.this, MainActivity.class);
                intent.putExtra("TipoPiezas", 3);
                startActivity(intent);
                finish();

            }
        });
        boton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
                SharedPreferences.Editor myEditor = preferences.edit();
                myEditor.putInt("TipoPiezas", 4);
                myEditor.commit();*/

                intent = new Intent(Cambiocolorpiezas.this, MainActivity.class);
                intent.putExtra("TipoPiezas", 4);
                startActivity(intent);
                finish();

            }
        });
        boton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
                SharedPreferences.Editor myEditor = preferences.edit();
                myEditor.putInt("TipoPiezas", 5);
                myEditor.commit();*/

                intent = new Intent(Cambiocolorpiezas.this, MainActivity.class);
                intent.putExtra("TipoPiezas", 5);
                startActivity(intent);
                finish();

            }
        });
        boton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
                SharedPreferences.Editor myEditor = preferences.edit();
                myEditor.putInt("TipoPiezas", 6);
                myEditor.commit();*/

                intent = new Intent(Cambiocolorpiezas.this, MainActivity.class);
                intent.putExtra("TipoPiezas", 6);
                startActivity(intent);
                finish();

            }
        });

    }

    public void volveratrascolorpiezas (View view){

        Intent intent = new Intent(Cambiocolorpiezas.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (TiempoDeEspera + 1500 > System.currentTimeMillis()) {
            Intent intent = new Intent(Cambiocolorpiezas.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Pulsa nuevamente para volver", Toast.LENGTH_SHORT).show();
        }

        TiempoDeEspera = System.currentTimeMillis();
    }
}
