package com.dgp2.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Ranking extends AppCompatActivity {

    long TiempoDeEspera;
    private TextView mostranombreranking1;
    private TextView mostrapuntosranking1;
    private TextView mostranombreranking2;
    private TextView mostrapuntosranking2;
    private TextView mostranombreranking3;
    private TextView mostrapuntosranking3;
    private TextView mostranombreranking4;
    private TextView mostrapuntosranking4;
    private TextView mostranombreranking5;
    private TextView mostrapuntosranking5;
    private TextView mostranombreranking6;
    private TextView mostrapuntosranking6;
    private TextView mostranombreranking7;
    private TextView mostrapuntosranking7;
    private TextView mostranombreranking8;
    private TextView mostrapuntosranking8;
    private TextView mostranombreranking9;
    private TextView mostrapuntosranking9;
    private TextView mostranombreranking10;
    private TextView mostrapuntosranking10;
    int valr[]= new int[10];
    String posnomr[]= new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        mostranombreranking1 = (TextView) findViewById(R.id.nombre1);
        mostrapuntosranking1 = (TextView) findViewById(R.id.puntos1);
        mostranombreranking2 = (TextView) findViewById(R.id.nombre2);
        mostrapuntosranking2 = (TextView) findViewById(R.id.puntos2);
        mostranombreranking3 = (TextView) findViewById(R.id.nombre3);
        mostrapuntosranking3 = (TextView) findViewById(R.id.puntos3);
        mostranombreranking4 = (TextView) findViewById(R.id.nombre4);
        mostrapuntosranking4 = (TextView) findViewById(R.id.puntos4);
        mostranombreranking5 = (TextView) findViewById(R.id.nombre5);
        mostrapuntosranking5 = (TextView) findViewById(R.id.puntos5);
        mostranombreranking6 = (TextView) findViewById(R.id.nombre6);
        mostrapuntosranking6 = (TextView) findViewById(R.id.puntos6);
        mostranombreranking7 = (TextView) findViewById(R.id.nombre7);
        mostrapuntosranking7 = (TextView) findViewById(R.id.puntos7);
        mostranombreranking8 = (TextView) findViewById(R.id.nombre8);
        mostrapuntosranking8 = (TextView) findViewById(R.id.puntos8);
        mostranombreranking9 = (TextView) findViewById(R.id.nombre9);
        mostrapuntosranking9 = (TextView) findViewById(R.id.puntos9);
        mostranombreranking10 = (TextView) findViewById(R.id.nombre10);
        mostrapuntosranking10 = (TextView) findViewById(R.id.puntos10);

        Bundle datos = this.getIntent().getExtras();
        if(datos != null) {

            valr[0] = datos.getInt("punt"+0);
            posnomr[0]=datos.getString("nom"+0);
            mostrapuntosranking1.setText(String.valueOf(valr[0]));
            mostranombreranking1.setText( String.valueOf(posnomr[0]));

            valr[1] = datos.getInt("punt"+1);
            posnomr[1]=datos.getString("nom"+1);
            mostrapuntosranking2.setText(String.valueOf(valr[1]));
            mostranombreranking2.setText( String.valueOf(posnomr[1]));

            valr[2] = datos.getInt("punt"+2);
            posnomr[2]=datos.getString("nom"+2);
            mostrapuntosranking3.setText(String.valueOf(valr[2]));
            mostranombreranking3.setText( String.valueOf(posnomr[2]));

            valr[3] = datos.getInt("punt"+3);
            posnomr[3]=datos.getString("nom"+3);
            mostrapuntosranking4.setText(String.valueOf(valr[3]));
            mostranombreranking4.setText( String.valueOf(posnomr[3]));

            valr[4] = datos.getInt("punt"+4);
            posnomr[4]=datos.getString("nom"+4);
            mostrapuntosranking5.setText(String.valueOf(valr[4]));
            mostranombreranking5.setText( String.valueOf(posnomr[4]));

            valr[5] = datos.getInt("punt"+5);
            posnomr[5]=datos.getString("nom"+5);
            mostrapuntosranking6.setText(String.valueOf(valr[5]));
            mostranombreranking6.setText( String.valueOf(posnomr[5]));

            valr[6] = datos.getInt("punt"+6);
            posnomr[6]=datos.getString("nom"+6);
            mostrapuntosranking7.setText(String.valueOf(valr[6]));
            mostranombreranking7.setText( String.valueOf(posnomr[6]));

            valr[7] = datos.getInt("punt"+7);
            posnomr[7]=datos.getString("nom"+7);
            mostrapuntosranking8.setText(String.valueOf(valr[7]));
            mostranombreranking8.setText( String.valueOf(posnomr[7]));

            valr[8] = datos.getInt("punt"+8);
            posnomr[8]=datos.getString("nom"+8);
            mostrapuntosranking9.setText(String.valueOf(valr[8]));
            mostranombreranking9.setText( String.valueOf(posnomr[8]));

            valr[9] = datos.getInt("punt"+9);
            posnomr[9]=datos.getString("nom"+9);
            mostrapuntosranking10.setText(String.valueOf(valr[9]));
            mostranombreranking10.setText( String.valueOf(posnomr[9]));

        }
    }


    public void volveratrasranking (View view){

        Intent intent = new Intent(Ranking.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (TiempoDeEspera + 1500 > System.currentTimeMillis()) {
            Intent intent = new Intent(Ranking.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Pulsa nuevamente para volver", Toast.LENGTH_SHORT).show();
        }

        TiempoDeEspera = System.currentTimeMillis();
    }
}
