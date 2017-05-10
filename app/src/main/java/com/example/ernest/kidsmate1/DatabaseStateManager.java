package com.example.ernest.kidsmate1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DatabaseStateManager {
    protected static final String TAG = DatabaseStateManager.class.getSimpleName();

    private static DatabaseStateManager mDatabaseStateManager = null;
    private String currentUname = "";
    private String currentCname = "";

    private DatabaseStateManager() {
    }

    public static DatabaseStateManager getInstance() { /// constructor
        if(mDatabaseStateManager == null) {
            synchronized (DatabaseStateManager.class) {
                if(mDatabaseStateManager == null) {
                    mDatabaseStateManager = new DatabaseStateManager();
                }
            }
        }
        return mDatabaseStateManager;
    }

    // 현재 user 확인
    public String getCurrentUname() {
        return currentUname;
    }
    // 현재 character 확인
    public String getCurrentCname() {
        return currentCname;
    }
    // 현재 user 변경
    public void switchUser(String uname) {
        this.currentUname = uname;
    }
    // 현재 character 변경
    public void switchCharacter(String cname) {
        this.currentCname = cname;
    }
    // 전직
    public void setCharacterJob(String job) {
        Database.setCharacterJob(currentUname, currentCname, job);
    }
    // 현재 character의 pet 변경
    public void setCurrentPet(String pid) {
        Database.setCurrentPet(currentUname, currentCname, pid);
    }
    // levelup 시 경험치 초기화
    public void initCharacterExp() {
        Database.setCharacterInfo("exp", currentUname, currentCname, 0);
    }
/////////////////////////////////////////////////////////////////////////////////////////// alpha set
    public void setUserAlphpa_kerberos(String value) {
        Database.setUserAlpha("alpha_kerberos", currentUname, value);
    }

    public void setUserAlpha_griffin(String value) {
        Database.setUserAlpha("alpha_griffin", currentUname, value);
    }

    public void setUserAlphpa_pyramid(String value) {
        Database.setUserAlpha("alpha_pyramid", currentUname, value);
    }
    /////////////////////////////////////////////////////////////////////////////////////////// user info add
    public void addUserTW_count(int increment) {
        int origin = Database.getUserInfo("TW_count", currentUname);
        origin += increment;
        Database.setUserInfo("TW_count", currentUname, origin);
    }

    public void addUserBG_count(int increment) {
        int origin = Database.getUserInfo("BG_count", currentUname);
        origin += increment;
        Database.setUserInfo("BG_count", currentUname, origin);
    }

    public void addUserIG_count(int increment) {
        int origin = Database.getUserInfo("IG_count", currentUname);
        origin += increment;
        Database.setUserInfo("IG_count", currentUname, origin);
    }

    public void addUserWC_count(int increment) {
        int origin = Database.getUserInfo("WC_count", currentUname);
        origin += increment;
        Database.setUserInfo("WC_count", currentUname, origin);
    }

    public void addUserBG_bcount(int increment) {
        int origin = Database.getUserInfo("BG_bcount", currentUname);
        origin += increment;
        Database.setUserInfo("BG_bcount", currentUname, origin);
    }

    public void addUserIG_bcount(int increment) {
        int origin = Database.getUserInfo("IG_bcount", currentUname);
        origin += increment;
        Database.setUserInfo("IG_bcount", currentUname, origin);
    }

    public void addUserWC_bcount(int increment) {
        int origin = Database.getUserInfo("WC_bcount", currentUname);
        origin += increment;
        Database.setUserInfo("WC_bcount", currentUname, origin);
    }
    /////////////////////////////////////////////////////////////////////////////////// user alpha full get
    public String getUserAlphpa_kerberos_fullString() {
        return "KERBEROS";
    }

    public String getUserAlpha_griffin_fullString() {
        return "GRIFFIN";
    }

    public String getUserAlphpa_pyramid_fullString() {
        return "PYRAMID";
    }
////////////////////////////////////////////////////////////////////////////////////// user info get
    public String getUserAlphpa_kerberos() {
       return Database.getUserAlpha("alpha_kerberos", currentUname);
    }

    public String getUserAlpha_griffin() {
        return Database.getUserAlpha("alpha_griffin", currentUname);
    }

    public String getUserAlphpa_pyramid() {
        return Database.getUserAlpha("alpha_pyramid", currentUname);
    }

    public int getUserTW_count() {
        return Database.getUserInfo("TW_count", currentUname);
    }

    public int getUserBG_count() {
        return Database.getUserInfo("BG_count", currentUname);
    }

    public int getUserIG_count() {
        return Database.getUserInfo("IG_count", currentUname);
    }

    public int getUserWC_count() {
        return Database.getUserInfo("WC_count", currentUname);
    }

    public int getUserBG_bcount() {
        return Database.getUserInfo("BG_bcount", currentUname);
    }

    public int getUserIG_bcount() {
        return Database.getUserInfo("IG_bcount", currentUname);
    }

    public int getUserWC_bcount() {
        return Database.getUserInfo("WC_bcount", currentUname);
    }
////////////////////////////////////////////////////////////////////////////////////////////////// char info add
    public void addCharacterLevel(int increment) {
        int origin = Database.getCharacterInfo("level", currentUname, currentCname);
        origin += increment;
        Database.setCharacterInfo("level", currentUname, currentCname, origin);
    }

    public boolean addCharacterExp(int increment) {
        boolean LevelUpChck = false;
        int origin = Database.getCharacterInfo("exp", currentUname, currentCname);
        origin += increment;
        while(origin >= this.getLevelUpExp()) {
            origin -= this.getLevelUpExp();
            addCharacterLevel(1);
            LevelUpChck = true;
        }

        Database.setCharacterInfo("exp", currentUname, currentCname, origin);
        return LevelUpChck;
    }

    public void addCharacterPower(int increment) {
        int origin = Database.getCharacterInfo("power", currentUname, currentCname);
        origin += increment;
        Database.setCharacterInfo("power", currentUname, currentCname, origin);
    }

    public void addCharacterSmart(int increment) {
        int origin = Database.getCharacterInfo("smart", currentUname, currentCname);
        origin += increment;
        Database.setCharacterInfo("smart", currentUname, currentCname, origin);
    }

    public void addCharacterLuck(int increment) {
        int origin = Database.getCharacterInfo("luck", currentUname, currentCname);
        origin += increment;
        Database.setCharacterInfo("luck", currentUname, currentCname, origin);
    }
////////////////////////////////////////////////////////////////////////////////////////////////// char info get
public int getCharacterLevel() {
    return Database.getCharacterInfo("level", currentUname, currentCname);
}

    public int getCharacterExp() {
        return Database.getCharacterInfo("exp", currentUname, currentCname);
    }

    public int getCharacterPower() {
        return Database.getCharacterInfo("power", currentUname, currentCname);
    }

    public int getCharacterSmart() {
        return Database.getCharacterInfo("smart", currentUname, currentCname);
    }

    public int getCharacterLuck() {
        return Database.getCharacterInfo("luck", currentUname, currentCname);
    }

    public int getCurrentPet() {
        return Database.getCurrentPet(currentUname, currentCname);
    }
    //////////////////////////////////////////////////////////////////////////////// char, pet, trop 추가
    public void addCharacter(String cname) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = simpleDateFormat.format(cal.getTime());
        Database.addCharacter(currentUname, cname, date);
    }
    public void addPet(int type) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String pid = simpleDateFormat.format(cal.getTime());
        Database.addPet(pid, currentUname, type);
    }
    public void addTrophy(int type) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String tid = simpleDateFormat.format(cal.getTime());
        Database.addTrophy(tid, currentUname, type);
    }
    ////////////////////////////////////////////////////////////////////////////////// get pet, trophy list
    public ArrayList<Pet> getPetList() {
        return Database.getPetList(currentUname);
    }
    public ArrayList<Integer> getTrophyList() {
        return Database.getTrophyList(currentUname);
    }
    //////////////////////////////////////////////////////////////////////////////// get char list
    public ArrayList<String> getCharacterList() {
        return Database.getCharacterList(currentUname);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////// 추가
    public int getLevelUpExp(){
        /*
        다음 레벨업에 필요한 경험치를 얻는다.
         */
        if(this.getCharacterLevel() <= 10){
            return 10;
        }else if(this.getCharacterLevel() <= 20){
            return 20;
        }else if(this.getCharacterLevel() <= 30){
            return 30;
        }else if(this.getCharacterLevel() <= 40){
            return 40;
        }else if(this.getCharacterLevel() <= 50){
            return 60;
        }else{
            return -1;
        }
    }
    // 한 세션(게임)의 최대 라운드를 구한다. (기본 10판이나 나중에 변경할수도 있으니)
    public int getMaxRound(){
        return 10;
    }
    // 경험치를 더 얻기 위해 이겨야 하는 라운드의 수를 구한다.
    public int getGoalRound(){
        if(this.getCharacterLevel() <= 10){
            return 4;
        }else if(this.getCharacterLevel() <= 20){
            return 5;
        }else if(this.getCharacterLevel() <= 30){
            return 6;
        }else if(this.getCharacterLevel() <= 40){
            return 7;
        }else if(this.getCharacterLevel() <= 50){
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
