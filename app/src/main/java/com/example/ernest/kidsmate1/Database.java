package com.example.ernest.kidsmate1;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

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
        if(!cursor.isAfterLast())
            word = cursor.getString(0);
        cursor.close();
        return word;
    }

    public static String[] getRandomWordMean() {
        int id = (int)(Math.random() * Integer.MAX_VALUE) % DB_MAX_SIZE + 1;
        String[] word = new String[]{"", ""};
        Cursor cursor = DB.rawQuery("SELECT word, mean FROM dic WHERE id = " + id, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            word[0] = cursor.getString(0);
            word[1] = cursor.getString(1);
        }
        cursor.close();
        return word;
    }

    public static String getRandomWordStartWith(String start) {
        String word = "";
        Cursor cursor = DB.rawQuery("SELECT word FROM dic WHERE word LIKE '" + start + "%' COLLATE NOCASE", null);
        cursor.moveToFirst();
        int id = 0;
        if(!cursor.isAfterLast()) {
            id = (int) (Math.random() * Integer.MAX_VALUE) % cursor.getCount();
            cursor.move(id);
            word = cursor.getString(0);
        }
        cursor.close();
        return word;
    }

    public static String[] getRandomWordMeanStartWith(String start) {
        String[] word = new String[]{"", ""};
        Cursor cursor = DB.rawQuery("SELECT word, mean FROM dic WHERE word LIKE '" + start + "%' COLLATE NOCASE", null);
        int id = (int)(Math.random() * Integer.MAX_VALUE) % cursor.getCount();
        cursor.move(id);
        if(!cursor.isAfterLast()) {
            word[0] = cursor.getString(0);
            word[1] = cursor.getString(1);
        }
        cursor.close();
        return word;
    }

    public static String getMean(String word) {
        Cursor cursor = DB.rawQuery("SELECT mean FROM dic WHERE word = '" + word + "' COLLATE NOCASE", null);
        cursor.moveToFirst();
        String mean = "";
        if(!cursor.isAfterLast())
            mean = cursor.getString(0);
        cursor.close();
        return mean;
    }
    public static String getWord(String mean) {
        Cursor cursor = DB.rawQuery("SELECT word FROM dic WHERE mean = '" + mean + "' ", null);
        cursor.moveToFirst();
        String word = "";
        if(!cursor.isAfterLast())
            word = cursor.getString(0);
        cursor.close();
        return word;
    }

    public static ArrayList<String> getUser() {
        Cursor cursor = DB.rawQuery("SELECT uname FROM user", null);
        cursor.moveToFirst();
        ArrayList<String> unames = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            unames.add(cursor.getString(0));
            cursor.moveToNext();
        }
        return unames;
    }

    public static void addUser(String uname) {
        DB.execSQL("INSERT INTO user (uname) VALUES ('" + uname + "')");
    }

    public static void delUser(String uname) {
        DB.execSQL("DELETE FROM user WHERE uname = '" + uname + "'");
    }

    public static void delCharacterAll(String uname) {
        DB.execSQL("DELETE FROM character WHERE uname = '" + uname + "'");
    }

    public static void delPetAll(String uname) {
        DB.execSQL("DELETE FROM pet WHERE uname = '" + uname + "'");
    }

    public static void delTrophyAll(String uname) {
        DB.execSQL("DELETE FROM trophy WHERE uname = '" + uname + "'");
    }

    public static int getUserInfo(String attr, String uname) {
        Cursor cursor = DB.rawQuery("SELECT " + attr + " FROM user WHERE uname = '" + uname + "'", null);
        cursor.moveToFirst();
        int result = -1;
        if(!cursor.isAfterLast())
            result = cursor.getInt(0);
        cursor.close();
        return result;
    }

    public static void setUserInfo(String attr, String uname, int value) {
        DB.execSQL("UPDATE user SET " + attr + " = " + value + " WHERE uname = '" + uname + "'");
    }

    public static int getCharacterInfo(String attr, String uname, String cname) {
        Cursor cursor = DB.rawQuery("SELECT " + attr + " FROM character WHERE uname = '" + uname + "', cname = '" + cname + "'", null);
        cursor.moveToFirst();
        int result = -1;
        if(!cursor.isAfterLast())
            result = cursor.getInt(0);
        cursor.close();
        return result;
    }

    public static void setCharacterInfo(String attr, String uname, String cname, int value) {
        DB.execSQL("UPDATE character SET " + attr + " = " + value + " WHERE uname = '" + uname + "', cname = '" + cname + "'");
    }

    public static void setCharacterJob(String uname, String cname, String job) {
        DB.execSQL("UPDATE character SET job = " + job + " WHERE uname = '" + uname + "', cname = '" + cname + "'");
    }

    public static void addCharacter(String uname, String cname, String date) {
        DB.execSQL("INSERT INTO character (uname, cname, date) VALUES ('" + uname + "', '" + cname + "', '" + date + "')");
    }

    public static void addPet(String pid, String uname, int type) {
        DB.execSQL("INSERT INTO pet (pid, uname, type) VALUES ('" + pid + "', '" + uname + "', " + type + ")");
    }

    public static void addTrophy(String tid, String uname, int type) {
        DB.execSQL("INSERT INTO trophy (tid, uname, type) VALUES ('" + tid + "', '" + uname + "', " + type + ")");
    }

    public static ArrayList<Pet> getPetList(String uname) {
        Cursor cursor = DB.rawQuery("SELECT type, uname, pid, cname FROM pet WHERE uname = '" + uname + "' ORDER BY pid", null);
        cursor.moveToFirst();
        ArrayList<Pet> result = new ArrayList<>();
        while(!cursor.isAfterLast()) {
            result.add(new Pet(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    public static ArrayList<Integer> getTrophyList(String uname) {
        Cursor cursor = DB.rawQuery("SELECT type FROM trophy WHERE uname = '" + uname + "' ORDER BY tid", null);
        cursor.moveToFirst();
        ArrayList<Integer> result = new ArrayList<Integer>();
        while(!cursor.isAfterLast()) {
            result.add(cursor.getInt(0));
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    public static ArrayList<String> getCharacterList(String uname) {
        Cursor cursor = DB.rawQuery("SELECT cname FROM character WHERE uname = '" + uname + "' ORDER BY date", null);
        cursor.moveToFirst();
        ArrayList<String> result = new ArrayList<>();
        while(!cursor.isAfterLast()) {
            result.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    public static int getCurrentPet(String uname, String cname) {
        Cursor cursor = DB.rawQuery("SELECT type FROM pet WHERE uname = '" + uname + "', cname = '" + cname + "'", null);
        cursor.moveToFirst();
        int result = -1;
        if(!cursor.isAfterLast()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        return result;
    }

    public static void setCurrentPet(String uname, String cname, String pid) {
        Cursor cursor = DB.rawQuery("SELECT pid FROM pet WHERE uname = '" + uname + "', cname = '" + cname + "'", null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            DB.execSQL("UPDATE pet SET cname = '' WHERE pid = '" + cursor.getString(0) + "'");cursor.getInt(0);
        }
        cursor.close();
        DB.execSQL("UPDATE pet SET cname = '" + cname + "' WHERE pid = '" + pid + "', uname = '" + uname + "'");
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
