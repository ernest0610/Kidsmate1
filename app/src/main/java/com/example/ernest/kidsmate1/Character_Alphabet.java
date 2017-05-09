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

    LinearLayout linearLayout_buttonScroll;

    Button buttonAdd;
    Button buttonConfirm;

    Button mButton1;
    Button mButton2;
    Button mButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_button_scroll_view);

        mDatabaseTestStub = DatabaseTestStub.getInstance();

        linearLayout_buttonScroll = (LinearLayout) findViewById(R.id.linearLayout_buttonScroll);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonConfirm = (Button) findViewById(R.id.buttonConfirm);

        buttonAdd.setEnabled(false);
        buttonConfirm.setEnabled(false);

        mButton1 = new Button(this);
        mButton1.setText(mDatabaseTestStub.getUserAlpha_griffin());
        linearLayout_buttonScroll.addView(mButton1);

        mButton2 = new Button(this);
        mButton2.setText(mDatabaseTestStub.getUserAlpha_kerberos());
        linearLayout_buttonScroll.addView(mButton2);

        mButton3 = new Button(this);
        mButton3.setText(mDatabaseTestStub.getUserAlpha_pyramid());
        linearLayout_buttonScroll.addView(mButton3);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mButton1.setText(mDatabaseTestStub.getUserAlpha_griffin());
        mButton2.setText(mDatabaseTestStub.getUserAlpha_kerberos());
        mButton3.setText(mDatabaseTestStub.getUserAlpha_pyramid());
    }

    protected void onBackPressed(View v){
        super.onBackPressed();
    }
}