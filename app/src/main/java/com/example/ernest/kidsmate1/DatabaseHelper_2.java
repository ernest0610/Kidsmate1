package com.example.ernest.kidsmate1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by User on 2017-05-20.
 */

public class DatabaseHelper_2 extends SQLiteOpenHelper {
    private static final String DBLOCATION = "/data/data/com.example.ernest.kidsmate1/databases/";
    private static final String DBNAME = "kidsmate.db";
    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    private File file;

    public DatabaseHelper_2(Context context) {
        super(context, DBLOCATION + DBNAME, null, 1);
        this.context = context;
        file = new File(DBLOCATION + DBNAME);
            if(!file.exists()) {
                if(copyDatabase(context)){
                    Toast.makeText(context, "copy", Toast.LENGTH_SHORT).show();
                }
            else{
                Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
            }
        }
        try {
            getReadableDatabase();
        }catch (Exception e){
            getReadableDatabase();
        }
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try {
            file = new File(DBLOCATION + DBNAME + "-journal");
            file.delete();
            file = new File(DBLOCATION + DBNAME);
            file.delete();
            copyDatabase(context);
        }catch (Exception e) {e.printStackTrace();}
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try {
            file = new File(DBLOCATION + DBNAME + "-journal");
            file.delete();
            file = new File(DBLOCATION + DBNAME);
            file.delete();
            copyDatabase(context);
        }catch (Exception e) {e.printStackTrace();}
    }

    public SQLiteDatabase openDatabase() {
        String dbPath = DBLOCATION + DBNAME;
        if(sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
            return null;
        }
        sqLiteDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
        return sqLiteDatabase;
    }

    public void closeDatabase() {
        if(sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
    }

    private boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(DBNAME);
            String outFileName = DBLOCATION + DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buf = new byte[1024];
            int length = 0;
            while((length = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            return  true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
