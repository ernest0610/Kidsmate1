package com.example.ernest.kidsmate1;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by User on 2017-05-21.
 */

public class Login_Test_2 extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listView;
    private MyAdapter_test myAdapter;
    private int a = 0;
    private ArrayList<String> unames = null;
    private EditText editText;
    private boolean isExist;
    private DatabaseStateManager_2 databaseStateManager;
    private ArrayList<String> cnames = null;
    private static final String TAG = Login_test.class.getSimpleName();

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String uname = unames.get(position);
        Toast.makeText(getApplicationContext(), uname + "님 환영합니다", Toast.LENGTH_SHORT).show();
        //databaseStateManager = DatabaseStateManager_2.getInstance(this);
        databaseStateManager.switchUser(uname);
        cnames = databaseStateManager.getCharacterList(uname);
        if(cnames.size() == 0) {
            ShowDialog_addCharacter();
        }
        else {
            Toast.makeText(getApplicationContext(), cnames.get(0) + "님 환영합니다", Toast.LENGTH_SHORT).show();
            databaseStateManager.switchCharacter(cnames.get(0));
            Intent intent = new Intent(Login_Test_2.this, Login_test.class);
            startActivity(intent);
            finish();
        }
    }

    private void ShowDialog_addCharacter() {
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
                    Toast.makeText(getApplicationContext(), cname + "님 환영합니다", Toast.LENGTH_SHORT).show();
                    databaseStateManager.addCharacter(cname);
                    databaseStateManager.switchCharacter(cname);
                    Intent intent = new Intent(Login_Test_2.this, Login_test.class);
                    startActivity(intent);
                    finish();
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
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ShowDialog_delUser(position);
        return false;
    }

    private void ShowDialog_delUser(final int position)
    {
        LayoutInflater dialog = LayoutInflater.from(this);
        final View dialogLayout = dialog.inflate(R.layout.dialog_del_user, null);
        final Dialog myDialog = new Dialog(this);

        myDialog.setTitle("사용자 '" + unames.get(position) + "' 를 삭제합니다");
        myDialog.setContentView(dialogLayout);
        myDialog.show();

        Button btn_ok = (Button)dialogLayout.findViewById(R.id.button_ok_mydialog_deluser_test);
        Button btn_cancel = (Button)dialogLayout.findViewById(R.id.button_cancel_mydialog_deluser_test);

        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), unames.get(position) + "님이 삭제 되었습니다", Toast.LENGTH_SHORT).show();
                databaseStateManager.delCharacterAll(unames.get(position));
                databaseStateManager.delPetAll(unames.get(position));
                databaseStateManager.delTrophyAll(unames.get(position));
                databaseStateManager.delUser(unames.get(position));
                myAdapter.removeItem(position);
                unames.remove(position);
                listView.setAdapter(myAdapter);
                listView.setSelection(myAdapter.getCount() - 1);
                myDialog.cancel();
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
    public void onBackPressed() {
        Intent intent = new Intent(Login_Test_2.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_test);

        listView = (ListView) findViewById(R.id.listView_test);
        myAdapter = new MyAdapter_test(getApplicationContext());
        editText = (EditText) findViewById(R.id.editText_test);
        isExist = false;
        databaseStateManager = DatabaseStateManager_2.getInstance(this);
        unames = databaseStateManager.getUserList();
        for (String uname : unames) {
            myAdapter.addItem(new Item_test(uname));
        }
        listView.setAdapter(myAdapter);
        listView.setSelection(myAdapter.getCount() - 1);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    protected void onTestClicked(View v) {
        isExist = false;
        if(editText.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "이름을 입력하세요", Toast.LENGTH_SHORT).show();
        }
        else {
            for (String uname : unames) {
                if (editText.getText().toString().equals(uname)) {
                    isExist = true;
                    break;
                }
            }
            if(isExist) {
                Toast.makeText(getApplicationContext(), "이미 등록된 이름입니다", Toast.LENGTH_SHORT).show();
                editText.setText("");
            }
            else {
                unames.add(editText.getText().toString());
                myAdapter.addItem(new Item_test(unames.get(unames.size() - 1)));
                listView.setAdapter(myAdapter);
                listView.setSelection(myAdapter.getCount() - 1);
                databaseStateManager.addUser(editText.getText().toString());
                Toast.makeText(getApplicationContext(), "등록되었습니다", Toast.LENGTH_SHORT).show();
                editText.setText("");
            }
        }
    }
}
