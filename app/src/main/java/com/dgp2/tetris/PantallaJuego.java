package com.dgp2.tetris;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GestureDetectorCompat;

import java.util.Random;

public class PantallaJuego extends Activity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
/*CrearLaForma  esta tiene que verse antes de fijarse*/
    int numeroFilas = 20;
    int numeroColumnas = 14;
    final int AlturaPantalla = 800;
    final int AnchuraPantalla = 400;
    final Handler operaciones = new Handler();
    final Forma[] formas = new Forma[11];
    final int IR_DERECHA = 1;
    final int IR_ABAJO = 2;
    final int IR_IZQUIERDA = 3;
    int RAPIDEZNORMAL = 500;
    int RAPIDEZDEPRISA = 50;
    String dificultad, rapidez;
    int puntuacion;
    boolean juegoEnMarcha, juegoEnPausa, estadoVelocidadRapidez, estadoActual;
    private long TiempoDeEspera;

    final int dx[] = {-1, 0, 1, 0};
    final int dy[] = {0, 1, 0, -1};

    ImageView vistaPiezaProxima;

    private GestureDetectorCompat detectorDeGestos;

    Random random = new Random();

    BoardCell[][] matrizDeJuego;
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;
    LinearLayout linearLayout;

    Forma formaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_juego);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        vistaPiezaProxima=  findViewById(R.id.verPieza);

        dificultad = prefs.getString("difficulty_preference", "Normal");
        numeroFilas = Integer.parseInt(prefs.getString("num_rows_preference", "20")) + 6;
        numeroColumnas = Integer.parseInt(prefs.getString("num_columns_preference", "10")) + 6;
        rapidez = prefs.getString("speed_preference", "Normal");
        switch (rapidez) {
            case "Despacio": {
                RAPIDEZNORMAL = 1000;
                RAPIDEZDEPRISA = 100;
                break;
            }
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

        TextView textView = (TextView) findViewById(R.id.game_over_textview);
        textView.setVisibility(View.INVISIBLE);
        TextView textView2 = (TextView) findViewById(R.id.game_over_textview2);
        textView2.setVisibility(View.INVISIBLE);

        bitmap = Bitmap.createBitmap(AnchuraPantalla, AlturaPantalla, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        linearLayout = (LinearLayout) findViewById(R.id.game_board);
        puntuacion = 0;
        estadoActual = false;

        detectorDeGestos = new GestureDetectorCompat(this, this);
        detectorDeGestos.setOnDoubleTapListener(this);

        InicializadorDeFormas();

        InicializadorDeJuego();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (juegoEnMarcha) {
            juegoEnPausa = true;
            PintarMatriz();
        }
    }

    private void InicializadorDeFormas() {
        int[][] a = new int[5][5];

        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 5; j++) {
                a[i-1][j-1] = 0;
            }
        }

        /*      +
        *       +
        *       ++
        * */
        a[1][2] = a[1][3] = a[2][3] = a[3][3] = 1;
        formas[0] = new Forma(a, Color.rgb(255, 51, 249), BoardCell.BEHAVIOR_IS_FALLING);
        a[1][2] = a[1][3] = a[2][3] = a[3][3] = 0;


        /*      ++
         *       ++
         * */
        a[2][1] = a[2][2] = a[3][2] = a[3][3] = 1;
        formas[1] = new Forma(a, Color.rgb(248, 138, 17), BoardCell.BEHAVIOR_IS_FALLING);
        a[2][1] = a[2][2] = a[3][2] = a[3][3] = 0;


        /*       +
         *       +
         *       +
         *       +
         * */
        a[1][2] = a[2][2] = a[3][2] = a[4][2] = 1;
        formas[2] = new Forma(a, Color.RED, BoardCell.BEHAVIOR_IS_FALLING);
        a[1][2] = a[2][2] = a[3][2] = a[4][2] = 0;


        /*       ++
         *       ++
         * */
        a[2][2] = a[2][3] = a[3][2] = a[3][3] = 1;
        formas[3] = new Forma(a, Color.BLUE, BoardCell.BEHAVIOR_IS_FALLING, false);
        a[2][2] = a[2][3] = a[3][2] = a[3][3] = 0;


        /*      +++
         *       +
         * */
        a[1][2] = a[2][2] = a[2][3] = a[3][2] = 1;
        formas[4] = new Forma(a, Color.rgb(108, 230, 21), BoardCell.BEHAVIOR_IS_FALLING);
        a[1][2] = a[2][2] = a[2][3] = a[3][2] = 0;


        /*        ++
         *       ++
         * */
        a[1][2] = a[2][2] = a[2][3] = a[3][3] = 1;
        formas[5] = new Forma(a, Color.CYAN, BoardCell.BEHAVIOR_IS_FALLING);
        a[1][2] = a[2][2] = a[2][3] = a[3][3] = 0;


        /*       +
         *       +
         *      ++
         * */
        a[1][3] = a[2][3] = a[3][2] = a[3][3] = 1;
        formas[6] = new Forma(a, Color.YELLOW, BoardCell.BEHAVIOR_IS_FALLING);
        a[1][3] = a[2][3] = a[3][2] = a[3][3] = 0;

    }

    private void copiarMatriz(BoardCell[][] A, BoardCell[][] B) {
        for (int i = 1; i <= numeroFilas; i++) {
            for (int j = 1; j <= numeroColumnas; j++) {
                B[i-1][j-1] = new BoardCell(A[i-1][j-1].getState(), A[i-1][j-1].getColor(), A[i-1][j-1].getBehavior());
            }
        }
    }

    private void FijarMatrizJuego() {
        for (int i = 3; i < numeroFilas - 3; i++) {
            for (int j = 3; j < numeroColumnas - 3; j++) {
                if (matrizDeJuego[i][j].getState() == 0) {
                    matrizDeJuego[i][j].setColor(Color.BLACK);
                    matrizDeJuego[i][j].setBehavior(BoardCell.BEHAVIOR_NOTHING);
                    continue;
                }
                if (matrizDeJuego[i][j].getBehavior() == BoardCell.BEHAVIOR_IS_FIXED)
                    continue;
                if (matrizDeJuego[i][j].getBehavior() == BoardCell.BEHAVIOR_IS_FALLING) {
                    int x, y, m, n;
                    for (x = 1, m = formaActual.x; x <= 4; x++, m++) {
                        for (y = 1, n = formaActual.y; y <= 4; y++, n++) {
                            if (m == i && n == j) {
                                if (formaActual.mat[x][y].getState() == 0) {
                                    matrizDeJuego[i][j] = new BoardCell();
                                }
                            }
                        }
                    }
                    continue;
                }
                if (matrizDeJuego[i][j].getBehavior() == BoardCell.BEHAVIOR_NOTHING) {
                    int x, y, m, n;
                    for (x = 1, m = formaActual.x; x <= 4; x++, ++m) {
                        for (y = 1, n = formaActual.y; y <= 4; y++, n++) {
                            if (m == i && n == j) {
                                if (formaActual.mat[x][y].getState() == 1) {
                                    matrizDeJuego[i][j] = formaActual.mat[x][y];
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
        BoardCell[][] aux = new BoardCell[numeroFilas][];
        for (int i = 0; i < numeroFilas; i++)
            aux[i] = new BoardCell[numeroColumnas];
        copiarMatriz(matrizDeJuego, aux);
        int i, m, j, n;
        // eliminar la forma de la tabla
        for (m = formaAct.x, i = 1; i <= 4; i++, m++) {
            for (n = formaAct.y, j = 1; j <= 4; j++, n++) {
                if (formaAct.mat[i][j].getState() == 1) {
                    matrizDeJuego[m][n] = new BoardCell();
                }
            }
        }

        // intentar mover la forma a la direccion indicada
        for (m = formaAct.x + dx[direccion], i = 1; i <= 4; i++, m++) {
            for (n = formaAct.y + dy[direccion], j = 1; j <= 4; j++, n++) {
                matrizDeJuego[m][n].setState(matrizDeJuego[m][n].getState() + formaAct.mat[i][j].getState());
                if (formaAct.mat[i][j].getState() == 1) {
                    matrizDeJuego[m][n].setColor(formaAct.mat[i][j].getColor());
                    matrizDeJuego[m][n].setBehavior(formaAct.mat[i][j].getBehavior());
                }
                if (matrizDeJuego[m][n].getState() > 1) {
                    copiarMatriz(aux, matrizDeJuego);
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

    private boolean RotarIzquierda(Forma formaAct) {
        // copiar la matriz de juego en un auxiliar
        BoardCell[][] aux = new BoardCell[numeroFilas][];
        for (int i = 0; i < numeroFilas; i++)
            aux[i] = new BoardCell[numeroColumnas];
        copiarMatriz(matrizDeJuego, aux);
        int i, m, j, n;
        // eliminar la forma de la matriz de juego
        for (m = formaAct.x, i = 1; i <= 4; i++, m++) {
            for (n = formaAct.y, j = 1; j <= 4; j++, n++) {
                if (formaAct.mat[i][j].getState() == 1) {
                    matrizDeJuego[m][n] = new BoardCell();
                }
            }
        }
        // rotar la forma a la izquierda
        formaAct.RotarALaIzquierda();
        for (m = formaAct.x, i = 1; i <= 4; i++, m++) {
            for (n = formaAct.y, j = 1; j <= 4; j++, n++) {
                matrizDeJuego[m][n].setState(matrizDeJuego[m][n].getState() + formaAct.mat[i][j].getState());
                if (formaAct.mat[i][j].getState() == 1) {
                    matrizDeJuego[m][n].setColor(formaAct.mat[i][j].getColor());
                    matrizDeJuego[m][n].setBehavior(formaAct.mat[i][j].getBehavior());
                }
                if (matrizDeJuego[m][n].getState() > 1) {
                    copiarMatriz(aux, matrizDeJuego);
                    formaAct.RotarALaDerecha();
                    FijarMatrizJuego();
                    return false;
                }
            }
        }
        FijarMatrizJuego();
        return true;
    }

    private boolean RotarDerecha(Forma formaActua) {
        // copiar la matriz de juego en un auxiliar
        BoardCell[][] aux = new BoardCell[numeroFilas][];
        for (int i = 0; i < numeroFilas; i++)
            aux[i] = new BoardCell[numeroColumnas];
        copiarMatriz(matrizDeJuego, aux);
        int i, m, j, n;
        // eliminar la forma de la matriz de juego
        for (m = formaActua.x, i = 1; i <= 4; i++, m++) {
            for (n = formaActua.y, j = 1; j <= 4; j++, n++) {
                if (formaActua.mat[i][j].getState() == 1) {
                    matrizDeJuego[m][n] = new BoardCell();
                }
            }
        }
        // rotar la forma a la derecha
        formaActua.RotarALaDerecha();
        for (m = formaActua.x, i = 1; i <= 4; i++, m++) {
            for (n = formaActua.y, j = 1; j <= 4; j++, n++) {
                matrizDeJuego[m][n].setState(matrizDeJuego[m][n].getState() + formaActua.mat[i][j].getState());
                if (formaActua.mat[i][j].getState() == 1) {
                    matrizDeJuego[m][n].setColor(formaActua.mat[i][j].getColor());
                    matrizDeJuego[m][n].setBehavior(formaActua.mat[i][j].getBehavior());
                }
                if (matrizDeJuego[m][n].getState() > 1) {
                    copiarMatriz(aux, matrizDeJuego);
                    formaActua.RotarALaIzquierda();
                    FijarMatrizJuego();
                    return false;
                }
            }
        }
        FijarMatrizJuego();
        return true;
    }

    private boolean CrearLaForma() {
        // generar la forma actual y ponerla en la matriz

        if (dificultad.compareTo("Normal") == 0) {
            formaActual = formas[random.nextInt(7)];
        } else {
            formaActual = formas[random.nextInt(formas.length)];
        }
        // generar aleatoriamente el numero de rotaciones
        int number_of_rotations = random.nextInt(4);
        for (int i = 1; i <= number_of_rotations; i++) {
            formaActual.RotarALaDerecha();
        }
        formaActual.x = 0;
        formaActual.y = 1 + (numeroColumnas - 6) / 2;
        // poner la nueva forma arriba del tablero si es posible
        for (int compensar = 0; compensar <= 3; ++compensar) {
            int i, m, j, n;
            boolean si = true;
            for (m = formaActual.x + compensar, i = 1; i <= 4; i++, m++) {
                for (n = formaActual.y, j = 1; j <= 4; j++, n++) {
                    matrizDeJuego[m][n].setState(matrizDeJuego[m][n].getState() + formaActual.mat[i][j].getState());
                    if (matrizDeJuego[m][n].getState() > 1) {
                        si = false;
                    }
                }
            }
            if (si) {
                for (i = 1, m = formaActual.x + compensar; i <= 4; i++, m++) {
                    for (j = 1, n = formaActual.y; j <= 4; j++, n++) {
                        if (formaActual.mat[i][j].getState() == 1) {
                            matrizDeJuego[m][n].setColor(formaActual.mat[i][j].getColor());
                            matrizDeJuego[m][n].setBehavior(formaActual.mat[i][j].getBehavior());
                        }
                    }
                }
                formaActual.x += compensar;
                FijarMatrizJuego();
                return true;
            } else {
                for (m = formaActual.x + compensar, i = 1; i <= 4; i++, m++) {
                    for (n = formaActual.y, j = 1; j <= 4; j++, n++) {
                        matrizDeJuego[m][n].setState(matrizDeJuego[m][n].getState() - formaActual.mat[i][j].getState());
                    }
                }
            }
        }
        FijarMatrizJuego();
        return false;
    }

    private boolean Check() {
        int k = 0;
        boolean found = false;
        for (int i = numeroFilas - 4; i >= 3; --i) {
            boolean si = true;
            for (int j = 3; j < numeroColumnas - 3; j++) {
                if (matrizDeJuego[i][j].getState() == 0) {
                    si = false;
                }
            }
            if (si) {
                ++k;
                found = true;
            } else {
                if (k == 0)
                    continue;
                for (int j = 3; j < numeroColumnas - 3; j++) {
                    int state = matrizDeJuego[i][j].getState();
                    int color = matrizDeJuego[i][j].getColor();
                    int behavior = matrizDeJuego[i][j].getBehavior();
                    matrizDeJuego[i + k][j] = new BoardCell(state, color, behavior);
                }
            }
        }
        for (int pas = 0; pas < k; ++pas) {
            for (int j = 3; j < numeroColumnas - 3; j++) {
                matrizDeJuego[3 + pas][j] = new BoardCell();
            }
        }
        // Actualizar la puntuacion
        puntuacion += (k * (k + 30) );
        FijarMatrizJuego();
        return found;
    }

    void PintarMatriz() {

        // pintar el fondo del tablero
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, AnchuraPantalla, AlturaPantalla, paint);

        // Pintar la cuadricula del tablero
        paint.setColor(Color.WHITE);
        for (int i = 0; i <= (numeroFilas - 6); i++) {
            canvas.drawLine(0, i * (AlturaPantalla / (numeroFilas - 6)), AnchuraPantalla,
                    i * (AlturaPantalla / (numeroFilas - 6)), paint);
        }
        for (int i = 0; i <= (numeroColumnas - 6); i++) {
            canvas.drawLine(i * (AnchuraPantalla / (numeroColumnas - 6)), 0,
                    i * (AnchuraPantalla / (numeroColumnas - 6)), AlturaPantalla, paint);
        }

        // Pintar los bloques de tetris
        for (int i = 3; i < numeroFilas - 3; i++) {
            for (int j = 3; j < numeroColumnas - 3; j++) {
                if (matrizDeJuego[i][j].getState() == 1) {
                    paint.setColor(matrizDeJuego[i][j].getColor());
                    canvas.drawRect((j - 3) * (AnchuraPantalla / (numeroColumnas - 6)),
                            (i - 3) * (AlturaPantalla / (numeroFilas - 6)),
                            (j + 1 - 3) * (AnchuraPantalla / (numeroColumnas - 6)),
                            (i + 1 - 3) * (AlturaPantalla / (numeroFilas - 6)),
                            paint);
                }
            }
        }

        // Pintar los bloques de tetris
        for (int i = 3; i < numeroFilas - 3; i++) {
            for (int j = 3; j < numeroColumnas - 3; j++) {
                if (matrizDeJuego[i][j].getState() == 1) {
                    paint.setColor(Color.BLACK);
                    canvas.drawLine((j - 3) * (AnchuraPantalla / (numeroColumnas - 6)),
                            (i - 3) * (AlturaPantalla / (numeroFilas - 6)),
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
            TextView textView = (TextView) findViewById(R.id.game_over_textview);
            textView.setVisibility(View.VISIBLE);
            TextView textView2 = (TextView) findViewById(R.id.game_over_textview2);
            textView2.setVisibility(View.VISIBLE);
        } else if (juegoEnPausa) {
            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(60);
            canvas.drawText("GAME PAUSED", (float) (AnchuraPantalla / 2.0), (float) (AlturaPantalla / 2.0), paint);
        }

        // Mostrar el actual dibujo
        linearLayout.setBackgroundDrawable(new BitmapDrawable(bitmap));

        // Actualizar la puntuacion del textview
        TextView textView = (TextView) findViewById(R.id.game_score_textview);
        textView.setText("Score: " + puntuacion);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

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
                        if (formaActual.mat[x][y].getState() == 1) {
                            matrizDeJuego[i][j].setBehavior(BoardCell.BEHAVIOR_IS_FIXED);
                            formaActual.mat[x][y].setBehavior(BoardCell.BEHAVIOR_IS_FIXED);
                        }
                    }
                }
                estadoActual = false;
                Check(); //busca lineas completas
                estadoActual = CrearLaForma(); // crea otra figura
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
            } else
                PintarMatriz();

            if (estadoVelocidadRapidez)
                operaciones.postDelayed(this, RAPIDEZDEPRISA);
            else
                operaciones.postDelayed(this, RAPIDEZNORMAL);
        }
    };

    void CambiarEstadoVelocidadAcelerada(boolean mFastSpeedState) {
        // estadoVelocidadRapidez = false rapidez normal
        // estadoVelocidadRapidez = true rapidez acelerada
        operaciones.removeCallbacks(runnable);
        estadoVelocidadRapidez = mFastSpeedState;
        if (estadoVelocidadRapidez)
            operaciones.postDelayed(runnable, RAPIDEZDEPRISA);
        else
            operaciones.postDelayed(runnable, RAPIDEZNORMAL);
    }

    void InicializadorDeJuego() {

        // Crear la pizarra del juego
        matrizDeJuego = new BoardCell[numeroFilas][];
        for (int i = 0; i < numeroFilas; i++) {
            matrizDeJuego[i] = new BoardCell[numeroColumnas];
            for (int j = 0; j < numeroColumnas; j++) {
                matrizDeJuego[i][j] = new BoardCell();
            }
        }

        for (int j = 0; j < numeroColumnas; j++) {
            for (int i = 0; i <= 2; i++) {
                matrizDeJuego[i][j] = new BoardCell(1, Color.BLACK);
            }
            for (int i = numeroFilas - 3; i < numeroFilas; i++) {
                matrizDeJuego[i][j] = new BoardCell(1, Color.BLACK);
            }
        }

        for (int i = 0; i < numeroFilas; i++) {
            for (int j = 0; j <= 2; j++) {
                matrizDeJuego[i][j] = new BoardCell(1, Color.BLACK);
            }
            for (int j = numeroColumnas - 3; j < numeroColumnas; j++) {
                matrizDeJuego[i][j] = new BoardCell(1, Color.BLACK);
            }
        }

        for (int j = 3; j < numeroColumnas - 3; j++) {
            matrizDeJuego[numeroFilas - 4][j] = new BoardCell(matrizDeJuego[numeroFilas - 4][j].getState(), matrizDeJuego[numeroFilas - 4][j].getColor(), BoardCell.BEHAVIOR_IS_FIXED);
        }

        // Crear el bloque de tetris inicial
        estadoActual = CrearLaForma();

        // Empezar el juego
        juegoEnMarcha = true;
        juegoEnPausa = false;

        // Pintar la matrix inicial
        PintarMatriz();

        //Cambiar el estado de la velocidad
        CambiarEstadoVelocidadAcelerada(false);
    }

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
            finish();
            return true;
        }
        if (juegoEnPausa || !estadoActual)
            return false;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float width = size.x;
        float x = e.getX();
        if (x > width / 2.0) {
            // rotar a la izquierda
            RotarIzquierda(formaActual);
            PintarMatriz();
        } else {
            // rotar a la derecha
            RotarDerecha(formaActual);
            PintarMatriz();
        }
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
        if (!juegoEnMarcha)
            return false;
        try {
            float x1 = e1.getX();
            float y1 = e1.getY();

            float x2 = e2.getX();
            float y2 = e2.getY();

            double angle = getAngle(x1, y1, x2, y2);

            if (inRange(angle, 45, 135)) {
                if (juegoEnPausa)
                    juegoEnPausa = false;
                else {
                    juegoEnPausa = true;
                    PintarMatriz();
                }
            } else if (inRange(angle, 0, 45) || inRange(angle, 315, 360)) {
                if (juegoEnPausa || !estadoActual)
                    return false;
                MoverForma(IR_DERECHA, formaActual);
                PintarMatriz();
            } else if (inRange(angle, 225, 315)) {
                if (juegoEnPausa || !estadoActual)
                    return false;
                CambiarEstadoVelocidadAcelerada(true);
            } else {
                if (juegoEnPausa || !estadoActual)
                    return false;
                MoverForma(IR_IZQUIERDA, formaActual);
                PintarMatriz();
            }

        } catch (Exception e) {
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detectorDeGestos.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        if (TiempoDeEspera + 1500 > System.currentTimeMillis()) {
            Intent intent = new Intent(PantallaJuego.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Pulsa nuevamente para acabar", Toast.LENGTH_SHORT).show();
        }

        TiempoDeEspera = System.currentTimeMillis();
    }

    public double getAngle(float x1, float y1, float x2, float y2) {

        double rad = Math.atan2(y1 - y2, x2 - x1) + Math.PI;
        return (rad * 180 / Math.PI + 180) % 360;
    }

    private boolean inRange(double angle, float init, float end) {
        return (angle >= init) && (angle < end);
    }

    public class BoardCell {
        public final static int BEHAVIOR_IS_FIXED = 2, BEHAVIOR_IS_FALLING = 1, BEHAVIOR_NOTHING = 0;
        private int state, color, behavior;

        public BoardCell() {
            state = 0;
            color = Color.BLACK;
            behavior = BEHAVIOR_NOTHING;
        }

        public BoardCell(int state, int color) {
            this.state = state;
            this.color = color;
            this.behavior = BEHAVIOR_NOTHING;
        }

        public BoardCell(int state, int color, int behavior) {
            this.state = state;
            this.color = color;
            this.behavior = behavior;
        }

        public int getState() {
            return state;
        }

        public int getColor() {
            return color;
        }

        public int getBehavior() {
            return behavior;
        }

        public void setState(int state) {
            this.state = state;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public void setBehavior(int behavior) {
            this.behavior = behavior;
        }
    }

    public class Forma {
        public int x, y;
        public BoardCell[][] mat = new BoardCell[5][5];
        public boolean canRotate;

        Forma(int[][] _mat, int _color, final int behavior) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (_mat[i][j] == 1)
                        mat[i][j] = new BoardCell(_mat[i][j], _color, behavior);
                    else
                        mat[i][j] = new BoardCell();

                }
            }
            canRotate = true;
        }

        Forma(int[][] _mat, int _color, final int behavior, boolean _canRotate) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (_mat[i][j] == 1)
                        mat[i][j] = new BoardCell(_mat[i][j], _color, behavior);
                    else
                        mat[i][j] = new BoardCell();

                }
            }
            canRotate = _canRotate;
        }

        void RotarALaIzquierda() {
            if (!this.canRotate) {
                return;
            }

            BoardCell[][] aux = new BoardCell[5][5];
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

        void RotarALaDerecha() {
            if (!this.canRotate) {
                return;
            }

            BoardCell[][] aux = new BoardCell[5][5];
            for (int i = 1; i < 5; i++) {
                for (int j = 1; j < 5; j++) {
                    aux[j][4 - i + 1] = mat[i][j];
                }
            }
            for (int i = 1; i < 5; i++) {
                for (int j = 1; j < 5; j++) {
                    mat[i][j] = aux[i][j];
                }
            }
        }
    }
}