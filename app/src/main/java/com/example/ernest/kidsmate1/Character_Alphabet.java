package com.example.ernest.kidsmate1;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class Character_Alphabet extends AppCompatActivity {

    DatabaseTestStub mDatabaseTestStub;

    Context context;

    LinearLayout linearLayout_buttonScroll;

    int petButtonListSize;

    int selectedButtonIndex;

    ArrayList<Button> petButtonList;
    ArrayList<Integer> petList;

    Button buttonAdd;
    Button buttonConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_button_scroll_view);

        mDatabaseTestStub = DatabaseTestStub.getInstance();

        context = this;

        linearLayout_buttonScroll = (LinearLayout) findViewById(R.id.linearLayout_buttonScroll);

        petButtonListSize = 0;

        selectedButtonIndex = mDatabaseTestStub.getCurrentPet();

        petButtonList = new ArrayList(6);


        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonConfirm = (Button) findViewById(R.id.buttonConfirm);

        buttonAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                petButtonListSize++;
                final Button button = new Button(context);
                button.setText(Integer.toString(petButtonListSize));
                petButtonList.add(button);
                button.setOnClickListener(new View.OnClickListener() {
                    Button mButton = button;
                    int index = petButtonListSize-1;
                    @Override
                    public void onClick(View v) {
                        petButtonList.get(selectedButtonIndex).setEnabled(true);
                        mButton.setEnabled(false);
                        selectedButtonIndex = index;
                    }
                });
                linearLayout_buttonScroll.addView(button);
                mDatabaseTestStub.addPet(petButtonListSize);
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mDatabaseTestStub.setCurrentPet(selectedButtonIndex+1);
                onBackPressed(v);
            }
        });

        buttonAdd.setEnabled(true);
        buttonConfirm.setEnabled(true);

        petList = mDatabaseTestStub.getPetList();
        if(petList.size() != 0) {
            for (int petcode : petList) {
                petButtonListSize++;
                final Button button = new Button(this);
                button.setText(Integer.toString(petcode));
                petButtonList.add(button);
                button.setOnClickListener(new View.OnClickListener() {
                    Button mButton = button;
                    int index = petButtonListSize-1;
                    @Override
                    public void onClick(View v) {
                        petButtonList.get(selectedButtonIndex).setEnabled(true);
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