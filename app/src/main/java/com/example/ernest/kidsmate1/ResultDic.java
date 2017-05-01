package com.example.ernest.kidsmate1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by User on 2017-04-27.
 */

public class ResultDic extends AppCompatActivity {
    private TextView wordText;
    private TextView meanText;
    private String ResponseText;
    private HttpClient client;
    private HttpGet get;
    private HttpResponse response;
    private HttpEntity resEntity;
    private Document doc;
    private Elements elements1;
    private Elements elements2;
    private static final String addr = "http://m.endic.naver.com/search.nhn?searchOption=all&query=";
    private String mean;
    private String word;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_dic);

        wordText = (TextView) findViewById(R.id.word);
        meanText = (TextView) findViewById(R.id.mean);
        Intent intent = getIntent();
        word = intent.getStringExtra("word");        //사전(Dictionary class)에서 전달한 단어를 받아오는 부분
        wordText.setText(word);

        mean = "";
        handler = new Handler() {
            public void handleMessage(Message msg) {
                Bundle bun = msg.getData();
                mean = bun.getString("HTML_DATA");
                if (mean.equals(""))
                    mean = "'" + word + "'에 대한 결과가 없습니다";
                meanText.setText(mean);
            }
        };
        new Thread() {
            public void run() {
                String mean = "";
                client = new DefaultHttpClient();
                get = new HttpGet(addr + word.replaceAll(" ", "%20"));
                try {
                    response = client.execute(get);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                resEntity = response.getEntity();
                if (resEntity != null) {
                    try {
                        ResponseText = EntityUtils.toString(resEntity, "UTF-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                doc = Jsoup.parse(ResponseText);
                elements1 = doc.select("div.entry_search_word");
                for (Element element1 : elements1) {
                    elements2 = element1.select("strong strong");
                    for (Element element2 : elements2) {
                        mean = mean + element2.text() + "\n";
                    }
                    elements2 = element1.select("li");
                    for (Element element2 : elements2) {
                        mean = mean + element2.text() + "\n";
                    }
                    elements2 = element1.select("p.example_stc");
                    for (Element element2 : elements2) {
                        mean = mean + "\nEx)" + element2.text() + "\n";
                    }
                    elements2 = element1.select("p.example_mean");
                    for (Element element2 : elements2) {
                        mean = mean + element2.text() + "\n";
                    }
                    mean = mean + "\n";
                }
                Bundle bun = new Bundle();
                bun.putString("HTML_DATA", mean);
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
            }
        }.start();
    }

    @Override
    public void onBackPressed() {       //뒤로가기 버튼 누르면 현재 activity 제거
        super.onBackPressed();
        finish();
    }
}
