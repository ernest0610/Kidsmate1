package com.example.ernest.kidsmate1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 2017-04-27.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBLOCATION = "/data/data/com.example.ernest.kidsmate1/databases/";
    public static final String DBNAME = "testDB.db";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        super(context, DBLOCATION + DBNAME, null, 1);
        this.mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public SQLiteDatabase openDatabase() {
        String dbPath = DBLOCATION + DBNAME;
        if(mDatabase != null && mDatabase.isOpen()) {
            return null;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
        return mDatabase;
    }

    public void closeDatabase() {
        if(mDatabase != null) {
            mDatabase.close();
        }
    }
}