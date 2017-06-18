package diogon.com.br.space1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by diogo and lucas on 18/06/17.
 */

public class Enemy {

    // bitmap para o inimigo
    // O bitmap se encontra na pasta res/drawable

    private Bitmap bitmap;

    // Cordenadas X e Y dos inimigos

    private int x;
    private int y;

    // Velocidade dos inimigos

    private int speed = 1;


    // coordenadas mínimas e máximas para manter o inimigo dentro da tela

    private int maxX;
    private int minX;
    private int maxY;
    private int minY;

    // Criando o retangulo que vai verificar a colisão entre as naves
    private Rect detectCollision;



    public Enemy(Context context, int screenX, int screenY) {

        //Pegando o Bitmap da pasta ./res/drawable
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);

        //Iniciando as cordenadas min e max

        maxX = screenX;
        maxY = screenY;
        minX =0;
        minY = 0;

        // Gerador de posições dos inimigos randomicamente
        Random generator = new Random();
        speed = generator.nextInt(6) +10;
        x = screenX;
        y = generator.nextInt(maxY) - bitmap.getHeight();

        // Inicializando o quadrado de Colisão
        detectCollision = new Rect(x,y, bitmap.getWidth(), bitmap.getHeight());




    }

    public void update (int playerSeep){

        // diminui a coordenada x para que o inimigo se mova da direita para a esquerda(Do final da tela para o começo)
        x -= playerSeep;
        x -= speed;

        //Quando o inimigo chega no final da tela esquerda (Lado do jogador)
        if(x < minX -bitmap.getWidth()){
            //Adiciona o inimigo novamente no lado direito da tela(Lado contrario do jogador)
            Random generator = new Random();
            speed = generator.nextInt(10) + 10;
            x = maxX;
            y = generator.nextInt(maxY) - bitmap.getHeight();

        }

        //Atualiza os pontos de colisão dos inimigos
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }
}
