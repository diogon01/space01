package diogon.com.br.space1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by diogo and Lucas on 18/06/17.
 */

public class Boom {

    //bitmap da explosão
    private Bitmap bitmap;

    //cordenadas X e Y da explosão
    private int x;
    private int y;

    public Boom(Context context) {
        //Pegando a imagem de pasta ./res/drawable
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boom);
        //Setando a cordenada X e Y da explosão para fora da tela
        //Assim o objeto fica escondido e aparece somente na colisão
        x = -250;
        y = -250;
    }

    //Seter para tornar o X visível
    public void setX(int x) {
        this.x = x;
    }

    //Seter para tornar o y visível
    public void setY(int y) {
        this.y = y;
    }

    //Seter do bitmap da explosão
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    //Os getters do objeto de explosão
    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
