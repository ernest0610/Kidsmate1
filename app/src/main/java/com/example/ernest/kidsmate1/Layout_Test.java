package com.example.ernest.kidsmate1;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by User on 2017-05-07.
 */

public class Layout_Test extends AppCompatActivity {
    private Test_GameView gameView;
    private MyImageView myCharacter;
    private MyButton mic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new Test_GameView(this);
        setContentView(gameView);

        gameView.setBackgroundResource(R.drawable.imagegamebackground);

        myCharacter = gameView.getMyCharacter();
        myCharacter.setImage(R.drawable.magumsa);

        mic = gameView.getMic();
        mic.setImage(R.drawable.buttonmic);
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "시작", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
