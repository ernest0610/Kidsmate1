package com.example.ernest.kidsmate1;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;

public class Character_Change extends AppCompatActivity {
    protected DatabaseStateManager mDatabaseStateManager;

    protected LinearLayout linearLayout_buttonScroll;

    protected ArrayList<String> characterList;

    protected String selectedString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_scrollview);
        mDatabaseStateManager = DatabaseStateManager.getInstance();
        linearLayout_buttonScroll = (LinearLayout) findViewById(R.id.linearLayout_buttonScroll);
    }

    @Override
    protected void onResume(){
        super.onResume();
        linearLayout_buttonScroll.removeAllViews();
        characterList = mDatabaseStateManager.getCharacterList();

        Button mButton;

        if(characterList.size() != 0) {
            for (String cname : characterList) {
                final String selectedName = cname;
                mButton = new Button(this);
                mButton.setText(cname);
                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedString = selectedName;
                        showConfirmDialog();
                    }
                });
                linearLayout_buttonScroll.addView(mButton);
            }
        }
    }

    protected void showConfirmDialog(){
        LayoutInflater dialog = LayoutInflater.from(this);
        final View dialogLayout = dialog.inflate(R.layout.dialog_confirm, null);
        final Dialog myDialog = new Dialog(this);
        myDialog.setContentView(dialogLayout);

        Button btn_ok = (Button)dialogLayout.findViewById(R.id.button_ok);
        Button btn_cancel = (Button)dialogLayout.findViewById(R.id.button_cancel);

        btn_ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                changeState();
                Character_Change.this.onBackPressed();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });

        myDialog.show();
    }

    protected void changeState(){
        mDatabaseStateManager.switchCharacter(selectedString);
    }

    protected void onBackPressed(View v){
        super.onBackPressed();
    }
}