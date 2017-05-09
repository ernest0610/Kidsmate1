package com.example.ernest.kidsmate1;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
    private MyButton pass;
    private MyButton play;
    private MyButton submit;
    private MyButton input;
    private MyTextView quiz;
    private MyTextView hint;
    private MyImageView enemy;
    private MyImageView imageQuiz;

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
        mic.setText("시작");
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "시작", Toast.LENGTH_SHORT).show();
                mic.setText("연결됨");
                mic.setEnabled(false);
            }
        });

        pass = gameView.getPass();
        pass.setImage(R.drawable.pass);
        pass.setText("패스!");
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "패스!", Toast.LENGTH_SHORT).show();
            }
        });

        play = gameView.getPlay();
        play.setImage(R.drawable.play);
        play.setText("재생");
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "재생", Toast.LENGTH_SHORT).show();
            }
        });

        submit = gameView.getSubmit();
        submit.setImage(R.drawable.submit);
        submit.setText("제출");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "제출", Toast.LENGTH_SHORT).show();
                input.setText("");
            }
        });

        input = gameView.getInput();
        input.setImage(R.drawable.input);

        //quiz = gameView.getQuizBlankGuessing();
        //quiz.setText("apple");

        hint = gameView.getHint();
        hint.setText("aaaaaaaaaaaaaaaaaaaaaaaa\nbbbbbbbbbbbbbbbbbbbbbbbb\ncccccccccccccccccc\n\ndddddddddddddddddddddddddd");

        enemy = gameView.getEnemy();
        enemy.setImage(R.drawable.bosssphinx);

        imageQuiz = gameView.getQuizImageGuessing();
        Drawable drawable = getResources().getDrawable(R.drawable.testwordchain);
        imageQuiz.setBackground(drawable);
    }
}