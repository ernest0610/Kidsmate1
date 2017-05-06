package com.example.ernest.kidsmate1;

import android.util.Log;

public class DatabaseTestStub {
    private static final String TAG = DatabaseTestStub.class.getSimpleName();

    private static DatabaseTestStub mDatabaseTestStub;

    private int exp;
    private String Cname;
    private int currentPet;

    private DatabaseTestStub(){
        exp = 0;
        Cname = "Ernest";
        currentPet = 0;
    }

    public static DatabaseTestStub getInstance(){
        if(mDatabaseTestStub == null) {
            synchronized (DatabaseTestStub.class) {
                if (mDatabaseTestStub == null) {
                    mDatabaseTestStub = new DatabaseTestStub();
                }
            }
        }
        return mDatabaseTestStub;
    }

    public String getCharacterCname(){
        return Cname;
    }

    public void addCharacterExp(int exp){
        this.exp += exp;
        Log.d(TAG, "exp added: "+Integer.toString(this.exp-exp)+" >> "+Integer.toString(this.exp));
    }

    public int getCurrentPet(){
        return currentPet;
    }
}
