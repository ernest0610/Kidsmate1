package com.example.ernest.kidsmate1;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Character_Trophy extends AppCompatActivity {

    DatabaseTestStub mDatabaseTestStub;

    Context context;

    LinearLayout linearLayout_buttonScroll;

    int num;

    Button buttonAdd;
    Button buttonConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabaseTestStub = DatabaseTestStub.getInstance();
        context = this;
        num = 0;

        setContentView(R.layout.dynamic_button_scroll_view);

        linearLayout_buttonScroll = (LinearLayout) findViewById(R.id.linearLayout_buttonScroll);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonConfirm = (Button) findViewById(R.id.buttonConfirm);

        buttonAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                num++;
                Button button = new Button(context);
                button.setText(Integer.toString(num));
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
