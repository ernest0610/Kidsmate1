package com.example.ernest.kidsmate1;

import android.util.Log;

public class DatabaseTestStub {
    private static final String TAG = DatabaseTestStub.class.getSimpleName();
    private int exp;

    public DatabaseTestStub(){
        exp = 0;
    }

    public String getCharacterCname(){
        return "";
    }

    public void addCharacterExp(int exp){
        this.exp += exp;
        Log.d(TAG, "exp added: "+Integer.toString(this.exp-exp)+" >> "+Integer.toString(this.exp));
    }
}
