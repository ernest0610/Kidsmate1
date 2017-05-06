package com.example.ernest.kidsmate1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.*;

public class Character_Info extends AppCompatActivity {

    ImageView imageView_characterPic;
    TextView textView_characterName;
    TextView textView_status;

    Button button_pet;
    Button button_alphabet;
    Button button_trophy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.character_info);
    }

}
