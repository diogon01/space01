package diogon.com.br.space1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable {

    //Variável boleana que indica se o player está jogando
    volatile boolean playing;

    //Definindo o Thread do jogo
    private Thread gameThread = null;

    //Definindo a nave como player(Jogador)
    private Player player;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;


    //Definindo a array de inimigos
    private Enemy[] enemies;

    //Definindo a explosão do jogo
    private Boom boom;

    //Adiciona 3 inimigos no começo do jogo
    private int enemyCount = 3;


    //Adicionando lista de Estrelas
    private ArrayList<Star> stars = new
            ArrayList<Star>();

    //Class constructor
    public GameView(Context context, int screenX, int screenY) {
        super(context);

        player = new Player(context, screenX, screenY);
        surfaceHolder = getHolder();
        paint = new Paint();


        //Adicionando cem estrelas no começo do jogo
        int starNums = 100;
        for (int i = 0; i < starNums; i++) {
            Star s = new Star(screenX, screenY);
            stars.add(s);
        }

        //Inicializando a Array de inimigos
        enemies = new Enemy[enemyCount];
        for (int i = 0; i < enemyCount; i++) {
            enemies[i] = new Enemy(context, screenX, screenY);
        }

        //Inicialização da explosão no jogo
        boom = new Boom(context);

    }

    @Override
    public void run() {
        //Mecanica do jogo, enquando o aplicativo esta ativo(RUN) o While é TRU
        while (playing) {
            //Atualização do jogo
            update();

            //Desenho que vão ser adicionado no loop
            draw();

            //Controle(Joystick) do jogador
            control();
        }

    }

    private void update() {
        //Atualiza de acordo o metodo update do jogador
        player.update();

        //Setando a explosão fora da tela do jogo
        boom.setX(-250);
        boom.setY(-250);

        // Atualizando as estrelas de acordo com a velocidade da nave(Jogador)
        for (Star s : stars) {
            s.update(player.getSpeed());
        }

        //Atualizando a coordenada do inimigo em relação à velocidade da nave(Jogador)
        for (int i = 0; i < enemyCount; i++) {
            enemies[i].update(player.getSpeed());
            //Verificando se os inimigos colidiram com a nave(Jogador)
            if (Rect.intersects(player.getDetectCollision(), enemies[i].getDetectCollision())) {

                //Mostra as explosões na posição onde o inimigo se encontra
                boom.setX(enemies[i].getX());
                boom.setY(enemies[i].getY());

                //Move o inimigo na margem esquerda
                enemies[i].setX(-200);
            }
        }

    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            //Adicionando a cor branca nas estrelas
            paint.setColor(Color.WHITE);

            //Desenhando as estrelas
            for (Star s : stars) {
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }

            //Desenhando a nave(Jogador)
            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint
            );

            //Desenhando os inimigos
            for (int i = 0; i < enemyCount; i++) {
                canvas.drawBitmap(
                        enemies[i].getBitmap(),
                        enemies[i].getX(),
                        enemies[i].getY(),
                        paint
                );
            }

            //Desenhando a explosão
            canvas.drawBitmap(
                    boom.getBitmap(),
                    boom.getX(),
                    boom.getY(),
                    paint
            );


            surfaceHolder.unlockCanvasAndPost(canvas);
        }

    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        //when the game is paused
        //setting the variable to false
        playing = false;
        try {
            //stopping the thread
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        //when the game is resumed
        //starting the thread again
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                //When the user presses on the screen
                //we will do something here
                player.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                //When the user releases the screen
                //do something here
                player.setBoosting();
                break;
        }
        return true;
    }

}
