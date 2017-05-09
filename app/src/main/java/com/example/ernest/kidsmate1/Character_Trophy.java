package com.example.ernest.kidsmate1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Character_Trophy extends AppCompatActivity {
    DatabaseTestStub mDatabaseTestStub;

    LinearLayout linearLayout_buttonScroll;

    Button buttonAdd;
    Button buttonConfirm;

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
    }

    @Override
    protected void onResume(){
        super.onResume();

        linearLayout_buttonScroll.removeAllViews();

        Button button;
        int listSize = mDatabaseTestStub.getTrophyListSize();
        if(listSize != 0) {
            for (int index = 0; index < listSize; index++) {
                if(mDatabaseTestStub.isTrophyAchieved(index)) {
                    button = new Button(this);
                    button.setText(mDatabaseTestStub.getTrophyString(index));
                    linearLayout_buttonScroll.addView(button);
                }
            }
        }
    }

    protected void onBackPressed(View v){
        super.onBackPressed();
    }
}
