package diogon.com.br.space1;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Botão de start do jogo
    private ImageButton buttonPlay;

    //Botão dos Records
    private ImageButton buttonScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting the orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //getting the button
        buttonPlay = (ImageButton) findViewById(R.id.buttonPlay);

        //get no Botão dos Escores
        buttonScore = (ImageButton) findViewById(R.id.buttonScore);

        //Adicionando Listener no botão do Score
        buttonScore.setOnClickListener(this);

        //adding a click listener
        buttonPlay.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v == buttonPlay) {
            //Transição da Main Activity para o Game View
            startActivity(new Intent(MainActivity.this, GameActivity.class));
        } else if (v == buttonScore) {
            //Transição da Main Activity para a View HighScore Activity
            startActivity(new Intent(MainActivity.this, HighScore.class));
        }

    }
}
