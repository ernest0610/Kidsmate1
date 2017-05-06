package com.example.ernest.kidsmate1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Character_Pet extends AppCompatActivity {

    DatabaseTestStub mDatabaseTestStub;

    Context context;

    LinearLayout linearLayout_buttonScroll;

    int num;

    int selectedButtonNumber;

    Button buttonAdd;
    Button buttonConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabaseTestStub = DatabaseTestStub.getInstance();
        context = this;
        num = 0;
        selectedButtonNumber = mDatabaseTestStub.getCurrentPet();

        setContentView(R.layout.dynamic_button_scroll_view);

        linearLayout_buttonScroll = (LinearLayout) findViewById(R.id.linearLayout_buttonScroll);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonConfirm = (Button) findViewById(R.id.buttonConfirm);

        buttonAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                num++;
                final Button button = new Button(context);
                button.setText(Integer.toString(num));
                button.setOnClickListener(new View.OnClickListener(){
                    int buttonNum = num;
                    Button mButton = button;
                    @Override
                    public void onClick(View v){
                        selectedButtonNumber = buttonNum;
                        mButton.setEnabled(false);
                    }
                });
                linearLayout_buttonScroll.addView(button);
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        buttonAdd.setEnabled(true);
        buttonConfirm.setEnabled(true);
    }

    protected void onBackPressed(View v){
        super.onBackPressed();
    }
}
