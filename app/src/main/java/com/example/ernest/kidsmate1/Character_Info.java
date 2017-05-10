package com.example.ernest.kidsmate1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

public class Character_Info extends AppCompatActivity {

    DatabaseTestStub mDatabaseTestStub;
    DatabaseStateManager mDatabaseStateManager;

    ImageView imageView_characterPic;
    TextView textView_characterName;
    TextView textView_status;

    Button button_pet;
    Button button_alphabet;
    Button button_trophy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabaseStateManager = DatabaseStateManager.getInstance();

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

        mDatabaseTestStub = DatabaseTestStub.getInstance();
    }

    @Override
    protected void onResume(){
        super.onResume();
        int petIndex = mDatabaseTestStub.getCurrentPetIndex();
        if(petIndex < 0){
            button_pet.setText("pet: 선택안함");
        }else {
            button_pet.setText("pet: " + mDatabaseTestStub.getPetString(petIndex));
        }
        textView_characterName.setText(mDatabaseStateManager.getCurrentCname());
        textView_status.setText("레벨:" + mDatabaseStateManager.getCharacterLevel() +
                "\n현재 경험치: " + mDatabaseStateManager.getCharacterExp() +
                "\n레벨업 경험치: " + mDatabaseStateManager.getLevelUpExp() +
                "\n빈칸 맞추기 맞춘 총 문제 수(Luck): " + mDatabaseStateManager.getCharacterLuck() +
                "\n그림 맞추기 맞춘 총 문제 수(Power): " + mDatabaseStateManager.getCharacterPower() +
                "\n끝말잇기 맞춘 총 문제 수(Smart): " + mDatabaseStateManager.getCharacterSmart() +
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

    protected void clicked_button_character_change(View v) {
        Intent intent = new Intent(Character_Info.this, Character_Change.class);
        startActivity(intent);
    }

    protected void clicked_button_character_promote(View v) {
        Intent intent = new Intent(Character_Info.this, Character_Promote.class);
        startActivity(intent);
    }
}
