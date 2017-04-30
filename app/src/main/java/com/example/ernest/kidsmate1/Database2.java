package com.example.ernest.kidsmate1;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.speech.clientapi.SpeechRecognitionResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.List;

public class Database2 extends Application {
    private static Database2 Database;
    private static DatabaseHelper mDBHelper;
    private static SQLiteDatabase DB;
    public static final String DBLOCATION = "/data/data/com.example.ernest.kidsmate1/databases/";
    public static final String DBNAME = "testDB.db";

    public static SQLiteDatabase getDB() {
        return DB;
    }

    public static void closeDB() {
        if(DB != null)
            DB.close();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Database = this;
        Database.initializeInstance();
    }

    protected void initializeInstance() {
        mDBHelper = new DatabaseHelper(this);
        File database = new File(DBLOCATION + DBNAME);
        if(false == database.exists()) {
            try{mDBHelper.getReadableDatabase();}catch (Exception e){e.printStackTrace();}

            if(copyDatabase(this)) {
                Toast.makeText(this, "Copy database succes", Toast.LENGTH_SHORT).show();
            }
        }
        DB = mDBHelper.openDatabase();
    }

    private boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(DBNAME);
            String outFileName = DBLOCATION + DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.v("MainActivity", "DB copied");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
