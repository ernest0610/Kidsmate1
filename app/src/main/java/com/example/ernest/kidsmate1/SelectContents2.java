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
        super.onBackPressed();
    }

    protected void clicked_Feature_Dictionary(View v) {
        Intent intent = new Intent(SelectContents2.this, Feature_Dictionary.class);
        startActivity(intent);
    }

    protected void clicked_Feature_TodaysWord(View v) {
        Intent intent = new Intent(SelectContents2.this, Feature_TodaysWord.class);
        startActivity(intent);
    }

    protected void clicked_Game_BlankGuessing(View v) {
        Intent intent = new Intent(SelectContents2.this, Game_BlankGuessing.class);
        startActivity(intent);
    }

    protected void clicked_Game_ImageGuessing(View v) {
        Intent intent = new Intent(SelectContents2.this, Game_ImageGuessing.class);
        startActivity(intent);
    }

    protected void clicked_Game_WordChain(View v) {
        Intent intent = new Intent(SelectContents2.this, Game_WordChain.class);
        startActivity(intent);
    }

    protected void clicked_Test_WordChain(View v) {
        Intent intent = new Intent(SelectContents2.this, Test_WordChain.class);
        startActivity(intent);
    }

    protected void clicked_Character_Info(View v) {
        Intent intent = new Intent(SelectContents2.this, Character_Info.class);
        startActivity(intent);
    }
}