package com.example.ernest.kidsmate1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class Character_Pet extends AppCompatActivity {
    DatabaseTestStub mDatabaseTestStub;

    LinearLayout linearLayout_buttonScroll;

    Button buttonAdd;
    Button buttonConfirm;

    int selectedIndex;

    ArrayList<Button> buttonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_button_scroll_view);

        mDatabaseTestStub = DatabaseTestStub.getInstance();

        linearLayout_buttonScroll = (LinearLayout) findViewById(R.id.linearLayout_buttonScroll);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonConfirm = (Button) findViewById(R.id.buttonConfirm);

        buttonConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mDatabaseTestStub.setCurrentPetIndex(selectedIndex);
                onBackPressed(v);
            }
        });

        buttonAdd.setEnabled(false);
        buttonConfirm.setEnabled(true);

        selectedIndex = -1;

        buttonList = new ArrayList();

        final int listSize = mDatabaseTestStub.getPetListSize();
        if(listSize != 0) {
            for (int index = 0; index < listSize; index++) {
                final Button mButton = new Button(this);
                final int mint = index;
                mButton.setOnClickListener(new View.OnClickListener() {
                    Button mmButton = mButton;
                    int mindex = mint;
                    @Override
                    public void onClick(View v) {
                        if(selectedIndex >= 0 && selectedIndex < listSize) {
                            buttonList.get(selectedIndex).setEnabled(true);
                        }
                        mmButton.setEnabled(false);
                        selectedIndex = mindex;
                    }
                });
                mButton.setEnabled(false);
                buttonList.add(mButton);
            }
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        linearLayout_buttonScroll.removeAllViews();

        Button mButton;

        int listSize = mDatabaseTestStub.getPetListSize();
        if(listSize != 0) {
            for (int index = 0; index < listSize; index++) {
                if(mDatabaseTestStub.isPetAchieved(index)) {
                    mButton = buttonList.get(index);
                    mButton.setText(mDatabaseTestStub.getPetString(index));
                    mButton.setEnabled(true);
                    linearLayout_buttonScroll.addView(mButton);
                }
            }
        }
    }

    protected void onBackPressed(View v){
        super.onBackPressed();
    }
}