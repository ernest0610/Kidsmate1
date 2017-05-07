package com.example.ernest.kidsmate1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

public class Character_Info extends AppCompatActivity {

    DatabaseTestStub mDatabaseTestStub;

    ImageView imageView_characterPic;
    TextView textView_characterName;
    TextView textView_status;

    Button button_pet;
    Button button_alphabet;
    Button button_trophy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabaseTestStub = DatabaseTestStub.getInstance();
        setContentView(R.layout.character_info);

        imageView_characterPic = (ImageView) findViewById(R.id.imageView_characterPic);
        textView_characterName = (TextView) findViewById(R.id.textView_characterName);
        textView_status = (TextView) findViewById(R.id.textView_status);

        button_pet = (Button) findViewById(R.id.button_pet);
        button_alphabet = (Button) findViewById(R.id.button_alphabet);
        button_trophy = (Button) findViewById(R.id.button_trophy);

        button_pet.setEnabled(true);
        button_alphabet.setEnabled(true);
        button_trophy.setEnabled(true);
    }

    @Override
    protected void onStart(){
        super.onStart();
        button_pet.setText("pet : " + Integer.toString(mDatabaseTestStub.getCurrentPet()));
        textView_characterName.setText(mDatabaseTestStub.getCharacterCname());
        textView_status.setText("level:" + mDatabaseTestStub.getLevel() +
                "\nCurrentExp: " + mDatabaseTestStub.getCurrentExp() +
                "\nLevelUpExp: " + mDatabaseTestStub.getLevelUpExp() +
                "\nGame_BlankGuessing Stat: " + mDatabaseTestStub.getStatBlankGuessing() +
                "\nGame_ImageGuessing Stat: " + mDatabaseTestStub.getStatImageGuessing() +
                "\nGame_WordChain Stat: " + mDatabaseTestStub.getStatWordChain() +
                "\n");
    }

    protected void clicked_button_pet(View v) {
        Intent intent = new Intent(Character_Info.this, Character_Pet.class);
        startActivity(intent);
    }

    protected void clicked_button_alphabet(View v) {
        Intent intent = new Intent(Character_Info.this, Character_Alphabet.class);
        startActivity(intent);
    }

    protected void clicked_button_trophy(View v) {
        Intent intent = new Intent(Character_Info.this, Character_Trophy.class);
        startActivity(intent);
    }

}