package com.example.ernest.kidsmate1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;

/**
 * Created by User on 2017-05-05.
 */

public class Login_test extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listView;
    private MyAdapter_test myAdapter;
    private int a = 0;
    private ArrayList<String> unames = null;
    private EditText editText;
    private boolean isExist;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), unames.get(position) + "님 환영합니다", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Login_test.this, SelectContents2.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), unames.get(position) + "님이 삭제 되었습니다", Toast.LENGTH_SHORT).show();
        Database.delUser(unames.get(position));
        myAdapter.removeItem(position);
        unames.remove(position);
        listView.setAdapter(myAdapter);
        listView.setSelection(myAdapter.getCount() - 1);
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Login_test.this, MainActivity.class);
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

        unames = Database.getUser();
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
                Database.addUser(editText.getText().toString());
                Toast.makeText(getApplicationContext(), "등록되었습니다", Toast.LENGTH_SHORT).show();
                editText.setText("");
            }
        }
    }
}
