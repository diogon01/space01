package diogon.com.br.space1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by diogo on 17/06/17.
 */

public class Player {

    private Bitmap bitmap;

    private int x;
    private int y;
    private int speed = 0;

    private boolean boosting;

    private final int GRAVITI = -10;

    private int maxY;
    private int minY;

    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;



    public Player(Context context, int screenX, int screenY){

        x = 75;
        y = 50;
        speed = 1;

        bitmap = BitmapFactory.decodeResource(context.getResources(),   R.drawable.player);

        boosting = false;

        maxY = screenY - bitmap.getHeight();

        minY = 0;

        boosting = false;

    }

    public void setBoosting() {
        boosting = true;
    }

    public void stopBoosting() {
        boosting = false;
    }

    //Method to update coordinate of character
    public void update(){
        if(boosting){
            speed +=2;
        }else {
            speed -= 5;
        }

        if(speed > MAX_SPEED){
            speed = MAX_SPEED;
        }

        if(speed < MIN_SPEED){
            speed = MIN_SPEED;
        }

        y -= speed + GRAVITI;

        if(y < minY){
            y = minY;
        }

        if(y > maxY){
            y = maxY;
        }

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
