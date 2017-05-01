package com.example.ernest.kidsmate1;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Database extends Application {
    private static Database Database;
    private static DatabaseHelper mDBHelper;
    private static SQLiteDatabase DB;
    public static final String DBLOCATION = "/data/data/com.example.ernest.kidsmate1/databases/";
    public static final String DBNAME = "testDB.db";
    private static final int DB_MAX_SIZE = 3017;

    public static SQLiteDatabase getDB() {
        return DB;
    }

    public static void closeDB() {
        if(DB != null)
            DB.close();
    }

    public static String getRandomWord() {
        int id = (int)(Math.random() * Integer.MAX_VALUE) % DB_MAX_SIZE + 1;
        String word = "";
        Cursor cursor = DB.rawQuery("SELECT word FROM dic WHERE id = " + id, null);
        cursor.moveToFirst();
        word = cursor.getString(0);
        cursor.close();
        return word;
    }

    public static String[] getRandomWordMean() {
        int id = (int)(Math.random() * Integer.MAX_VALUE) % DB_MAX_SIZE + 1;
        String[] word = new String[]{"", ""};
        Cursor cursor = DB.rawQuery("SELECT word, mean FROM dic WHERE id = " + id, null);
        cursor.moveToFirst();
        word[0] = cursor.getString(0);
        word[1] = cursor.getString(1);
        cursor.close();
        return word;
    }

    public static String getRandomWordStartWith(String start) {
        String word = "";
        Cursor cursor = DB.rawQuery("SELECT word FROM dic WHERE word LIKE '" + start + "%' COLLATE NOCASE", null);
        int id = (int)(Math.random() * Integer.MAX_VALUE) % cursor.getCount();
        cursor.move(id);
        word = cursor.getString(0);
        cursor.close();
        return word;
    }

    public static String[] getRandomWordMeanStartWith(String start) {
        String[] word = new String[]{"", ""};
        Cursor cursor = DB.rawQuery("SELECT word, mean FROM dic WHERE word LIKE '" + start + "%' COLLATE NOCASE", null);
        int id = (int)(Math.random() * Integer.MAX_VALUE) % cursor.getCount();
        cursor.move(id);
        word[0] = cursor.getString(0);
        word[1] = cursor.getString(1);
        cursor.close();
        return word;
    }

    public static String getMean(String word) {
        Cursor cursor = DB.rawQuery("SELECT mean FROM dic WHERE word = '" + word + "' COLLATE NOCASE", null);
        cursor.moveToFirst();
        String mean = cursor.getString(0);
        cursor.close();
        return mean;
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
