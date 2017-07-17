package diogon.com.br.space1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;


public class GameView extends SurfaceView implements Runnable {


    //a screenX holder
    int screenX;

    //to count the number of Misses
    int countMisses;

    //indicator that the enemy has just entered the game screen
    boolean flag;

    //Indicador de o jogo cabou
    private boolean isGameOver;

    //Variável boleana que indica se o player está jogando
    volatile boolean playing;

    //Definindo o Thread do jogo
    private Thread gameThread = null;

    //Definindo a nave como player(Jogador)
    private Player player;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    //Definindo as naves amigas
    private Friend friend;

    //Definindo a array de inimigos
    private Enemy enemies;

    //Definindo a explosão do jogo
    private Boom boom;

    //Adiciona 3 inimigos no começo do jogo
    private int enemyCount = 3;


    //Adicionando lista de Estrelas
    private ArrayList<Star> stars = new
            ArrayList<Star>();

    //Placar atual
    int score;

    //O melhor placar
    int highScore[] = new int[4];

    //Shared Prefernces to store the High Scores
    SharedPreferences sharedPreferences;

    //Declarando os sons do jogo
    static MediaPlayer gameOnSound;
    final MediaPlayer killedEnemysound;
    final MediaPlayer gameOversound;

    //Declarando o contexto
    Context context;


    //Class constructor
    public GameView(Context context, int screenX, int screenY) {
        super(context);

        gameOnSound = MediaPlayer.create(context, R.raw.gameon);
        this.killedEnemysound = MediaPlayer.create(context, R.raw.killedenemy);
        this.gameOversound = MediaPlayer.create(context, R.raw.gameover);

        //Seta o placar para zero no começo do jogo
        score = 0;

        sharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);

        //Inicializa a array de placares de acordo com os anteriores
        highScore[0] = sharedPreferences.getInt("score1", 0);
        highScore[1] = sharedPreferences.getInt("score2", 0);
        highScore[2] = sharedPreferences.getInt("score3", 0);
        highScore[3] = sharedPreferences.getInt("score4", 0);

        player = new Player(context, screenX, screenY);
        surfaceHolder = getHolder();
        paint = new Paint();

        this.screenX = screenX;

        countMisses = 0;

        isGameOver = false;


        //Adicionando cem estrelas no começo do jogo
        int starNums = 100;
        for (int i = 0; i < starNums; i++) {
            Star s = new Star(screenX, screenY);
            stars.add(s);
        }

        enemies = new Enemy(context, screenX, screenY);

        //Inicialização da explosão no jogo
        boom = new Boom(context);

        //Inicialização da classe do amigo
        friend = new Friend(context, screenX, screenY);

        //Inicializando a música do jogo
        gameOnSound.start();

        this.context = context;

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

        //Adiciona placar conforme o tempo passa
        score++;
        //Atualiza de acordo o metodo update do jogador
        player.update();

        //Setando a explosão fora da tela do jogo
        boom.setX(-250);
        boom.setY(-250);

        // Atualizando as estrelas de acordo com a velocidade da nave(Jogador)
        for (Star s : stars) {
            s.update(player.getSpeed());
        }

        // definindo a flag  para TRUE quando o inimigo estiver no jogo
        if (enemies.getX() == screenX) {
            flag = true;
        }

        enemies.update(player.getSpeed());

        //Verifica se a nave bateu com o player
        if (Rect.intersects(player.getDetectCollision(), enemies.getDetectCollision())) {
            //Carrega a imagem de explosão
            boom.setX(enemies.getX());
            boom.setY(enemies.getY());

            //Adicionando som de que matou inimigo
            killedEnemysound.start();

            //will play a sound at the collision between player and the enemy
            enemies.setX(-200);


        } else {
            // se o inimigo acabou de entrar
            if (flag) {
                // Se a coordenada x do jogador é mais do que a coordenada x dos inimigos. O inimigo
                // acabou de atravessar o jogador
                if (player.getDetectCollision().exactCenterX() >= enemies.getDetectCollision().exactCenterX()) {
                    //incrementa esquivas
                    countMisses++;
                    // configurando a bandeira falso para que a outra parte seja executada somente
                    // quando o novo inimigo entra na tela
                    flag = false;
                    // se as esquivas é igual a 3, o jogo acabou.
                    if (countMisses == 3) {
                        //Muda para falço o playing, assim parando o jogo
                        playing = false;
                        isGameOver = true;

                        //Parando a música do jogo
                        gameOnSound.stop();
                        //Inicia a música de game over
                        gameOversound.start();

                        //Atualizando as novas pontuações na Array de Escores
                        for (int i = 0; i < 4; ++i) {
                            if (highScore[i] < score) {
                                final int finalI = i;
                                highScore[i] = score;
                                break;
                            }
                        }

                        // armazenando as pontuações através de preferências compartilhadas
                        SharedPreferences.Editor e = sharedPreferences.edit();
                        for (int i = 0; i < 4; i++) {
                            int j = i + 1;
                            e.putInt("score" + j, highScore[i]);
                        }
                        e.apply();
                    }
                }
            }
        }

        //Atualizando as cordenadas do amigo
        friend.update(player.getSpeed());

        //Verifica se teve colisão entre o jogador e a nave amiga
        if (Rect.intersects(player.getDetectCollision(), friend.getDetectCollision())) {
            //Chama as explosões nas colisões
            boom.setX(friend.getX());
            boom.setY(friend.getY());
            //Muda playing para falso e para o jogo
            playing = false;
            //Muda pra game over e finaliza o jogo
            isGameOver = true;

            //Parando a música do jogo
            gameOnSound.stop();
            //Inicia a música de game over
            gameOversound.start();

            // Atribuindo as pontuações à matriz de números de pontuação elevada
            for (int i = 0; i < 4; i++) {
                if (highScore[i] < score) {

                    final int finalI = i;
                    highScore[i] = score;
                    break;
                }
            }
            // Armazenando as pontuações através de preferências compartilhadas
            SharedPreferences.Editor e = sharedPreferences.edit();
            for (int i = 0; i < 4; i++) {
                int j = i + 1;
                e.putInt("score" + j, highScore[i]);
            }
            e.apply();
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

            //Desenhando o placar do jogador
            paint.setTextSize(30);
            canvas.drawText("Score" + score, 100, 50, paint);

            //Desenhando a nave(Jogador)
            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint
            );

            //Desenhando os inimigos
            canvas.drawBitmap(
                    enemies.getBitmap(),
                    enemies.getX(),
                    enemies.getY(),
                    paint
            );

            //Desenhando a explosão
            canvas.drawBitmap(
                    boom.getBitmap(),
                    boom.getX(),
                    boom.getY(),
                    paint
            );

            //Desenhando os amigos
            canvas.drawBitmap(
                    friend.getBitmap(),
                    friend.getX(),
                    friend.getY(),
                    paint
            );

            //Desenhando fim do jogo
            if (isGameOver) {
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);

                int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Game Over", canvas.getWidth() / 2, yPos, paint);
            }

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

    //stop the music on exit
    public static void stopMusic(){
        gameOnSound.stop();
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
        //Se acabou o jogo a tela te manda para a MainActive
        if(isGameOver){
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                context.startActivity(new Intent(context, MainActivity.class));
            }
        }

        return true;
    }


}
