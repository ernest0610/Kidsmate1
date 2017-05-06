package com.example.ernest.kidsmate1;

import android.util.Log;

import java.util.ArrayList;

public class DatabaseTestStub {
    private static final String TAG = DatabaseTestStub.class.getSimpleName();

    private static DatabaseTestStub mDatabaseTestStub;

    private int currentExp;
    private String Cname;
    private int currentPet;
    private ArrayList<Integer> petArrayList;

    private int statBlankGuessing;
    private int statImageGuessing;
    private int statWordChain;

    private int level;

    private int levelUpExp;

    private DatabaseTestStub(){
        currentExp = 0;
        Cname = "Ernest";
        petArrayList = new ArrayList(6);
        petArrayList.add(1);
        currentPet = 1;

        statBlankGuessing = 0;
        statImageGuessing = 0;
        statWordChain = 0;

        level = 1;
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
        currentExp += exp;
        Log.d(TAG, "exp added: "+Integer.toString(currentExp-exp)+" >> "+Integer.toString(currentExp));
        if(exp > levelUpExp && levelUpExp > 0 ){
            currentExp = currentExp - levelUpExp;
            level++;
            levelUpExp = getLevelUpExp(level);
        }
    }

    public int getCurrentExp(){
        return currentExp;
    }

    private int getLevelUpExp(int level){
        if(level <= 10){
            return 10;
        }else if(level <= 20){
            return 20;
        }else if(level <= 30){
            return 30;
        }else if(level <= 40){
            return 40;
        }else if(level <= 50){
            return 60;
        }else{
            return -1;
        }
    }

    public int getCurrentPet(){
        return currentPet;
    }

    public void setCurrentPet(int code){
        currentPet = code;
    }

    public ArrayList<Integer> getPetList(){
        return petArrayList;
    }

    public void addPet(int petcode){
        petArrayList.add(petcode);
    }

    public void addStatBlankGuessing(int point){
        statBlankGuessing += point;
    }
    public void addStatImageGuessing(int point){
        statImageGuessing += point;
    }
    public void addStatWordChain(int point){
        statWordChain += point;
    }

    public int getStatBlankGuessing(){
        return statBlankGuessing;
    }
    public int getStatImageGuessing(){
        return statImageGuessing;
    }
    public int getStatWordChain(){
        return statWordChain;
    }

    public int getLevel(){
        return level;
    }

    public int getLevelUpExp(){
        return this.levelUpExp;
    }


    public int getMaxRound(){
        return 10;
    }

    public int getGoalRound(){
        if(level <= 10){
            return 4;
        }else if(level <= 20){
            return 5;
        }else if(level <= 30){
            return 6;
        }else if(level <= 40){
            return 7;
        }else if(level <= 50){
            return 8;
        }else{
            return -1;
        }
    }

    public int getEarnedExpWhenSuccess(){
        return 10;
    }

    public int getEarnedExpWhenFailure(){
        return 5;
    }
}
