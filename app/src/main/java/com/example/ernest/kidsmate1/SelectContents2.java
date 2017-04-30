package com.example.ernest.kidsmate1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SelectContents2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_contents2);
    }

    @Override
    public void onBackPressed() {
    }

    protected void clicked_todaysWord(View v) {
        Intent intent = new Intent(SelectContents2.this, TodayWord.class);
        startActivity(intent);
    }

    protected void clicked_dictionary(View v) {
        Intent intent = new Intent(SelectContents2.this, Dictionary.class);
        startActivity(intent);
    }

    protected void clicked_game_blank(View v) {
        Intent intent = new Intent(SelectContents2.this, Blank.class);
        startActivity(intent);
    }

    protected void clicked_game_image(View v) {
        Intent intent = new Intent(SelectContents2.this, Image.class);
        startActivity(intent);
    }

    protected void clicked_game_wordChain(View v) {
        Intent intent = new Intent(SelectContents2.this, WordChain.class);
        startActivity(intent);
    }

    protected void clicked_todaysWord2(View v) {
        Intent intent = new Intent(SelectContents2.this, Game_todaysWord.class);
        startActivity(intent);
    }

    protected void clicked_game_wordChain2(View v) {
        Intent intent = new Intent(SelectContents2.this, Game_WordChain.class);
        startActivity(intent);
    }

    protected void clicked_main(View v) {
        Intent intent = new Intent(SelectContents2.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
