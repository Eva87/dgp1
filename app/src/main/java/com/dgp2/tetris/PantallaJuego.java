package com.dgp2.tetris;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GestureDetectorCompat;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class PantallaJuego extends Activity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private GestureDetectorCompat detectorDeGestos;
    int AlturaPantalla = 800;
    int AnchuraPantalla = 400;
    int numeroFilas ,numeroColumnas ;
    int numfilasmodifpreferences=20; /*si 40 tarda 28 segundos*/
    int numcolumnasasmodifpreferences=10; /*40*20*/
    int varcortar=-2,variablepuntosvuelta=0;
    /*Para la siguiente version que vengan desde las opciones de configuracion
    * el numero de las filas y de las columnas*/
    final Handler operaciones = new Handler();
    final Forma[] formas = new Forma[7];
    final int IR_DERECHA = 1;
    final int IR_ABAJO = 2;
    final int IR_IZQUIERDA = 3;
    public static final int CONTADORDEMOVIMIENTOS=0;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    private float timestamp;
    PizarradeCeldas[][] matrizDeJuego;
    boolean estadoVelocidadRapidez;
    int piezaextra=0;
    long tiempo50,tiempo0;
    /*se inicializan los colores a jugar*/
    int colorJ,color2,color5,colorl,colori,colort, coloro;
    int incremento=5;
    long milisfutures=999999999;
    int tiempos10=10000, tiempos20=20000,tiempos30=30000, tiempos50=50000;
    int puntossumar=30;
    int puntosrestar=0;

    int contadoraleatorias=0;

    boolean boleanolinea=false;

    int RAPIDEZNORMAL = 500;
    int RAPIDEZDEPRISA = 50;
    boolean inicio;
    String rapidez;
    int puntuacion=0;
    boolean juegoEnMarcha, juegoEnPausa, estadoActual,p;
    private long TiempoDeEspera;
    ImageButton botonizquierda, botonderecha,botonderecha2,botonpausa, botonabajo, botonrapido, reiniciarjuego, volverjugar250ptos, giro, finjuego;

    /*el que cada 30 segundos salga una ficha nueva solo seria posible si el numero de filas fuera mas grande
    * ya que tarda 8 segundos en llegar abajo*/

    final int dx[] = {-1, 0, 1, 0};
    final int dy[] = {0, 1, 0, -1};

    private String username;
    MediaPlayer mp1,mp2,mp3,mp4,mp5,mp6,mp7, mp;
    int posmu=0, musicon=1,avatar, finrapidez=100;

    boolean boleanouno=true,boleanodos=true,boleanotres=true;
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorproximidad;

    ImageView vistaPiezaProxima, vistaavatar;

    int tpj, niveljuego=0;
    String nombrejugador;

    Random random = new Random();

    Vibrator vibrador;

    Bitmap bitmap;
    Canvas canvas;

    Bitmap bitmapnuevaforma;
    Canvas canvasnuevaforma;

    Paint paint,paintnueva;
    LinearLayout linearLayout;

    Forma formaActual, proximaForma;
    int variablemusica=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_juego);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        iniciarvistas();

        numeroFilas=numfilasmodifpreferences+6;
        numeroColumnas=numcolumnasasmodifpreferences+6;

        inicio=false;
        estadoActual=false;

        rapidez = prefs.getString("speed_preference", "Normal");
        switch (rapidez) {
            case "Normal": {
                RAPIDEZNORMAL = 500;
                RAPIDEZDEPRISA = 50;
                break;
            }
            case "Rapido": {
                RAPIDEZNORMAL = 250;
                RAPIDEZDEPRISA = 25;
                break;
            }
        }

       // puntosrestar=puntuacion-puntosrestar;

        inicializarpiezas();
        inicializarmatrizjuego();

        Bundle datos = this.getIntent().getExtras();
        SharedPreferences preferences = getSharedPreferences(username, Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = preferences.edit();
        musicon = preferences.getInt("Music", 0);

        if (musicon == 1) {
            mp1.start();
        }
        if(datos != null) {
           // tpj = datos.getInt("TipoPieza");
            nombrejugador=datos.getString("Nombre");
            puntuacion=datos.getInt("Puntuacion");
            puntosrestar=puntuacion;
            variablepuntosvuelta=datos.getInt("Variablepuntosvuelta");
            avatar=datos.getInt("avatar");
            if(nombrejugador.equals("Introduce tu nombre")){
                nombrejugador="sith";
            }
            if(nombrejugador.equals("")){
                nombrejugador="sith";
            }

            if((puntuacion>110)){//lo ideal seria 500
                if(boleanouno){
                    incremento=15;
                    boleanouno=false;
                }
            }
            if((puntuacion>230)){//lo ideal seria 1000
                if(boleanodos){
                    incremento=25;
                    boleanodos=false;
                }
            }
            if((puntuacion>350)){//lo ideal seria 1490
                if(boleanotres){
                    incremento=35;
                    boleanotres=false;
                }
            }
        }
        else{
            tpj=0;
            nombrejugador="Anonimo";
            avatar=1;
        }

        tpj = preferences.getInt("colorfichas", 1);
        switchavatar();

        Toast.makeText(this, "hola "+ nombrejugador, Toast.LENGTH_SHORT).show();

        Thread hilo1 = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                //Código a ejecutar
                //Forma f=formas[random.nextInt(7)];;
                Forma f=formas[3];;
                if(boleanolinea==true){
                    for(int i=0;i<5;i++)
                        for(int j=0;j<5;j++)
                            f.mat[i][j].setColor(Color.GRAY);
                }
                f.x = 0;
                f.y = 1 + (numeroColumnas - 6) / 2;
                // poner la nueva forma arriba del tablero si es posible
                for (int compensar = 0; compensar <= 3; ++compensar) {
                    int i, m, j, n;
                    boolean si = true;
                    for (m = f.x + compensar, i = 1; i <= 4; i++, m++) {
                        for (n = f.y, j = 1; j <= 4; j++, n++) {
                            matrizDeJuego[m][n].setEstado(matrizDeJuego[m][n].getEstado() + f.mat[i][j].getEstado());
                            if (matrizDeJuego[m][n].getEstado() > 1) {
                                si = false;
                            }
                        }
                    }
                    if (si) {
                        for (i = 1, m = f.x + compensar; i <= 4; i++, m++) {
                            for (j = 1, n = f.y; j <= 4; j++, n++) {
                                if (f.mat[i][j].getEstado() == 1) {
                                    matrizDeJuego[m][n].setColor(f.mat[i][j].getColor());
                                    matrizDeJuego[m][n].setComportamiento(f.mat[i][j].getComportamiento());
                                }
                            }
                        }
                        f.x += compensar;
                        FijarMatrizJuego();
                    } else {
                        for (m = f.x + compensar, i = 1; i <= 4; i++, m++) {
                            for (n = f.y, j = 1; j <= 4; j++, n++) {
                                matrizDeJuego[m][n].setEstado(matrizDeJuego[m][n].getEstado() - f.mat[i][j].getEstado());
                            }
                        }
                    }
                }
                FijarMatrizJuego();
            }
        });

        sensorproximidad=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                /*si el sensor detecta que esta tapado pausa el juego, al destaparse se reanuda*/
                if(sensorEvent.values[0]<sensor.getMaximumRange()){
                    juegoEnPausa = true;
                    PintarMatriz();
                }else{
                    juegoEnPausa = false;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        botonderecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*al pulsar el boton se mueve la ficha a la derecha una posicion*/
                if(estadoActual){
                    MoverForma(IR_DERECHA, formaActual);
                    PintarMatriz();
                }
            }
        });

        botonderecha2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*al pulsar el boton se mueve la ficha a la derecha una posicion*/
                if(estadoActual){
                    MoverForma(IR_DERECHA, formaActual);
                    PintarMatriz();
                }
            }
        });

        giro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*al pulsar el boton se mueve la ficha a la derecha una posicion*/
                Rotar(formaActual);
                PintarMatriz();
            }
        });

        botonizquierda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*al pulsar el boton se mueve la ficha a la izquierda una posicion*/
                if(estadoActual){
                    MoverForma(IR_IZQUIERDA, formaActual);
                    PintarMatriz();
                }
            }
        });

        botonpausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*al pulsar el boton se pausa o reanuda el juego segun como este en ese momento si pausado o en accion*/
                if (juegoEnPausa)
                    juegoEnPausa = false;
                else {
                    juegoEnPausa = true;
                    PintarMatriz();
                }
            }
        });

        botonabajo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*al pulsar el boton se mueve la ficha abajo una posicion*/
                if(estadoActual){
                    MoverForma(IR_ABAJO, formaActual);
                    PintarMatriz();
                }
            }
        });

        botonrapido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //operaciones.postDelayed(runnable, RAPIDEZDEPRISA);
                estadoVelocidadRapidez = true;
            }
        });

        finjuego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //operaciones.postDelayed(runnable, RAPIDEZDEPRISA);
                funcionvolvermain();
            }
        });

        reiniciarjuego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*al pulsar el boton se reinicia el juego si no te gusta como esta llendo la partida*/
                funcionvolverjuego();
            }
        });

        volverjugar250ptos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*al pulsar el boton se reinicia el juego si no te gusta como esta llendo la partida*/
                if((puntuacion-puntosrestar)>=120){

                    mp1.stop();
                    mp2.stop();
                    mp3.stop();
                    mp4.stop();
                    mp5.stop();
                    mp6.stop();
                    mp7.stop();
                    variablemusica=0;
                    try {
                        mp1.prepare();
                        mp2.prepare();
                        mp3.prepare();
                        mp4.prepare();
                        mp5.prepare();
                        mp6.prepare();
                        mp7.prepare();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    funcionvolverjuego();
                }
            }
        });
        start();
    }

    CountDownTimer countDownTimer = new CountDownTimer(milisfutures, tiempos50) {
        public void onTick(long millisUntilFinished) {
            //Toast.makeText(getBaseContext(), (String.format(Locale.getDefault(), "%d sec.", millisUntilFinished / 1000L)), Toast.LENGTH_SHORT).show();
            if(niveljuego>3){
                if(numfilasmodifpreferences>4) {
                    varcortar=varcortar+2;
                }
            }
        }
        public void onFinish() {
           // Toast.makeText(getBaseContext(), "Tiempo " + varcortar, Toast.LENGTH_SHORT).show();
        }
    }.start();

    CountDownTimer countDownTimer2 = new CountDownTimer(milisfutures, tiempos30) {//poner  1000000000  y  30000
        public void onTick(long millisUntilFinished) {
           // Toast.makeText(getBaseContext(), (String.format(Locale.getDefault(), "%d sec.", millisUntilFinished / 1000L)), Toast.LENGTH_SHORT).show();
            if(niveljuego>1){
                if(piezaextra==0 && p ){
                    piezaextra=1;
                }
                if(!p){
                    p=true;
                }
            }
            contadoraleatorias++;
        }

        public void onFinish() {
         //   Toast.makeText(getBaseContext(), "Tiempo " + varcortar, Toast.LENGTH_SHORT).show();
        }
    }.start();

    CountDownTimer countDownTimerrapido = new CountDownTimer(milisfutures, tiempos10) {//poner  1000000000  y  30000
        public void onTick(long millisUntilFinished) {
            // Toast.makeText(getBaseContext(), (String.format(Locale.getDefault(), "%d sec.", millisUntilFinished / 1000L)), Toast.LENGTH_SHORT).show();
           if(RAPIDEZNORMAL>100 && RAPIDEZNORMAL<=finrapidez){//500 450 400 350
               RAPIDEZNORMAL=RAPIDEZNORMAL-incremento;
           }
        }

        public void onFinish() {
            //   Toast.makeText(getBaseContext(), "Tiempo " + varcortar, Toast.LENGTH_SHORT).show();
        }
    }.start();

    CountDownTimer countDownTimermusica = new CountDownTimer(milisfutures, tiempos20) {//poner  1000000000  y  30000
        public void onTick(long millisUntilFinished) {
            // Toast.makeText(getBaseContext(), (String.format(Locale.getDefault(), "%d sec.", millisUntilFinished / 1000L)), Toast.LENGTH_SHORT).show();

            if (musicon == 0) {
                variablemusica=0;
            }
            switch (variablemusica){
                case 1:
                    variablemusica=2;
                    mp1.stop();
                    mp2.start();
                    break;
                case 2:
                    variablemusica=3;
                    mp2.stop();
                    mp3.start();
                    break;
                case 3:
                    variablemusica=4;
                    mp3.stop();
                    mp4.start();
                    break;
                case 4:
                    variablemusica=5;
                    mp4.stop();
                    mp5.start();
                    break;
                case 5:
                    variablemusica=6;
                    mp5.stop();
                    mp6.start();
                    break;
                case 6:
                    variablemusica=7;
                    mp6.stop();
                    mp7.start();
                    break;
                case 7:
                    variablemusica=1;
                    mp7.stop();
                    initmusic();
                    mp1.start();
                    break;
            }
        }

        public void onFinish() {
            //   Toast.makeText(getBaseContext(), "Tiempo " + varcortar, Toast.LENGTH_SHORT).show();
        }
    }.start();

    public void start(){

        /*inicializa el sensor de proximidad*/
        sensorManager.registerListener(sensorproximidad,sensor,2000*1000);
        juegoEnPausa = true;
        PintarMatriz();
    }

    @Override
    protected void onStop() {
        /*al salir del juego usando el boton de android que no del juego se pausa la partida*/
        super.onStop();
            mp1.stop();
            mp2.stop();
            mp3.stop();
            mp4.stop();
            mp5.stop();
            mp6.stop();
            mp7.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mp1.start();
        variablemusica=1;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (musicon == 0) {
            variablemusica=0;
        }
        switch (variablemusica){
            case 1:
                variablemusica=2;
                mp1.stop();
                mp2.start();
                break;
            case 2:
                variablemusica=3;
                mp2.stop();
                mp3.start();
                break;
            case 3:
                variablemusica=4;
                mp3.stop();
                mp4.start();
                break;
            case 4:
                variablemusica=5;
                mp4.stop();
                mp5.start();
                break;
            case 5:
                variablemusica=6;
                mp5.stop();
                mp6.start();
                break;
            case 6:
                variablemusica=7;
                mp6.stop();
                mp7.start();
                break;
            case 7:
                variablemusica=1;
                mp7.stop();
                initmusic();
                mp1.start();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
            mp1.stop();
            mp2.stop();
            mp3.stop();
            mp4.stop();
            mp5.stop();
            mp6.stop();
            mp7.stop();
    }

    public void cambiarcolorconfiguracion(){
        switch (tpj){
            case 1:
                colorJ=Color.RED;
                color2=Color.rgb(108, 230, 21);//verde
                color5=Color.rgb(248, 138, 17);//naranja
                colorl=Color.YELLOW;
                colori=Color.CYAN;
                colort=Color.BLUE;
                coloro=Color.rgb(255, 51, 249);//rosa
                break;
            case 2:
                colorJ=Color.CYAN;
                color2=Color.BLUE;
                color5=Color.rgb(108, 230, 21);//verde
                colorl=Color.RED;
                colori=Color.rgb(248, 138, 17);//naranja
                colort=Color.rgb(255, 51, 249);//rosa
                coloro=Color.YELLOW;
                break;
            case 3:
                colorJ=Color.rgb(248, 138, 17);//naranja
                color2=Color.rgb(255, 51, 249);//rosa
                color5=Color.BLUE;
                colorl=Color.CYAN;
                colori=Color.rgb(108, 230, 21);//verde
                colort=Color.YELLOW;
                coloro=Color.RED;
                break;
            case 4://rojos
                colorJ=Color.rgb(239, 28,58);
                color2=Color.rgb(242,154,193);
                color5=Color.rgb(241,124,171);
                colorl=Color.rgb(241,59,97);
                colori=Color.rgb(254, 0, 0);
                colort=Color.rgb(244,99,150);
                coloro=Color.rgb(238,81,125);
                break;
            case 5://verdes
                colorJ=Color.rgb(115,175,100);
                color2=Color.rgb(174,245,199);
                color5=Color.rgb(145,245,170);
                colorl=Color.rgb(125,200,110);
                colori=Color.rgb(30,170,5);
                colort=Color.rgb(130,242,133);
                coloro=Color.rgb(125,225,110);
                break;
            case 6://azules
                colorJ=Color.rgb(24,82,251);
                color2=Color.rgb(154,242,248);
                color5=Color.rgb(118,212,247);
                colorl=Color.rgb(42,116,250);
                colori=Color.rgb(8,2,254);
                colort=Color.rgb(89,186,248);
                coloro=Color.rgb(59,153,250);
                break;
            default:
                colorJ=Color.YELLOW;
                color2=Color.rgb(248, 138, 17);
                color5=Color.CYAN;
                colorl=Color.rgb(255, 51, 249);
                colori=Color.RED;
                colort=Color.rgb(108, 230, 21);
                coloro=Color.BLUE;
                break;
        }
       // matrizDeJuego
    }

    private void FijarMatrizJuego() {
        /*se hace que la matriz se guarden las posiciones en la pantalla*/
        for (int ejex = 3; ejex < numeroFilas - 3; ejex++) {
            for (int ejey = 3; ejey < numeroColumnas - 3; ejey++) {
                if (matrizDeJuego[ejex][ejey].getEstado() == 0) {
                    matrizDeJuego[ejex][ejey].setColor(Color.rgb(244,242,205));
                    matrizDeJuego[ejex][ejey].setComportamiento(PizarradeCeldas.BEHAVIOR_NOTHING);
                    continue;
                }
                if (matrizDeJuego[ejex][ejey].getComportamiento() == PizarradeCeldas.BEHAVIOR_IS_FIXED)
                    continue;
                if (matrizDeJuego[ejex][ejey].getComportamiento() == PizarradeCeldas.BEHAVIOR_IS_FALLING) {
                    int x, y, m, n;
                    for (x = 1, m = formaActual.x; x <= 4; x++, m++) {
                        for (y = 1, n = formaActual.y; y <= 4; y++, n++) {
                            if (m == ejex && n == ejey) {
                                if (formaActual.mat[x][y].getEstado() == 0) {
                                    matrizDeJuego[ejex][ejey] = new PizarradeCeldas();
                                }
                            }
                        }
                    }
                    continue;
                }
                if (matrizDeJuego[ejex][ejey].getComportamiento() == PizarradeCeldas.BEHAVIOR_NOTHING) {
                    int x, y, m, n;
                    for (x = 1, m = formaActual.x; x <= 4; x++, ++m) {
                        for (y = 1, n = formaActual.y; y <= 4; y++, n++) {
                            if (m == ejex && n == ejey) {
                                if (formaActual.mat[x][y].getEstado() == 1) {
                                    matrizDeJuego[ejex][ejey] = formaActual.mat[x][y];
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean MoverForma(final int direccion, Forma formaAct) {
        // copiar la matriz de juego en un auxiliar
        PizarradeCeldas[][] aux = new PizarradeCeldas[numeroFilas][];
        for (int i = 0; i < numeroFilas; i++)
            aux[i] = new PizarradeCeldas[numeroColumnas];
        PizarradeCeldas[][] AuxB=aux;
        PizarradeCeldas[][] AuxA=matrizDeJuego;
        for (int ejex = 1; ejex <= numeroFilas; ejex++) {
            for (int ejey = 1; ejey <= numeroColumnas; ejey++) {
                AuxB[ejex-1][ejey-1] = new PizarradeCeldas(AuxA[ejex-1][ejey-1].getEstado(), AuxA[ejex-1][ejey-1].getColor(), AuxA[ejex-1][ejey-1].getComportamiento());
            }
        }
        int i, m, j, n;
        // eliminar la forma de la tabla
        for (m = formaAct.x, i = 1; i <= 4; i++, m++) {
            for (n = formaAct.y, j = 1; j <= 4; j++, n++) {
                if (formaAct.mat[i][j].getEstado() == 1) {
                    matrizDeJuego[m][n] = new PizarradeCeldas();
                }
            }
        }

        // intentar mover la forma a la direccion indicada
        for (m = formaAct.x + dx[direccion], i = 1; i <= 4; i++, m++) {
            for (n = formaAct.y + dy[direccion], j = 1; j <= 4; j++, n++) {
                matrizDeJuego[m][n].setEstado(matrizDeJuego[m][n].getEstado() + formaAct.mat[i][j].getEstado());
                if (formaAct.mat[i][j].getEstado() == 1) {
                    matrizDeJuego[m][n].setColor(formaAct.mat[i][j].getColor());
                    matrizDeJuego[m][n].setComportamiento(formaAct.mat[i][j].getComportamiento());
                }
                if (matrizDeJuego[m][n].getEstado() > 1) {
                    AuxB=matrizDeJuego;
                    AuxA=aux;
                    for (int ejex = 1; ejex <= numeroFilas; ejex++) {
                        for (int ejey = 1; ejey <= numeroColumnas; ejey++) {
                            AuxB[ejex-1][ejey-1] = new PizarradeCeldas(AuxA[ejex-1][ejey-1].getEstado(), AuxA[ejex-1][ejey-1].getColor(), AuxA[ejex-1][ejey-1].getComportamiento());
                        }
                    }
                    FijarMatrizJuego();
                    return false;
                }
            }
        }
        formaAct.x += dx[direccion];
        formaAct.y += dy[direccion];
        FijarMatrizJuego();
        return true;
    }

    private boolean Rotar(Forma formaAct) {

       /*si es un cuadrado no se mueve ni se gira, y si es una pieza larga se mueve dos cuadros, el resto es comun a todas
       * para que no haya bugs de movimiento en los laterales*/
       if(formaAct.y<=2 && formaAct.tipoformacuadrado==false ) {
            MoverForma(IR_DERECHA, formaAct);
            if( formaAct.tipoformalarga==true) {
                MoverForma(IR_DERECHA, formaAct);
            }
        }
        else if (formaAct.y>numcolumnasasmodifpreferences-1 && formaAct.tipoformacuadrado==false ) {
            MoverForma(IR_IZQUIERDA, formaAct);

            if( formaAct.tipoformalarga==true) {
                MoverForma(IR_IZQUIERDA, formaAct);
            }
        }

        if( formaAct.x>0 && formaAct.x<numfilasmodifpreferences  && formaAct.tipoformacuadrado==false ) {
            // copiar la matriz de juego en un auxiliar
            PizarradeCeldas[][] aux = new PizarradeCeldas[numeroFilas][];
            for (int i = 0; i < numeroFilas; i++)
                aux[i] = new PizarradeCeldas[numeroColumnas];

            PizarradeCeldas[][] AuxB = aux;
            PizarradeCeldas[][] AuxA = matrizDeJuego;
            for (int ejex = 1; ejex <= numeroFilas; ejex++) {
                for (int ejey = 1; ejey <= numeroColumnas; ejey++) {
                    AuxB[ejex - 1][ejey - 1] = new PizarradeCeldas(AuxA[ejex - 1][ejey - 1].getEstado(), AuxA[ejex - 1][ejey - 1].getColor(), AuxA[ejex - 1][ejey - 1].getComportamiento());
                }
            }
            int i, m, j, n;
            // eliminar la forma de la matriz de juego
            for (m = formaAct.x, i = 1; i <= 4; i++, m++) {
                for (n = formaAct.y, j = 1; j <= 4; j++, n++) {
                    if (formaAct.mat[i][j].getEstado() == 1) {
                        matrizDeJuego[m][n] = new PizarradeCeldas();
                    }
                }
            }
            // rotar la forma
            formaAct.RotarLaPieza();
            for (m = formaAct.x, i = 1; i <= 4; i++, m++) {
                for (n = formaAct.y, j = 1; j <= 4; j++, n++) {
                    matrizDeJuego[m][n].setEstado(matrizDeJuego[m][n].getEstado() + formaAct.mat[i][j].getEstado());
                    if (formaAct.mat[i][j].getEstado() == 1) {
                        matrizDeJuego[m][n].setColor(formaAct.mat[i][j].getColor());
                        matrizDeJuego[m][n].setComportamiento(formaAct.mat[i][j].getComportamiento());
                    }
                }
            }
            FijarMatrizJuego();
        }
            return true;
    }

    private boolean CrearLaForma() {
        // generar la forma actual y ponerla en la matriz
        if(inicio==false){
            /*Aqui solo entra en la primera iteracion del juego, inicializando la primera forma, ya que de no ser asi
             * se inicializarian dos veces las formas y no coincidiria con lo que tiene que tocar*/
            proximaForma = formas[random.nextInt(7)];
            inicio=true;
        }
        /*se guarda la forma proxima de la vuelta anterior para que sea la actual y se genera una nueva proxima forma que se
         * mostrara en la pantalla de la proxima forma siendo aleatoria la forma que vaya a salir*/
        formaActual=proximaForma;
        if(formaActual.tipoforma2){
            for(int i=0;i<5;i++)
                for(int j=0;j<5;j++)
                    formaActual.mat[i][j].setColor(color2);
        }else if(formaActual.tipoforma5){
            for(int i=0;i<5;i++)
                for(int j=0;j<5;j++)
                    formaActual.mat[i][j].setColor(color5);
        }else if(formaActual.tipoformaL){
            for(int i=0;i<5;i++)
                for(int j=0;j<5;j++)
                    formaActual.mat[i][j].setColor(colorl);
        }else if(formaActual.tipoformaJ){
            for(int i=0;i<5;i++)
                for(int j=0;j<5;j++)
                    formaActual.mat[i][j].setColor(colorJ);
        }else if(formaActual.tipoformaT){
            for(int i=0;i<5;i++)
                for(int j=0;j<5;j++)
                    formaActual.mat[i][j].setColor(colort);
        }else if(formaActual.tipoformacuadrado){
            for(int i=0;i<5;i++)
                for(int j=0;j<5;j++)
                    formaActual.mat[i][j].setColor(coloro);
        }else if(formaActual.tipoformalarga){
            for(int i=0;i<5;i++)
                for(int j=0;j<5;j++)
                    formaActual.mat[i][j].setColor(colori);
        }
        int alea=random.nextInt(7);
        proximaForma = formas[alea];
        switch (alea){
            case 0:
                if(boleanolinea==true){
                    for(int i=0;i<5;i++)
                        for(int j=0;j<5;j++)
                            proximaForma.mat[i][j].setColor(colorl);
                }
                vistaPiezaProxima.setImageDrawable(getResources().getDrawable(R.drawable.figural));
                vistaPiezaProxima.setColorFilter(colorl);

                proximaForma.tipoformaL=true;
                break;
            case 1:
                if(boleanolinea==true){
                    for(int i=0;i<5;i++)
                        for(int j=0;j<5;j++)
                            proximaForma.mat[i][j].setColor(color2);
                }
                vistaPiezaProxima.setImageDrawable(getResources().getDrawable(R.drawable.figura2));
                vistaPiezaProxima.setColorFilter(color2);
                proximaForma.tipoforma2=true;
                break;
            case 2:
                if(boleanolinea==true){
                    for(int i=0;i<5;i++)
                        for(int j=0;j<5;j++)
                            proximaForma.mat[i][j].setColor(colori);
                }
                vistaPiezaProxima.setImageDrawable(getResources().getDrawable(R.drawable.figurai));
                vistaPiezaProxima.setColorFilter(colori);
                proximaForma.tipoformalarga=true;
                break;
            case 3:
                if(boleanolinea==true){
                    for(int i=0;i<5;i++)
                        for(int j=0;j<5;j++)
                            proximaForma.mat[i][j].setColor(coloro);
                }
                vistaPiezaProxima.setImageDrawable(getResources().getDrawable(R.drawable.figurao));
                vistaPiezaProxima.setColorFilter(coloro);
                break;
            case 4:
                if(boleanolinea==true){
                    for(int i=0;i<5;i++)
                        for(int j=0;j<5;j++)
                            proximaForma.mat[i][j].setColor(colort);
                }
                vistaPiezaProxima.setImageDrawable(getResources().getDrawable(R.drawable.figurat));
                vistaPiezaProxima.setColorFilter(colort);
                proximaForma.tipoformaT=true;
                break;
            case 5:
                if(boleanolinea==true){
                    for(int i=0;i<5;i++)
                        for(int j=0;j<5;j++)
                            proximaForma.mat[i][j].setColor(color5);
                }
                vistaPiezaProxima.setImageDrawable(getResources().getDrawable(R.drawable.figura5));
                vistaPiezaProxima.setColorFilter(color5);
                proximaForma.tipoforma5=true;
                break;
            case 6:
                if(boleanolinea==true){
                    for(int i=0;i<5;i++)
                        for(int j=0;j<5;j++)
                            proximaForma.mat[i][j].setColor(colorJ);
                }
                vistaPiezaProxima.setImageDrawable(getResources().getDrawable(R.drawable.figuraj));
                vistaPiezaProxima.setColorFilter(colorJ);
                proximaForma.tipoformaJ=true;
                break;

        }

        // generar aleatoriamente el numero de rotaciones
        int numeroderotaciones = random.nextInt(5);
        int aux=1;
        while ( aux <= numeroderotaciones) {
            formaActual.RotarLaPieza();
            aux++;
        }

        formaActual.x = 0;
        formaActual.y = 1 + (numeroColumnas - 6) / 2;
        // poner la nueva forma arriba del tablero si es posible
        for (int compensar = 0; compensar <= 3; ++compensar) {
            int i, m, j, n;
            boolean si = true;
            for (m = formaActual.x + compensar, i = 1; i <= 4; i++, m++) {
                for (n = formaActual.y, j = 1; j <= 4; j++, n++) {
                    matrizDeJuego[m][n].setEstado(matrizDeJuego[m][n].getEstado() + formaActual.mat[i][j].getEstado());
                    if (matrizDeJuego[m][n].getEstado() > 1) {
                        si = false;
                    }
                }
            }
            if (si) {
                for (i = 1, m = formaActual.x + compensar; i <= 4; i++, m++) {
                    for (j = 1, n = formaActual.y; j <= 4; j++, n++) {
                        if (formaActual.mat[i][j].getEstado() == 1) {
                            matrizDeJuego[m][n].setColor(formaActual.mat[i][j].getColor());
                            matrizDeJuego[m][n].setComportamiento(formaActual.mat[i][j].getComportamiento());
                        }
                    }
                }
                formaActual.x += compensar;
                FijarMatrizJuego();
                return true;
            } else {
                for (m = formaActual.x + compensar, i = 1; i <= 4; i++, m++) {
                    for (n = formaActual.y, j = 1; j <= 4; j++, n++) {
                        matrizDeJuego[m][n].setEstado(matrizDeJuego[m][n].getEstado() - formaActual.mat[i][j].getEstado());
                    }
                }
            }
        }
        FijarMatrizJuego();
        return false;
    }

    void CambiarEstadoVelocidadAcelerada(boolean estadodelavelocidad) {
         estadoVelocidadRapidez = false;  //rapidez normal
         estadoVelocidadRapidez = true; // rapidez acelerada
        operaciones.removeCallbacks(runnable);
        estadoVelocidadRapidez = estadodelavelocidad;
        if (estadoVelocidadRapidez)
            operaciones.postDelayed(runnable, RAPIDEZDEPRISA);
        else
            operaciones.postDelayed(runnable, RAPIDEZNORMAL);
    }

    private boolean Check() {
        int cont =0;
        /* comprueba si se ha hecho una linea completa y en caso afirmativo esta o estas se eliminaran del tablero*/
        int k = 0;
        boolean encontrado = false;
        for (int i = numeroFilas - 4; i >= 3; --i) {
            boolean si = true;
            for (int j = 3; j < numeroColumnas - 3; j++) {
                if (matrizDeJuego[i][j].getEstado() == 0) {
                    si = false;
                }
            }
            if (si) {
                ++k;
                cont++;
                // se actualiza la puntuacion incrementando 30 por linea
                puntuacion+=puntossumar;
                encontrado = true;
                tpj=tpj+1;
                if(tpj>6){
                    tpj=1;
                }
                cambiarcolorconfiguracion();

                boleanolinea=true;
             /*   for (int l = numeroFilas - 4; l >= 3; --l) {
                    for (int j = 3; j < numeroColumnas - 3; j++) {
                        matrizDeJuego[l][j].setColor(colorJ);
                    }}*/

            } else {
                if (k == 0)
                    continue;
                for (int j = 3; j < numeroColumnas - 3; j++) {
                    int estado = matrizDeJuego[i][j].getEstado();
                    int color = matrizDeJuego[i][j].getColor();
                    int comportamiento = matrizDeJuego[i][j].getComportamiento();
                    matrizDeJuego[i + k][j] = new PizarradeCeldas(estado, color, comportamiento);
                }
            }
        }

        if(cont>1){

            tpj = random.nextInt(7);
            cont=0;
        }
           cambiarcolorconfiguracion();

        for (int pas = 0; pas < k; ++pas) {
            for (int j = 3; j < numeroColumnas - 3; j++) {
                matrizDeJuego[3 + pas][j] = new PizarradeCeldas();
            }
        }

        if(k>0){
            long patronvibracion[]={0,100,100,100,100};
            vibrador.vibrate(patronvibracion,-1);
        }
        FijarMatrizJuego();

        if((puntuacion > 110 && avatar < 4)){//lo ideal seria 500
            if(boleanouno){
                avatar+=3;
                incremento=15;
                boleanouno=false;
            }
        }
        if((puntuacion > 230 && avatar < 7)){//lo ideal seria 1000
            if(boleanodos){
                avatar+=3;
                incremento=25;
                boleanodos=false;
            }
        }
        if((puntuacion > 350 && avatar < 10)){//lo ideal seria 1490
            if(boleanotres){
                avatar+=3;
                incremento=35;
                boleanotres=false;
            }
        }

        switchavatar();
        return encontrado;
    }

    void PintarMatriz() {

        // pintar el fondo del tablero
        paint.setColor(Color.rgb(34,31,84));
        canvas.drawRect(0,  varcortar, AnchuraPantalla, AlturaPantalla, paint);

        // Pintar la cuadricula del tablero
        paint.setColor(Color.rgb(244,242,205));
        for (int i = varcortar; i <= (numeroFilas - 6); i++) {
            canvas.drawLine(0, i * (AlturaPantalla / (numeroFilas - 6)), AnchuraPantalla,
                    i * (AlturaPantalla / (numeroFilas - 6)), paint);
        }
        for (int i = 0; i <= (numeroColumnas - 6); i++) {
            canvas.drawLine(i * (AnchuraPantalla / (numeroColumnas - 6)), 0,
                    i * (AnchuraPantalla / (numeroColumnas - 6)), AlturaPantalla, paint);
        }

        // Pintar los bloques de tetris
        for (int i = 3+varcortar; i < numeroFilas - 3; i++) {
            for (int j = 3; j < numeroColumnas - 3; j++) {
                if (matrizDeJuego[i][j].getEstado() == 1) {
                    paint.setColor(matrizDeJuego[i][j].getColor());
                    canvas.drawRect((j - 3) * (AnchuraPantalla / (numeroColumnas - 6)),
                            (i - 3) * (AlturaPantalla / (numeroFilas - 6)),
                            (j + 1 - 3) * (AnchuraPantalla / (numeroColumnas - 6)),
                            (i + 1 - 3) * (AlturaPantalla / (numeroFilas - 6)),
                            paint);
                }
            }
        }

        // Pintar los bordes de tetris
        for (int i = 3+varcortar; i < (numeroFilas - 3); i++) {
            for (int j = 3; j < numeroColumnas - 3; j++) {
                if (matrizDeJuego[i][j].getEstado() == 1) {
                    paint.setColor(Color.rgb(244,242,205));
                    canvas.drawLine((j - 3) * (AnchuraPantalla / (numeroColumnas - 6)),
                            (i - 3) * ((AlturaPantalla / (numeroFilas - 6))),
                            (j - 3) * (AnchuraPantalla / (numeroColumnas - 6)),
                            (i + 1 - 3) * (AlturaPantalla / (numeroFilas - 6)),
                            paint);
                    canvas.drawLine((j - 3) * (AnchuraPantalla / (numeroColumnas - 6)),
                            (i - 3) * (AlturaPantalla / (numeroFilas - 6)),
                            (j + 1 - 3) * (AnchuraPantalla / (numeroColumnas - 6)),
                            (i - 3) * (AlturaPantalla / (numeroFilas - 6)),
                            paint);
                    canvas.drawLine((j + 1 - 3) * (AnchuraPantalla / (numeroColumnas - 6)),
                            (i - 3) * (AlturaPantalla / (numeroFilas - 6)),
                            (j + 1 - 3) * (AnchuraPantalla / (numeroColumnas - 6)),
                            (i + 1 - 3) * (AlturaPantalla / (numeroFilas - 6)),
                            paint);
                    canvas.drawLine((j - 3) * (AnchuraPantalla / (numeroColumnas - 6)),
                            (i + 1 - 3) * (AlturaPantalla / (numeroFilas - 6)),
                            (j + 1 - 3) * (AnchuraPantalla / (numeroColumnas - 6)),
                            (i + 1 - 3) * (AlturaPantalla / (numeroFilas - 6)),
                            paint);
                }
            }
        }

        if (!juegoEnMarcha) {
            /*si se pierde la partida se invisibiliza el boton de reiniciar el juego poniendo visibilidad al cartel de has perdido
            * y un segundo y medio mas tarde  mostrando el carte del pulsar la pantalla para volver al menu inicio*/
            reiniciarjuego.setVisibility(View.INVISIBLE);
            vistaavatar.setVisibility(View.INVISIBLE);

            finjuego.setVisibility(View.VISIBLE);

            if((puntuacion-puntosrestar)>=120){
          //  if((puntuacion-(120*variablepuntosvuelta))>=120){
                volverjugar250ptos.setVisibility(View.VISIBLE);
            }
            TextView textView = findViewById(R.id.pierde);
            textView.setVisibility(View.VISIBLE);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // despues de 1.5 segundos

                    TextView textView2 = findViewById(R.id.volverajugar);
                    textView2.setVisibility(View.VISIBLE);
                }
            }, 1500);
        } else if (juegoEnPausa) {
            /*Si pausas el juego se muestra un texto de que el juego esta pausado*/
            paint.setTextSize(40);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setColor(Color.YELLOW);
            canvas.drawText("JUEGO PAUSADO", (float) (AnchuraPantalla / 2), (float) (AlturaPantalla / 2), paint);
        }
        vistaPiezaProxima.setBackgroundDrawable(new BitmapDrawable(bitmapnuevaforma));
        // Mostrar el actual dibujo
        linearLayout.setBackgroundDrawable(new BitmapDrawable(bitmap));

        // Actualizar la puntuacion del textview
        TextView textView = findViewById(R.id.puntos);
        textView.setText("Puntuación: " + puntuacion);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {


            if(piezaextra>0){
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // despues de 1.6 segundos
                        estadoActual = false;
                        for (int m=0;m<numfilasmodifpreferences;m++){
                            MoverForma(IR_ABAJO, formaActual);
                        }


                        estadoActual = CrearLaForma();

                        estadoVelocidadRapidez = false;

                    }
                }, 1600);
                estadoVelocidadRapidez = true;
                piezaextra=0;
            }

            if (!juegoEnMarcha) {
                return;
            }

            if (juegoEnPausa) {
                PintarMatriz();
                if (estadoVelocidadRapidez)
                    operaciones.postDelayed(this, RAPIDEZDEPRISA);
                else
                    operaciones.postDelayed(this, RAPIDEZNORMAL);
                return;
            }

            boolean movido = MoverForma(IR_ABAJO, formaActual);
            if (!movido) {
                int x, y, i, j;
                for (x = 1, i = formaActual.x; x <= 4; x++, i++) {
                    for (y = 1, j = formaActual.y; y <= 4; y++, j++) {
                        if (formaActual.mat[x][y].getEstado() == 1) {
                            matrizDeJuego[i][j].setComportamiento(PizarradeCeldas.BEHAVIOR_IS_FIXED);
                            formaActual.mat[x][y].setComportamiento(PizarradeCeldas.BEHAVIOR_IS_FIXED);
                        }
                    }
                }
                estadoActual = false;
                Check(); //busca lineas completas
                estadoActual = CrearLaForma(); // crea otra figura
               // operaciones.postAtTime(runnable,CrearLaFormaextra(),10000);
                if (!estadoActual) // si no se puede, se acaba el juego
                {
                    juegoEnMarcha = false;
                    PintarMatriz();

                    return;
                }
                PintarMatriz();

                if (estadoVelocidadRapidez) {
                    CambiarEstadoVelocidadAcelerada(false);
                    return;
                }
                    operaciones.removeCallbacks(runnable);
                    operaciones.postDelayed(runnable, RAPIDEZNORMAL);
                    return;
            } else
                PintarMatriz();

            if (estadoVelocidadRapidez)
                operaciones.postDelayed(this, RAPIDEZDEPRISA);
            else
                operaciones.postDelayed(this, RAPIDEZNORMAL);
        }
    };

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (!juegoEnMarcha) {
            /*al perder y tocar la pantalla se vuelve a la pantalla principal*/
            Toast.makeText(getBaseContext(), "Vuelve a la pantalla principal ", Toast.LENGTH_SHORT).show();

            funcionvolvermain();
            return true;
        }
        if (juegoEnPausa || !estadoActual)
            return false;

        Rotar(formaActual);
        PintarMatriz();
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detectorDeGestos.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        /*al pulsar dos veces se vuelve a la pantalla anterior*/
        if (TiempoDeEspera + 1500 > System.currentTimeMillis()) {
            funcionvolvermain();
        } else {
            Toast.makeText(this, "Pulsa nuevamente para volver", Toast.LENGTH_SHORT).show();
        }
        TiempoDeEspera = System.currentTimeMillis();
    }

    public void funcionvolverjuego(){

        Intent intent = new Intent(PantallaJuego.this, PantallaJuego.class);
        intent.putExtra("Nombre", nombrejugador);
        intent.putExtra("avatar", avatar);
        intent.putExtra("Puntuacion", puntuacion);
        intent.putExtra("Variablepuntosvuelta", variablepuntosvuelta+1);

        startActivity(intent);
        finish();
    }

    public void funcionvolvermain(){
        Intent intent = new Intent(PantallaJuego.this, MainActivity.class);
        //aqui devolver puntuacion y nombre usuario
        intent.putExtra("Puntuacion", puntuacion);
        intent.putExtra("Nombrevuelta", nombrejugador);
        intent.putExtra("avatar", avatar);
        variablemusica=0;
        mp1.stop();
        mp2.stop();
        mp3.stop();
        mp4.stop();
        mp5.stop();
        mp6.stop();
        mp7.stop();
        startActivity(intent);
        finish();
    }

    public void switchavatar(){
        switch (avatar){
            case 1:
                vistaavatar.setBackground(getDrawable(R.drawable.imafo1));
                niveljuego=1;
                incremento=5;
                finrapidez=450;
                puntossumar=30;
                break;
            case 2:
                vistaavatar.setBackground(getDrawable(R.drawable.imafo2));
                niveljuego=1;
                incremento=5;
                finrapidez=450;
                puntossumar=30;
                break;
            case 3:
                vistaavatar.setBackground(getDrawable(R.drawable.imafo3));
                niveljuego=1;
                incremento=5;
                finrapidez=450;
                puntossumar=30;
                break;
            case 4:
                vistaavatar.setBackground(getDrawable(R.drawable.imafo4));
                niveljuego=2;
                incremento=15;
                RAPIDEZNORMAL=450;
                finrapidez=400;
                puntossumar=40;
                break;
            case 5:
                vistaavatar.setBackground(getDrawable(R.drawable.imafo5));
                niveljuego=2;
                incremento=15;
                RAPIDEZNORMAL=450;
                finrapidez=400;
                puntossumar=40;
                break;
            case 6:
                vistaavatar.setBackground(getDrawable(R.drawable.imafo6));
                niveljuego=2;
                incremento=15;
                RAPIDEZNORMAL=450;
                finrapidez=400;
                puntossumar=40;
                break;
            case 7:
                vistaavatar.setBackground(getDrawable(R.drawable.imafo7));
                niveljuego=3;
                incremento=25;
                RAPIDEZNORMAL=400;
                finrapidez=350;
                puntossumar=50;
                break;
            case 8:
                vistaavatar.setBackground(getDrawable(R.drawable.imafo8));
                niveljuego=3;
                incremento=25;
                RAPIDEZNORMAL=400;
                finrapidez=350;
                puntossumar=50;
                break;
            case 9:
                vistaavatar.setBackground(getDrawable(R.drawable.imafo9));
                niveljuego=3;
                incremento=25;
                RAPIDEZNORMAL=400;
                finrapidez=350;
                puntossumar=50;
                break;
            case 10:
                vistaavatar.setBackground(getDrawable(R.drawable.imafo10));
                niveljuego=4;
                incremento=35;
                RAPIDEZNORMAL=350;
                finrapidez=100;
                puntossumar=60;
                break;
            case 11:
                vistaavatar.setBackground(getDrawable(R.drawable.imafo10));
                niveljuego=4;
                incremento=35;
                RAPIDEZNORMAL=350;
                finrapidez=100;
                puntossumar=60;
                break;
            case 12:
                vistaavatar.setBackground(getDrawable(R.drawable.imafo10));
                niveljuego=4;
                incremento=35;
                RAPIDEZNORMAL=350;
                finrapidez=100;
                puntossumar=60;
                break;
            default:
                vistaavatar.setBackground(getDrawable(R.drawable.imafo10));
                niveljuego=4;
                incremento=35;
                RAPIDEZNORMAL=350;
                finrapidez=100;
                puntossumar=60;
                break;
        }
    }

    public void inicializarmatrizjuego(){

        /*inicializar la base del juego */
        // Crear la pizarra del juego
        matrizDeJuego = new PizarradeCeldas[numeroFilas][];
        for (int i = 0; i < numeroFilas; i++) {
            matrizDeJuego[i] = new PizarradeCeldas[numeroColumnas];
            for (int j = 0; j < numeroColumnas; j++) {
                matrizDeJuego[i][j] = new PizarradeCeldas();
            }
        }

        for (int j = 0; j < numeroColumnas; j++) {
            for (int i = 0; i <= 2; i++) {
                matrizDeJuego[i][j] = new PizarradeCeldas(1, Color.rgb(244,242,205));
            }
            for (int i = numeroFilas - 3; i < numeroFilas; i++) {
                matrizDeJuego[i][j] = new PizarradeCeldas(1, Color.rgb(244,242,205));
            }
        }

        for (int i = 0; i < numeroFilas; i++) {
            for (int j = 0; j <= 2; j++) {
                matrizDeJuego[i][j] = new PizarradeCeldas(1, Color.rgb(244,242,205));
            }
            for (int j = numeroColumnas - 3; j < numeroColumnas; j++) {
                matrizDeJuego[i][j] = new PizarradeCeldas(1, Color.rgb(244,242,205));
            }
        }

        for (int j = 3; j < numeroColumnas - 3; j++) {
            matrizDeJuego[numeroFilas - 4][j] = new PizarradeCeldas(matrizDeJuego[numeroFilas - 4][j].getEstado(), matrizDeJuego[numeroFilas - 4][j].getColor(), PizarradeCeldas.BEHAVIOR_IS_FIXED);
        }

        // tiempo0=SystemClock.elapsedRealtime();
        tiempo0=System.currentTimeMillis();
        // Crear el bloque de tetris inicial
        estadoActual = CrearLaForma();

        // Empezar el juego
        juegoEnMarcha = true;
        juegoEnPausa = false;

        // Pintar la matrix inicial
        PintarMatriz();

        CambiarEstadoVelocidadAcelerada(false);

        //Cambiar el estado de la velocidad
        operaciones.removeCallbacks(runnable);
        operaciones.postDelayed(runnable, RAPIDEZNORMAL);


        if(sensor==null){
            //finish();
        }
    }

    public void inicializarpiezas(){

        /*inicializacion de las piezas del juego*/
        int[][] tipoforma = new int[5][5];
        int ejex=1, ejey=1;
        while (ejex<=5){
            while (ejey<=5){
                tipoforma[ejex-1][ejey-1] = 0;
                ejey++;
            }
            ejex++;
        }
        cambiarcolorconfiguracion();

        /*       +
         *       +
         *       ++
         * */
        tipoforma[1][2] = 1;
        tipoforma[1][3] = 1;
        tipoforma[2][3] = 1;
        tipoforma[3][3] = 1;
        // formas[0] = new Forma(tipoforma, Color.rgb(255, 51, 249), PizarradeCeldas.BEHAVIOR_IS_FALLING);
        formas[0] = new Forma(tipoforma, colorl, PizarradeCeldas.BEHAVIOR_IS_FALLING);
        tipoforma[1][2] = 0;
        tipoforma[1][3] = 0;
        tipoforma[2][3] = 0;
        tipoforma[3][3] = 0;

        /*      ++
         *       ++
         * */
        tipoforma[2][1] = 1;
        tipoforma[2][2] = 1;
        tipoforma[3][2] = 1;
        tipoforma[3][3] = 1;
        // formas[1] = new Forma(tipoforma, Color.rgb(248, 138, 17), PizarradeCeldas.BEHAVIOR_IS_FALLING);
        formas[1] = new Forma(tipoforma, color2, PizarradeCeldas.BEHAVIOR_IS_FALLING);
        tipoforma[2][1] = 0;
        tipoforma[2][2] = 0;
        tipoforma[3][2] = 0;
        tipoforma[3][3] = 0;

        /*       +
         *       +
         *       +
         *       +
         * */
        tipoforma[1][2] = 1;
        tipoforma[2][2] = 1;
        tipoforma[3][2] = 1;
        tipoforma[4][2] = 1;
        //formas[2] = new Forma(tipoforma, Color.RED, PizarradeCeldas.BEHAVIOR_IS_FALLING);
        formas[2] = new Forma(tipoforma, colori, PizarradeCeldas.BEHAVIOR_IS_FALLING);
        formas[2].tipoformalarga=true;
        tipoforma[1][2] = 0;
        tipoforma[2][2] = 0;
        tipoforma[3][2] = 0;
        tipoforma[4][2] = 0;

        /*       ++
         *       ++
         * */
        tipoforma[2][2] = 1;
        tipoforma[2][3] = 1;
        tipoforma[3][2] = 1;
        tipoforma[3][3] = 1;
        //formas[3] = new Forma(tipoforma, Color.BLUE, PizarradeCeldas.BEHAVIOR_IS_FALLING);
        formas[3] = new Forma(tipoforma, coloro, PizarradeCeldas.BEHAVIOR_IS_FALLING);
        formas[3].tipoformacuadrado=true;
        tipoforma[2][2] = 0;
        tipoforma[2][3] = 0;
        tipoforma[3][2] = 0;
        tipoforma[3][3] = 0;

        /*      +++
         *       +
         * */
        tipoforma[1][2] = 1;
        tipoforma[2][2] = 1;
        tipoforma[2][3] = 1;
        tipoforma[3][2] = 1;
        //formas[4] = new Forma(tipoforma, Color.rgb(108, 230, 21), PizarradeCeldas.BEHAVIOR_IS_FALLING);
        formas[4] = new Forma(tipoforma,colort, PizarradeCeldas.BEHAVIOR_IS_FALLING);
        tipoforma[1][2] = 0;
        tipoforma[2][2] = 0;
        tipoforma[2][3] = 0;
        tipoforma[3][2] = 0;

        /*        ++
         *       ++
         * */
        tipoforma[1][2] = 1;
        tipoforma[2][2] = 1;
        tipoforma[2][3] = 1;
        tipoforma[3][3] = 1;
        //formas[5] = new Forma(tipoforma, Color.CYAN, PizarradeCeldas.BEHAVIOR_IS_FALLING);
        formas[5] = new Forma(tipoforma, color5, PizarradeCeldas.BEHAVIOR_IS_FALLING);
        tipoforma[1][2] = 0;
        tipoforma[2][2] = 0;
        tipoforma[2][3] = 0;
        tipoforma[3][3] = 0;

        /*       +
         *       +
         *      ++
         * */
        tipoforma[1][3] = 1;
        tipoforma[2][3] = 1;
        tipoforma[3][2] = 1;
        tipoforma[3][3] = 1;
        //formas[6] = new Forma(tipoforma, Color.YELLOW, PizarradeCeldas.BEHAVIOR_IS_FALLING);
        formas[6] = new Forma(tipoforma, colorJ, PizarradeCeldas.BEHAVIOR_IS_FALLING);
        tipoforma[1][3] = 0;
        tipoforma[2][3] = 0;
        tipoforma[3][2] = 0;
        tipoforma[3][3] = 0;
    }

    public void initmusic(){
        mp1=MediaPlayer.create(this, R.raw.mus1);
        mp2=MediaPlayer.create(this, R.raw.mus2);
        mp3=MediaPlayer.create(this, R.raw.mus3);
        mp4=MediaPlayer.create(this, R.raw.mus4);
        mp5=MediaPlayer.create(this, R.raw.mus5);
        mp6=MediaPlayer.create(this, R.raw.mus6);
        mp7=MediaPlayer.create(this, R.raw.mus7);
    }

    public void iniciarvistas(){

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        vibrador = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        volverjugar250ptos=findViewById(R.id.volverjugar250);
        volverjugar250ptos.setVisibility(View.INVISIBLE);
        vistaPiezaProxima=  findViewById(R.id.verPieza);
        finjuego=findViewById(R.id.finjuego);
        initmusic();

        TextView textView = findViewById(R.id.pierde);
        textView.setVisibility(View.INVISIBLE);
        TextView textView2 = findViewById(R.id.volverajugar);
        textView2.setVisibility(View.INVISIBLE);
        finjuego.setVisibility(View.INVISIBLE);
        botonizquierda=findViewById(R.id.botonizquierda);
        botonderecha=findViewById(R.id.botonderecha);
        botonderecha2=findViewById(R.id.derecha2);
        botonrapido=findViewById(R.id.abajorapido);
        botonabajo=findViewById(R.id.botonabajo);
        botonpausa=findViewById(R.id.botonpausa);
        giro=findViewById(R.id.girarpieza);
        reiniciarjuego=findViewById(R.id.reiniciojuego);
        reiniciarjuego.setVisibility(View.VISIBLE);
        bitmap = Bitmap.createBitmap(AnchuraPantalla, AlturaPantalla, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        bitmapnuevaforma = Bitmap.createBitmap(4, 4, Bitmap.Config.ARGB_8888);
        canvasnuevaforma = new Canvas(bitmapnuevaforma);
        vistaavatar=findViewById(R.id.avatarjugador);

        paint = new Paint();
        paintnueva=new Paint();
        linearLayout = findViewById(R.id.game_board);
        estadoActual = false;

        detectorDeGestos = new GestureDetectorCompat(this, this);
        detectorDeGestos.setOnDoubleTapListener(this);

        botonrapido.setVisibility(View.INVISIBLE);
        vistaavatar.setVisibility(View.VISIBLE);

        botonrapido.setVisibility(View.VISIBLE);
        //inicializa el sensor de proximidad para que posteriormente se pause el juego cuando lo detecte
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

    }

    public class PizarradeCeldas {
        public final static int BEHAVIOR_IS_FIXED = 2, BEHAVIOR_IS_FALLING = 1, BEHAVIOR_NOTHING = 0;
        private int estado, color, comportamiento;

        public PizarradeCeldas() {
            estado = 0;
            color = Color.rgb(244,242,205);
            comportamiento = BEHAVIOR_NOTHING;
        }

        public PizarradeCeldas(int estado, int color) {
            this.estado = estado;
            this.color = color;
            this.comportamiento = BEHAVIOR_NOTHING;
        }

        public PizarradeCeldas(int estado, int color, int comportamiento) {
            this.estado = estado;
            this.color = color;
            this.comportamiento = comportamiento;
        }

        public int getEstado() {
            return estado;
        }

        public int getColor() {
            return color;
        }

        public int getComportamiento() {
            return comportamiento;
        }

        public void setEstado(int estado) {
            this.estado = estado;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public void setComportamiento(int comportamiento) {
            this.comportamiento = comportamiento;
        }
    }

    public class Forma {
        public int x, y;
        boolean tipoformalarga=false;
        boolean tipoformacuadrado=false;
        boolean tipoforma5=false;
        boolean tipoforma2=false;
        boolean tipoformaT=false;
        boolean tipoformaL=false;
        boolean tipoformaJ=false;
        public PizarradeCeldas[][] mat = new PizarradeCeldas[5][5];

        Forma(int[][] _mat, int _color, final int comportamiento) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (_mat[i][j] != 1)
                        mat[i][j] = new PizarradeCeldas();
                    else
                        mat[i][j] = new PizarradeCeldas(_mat[i][j], _color, comportamiento);
                }
            }
        }

        void RotarLaPieza() {

            PizarradeCeldas[][] aux = new PizarradeCeldas[5][5];
                for (int i = 1; i < 5; i++) {
                    for (int j = 1; j < 5; j++) {
                        aux[4 - j + 1][i] = mat[i][j];
                    }
                }
                for (int i = 1; i < 5; i++) {
                    for (int j = 1; j < 5; j++) {
                        mat[i][j] = aux[i][j];
                    }
                }
        }
        void rlp(){

            PizarradeCeldas[][] aux = new PizarradeCeldas[5][5];
            for (int i=0 ; i<=4;i++){
                for(int j=0; j<4; j++){
                    aux[3-j][i]=mat[i][j];
                }
            }

            for (int i=0 ; i<=4;i++){
                for(int j=0 ;j<4; j++){
                    mat[i][j] = aux[i][j];
                }
            }
        }
    }
    public class CrearLaFormaextra {


        public CrearLaFormaextra(){

        }
        public void crearformaextra(){//extends thread o runable

            //Forma f=formas[random.nextInt(7)];;
            Forma f=formas[3];;
            if(boleanolinea==true){
                for(int i=0;i<5;i++)
                    for(int j=0;j<5;j++)
                        f.mat[i][j].setColor(Color.GRAY);
            }
            f.x = 0;
            f.y = 1 + (numeroColumnas - 6) / 2;
            // poner la nueva forma arriba del tablero si es posible
            for (int compensar = 0; compensar <= 3; ++compensar) {
                int i, m, j, n;
                boolean si = true;
                for (m = f.x + compensar, i = 1; i <= 4; i++, m++) {
                    for (n = f.y, j = 1; j <= 4; j++, n++) {
                        matrizDeJuego[m][n].setEstado(matrizDeJuego[m][n].getEstado() + f.mat[i][j].getEstado());
                        if (matrizDeJuego[m][n].getEstado() > 1) {
                            si = false;
                        }
                    }
                }
                if (si) {
                    for (i = 1, m = f.x + compensar; i <= 4; i++, m++) {
                        for (j = 1, n = f.y; j <= 4; j++, n++) {
                            if (f.mat[i][j].getEstado() == 1) {
                                matrizDeJuego[m][n].setColor(f.mat[i][j].getColor());
                                matrizDeJuego[m][n].setComportamiento(f.mat[i][j].getComportamiento());
                            }
                        }
                    }
                    f.x += compensar;
                    FijarMatrizJuego();
                } else {
                    for (m = f.x + compensar, i = 1; i <= 4; i++, m++) {
                        for (n = f.y, j = 1; j <= 4; j++, n++) {
                            matrizDeJuego[m][n].setEstado(matrizDeJuego[m][n].getEstado() - f.mat[i][j].getEstado());
                        }
                    }
                }
            }
            FijarMatrizJuego();
        }
       // @Override
        public void run() {
            this.crearformaextra();
        }
    }
}