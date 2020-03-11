package com.dgp2.tetris;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import android.graphics.Color;

import androidx.core.view.GestureDetectorCompat;

import java.util.Locale;
import java.util.Random;

import static android.util.Half.EPSILON;

public class PantallaJuego extends Activity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private GestureDetectorCompat detectorDeGestos;
    int AlturaPantalla = 800;
    int AnchuraPantalla = 400;
    int numeroFilas ;
    int numeroColumnas ;
    int numfilasmodifpreferences=20; /*si 40 tarda 28 segundos*/
    int numcolumnasasmodifpreferences=10; /*40*20*/
    int varcortar=-2;
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
    long tiempo50;
    long tiempo0;
    /*se inicializan los colores a jugar*/
    int colorJ;
    int color2;
    int color5;
    int colorl;
    int colori;
    int colort;
    int coloro;

    boolean boleanolinea=false;

    int RAPIDEZNORMAL = 500;
    int RAPIDEZDEPRISA = 50;
    boolean inicio;
    String rapidez;
    int puntuacion;
    boolean juegoEnMarcha, juegoEnPausa, estadoActual,p;
    private long TiempoDeEspera;
    ImageButton botonizquierda, botonderecha,botonpausa, botonabajo, botonrapido, reiniciarjuego;

    /*el que cada 30 segundos salga una ficha nueva solo seria posible si el numero de filas fuera mas grande
    * ya que tarda 8 segundos en llegar abajo*/

    final int dx[] = {-1, 0, 1, 0};
    final int dy[] = {0, 1, 0, -1};

    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorproximidad;

    ImageView vistaPiezaProxima;

    int tpj;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_juego);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        vibrador = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        inicio=false;
        estadoActual=false;



        vistaPiezaProxima=  findViewById(R.id.verPieza);
        numeroFilas=numfilasmodifpreferences+6;
        numeroColumnas=numcolumnasasmodifpreferences+6;
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

      /*  int colorL=(Integer.parseInt("#e615dc"));
        int colorJ=(Integer.parseInt("#e0e615"));
        int color2=(Integer.parseInt("#e68a15"));
        int color5=(Integer.parseInt("#15e6e6"));
        int color0=(Integer.parseInt("#2b15e6"));
        int colorI=(Integer.parseInt("#e61515"));
        int colorT=(Integer.parseInt("#6ce615"));


*/

        Bundle datos = this.getIntent().getExtras();

        if(datos != null) {
            tpj = datos.getInt("TipoPieza");
            nombrejugador=datos.getString("Nombre");
        }
        else{
            tpj=0;
            nombrejugador="Anonimo";
        }

        Toast.makeText(this, nombrejugador, Toast.LENGTH_SHORT).show();

        TextView textView = findViewById(R.id.pierde);
        textView.setVisibility(View.INVISIBLE);
        TextView textView2 = findViewById(R.id.volverajugar);
        textView2.setVisibility(View.INVISIBLE);
        botonizquierda=findViewById(R.id.botonizquierda);
        botonderecha=findViewById(R.id.botonderecha);
        botonrapido=findViewById(R.id.abajorapido);
        botonabajo=findViewById(R.id.botonabajo);
        botonpausa=findViewById(R.id.botonpausa);
        reiniciarjuego=findViewById(R.id.reiniciojuego);
        reiniciarjuego.setVisibility(View.VISIBLE);
        bitmap = Bitmap.createBitmap(AnchuraPantalla, AlturaPantalla, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        bitmapnuevaforma = Bitmap.createBitmap(4, 4, Bitmap.Config.ARGB_8888);
        canvasnuevaforma = new Canvas(bitmapnuevaforma);

        paint = new Paint();
        paintnueva=new Paint();
        linearLayout = findViewById(R.id.game_board);
        puntuacion = 0;
        estadoActual = false;

        detectorDeGestos = new GestureDetectorCompat(this, this);
        detectorDeGestos.setOnDoubleTapListener(this);

        botonrapido.setVisibility(View.INVISIBLE);

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

        /*      +
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

        botonrapido.setVisibility(View.VISIBLE);

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

        //inicializa el sensor de proximidad para que posteriormente se pause el juego cuando lo detecte

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

       // cambiarcoloraleatorio();
        if(sensor==null){
            //finish();
        }
      /*  Runnable proceso2 = new CrearLaFormaextra();

        new Thread(proceso2).start();*/


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

        reiniciarjuego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*al pulsar el boton se reinicia el juego si no te gusta como esta llendo la partida*/

                Intent intent = new Intent(PantallaJuego.this, PantallaJuego.class);
                intent.putExtra("Nombrevuelta", nombrejugador);
                startActivity(intent);
                finish();

            }
        });




        start();
    }
    CountDownTimer countDownTimer = new CountDownTimer(1000000000, 50000) {
        public void onTick(long millisUntilFinished) {
            //Toast.makeText(getBaseContext(), (String.format(Locale.getDefault(), "%d sec.", millisUntilFinished / 1000L)), Toast.LENGTH_SHORT).show();
            if(numfilasmodifpreferences>4) {
               /* numfilasmodifpreferences -= 2;
                numeroFilas = numfilasmodifpreferences + 6;*/
                varcortar=varcortar+2;
            }

        }

        public void onFinish() {

           // Toast.makeText(getBaseContext(), "Tiempo " + varcortar, Toast.LENGTH_SHORT).show();
            // numeroFilas=numeroFilas+2;
            //setnumerofilas();
            /*numeroFilas=numeroFilas-2;
            numfilasmodifpreferences=numfilasmodifpreferences-2;*/
        }
    }.start();

    CountDownTimer countDownTimer2 = new CountDownTimer(100000, 30000) {//poner  1000000000  y  30000
        public void onTick(long millisUntilFinished) {
            Toast.makeText(getBaseContext(), (String.format(Locale.getDefault(), "%d sec.", millisUntilFinished / 1000L)), Toast.LENGTH_SHORT).show();

         /*   CrearLaFormaextra clfe = new CrearLaFormaextra();
            clfe.crearformaextra();
            FijarMatrizJuego();*/
           // estadoVelocidadRapidez=true;
           // estadoActual= CrearLaForma();
            if(piezaextra==0&&p){
                piezaextra=1;
            }
            if(!p){
                p=true;
            }
        }

        public void onFinish() {

         //   Toast.makeText(getBaseContext(), "Tiempo " + varcortar, Toast.LENGTH_SHORT).show();

        }
    }.start();

    public void cambiarcoloracapon(){

       /* int colorJ=Color.YELLOW;
        int color2=Color.rgb(248, 138, 17);
        int color5=Color.CYAN;
        int colorl=Color.rgb(255, 51, 249);
        int colori=Color.RED;
        int colort=Color.rgb(108, 230, 21);
        int coloro=Color.BLUE;*/

    }

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
        if (juegoEnMarcha) {
            juegoEnPausa = true;
            PintarMatriz();
        }
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

    public void cambiarcoloraleatorio(){
        int a[]= new int[7];
        boolean a1[]=new boolean[7];
        int val;
        int cont=0;
        for(int i=0;i<7;i++){
            a1[i]=false;
        }
        while (cont<7){
            val = random.nextInt(7);
            if(a1[val]==false){
                a[cont]=val;
                a1[val]=true;
                cont++;
            }
        }
        switch (cont){
            case 0:
                switch (a[cont]){
                    case 0:
                        colorJ=Color.YELLOW;
                        break;
                    case 1:
                        color2=Color.rgb(248, 138, 17);
                        break;
                    case 2:
                        color5=Color.CYAN;
                        break;
                    case 3:
                        colorl=Color.rgb(255, 51, 249);
                        break;
                    case 4:
                        colort=Color.rgb(108, 230, 21);
                        break;
                    case 5:
                        colori=Color.RED;
                        break;
                    case 6:
                        coloro=Color.BLUE;
                        break;
                }
                break;
            case 1:
                switch (a[cont]){
                    case 0:
                        color2=Color.rgb(248, 138, 17);
                        break;
                    case 1:
                        colorJ=Color.YELLOW;
                        break;
                    case 2:
                        colorl=Color.rgb(255, 51, 249);
                        break;
                    case 3:
                        color5=Color.CYAN;
                        break;
                    case 4:
                        coloro=Color.BLUE;
                        break;
                    case 5:
                        colori=Color.RED;
                        break;
                    case 6:
                        colort=Color.rgb(108, 230, 21);
                        break;
                }
                break;
            case 2:
                switch (a[cont]){
                    case 0:
                        color5=Color.CYAN;
                        break;
                    case 1:
                        color2=Color.rgb(248, 138, 17);
                        break;
                    case 2:
                        colorJ=Color.YELLOW;
                        break;
                    case 3:
                        colorl=Color.rgb(255, 51, 249);
                        break;
                    case 4:
                        coloro=Color.BLUE;
                        break;
                    case 5:
                        colori=Color.RED;
                        break;
                    case 6:
                        colort=Color.rgb(108, 230, 21);
                        break;
                }
                break;
            case 3:
                switch (a[cont]){
                    case 0:
                        colort=Color.rgb(108, 230, 21);
                        break;
                    case 1:
                        color2=Color.rgb(248, 138, 17);
                        break;
                    case 2:
                        coloro=Color.BLUE;
                        break;
                    case 3:
                        colorl=Color.rgb(255, 51, 249);
                        break;
                    case 4:
                        colorJ=Color.YELLOW;
                        break;
                    case 5:
                        colori=Color.RED;
                        break;
                    case 6:
                        color5=Color.CYAN;
                        break;
                }
                break;
            case 4:
                switch (a[cont]){
                    case 0:
                        colori=Color.RED;
                        break;
                    case 1:
                        coloro=Color.BLUE;
                        break;
                    case 2:
                        color5=Color.CYAN;
                        break;
                    case 3:
                        colorl=Color.rgb(255, 51, 249);
                        break;
                    case 4:
                        colort=Color.rgb(108, 230, 21);
                        break;
                    case 5:
                        colorJ=Color.YELLOW;
                        break;
                    case 6:
                        color2=Color.rgb(248, 138, 17);
                        break;
                }
                break;
            case 5:
                switch (a[cont]){
                    case 0:
                        colorJ=Color.YELLOW;
                        break;
                    case 1:
                        colort=Color.rgb(108, 230, 21);
                        break;
                    case 2:
                        color5=Color.CYAN;
                        break;
                    case 3:
                        colorl=Color.rgb(255, 51, 249);
                        break;
                    case 4:
                        coloro=Color.BLUE;
                        break;
                    case 5:
                        colori=Color.RED;
                        break;
                    case 6:
                        color2=Color.rgb(248, 138, 17);
                        break;
                }
                break;
            case 6:
                switch (a[cont]){
                    case 0:
                        colorJ=Color.YELLOW;
                        break;
                    case 1:
                        colorl=Color.rgb(255, 51, 249);
                        break;
                    case 2:
                        color5=Color.CYAN;
                        break;
                    case 3:
                        colort=Color.rgb(108, 230, 21);
                        break;
                    case 4:
                        colori=Color.RED;
                        break;
                    case 5:
                        coloro=Color.BLUE;
                        break;
                    case 6:
                        color2=Color.rgb(248, 138, 17);
                        break;
                }
                break;
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

        /*Cambiar el movimiento para que se mueva uno a la derecha o a la izquierda
         si esta en los lados al moverse*/
       /* if(formaAct.y<1 ) {
            MoverForma(IR_DERECHA, formaAct);
        }
        else*/
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
                puntuacion+=30;
                encontrado = true;
                tpj=tpj+1;
                if(tpj>6){
                    tpj=1;
                }
                cambiarcolorconfiguracion();

                boleanolinea=true;
                for (int l = numeroFilas - 4; l >= 3; --l) {
                    for (int j = 3; j < numeroColumnas - 3; j++) {
                        matrizDeJuego[l][j].setColor(colorJ);
                    }}

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
        if(cont>1)
           tpj = random.nextInt(7);
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

           /* Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // despues de 2 segundos
                    Toast.makeText(getBaseContext(), "Comienza la nueva partida", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PantallaJuego.this, PantallaJuego.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);*/

           /* handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // despues de 5 segundos
                    Toast.makeText(getBaseContext(), "Vuelve a la pantalla principal ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PantallaJuego.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 5000);*/



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

    public void cambiarcolor(int colorcambiar){

        colorJ=Color.YELLOW;
        color2=Color.rgb(248, 138, 17);
        color5=Color.CYAN;
        colorl=Color.rgb(255, 51, 249);
        colori=Color.RED;
        colort=Color.rgb(108, 230, 21);
        coloro=Color.BLUE;
    }


    public void onSensorChanged(SensorEvent event) {
        // This time step's delta rotation to be multiplied by the current rotation
        // after computing it from the gyro sample data.
        if (timestamp != 0) {
            final float dT = (event.timestamp - timestamp) * NS2S;
            // Axis of the rotation sample, not normalized yet.
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];

            // Calculate the angular speed of the sample
            float omegaMagnitude = (float) Math.sqrt(axisX*axisX + axisY*axisY + axisZ*axisZ);

            // Normalize the rotation vector if it's big enough to get the axis
            if (omegaMagnitude > EPSILON) {
                axisX /= omegaMagnitude;
                axisY /= omegaMagnitude;
                axisZ /= omegaMagnitude;
            }

            // Integrate around this axis with the angular speed by the time step
            // in order to get a delta rotation from this sample over the time step
            // We will convert this axis-angle representation of the delta rotation
            // into a quaternion before turning it into the rotation matrix.
            float thetaOverTwo = (float) omegaMagnitude * dT / 2.0f;
            float sinThetaOverTwo = (float) Math.sin(thetaOverTwo);
            float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);
            deltaRotationVector[0] = sinThetaOverTwo * axisX;
            deltaRotationVector[1] = sinThetaOverTwo * axisY;
            deltaRotationVector[2] = sinThetaOverTwo * axisZ;
            deltaRotationVector[3] = cosThetaOverTwo;
        }
        timestamp = event.timestamp;
        float[] deltaRotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
        // User code should concatenate the delta rotation we computed with the current
        // rotation in order to get the updated rotation.
        // rotationCurrent = rotationCurrent * deltaRotationMatrix;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //long tiempo50=SystemClock.elapsedRealtime();
         /*   tiempo50=System.currentTimeMillis();
            if(tiempo50-tiempo0==500) {
                juegoEnPausa = true;
                Toast.makeText(getBaseContext(), "Tiempo ", Toast.LENGTH_SHORT).show();
                tiempo0=tiempo50;
            }*/
            if(piezaextra>0){
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // despues de 1.6 segundos
                        estadoActual = false;
                        estadoActual = CrearLaForma();

                        estadoVelocidadRapidez = true;

                       /* MoverForma(IR_ABAJO, formaActual);
                        MoverForma(IR_ABAJO, formaActual);
                        MoverForma(IR_ABAJO, formaActual);
                        MoverForma(IR_ABAJO, formaActual);
                        MoverForma(IR_DERECHA, formaActual);
                        MoverForma(IR_DERECHA, formaActual);
                        MoverForma(IR_DERECHA, formaActual);*/
                       // estadoActual = CrearLaForma();
                    }
                }, 1600);
                estadoVelocidadRapidez = false;
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


    /*Del paint como inspiracion para ver com ohacer algunas cosas
    *
    *     public boolean onTouchEvent (MotionEvent event){
            float x=event.getX();
            float y=event.getY();
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    touchStart(x,y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touchMove(x,y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touchUp();
                    invalidate();
                    break;
            }
            return true;
        }
        *
        *
    private void touchUp (){
        path.lineTo(valX,valY);
        canvas.drawPath(path,paint);
        path.reset();
    }

    protected void onDraw (Canvas canvas){
        //fondo
        canvas.drawColor(0xFFBBBBBB);
        //lo pintado
        canvas.drawBitmap(bitmap,0,0,null);
        //trazo actual
        canvas.drawPath(path,paint);
    }*/

    /*del nivel ideas dibujar cuadricula
    *
    * public void onDraw (Canvas lienzo){
        int lado, radio, radioPeq, trazo;

        lienzo.drawColor(Color.BLACK);

        lado = getResources().getConfiguration().screenHeightDp;

        radio=lado/2;

        radioPeq=lado/10;

        trazo=lado/100;

        Paint lapiz=new Paint();

        lapiz.setColor(Color.BLUE);

        lienzo.drawCircle((int)(radio*1.8), (int)(radio*2.8), (int)(radio*1.8), lapiz);

        lapiz.setColor(Color.GREEN);

        lienzo.drawCircle((int)(radio*1.8), (int)(radio*2.8), radio-trazo, lapiz);

        lapiz.setColor(Color.BLUE);

        lienzo.drawCircle((int)(radio*1.8), (int)(radio*2.8), radioPeq+trazo, lapiz);

        lapiz.setStrokeWidth(trazo);


        lienzo.drawLine((float)(radio*1.8), (float)(radio*1.8), (float)(radio*1.8), (float)(radio*4), lapiz);


        lienzo.drawLine(0, (float)(radio*2.8), (float)(lado*1.8), (float)(radio*2.8), lapiz);

        pincel.setColor(Color.RED);
        lienzo.drawCircle((float)(ejex*3),(float)(ejey*2),ejez+tamano,pincel);
    }*/

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
            Intent intent = new Intent(PantallaJuego.this, MainActivity.class);

            intent.putExtra("Puntuacion", puntuacion);
            intent.putExtra("Nombrevuelta", nombrejugador);

            startActivity(intent);
            finish();
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
            Intent intent = new Intent(PantallaJuego.this, MainActivity.class);
            //aqui devolver puntuacion y nombre usuario
            intent.putExtra("Puntuacion", puntuacion);
            intent.putExtra("Nombrevuelta", nombrejugador);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Pulsa nuevamente para volver", Toast.LENGTH_SHORT).show();
        }

        TiempoDeEspera = System.currentTimeMillis();
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

       /* void setcolorpieza(){

            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    mat[i][j].setColor();
                }
            }
        }*/

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