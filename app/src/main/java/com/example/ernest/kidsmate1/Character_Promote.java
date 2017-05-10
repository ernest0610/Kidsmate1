package com.example.ernest.kidsmate1;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Character_Promote extends AppCompatActivity {

    protected DatabaseTestStub mDatabaseTestStub;
    protected DatabaseStateManager mDatabaseStateManager;

    protected Button button_junior_warrior;
    protected Button button_junior_thief;
    protected Button button_junior_wizard;

    protected Button button_senior_warrior1;
    protected Button button_senior_warrior2;
    protected Button button_senior_thief1;
    protected Button button_senior_thief2;
    protected Button button_senior_wizard1;
    protected Button button_senior_wizard2;

    protected String selectedString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.character_promote);

        mDatabaseStateManager = DatabaseStateManager.getInstance();

        button_junior_warrior = (Button) findViewById(R.id.button_junior_warrior);
        button_junior_thief = (Button) findViewById(R.id.button_junior_thief);
        button_junior_wizard = (Button) findViewById(R.id.button_junior_wizard);
        button_senior_warrior1 = (Button) findViewById(R.id.button_senior_warrior1);
        button_senior_warrior2 = (Button) findViewById(R.id.button_senior_warrior2);
        button_senior_thief1 = (Button) findViewById(R.id.button_senior_thief1);
        button_senior_thief2 = (Button) findViewById(R.id.button_senior_thief2);
        button_senior_wizard1 = (Button) findViewById(R.id.button_senior_wizard1);
        button_senior_wizard2 = (Button) findViewById(R.id.button_senior_wizard2);

        button_junior_warrior.setEnabled(false);
        button_junior_thief.setEnabled(false);
        button_junior_wizard.setEnabled(false);
        button_senior_warrior1.setEnabled(false);
        button_senior_warrior2.setEnabled(false);
        button_senior_thief1.setEnabled(false);
        button_senior_thief2.setEnabled(false);
        button_senior_wizard1.setEnabled(false);
        button_senior_wizard2.setEnabled(false);

        button_junior_warrior.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectedString = "견습 전사";
                showConfirmDialog();
            }
        });
        button_junior_thief.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectedString = "견습 도적";
                showConfirmDialog();
            }
        });
        button_junior_wizard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectedString = "견습 마법사";
                showConfirmDialog();
            }
        });
        button_senior_warrior1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectedString = "성기사";
                showConfirmDialog();
            }
        });
        button_senior_warrior2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectedString = "마검사";
                showConfirmDialog();
            }
        });
        button_senior_thief1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectedString = "닌자";
                showConfirmDialog();
            }
        });
        button_senior_thief2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectedString = "해적";
                showConfirmDialog();
            }
        });
        button_senior_wizard1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectedString = "마도사";
                showConfirmDialog();
            }
        });
        button_senior_wizard2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectedString = "사제";
                showConfirmDialog();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        button_junior_warrior.setEnabled(false);
        button_junior_thief.setEnabled(false);
        button_junior_wizard.setEnabled(false);
        button_senior_warrior1.setEnabled(false);
        button_senior_warrior2.setEnabled(false);
        button_senior_thief1.setEnabled(false);
        button_senior_thief2.setEnabled(false);
        button_senior_wizard1.setEnabled(false);
        button_senior_wizard2.setEnabled(false);
        String temp = mDatabaseStateManager.getChracterJob();

        if(!temp.equals("성기사") || !temp.equals("마검사") || !temp.equals("닌자") ||
                !temp.equals("해적") || !temp.equals("마도사") || !temp.equals("사제")) {
            if (mDatabaseStateManager.getCharacterLevel() >= 50) {
                if (mDatabaseStateManager.getChracterJob().equals("견습 전사")) {
                    button_senior_warrior1.setEnabled(true);
                    button_senior_warrior2.setEnabled(true);
                }else if (mDatabaseStateManager.getChracterJob().equals("견습 도적")) {
                    button_senior_thief1.setEnabled(true);
                    button_senior_thief2.setEnabled(true);
                }else if (mDatabaseStateManager.getChracterJob().equals("견습 마법사")) {
                    button_senior_wizard1.setEnabled(true);
                    button_senior_wizard2.setEnabled(true);
                }
            }else{
                if (mDatabaseStateManager.getCharacterLevel() >= 30) {
                    if(!temp.equals("견습 전사") || !temp.equals("견습 도적") || !temp.equals("견습 마법사")) {
                        if (mDatabaseStateManager.getCharacterPower() >= 180) {
                            button_junior_warrior.setEnabled(true);
                        }
                        if (mDatabaseStateManager.getCharacterLuck() >= 180) {
                            button_junior_thief.setEnabled(true);
                        }
                        if (mDatabaseStateManager.getCharacterSmart() >= 180) {
                            button_junior_wizard.setEnabled(true);
                        }
                    }
                }
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
                Character_Promote.this.onBackPressed();
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
        mDatabaseStateManager.setCharacterJob(selectedString);
    }

    protected void onBackPressed(View v){
        super.onBackPressed();
    }
}