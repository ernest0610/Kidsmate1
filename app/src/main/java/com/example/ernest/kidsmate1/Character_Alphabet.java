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

public class Character_Alphabet extends AppCompatActivity {

    DatabaseTestStub mDatabaseTestStub;
    DatabaseStateManager mDatabaseStateManager;

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
        mDatabaseStateManager = DatabaseStateManager.getInstance();

        linearLayout_buttonScroll = (LinearLayout) findViewById(R.id.linearLayout_buttonScroll);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonConfirm = (Button) findViewById(R.id.buttonConfirm);

        buttonAdd.setEnabled(false);
        buttonConfirm.setEnabled(false);

        mButton1 = new Button(this);
        mButton1.setText(mDatabaseTestStub.getUserAlpha_griffin());
        mButton1.setEnabled(false);
        linearLayout_buttonScroll.addView(mButton1);
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCharacter(1);
                // // TODO: 2017-05-10 캐릭터 추가 루틴
            }
        });

        mButton2 = new Button(this);
        mButton2.setText(mDatabaseTestStub.getUserAlpha_kerberos());
        mButton2.setEnabled(false);
        linearLayout_buttonScroll.addView(mButton2);
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCharacter(2);
                // // TODO: 2017-05-10 캐릭터 추가 루틴
            }
        });

        mButton3 = new Button(this);
        mButton3.setText(mDatabaseTestStub.getUserAlpha_pyramid());
        mButton3.setEnabled(false);
        linearLayout_buttonScroll.addView(mButton3);
        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCharacter(3);
                // // TODO: 2017-05-10 캐릭터 추가 루틴
            }
        });
    }

    protected void addCharacter(final int whatBoss){
        LayoutInflater dialog = LayoutInflater.from(this);
        final View dialogLayout = dialog.inflate(R.layout.dialog_get_string, null);
        final Dialog myDialog = new Dialog(this);

        myDialog.setTitle("캐릭터를 생성합니다");
        myDialog.setContentView(dialogLayout);
        myDialog.show();

        Button btn_ok = (Button)dialogLayout.findViewById(R.id.button_ok_mydialog_addchar_test);
        Button btn_cancel = (Button)dialogLayout.findViewById(R.id.button_cancel_mydialog_addchar_test);
        final EditText editText = (EditText)dialogLayout.findViewById(R.id.editText_mydialog_addchar_test);

        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String cname = editText.getText().toString();
                if(cname.equals("")) {
                    Toast.makeText(getApplicationContext(), "캐릭터 이름을 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(whatBoss==1){
                        mDatabaseTestStub.setUserAlpha_griffin("_______");
                        mButton1.setEnabled(false);
                    }else if(whatBoss==2){
                        mDatabaseTestStub.setUserAlpha_kerberos("________");
                        mButton2.setEnabled(false);
                    }else{ //(whatBoss==3)
                        mDatabaseTestStub.setUserAlpha_pyramid("_______");
                        mButton3.setEnabled(false);
                    }
                    mDatabaseStateManager.addCharacter(cname);
                    Toast.makeText(getApplicationContext(), "캐릭터 "+cname+"가 추가되었습니다.", Toast.LENGTH_SHORT).show();

                    myDialog.cancel();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDialog.cancel();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        String mString;

        mString = mDatabaseTestStub.getUserAlpha_griffin();
        mButton1.setText(mString);
        if(mString.equals(mDatabaseTestStub.getUserAlpha_griffin_fullString())){
            mButton1.setEnabled(true);
        }else{
            mButton1.setEnabled(false);
        }

        mString = mDatabaseTestStub.getUserAlpha_kerberos();
        mButton2.setText(mString);
        if(mString.equals(mDatabaseTestStub.getUserAlpha_kerberos_fullString())){
            mButton2.setEnabled(true);
        }else{
            mButton2.setEnabled(false);
        }

        mString = mDatabaseTestStub.getUserAlpha_pyramid();
        mButton3.setText(mString);
        if(mString.equals(mDatabaseTestStub.getUserAlpha_pyramid_fullString())){
            mButton3.setEnabled(true);
        }else{
            mButton3.setEnabled(false);
        }
    }

    protected void onBackPressed(View v){
        super.onBackPressed();
    }
}