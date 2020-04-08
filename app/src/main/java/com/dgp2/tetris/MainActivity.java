package com.dgp2.tetris;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ImageButton botonplay,botonranking, botonajustes, botonayuda, botoncompartir;
    private Intent intent=null;
    private EditText recogernombre;
    private long tiempoesperavolver;
    private String username;
    private int puntos;
    private String nombre="padawan";
    int tp;
    int val[]= new int[10];
    String posnom[]= new String[10];
    String posfoto[]= new String[10];
    MediaPlayer mp;
    int posmu=0, musicon;
    Bitmap bitmap;
    String u="";
    boolean variable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        225);
            }
        }if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        226);
            }
        }

        initViews();
        variable=false;
        Bundle datos = this.getIntent().getExtras();
        SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = preferences.edit();

        musicon = preferences.getInt("Music", 0);
        if(datos != null) {
            tp = datos.getInt("TipoPiezas");
            puntos=datos.getInt("Puntuacion");
            nombre=datos.getString("Nombrevuelta");
            posmu = datos.getInt("posicionmusica");
        }
        else{
            tp=1;
        }

        if (musicon == 1) {
            if(posmu>0){
                mp.seekTo(posmu);
            }
            mp.start();
        }

        if(puntos>=30){

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, 1);
            }


        }


        if(variable){
            int posicion=0;
            boolean si=false;
            for (int i=0;i<10;i++){
                val[i] = preferences.getInt("punt"+i, 0);
                if(!si && puntos>val[i]){
                    posicion=i;
                    si=true;
                }
                posnom[i] = preferences.getString("nom"+i, "padawan");
                posfoto[i] = preferences.getString("Foto"+i, "");
            }
            if(puntos!=0){
                for (int i=9;i>posicion;i--){
                    val[i] = val[i-1];
                    posnom[i]=posnom[i-1];
                    posfoto[i]=posfoto[i-1];
                }

                val[posicion]=puntos;
                posnom[posicion]=nombre;
                posfoto[posicion]=" ";
            }

            for (int i=0;i<10;i++){
                myEditor.putInt("punt"+i, val[i]);
                myEditor.putString("nom"+i,  posnom[i]);
                myEditor.putString("Foto"+i, posfoto[i]);

            }
            myEditor.commit();

        }
      /*  botonranking.setVisibility(View.INVISIBLE);
          botonajustes.setVisibility(View.INVISIBLE);*/



        botonranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                intent = new Intent(MainActivity.this, Ranking.class);

                intent.putExtra("posicionmusica", mp.getCurrentPosition());

                mp.stop();
                try {
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.seekTo(0);


                for (int i=0;i<10;i++){
                    intent.putExtra("punt"+i, val[i]);
                    intent.putExtra("nom"+i, posnom[i]);
                    intent.putExtra("Foto"+i, posfoto[i]);
                }
                startActivity(intent);
                finish();
            }
        });

        botonajustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(MainActivity.this, Configuracion.class);


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
        });

        botonayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(MainActivity.this, ayuda.class);



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
        });

        botoncompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Tetris Wars el mejor juego del todas las galaxias, "+ " mi nombre es: " + nombre + " y he conseguido: " + " "+ puntos+ " puntos ");
                startActivity(Intent.createChooser(intent, "Compartir con"));

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
        intent.putExtra("Music", 0);
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

    private void initViews(){
        botonplay = findViewById(R.id.botonPlay);

        botonranking=findViewById(R.id.ranking);
        botonajustes=findViewById(R.id.configuracion);
        botonayuda=findViewById(R.id.ayuda);
        recogernombre=findViewById(R.id.nombrejugador);
        botoncompartir=findViewById(R.id.botoncompartirredes);

        mp=MediaPlayer.create(this, R.raw.musfondo);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            variable=true;
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            u=codetobase64(imageBitmap);

            SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
            SharedPreferences.Editor myEditor = preferences.edit();
            int posicion=0;
            boolean si=false;
            for (int i=0;i<10;i++){
                val[i] = preferences.getInt("punt"+i, 0);
                if(!si && puntos>val[i]){
                    posicion=i;
                    si=true;
                }
                posnom[i] = preferences.getString("nom"+i, "padawan");
                posfoto[i] = preferences.getString("Foto"+i, "");
            }
            if(puntos!=0){
                for (int i=9;i>posicion;i--){
                    val[i] = val[i-1];
                    posnom[i]=posnom[i-1];
                    posfoto[i]=posfoto[i-1];
                }

                val[posicion]=puntos;
                posnom[posicion]=nombre;
                posfoto[posicion]=u;
            }

            for (int i=0;i<10;i++){
                myEditor.putInt("punt"+i, val[i]);
                myEditor.putString("nom"+i,  posnom[i]);
                myEditor.putString("Foto"+i, posfoto[i]);

            }
            myEditor.commit();
        }else{

            variable=true;

            SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
            SharedPreferences.Editor myEditor = preferences.edit();
            int posicion=0;
            boolean si=false;
            for (int i=0;i<10;i++){
                val[i] = preferences.getInt("punt"+i, 0);
                if(!si && puntos>val[i]){
                    posicion=i;
                    si=true;
                }
                posnom[i] = preferences.getString("nom"+i, "padawan");
                posfoto[i] = preferences.getString("Foto"+i, "");
            }
            if(puntos!=0){
                for (int i=9;i>posicion;i--){
                    val[i] = val[i-1];
                    posnom[i]=posnom[i-1];
                    posfoto[i]=posfoto[i-1];
                }

                val[posicion]=puntos;
                posnom[posicion]=nombre;
                posfoto[posicion]="";
            }

            for (int i=0;i<10;i++){
                myEditor.putInt("punt"+i, val[i]);
                myEditor.putString("nom"+i,  posnom[i]);
                myEditor.putString("Foto"+i, posfoto[i]);

            }
            myEditor.commit();
        }
    }


    public void onBackPressed(){
        if(tiempoesperavolver+1600>System.currentTimeMillis()){
            finish();

            mp.stop();
            try {
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.seekTo(0);
        }
        else{
            Toast.makeText(this, "Pulsa nuevamente para salir", Toast.LENGTH_SHORT).show();
        }
        tiempoesperavolver =System.currentTimeMillis();
    }
    //to pass the bitmap to png and return it in a String
    public static String codetobase64( Bitmap image){
        Bitmap imag=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imag.compress((Bitmap.CompressFormat.PNG),100,baos);
        byte[] b=baos.toByteArray();
        String imageencoded= Base64.encodeToString(b,Base64.DEFAULT);
        Log.d("Image Log:",imageencoded);
        return imageencoded;
    }

    //to pass the String to Bitmap and return it
    public static Bitmap decodeBase64(String input){
        byte[] decodedByte= Base64.decode(input,0);
        return BitmapFactory.decodeByteArray(decodedByte,0,decodedByte.length);
    }

}
