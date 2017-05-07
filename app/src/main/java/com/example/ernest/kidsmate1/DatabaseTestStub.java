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

        levelUpExp = getLevelUpExp(level);
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
        /*
        캐릭터의 이름을 얻는다.
         */
        return Cname;
    }

    public void addCharacterExp(int exp){
        /*
        현재 캐릭터에 경험치를 더한다.
        단, 더한 즉시 레벨업을 진행하고, 레벨업 여부는 경험치를 더한 다음 액티비티가 '직접' 체크해야한다.
        왜냐면 레벨업 직후 메시지를 액티비티에 던져주지 않기 때문.
         */
        currentExp += exp;
        Log.d(TAG, "exp added: "+Integer.toString(currentExp-exp)+" >> "+Integer.toString(currentExp));
        if(exp >= levelUpExp && levelUpExp > 0 ){
            currentExp = currentExp - levelUpExp;
            level++;
            levelUpExp = getLevelUpExp(level);
        }
    }

    public int getCurrentExp(){
        /*
        현재 경험치량을 구한다. 이때 CurrentExp는 총 경험치가 아닌, 현재 레벨에서의 경험치이다.
         */
        return currentExp;
    }

    private int getLevelUpExp(int level){
        /*
        다음 레벨업에 필요한 경험치를 얻는다.
         */
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
        /*
        현재 펫의 정보를 얻는다. (객체, 문자열)
         */
        return currentPet;
    }

    public void setCurrentPet(int code){
        /*
        펫을 교체한다.
         */
        currentPet = code;
    }

    public ArrayList<Integer> getPetList(){
        /*
        펫의 리스트를 얻는다.
         */
        return petArrayList;
    }

    public void addPet(int petcode){
        /*
        펫을 추가한다.
         */
        petArrayList.add(petcode);
    }

    public void addStatBlankGuessing(int point){
        /*
        빈칸맞추기로 얻은 스탯을 더한다.
         */
        statBlankGuessing += point;
    }
    public void addStatImageGuessing(int point){
        /*
        그림맞추기로 얻은 스탯을 더한다.
         */
        statImageGuessing += point;
    }
    public void addStatWordChain(int point){
        /*
        끝말잇기로 얻은 스탯을 더한다.
         */
        statWordChain += point;
    }

    // 총 스탯을 얻어온다.
    public int getStatBlankGuessing(){
        return statBlankGuessing;
    }
    public int getStatImageGuessing(){
        return statImageGuessing;
    }
    public int getStatWordChain(){
        return statWordChain;
    }

    // 현재 레벨을 구한다.
    public int getLevel(){
        return level;
    }

    // 다음 레벨업에 필요한 경험치를 구한다.
    public int getLevelUpExp(){
        return this.levelUpExp;
    }


    // 한 세션(게임)의 최대 라운드를 구한다. (기본 10판이나 나중에 변경할수도 있으니)
    public int getMaxRound(){
        return 10;
    }

    // 경험치를 더 얻기 위해 이겨야 하는 라운드의 수를 구한다.
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

    // 이겼을때 경험치를 얼마나 얻는가
    public int getEarnedExpWhenSuccess(){
        return 10;
    }

    // 졌을때 경험치를 얼마나 얻는가
    public int getEarnedExpWhenFailure(){
        return 5;
    }
}
