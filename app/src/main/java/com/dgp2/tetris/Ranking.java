package com.dgp2.tetris;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

    private ImageView verfoto1;
    private ImageView verfoto2;
    private ImageView verfoto3;
    private ImageView verfoto4;
    private ImageView verfoto5;
    private ImageView verfoto6;
    private ImageView verfoto7;
    private ImageView verfoto8;
    private ImageView verfoto9;
    private ImageView verfoto10;
    private ImageView avatar1,avatar2,avatar3,avatar4,avatar5,avatar6,avatar7,avatar8,avatar9,avatar10;

    int valr[]= new int[10];
    int avatars[]= new int[10];
    String posnomr[]= new String[10];
    MediaPlayer mp;
    int posmu, musicon;
    private String username;
    Intent intent;
    SharedPreferences preferences;
    Bitmap bitmap;
    SharedPreferences.Editor myEditor;
    String u;
    Context context;
    boolean vuelta=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mp=MediaPlayer.create(this, R.raw.musfondo);
        Bundle datos = this.getIntent().getExtras();
        SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = preferences.edit();

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

        verfoto1=(ImageView)findViewById(R.id.imagenranking1);
        verfoto2=(ImageView)findViewById(R.id.imagenranking2);
        verfoto3=(ImageView)findViewById(R.id.imagenranking3);
        verfoto4=(ImageView)findViewById(R.id.imagenranking4);
        verfoto5=(ImageView)findViewById(R.id.imagenranking5);
        verfoto6=(ImageView)findViewById(R.id.imagenranking6);
        verfoto7=(ImageView)findViewById(R.id.imagenranking7);
        verfoto8=(ImageView)findViewById(R.id.imagenranking8);
        verfoto9=(ImageView)findViewById(R.id.imagenranking9);
        verfoto10=(ImageView)findViewById(R.id.imagenranking10);


        avatar1=(ImageView)findViewById(R.id.avat1);
        avatar2=(ImageView)findViewById(R.id.avat2);
        avatar3=(ImageView)findViewById(R.id.avat3);
        avatar4=(ImageView)findViewById(R.id.avat4);
        avatar5=(ImageView)findViewById(R.id.avat5);
        avatar6=(ImageView)findViewById(R.id.avat6);
        avatar7=(ImageView)findViewById(R.id.avat7);
        avatar8=(ImageView)findViewById(R.id.avat8);
        avatar9=(ImageView)findViewById(R.id.avat9);
        avatar10=(ImageView)findViewById(R.id.avat10);

        musicon = preferences.getInt("Music", 0);
        if(datos != null) {
            vuelta=true;
            posmu = datos.getInt("posicionmusica");


            valr[0] = preferences.getInt("punt0", 0);
            posnomr[0]=preferences.getString("nom0","padawan");
            mostrapuntosranking1.setText(String.valueOf(valr[0]));
            mostranombreranking1.setText( String.valueOf(posnomr[0]));

            avatars[0] = preferences.getInt("avatar0", 0);


            switch (avatars[0]){
                case 0:
                    avatar1.setImageDrawable(getDrawable(R.drawable.imafo1));
                    break;
                case 1:
                    avatar1.setImageDrawable(getDrawable(R.drawable.imafo1));
                    break;
                case 2:
                    avatar1.setImageDrawable(getDrawable(R.drawable.imafo2));
                    break;
                case 3:
                    avatar1.setImageDrawable(getDrawable(R.drawable.imafo3));
                    break;
                case 4:
                    avatar1.setImageDrawable(getDrawable(R.drawable.imafo4));
                    break;
                case 5:
                    avatar1.setImageDrawable(getDrawable(R.drawable.imafo5));
                    break;
                case 6:
                    avatar1.setImageDrawable(getDrawable(R.drawable.imafo6));
                    break;
                case 7:
                    avatar1.setImageDrawable(getDrawable(R.drawable.imafo7));
                    break;
                case 8:
                    avatar1.setImageDrawable(getDrawable(R.drawable.imafo8));
                    break;
                case 9:
                    avatar1.setImageDrawable(getDrawable(R.drawable.imafo9));
                    break;
                case 10:
                    avatar1.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                case 11:
                    avatar1.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                case 12:
                    avatar1.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                default:
                    avatar1.setImageDrawable(getDrawable(R.drawable.imafo1));
                    break;
            }

            preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
            //Write image preferences

            u=preferences.getString("Foto0", "");
            if(u==""){
                verfoto1.setImageDrawable(getDrawable( R.drawable.nofoto));
            }
            else {
                bitmap=decodeBase64(u);
                verfoto1.setImageBitmap(bitmap);
            }

            valr[1] = preferences.getInt("punt"+1, 0);
            posnomr[1]=preferences.getString("nom"+1,"padawan");
            mostrapuntosranking2.setText(String.valueOf(valr[1]));
            mostranombreranking2.setText( String.valueOf(posnomr[1]));

            avatars[1] = preferences.getInt("avatar1", 0);
            switch (avatars[1]){
                case 0:
                    avatar2.setImageDrawable(getDrawable(R.drawable.imafo2));
                    break;
                case 1:
                    avatar2.setImageDrawable(getDrawable(R.drawable.imafo1));
                    break;
                case 2:
                    avatar2.setImageDrawable(getDrawable(R.drawable.imafo2));
                    break;
                case 3:
                    avatar2.setImageDrawable(getDrawable(R.drawable.imafo3));
                    break;
                case 4:
                    avatar2.setImageDrawable(getDrawable(R.drawable.imafo4));
                    break;
                case 5:
                    avatar2.setImageDrawable(getDrawable(R.drawable.imafo5));
                    break;
                case 6:
                    avatar2.setImageDrawable(getDrawable(R.drawable.imafo6));
                    break;
                case 7:
                    avatar2.setImageDrawable(getDrawable(R.drawable.imafo7));
                    break;
                case 8:
                    avatar2.setImageDrawable(getDrawable(R.drawable.imafo8));
                    break;
                case 9:
                    avatar2.setImageDrawable(getDrawable(R.drawable.imafo9));
                    break;
                case 10:
                    avatar2.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                case 11:
                    avatar2.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                case 12:
                    avatar2.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                default:
                    avatar2.setImageDrawable(getDrawable(R.drawable.imafo2));
                    break;
            }
            u=preferences.getString("Foto1", "");
            if(u=="")
                verfoto2.setImageDrawable(getDrawable( R.drawable.nofoto));
            else {
                bitmap=decodeBase64(u);
                verfoto2.setImageBitmap(bitmap);
            }

            valr[2] = preferences.getInt("punt"+2, 0);
            posnomr[2]=preferences.getString("nom"+2,"padawan");
            mostrapuntosranking3.setText(String.valueOf(valr[2]));
            mostranombreranking3.setText( String.valueOf(posnomr[2]));

            avatars[2] = preferences.getInt("avatar2", 0);
            switch (avatars[2]){
                case 0:
                    avatar3.setImageDrawable(getDrawable(R.drawable.imafo3));
                    break;
                case 1:
                    avatar3.setImageDrawable(getDrawable(R.drawable.imafo1));
                    break;
                case 2:
                    avatar3.setImageDrawable(getDrawable(R.drawable.imafo2));
                    break;
                case 3:
                    avatar3.setImageDrawable(getDrawable(R.drawable.imafo3));
                    break;
                case 4:
                    avatar3.setImageDrawable(getDrawable(R.drawable.imafo4));
                    break;
                case 5:
                    avatar3.setImageDrawable(getDrawable(R.drawable.imafo5));
                    break;
                case 6:
                    avatar3.setImageDrawable(getDrawable(R.drawable.imafo6));
                    break;
                case 7:
                    avatar3.setImageDrawable(getDrawable(R.drawable.imafo7));
                    break;
                case 8:
                    avatar3.setImageDrawable(getDrawable(R.drawable.imafo8));
                    break;
                case 9:
                    avatar3.setImageDrawable(getDrawable(R.drawable.imafo9));
                    break;
                case 10:
                    avatar3.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                case 11:
                    avatar3.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                case 12:
                    avatar3.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                default:
                    avatar3.setImageDrawable(getDrawable(R.drawable.imafo3));
                    break;
            }
            u=preferences.getString("Foto2", "");
            if(u=="")
                verfoto3.setImageDrawable(getDrawable( R.drawable.nofoto));
            else {
                bitmap=decodeBase64(u);
                verfoto3.setImageBitmap(bitmap);
            }

            valr[3] = preferences.getInt("punt"+3, 0);
            posnomr[3]=preferences.getString("nom"+3,"padawan");
            mostrapuntosranking4.setText(String.valueOf(valr[3]));
            mostranombreranking4.setText( String.valueOf(posnomr[3]));

            avatars[3] = preferences.getInt("avatar3", 0);
            switch (avatars[3]){
                case 0:
                    avatar4.setImageDrawable(getDrawable(R.drawable.imafo4));
                    break;
                case 1:
                    avatar4.setImageDrawable(getDrawable(R.drawable.imafo1));
                    break;
                case 2:
                    avatar4.setImageDrawable(getDrawable(R.drawable.imafo2));
                    break;
                case 3:
                    avatar4.setImageDrawable(getDrawable(R.drawable.imafo3));
                    break;
                case 4:
                    avatar4.setImageDrawable(getDrawable(R.drawable.imafo4));
                    break;
                case 5:
                    avatar4.setImageDrawable(getDrawable(R.drawable.imafo5));
                    break;
                case 6:
                    avatar4.setImageDrawable(getDrawable(R.drawable.imafo6));
                    break;
                case 7:
                    avatar4.setImageDrawable(getDrawable(R.drawable.imafo7));
                    break;
                case 8:
                    avatar4.setImageDrawable(getDrawable(R.drawable.imafo8));
                    break;
                case 9:
                    avatar4.setImageDrawable(getDrawable(R.drawable.imafo9));
                    break;
                case 10:
                    avatar4.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                case 11:
                    avatar4.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                case 12:
                    avatar4.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                default:
                    avatar4.setImageDrawable(getDrawable(R.drawable.imafo4));
                    break;
            }
            u=preferences.getString("Foto3", "");
            if(u=="")
                verfoto4.setImageDrawable(getDrawable( R.drawable.nofoto));
            else {
                bitmap=decodeBase64(u);
                verfoto4.setImageBitmap(bitmap);
            }

            valr[4] = preferences.getInt("punt"+4, 0);
            posnomr[4]=preferences.getString("nom"+4,"padawan");
            mostrapuntosranking5.setText(String.valueOf(valr[4]));
            mostranombreranking5.setText( String.valueOf(posnomr[4]));

            avatars[4] = preferences.getInt("avatar4", 0);
            switch (avatars[4]){
                case 0:
                    avatar5.setImageDrawable(getDrawable(R.drawable.imafo5));
                    break;
                case 1:
                    avatar5.setImageDrawable(getDrawable(R.drawable.imafo1));
                    break;
                case 2:
                    avatar5.setImageDrawable(getDrawable(R.drawable.imafo2));
                    break;
                case 3:
                    avatar5.setImageDrawable(getDrawable(R.drawable.imafo3));
                    break;
                case 4:
                    avatar5.setImageDrawable(getDrawable(R.drawable.imafo4));
                    break;
                case 5:
                    avatar5.setImageDrawable(getDrawable(R.drawable.imafo5));
                    break;
                case 6:
                    avatar5.setImageDrawable(getDrawable(R.drawable.imafo6));
                    break;
                case 7:
                    avatar5.setImageDrawable(getDrawable(R.drawable.imafo7));
                    break;
                case 8:
                    avatar5.setImageDrawable(getDrawable(R.drawable.imafo8));
                    break;
                case 9:
                    avatar5.setImageDrawable(getDrawable(R.drawable.imafo9));
                    break;
                case 10:
                    avatar5.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                case 11:
                    avatar5.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                case 12:
                    avatar5.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                default:
                    avatar5.setImageDrawable(getDrawable(R.drawable.imafo5));
                    break;
            }
            u=preferences.getString("Foto4", "");
            if(u=="")
                verfoto5.setImageDrawable(getDrawable( R.drawable.nofoto));
            else {
                bitmap=decodeBase64(u);
                verfoto5.setImageBitmap(bitmap);
            }

            valr[5] = preferences.getInt("punt"+5, 0);
            posnomr[5]=preferences.getString("nom"+5,"padawan");
            mostrapuntosranking6.setText(String.valueOf(valr[5]));
            mostranombreranking6.setText( String.valueOf(posnomr[5]));

            avatars[5] = preferences.getInt("avatar5", 0);
            switch (avatars[5]){
                case 0:
                    avatar6.setImageDrawable(getDrawable(R.drawable.imafo6));
                    break;
                case 1:
                    avatar6.setImageDrawable(getDrawable(R.drawable.imafo1));
                    break;
                case 2:
                    avatar6.setImageDrawable(getDrawable(R.drawable.imafo2));
                    break;
                case 3:
                    avatar6.setImageDrawable(getDrawable(R.drawable.imafo3));
                    break;
                case 4:
                    avatar6.setImageDrawable(getDrawable(R.drawable.imafo4));
                    break;
                case 5:
                    avatar6.setImageDrawable(getDrawable(R.drawable.imafo5));
                    break;
                case 6:
                    avatar6.setImageDrawable(getDrawable(R.drawable.imafo6));
                    break;
                case 7:
                    avatar6.setImageDrawable(getDrawable(R.drawable.imafo7));
                    break;
                case 8:
                    avatar6.setImageDrawable(getDrawable(R.drawable.imafo8));
                    break;
                case 9:
                    avatar6.setImageDrawable(getDrawable(R.drawable.imafo9));
                    break;
                case 10:
                    avatar6.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                case 11:
                    avatar6.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                case 12:
                    avatar6.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                default:
                    avatar6.setImageDrawable(getDrawable(R.drawable.imafo6));
                    break;
            }
            u=preferences.getString("Foto5", "");
            if(u=="")
                verfoto6.setImageDrawable(getDrawable( R.drawable.nofoto));
            else {
                bitmap=decodeBase64(u);
                verfoto6.setImageBitmap(bitmap);
            }

            valr[6] = preferences.getInt("punt"+6, 0);
            posnomr[6]=preferences.getString("nom"+6,"padawan");
            mostrapuntosranking7.setText(String.valueOf(valr[6]));
            mostranombreranking7.setText( String.valueOf(posnomr[6]));

            avatars[6] = preferences.getInt("avatar6", 0);
            switch (avatars[6]){
                case 0:
                    avatar7.setImageDrawable(getDrawable(R.drawable.imafo7));
                    break;
                case 1:
                    avatar7.setImageDrawable(getDrawable(R.drawable.imafo1));
                    break;
                case 2:
                    avatar7.setImageDrawable(getDrawable(R.drawable.imafo2));
                    break;
                case 3:
                    avatar7.setImageDrawable(getDrawable(R.drawable.imafo3));
                    break;
                case 4:
                    avatar7.setImageDrawable(getDrawable(R.drawable.imafo4));
                    break;
                case 5:
                    avatar7.setImageDrawable(getDrawable(R.drawable.imafo5));
                    break;
                case 6:
                    avatar7.setImageDrawable(getDrawable(R.drawable.imafo6));
                    break;
                case 7:
                    avatar7.setImageDrawable(getDrawable(R.drawable.imafo7));
                    break;
                case 8:
                    avatar7.setImageDrawable(getDrawable(R.drawable.imafo8));
                    break;
                case 9:
                    avatar7.setImageDrawable(getDrawable(R.drawable.imafo9));
                    break;
                case 10:
                    avatar7.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                case 11:
                    avatar7.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                case 12:
                    avatar7.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                default:
                    avatar7.setImageDrawable(getDrawable(R.drawable.imafo7));
                    break;
            }
            u=preferences.getString("Foto6", "");
            if(u=="")
                verfoto7.setImageDrawable(getDrawable( R.drawable.nofoto));
            else {
                bitmap=decodeBase64(u);
                verfoto7.setImageBitmap(bitmap);
            }

            valr[7] = preferences.getInt("punt"+7, 0);
            posnomr[7]=preferences.getString("nom"+7,"padawan");
            mostrapuntosranking8.setText(String.valueOf(valr[7]));
            mostranombreranking8.setText( String.valueOf(posnomr[7]));

            avatars[7] = preferences.getInt("avatar7", 0);
            switch (avatars[7]){
                case 0:
                    avatar8.setImageDrawable(getDrawable(R.drawable.imafo8));
                    break;
                case 1:
                    avatar8.setImageDrawable(getDrawable(R.drawable.imafo1));
                    break;
                case 2:
                    avatar8.setImageDrawable(getDrawable(R.drawable.imafo2));
                    break;
                case 3:
                    avatar8.setImageDrawable(getDrawable(R.drawable.imafo3));
                    break;
                case 4:
                    avatar8.setImageDrawable(getDrawable(R.drawable.imafo4));
                    break;
                case 5:
                    avatar8.setImageDrawable(getDrawable(R.drawable.imafo5));
                    break;
                case 6:
                    avatar8.setImageDrawable(getDrawable(R.drawable.imafo6));
                    break;
                case 7:
                    avatar8.setImageDrawable(getDrawable(R.drawable.imafo7));
                    break;
                case 8:
                    avatar8.setImageDrawable(getDrawable(R.drawable.imafo8));
                    break;
                case 9:
                    avatar8.setImageDrawable(getDrawable(R.drawable.imafo9));
                    break;
                case 10:
                    avatar8.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                case 11:
                    avatar8.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                case 12:
                    avatar8.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                default:
                    avatar8.setImageDrawable(getDrawable(R.drawable.imafo8));
                    break;
            }
            u=preferences.getString("Foto7", "");
            if(u=="")
                verfoto8.setImageDrawable(getDrawable( R.drawable.nofoto));
            else {
                bitmap=decodeBase64(u);
                verfoto8.setImageBitmap(bitmap);
            }

            valr[8] = preferences.getInt("punt"+8, 0);
            posnomr[8]=preferences.getString("nom"+8,"padawan");
            mostrapuntosranking9.setText(String.valueOf(valr[8]));
            mostranombreranking9.setText( String.valueOf(posnomr[8]));

            avatars[8] = preferences.getInt("avatar8", 0);
            switch (avatars[8]){
                case 0:
                    avatar9.setImageDrawable(getDrawable(R.drawable.imafo9));
                    break;
                case 1:
                    avatar9.setImageDrawable(getDrawable(R.drawable.imafo1));
                    break;
                case 2:
                    avatar9.setImageDrawable(getDrawable(R.drawable.imafo2));
                    break;
                case 3:
                    avatar9.setImageDrawable(getDrawable(R.drawable.imafo3));
                    break;
                case 4:
                    avatar9.setImageDrawable(getDrawable(R.drawable.imafo4));
                    break;
                case 5:
                    avatar9.setImageDrawable(getDrawable(R.drawable.imafo5));
                    break;
                case 6:
                    avatar9.setImageDrawable(getDrawable(R.drawable.imafo6));
                    break;
                case 7:
                    avatar9.setImageDrawable(getDrawable(R.drawable.imafo7));
                    break;
                case 8:
                    avatar9.setImageDrawable(getDrawable(R.drawable.imafo8));
                    break;
                case 9:
                    avatar9.setImageDrawable(getDrawable(R.drawable.imafo9));
                    break;
                case 10:
                    avatar9.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                case 11:
                    avatar9.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                case 12:
                    avatar9.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                default:
                    avatar9.setImageDrawable(getDrawable(R.drawable.imafo9));
                    break;
            }
            u=preferences.getString("Foto8", "");
            if(u=="")
                verfoto9.setImageDrawable(getDrawable( R.drawable.nofoto));
            else {
                bitmap=decodeBase64(u);
                verfoto9.setImageBitmap(bitmap);
            }

            valr[9] = preferences.getInt("punt"+9, 0);
            posnomr[9]=preferences.getString("nom"+9,"padawan");
            mostrapuntosranking10.setText(String.valueOf(valr[9]));
            mostranombreranking10.setText( String.valueOf(posnomr[9]));

            avatars[9] = preferences.getInt("avatar9", 0);
            switch (avatars[9]){
                case 0:
                    avatar10.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                case 1:
                    avatar10.setImageDrawable(getDrawable(R.drawable.imafo1));
                    break;
                case 2:
                    avatar10.setImageDrawable(getDrawable(R.drawable.imafo2));
                    break;
                case 3:
                    avatar10.setImageDrawable(getDrawable(R.drawable.imafo3));
                    break;
                case 4:
                    avatar10.setImageDrawable(getDrawable(R.drawable.imafo4));
                    break;
                case 5:
                    avatar10.setImageDrawable(getDrawable(R.drawable.imafo5));
                    break;
                case 6:
                    avatar9.setImageDrawable(getDrawable(R.drawable.imafo6));
                    break;
                case 7:
                    avatar10.setImageDrawable(getDrawable(R.drawable.imafo7));
                    break;
                case 8:
                    avatar10.setImageDrawable(getDrawable(R.drawable.imafo8));
                    break;
                case 9:
                    avatar10.setImageDrawable(getDrawable(R.drawable.imafo9));
                    break;
                case 10:
                    avatar10.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                case 11:
                    avatar10.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                case 12:
                    avatar10.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
                default:
                    avatar10.setImageDrawable(getDrawable(R.drawable.imafo10));
                    break;
            }
            u=preferences.getString("Foto9", "");
            if(u=="")
                verfoto10.setImageDrawable(getDrawable( R.drawable.nofoto));
            else {
                bitmap=decodeBase64(u);
                verfoto10.setImageBitmap(bitmap);
            }

            if (musicon == 1) {
                if(posmu>0){
                    mp.seekTo(posmu);
                }
                mp.start();
            }
        }
    }

    public void volveratrasranking (View view){
        Intent intent = new Intent(Ranking.this, MainActivity.class);
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
            Intent intent = new Intent(Ranking.this, MainActivity.class);
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
