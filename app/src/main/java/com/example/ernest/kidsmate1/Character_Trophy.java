package com.example.ernest.kidsmate1;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class Character_Trophy extends AppCompatActivity {

    DatabaseTestStub mDatabaseTestStub;

    Context context;

    LinearLayout linearLayout_buttonScroll;

    //int petButtonListSize;

    //int selectedButtonIndex;

    ArrayList<Button> trophyButtonList;
    ArrayList<String> trophyList;

    Button buttonAdd;
    Button buttonConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_button_scroll_view);

        mDatabaseTestStub = DatabaseTestStub.getInstance();

        context = this;

        linearLayout_buttonScroll = (LinearLayout) findViewById(R.id.linearLayout_buttonScroll);

        trophyButtonList = new ArrayList(7);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonConfirm = (Button) findViewById(R.id.buttonConfirm);

        buttonAdd.setEnabled(false);
        buttonConfirm.setEnabled(false);

        trophyList = mDatabaseTestStub.getTrophyList();
        if(trophyList.size() != 0) {
            for (String str : trophyList) {
                final Button button = new Button(this);
                button.setText(str);
                trophyButtonList.add(button);
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
