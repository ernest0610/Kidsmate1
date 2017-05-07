package com.example.ernest.kidsmate1;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Random;

public class Character_Alphabet extends AppCompatActivity {

    DatabaseTestStub mDatabaseTestStub;

    Context context;

    LinearLayout linearLayout_buttonScroll;

    int mListSize; // 수정!

    int selectedButtonIndex;

    ArrayList<Button> buttonList; // 수정!
    ArrayList<Character> mList; // 수정!

    Button buttonAdd;
    Button buttonConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_button_scroll_view);

        mDatabaseTestStub = DatabaseTestStub.getInstance();

        context = this;

        linearLayout_buttonScroll = (LinearLayout) findViewById(R.id.linearLayout_buttonScroll);

        mListSize = 0;

        //selectedButtonIndex = mDatabaseTestStub.getCurrentPet(); // 수정!

        buttonList = new ArrayList(6);


        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonConfirm = (Button) findViewById(R.id.buttonConfirm);

        buttonAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Random random = new Random();
                String atoz = "abcdefghijklmnopqrstuvwxyz";
                Character randomCh = atoz.charAt(random.nextInt(atoz.length()));

                mDatabaseTestStub.addAlphabet(randomCh); // 수정!

                mListSize++;
                final Button button = new Button(context);
                button.setText(randomCh);
                buttonList.add(button);
                button.setOnClickListener(new View.OnClickListener() {
                    Button mButton = button;
                    int index = mListSize-1;
                    @Override
                    public void onClick(View v) {
                        buttonList.get(selectedButtonIndex).setEnabled(true);
                        mButton.setEnabled(false);
                        selectedButtonIndex = index;
                    }
                });
                linearLayout_buttonScroll.addView(button);
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //mDatabaseTestStub.setCurrentPet(selectedButtonIndex+1); // 수정!
                onBackPressed(v);
            }
        });

        buttonAdd.setEnabled(true);
        buttonConfirm.setEnabled(true);

        mList = mDatabaseTestStub.getAlphabetList(); // 수정!
        if(mList.size() != 0) {
            for (char ch : mList) {
                mListSize++;
                final Button button = new Button(this);
                button.setText(ch);
                buttonList.add(button);
                button.setOnClickListener(new View.OnClickListener() {
                    Button mButton = button;
                    int index = mListSize-1;
                    @Override
                    public void onClick(View v) {
                        buttonList.get(selectedButtonIndex).setEnabled(true);
                        mButton.setEnabled(false);
                        selectedButtonIndex = index;
                    }
                });
                linearLayout_buttonScroll.addView(button);
            }
        }
    }
    @Override
    protected void onStart(){
        super.onStart();
    }

    protected void onBackPressed(View v){
        super.onBackPressed();
    }
}