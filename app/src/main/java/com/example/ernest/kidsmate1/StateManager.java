package com.example.ernest.kidsmate1;

import android.content.Context;
import android.database.Cursor;

import com.naver.speech.clientapi.SpeechRecognitionException;
import com.naver.speech.clientapi.SpeechRecognizer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by User on 2017-05-06.
 */

public class StateManager {
    private static StateManager mStateManager = null;
    private String currentUname = "";
    private String currentCname = "";

    private StateManager(Context context) {
    }

    public static StateManager getInstance(Context context) { // constructor
        if(mStateManager == null) {
            synchronized (StateManager.class) {
                if(mStateManager == null) {
                    mStateManager = new StateManager(context);
                }
            }
        }
        return mStateManager;
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
/////////////////////////////////////////////////////////////////////////////////////////// user info add
    public void addUserAlphpa_kerberos(int increment) {
        int origin = Database.getUserInfo("alpha_kerberos", currentUname);
        origin += increment;
        Database.setUserInfo("alpha_kerberos", currentUname, origin);
    }

    public void addUserAlpha_griffon(int increment) {
        int origin = Database.getUserInfo("alpha_griffon", currentUname);
        origin += increment;
        Database.setUserInfo("alpha_griffon", currentUname, origin);
    }

    public void addUserAlphpa_sphinx(int increment) {
        int origin = Database.getUserInfo("alpha_sphinx", currentUname);
        origin += increment;
        Database.setUserInfo("alpha_sphinx", currentUname, origin);
    }

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
////////////////////////////////////////////////////////////////////////////////////// user info get
    public int getUserAlphpa_kerberos() {
        return Database.getUserInfo("alpha_kerberos", currentUname);
    }

    public int getUserAlpha_griffon() {
        return Database.getUserInfo("alpha_griffon", currentUname);
    }

    public int getUserAlphpa_sphinx() {
        return Database.getUserInfo("alpha_sphinx", currentUname);
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
        Database.setCharacterInfo("level", currentUname, currentUname, origin);
    }

    public void addCharacterExp(int increment) {
        int origin = Database.getCharacterInfo("exp", currentUname, currentCname);
        origin += increment;
        Database.setCharacterInfo("exp", currentUname, currentUname, origin);
    }

    public void addCharacterPower(int increment) {
        int origin = Database.getCharacterInfo("power", currentUname, currentCname);
        origin += increment;
        Database.setCharacterInfo("power", currentUname, currentUname, origin);
    }

    public void addCharacterSmart(int increment) {
        int origin = Database.getCharacterInfo("smart", currentUname, currentCname);
        origin += increment;
        Database.setCharacterInfo("smart", currentUname, currentUname, origin);
    }

    public void addCharacterLuck(int increment) {
        int origin = Database.getCharacterInfo("luck", currentUname, currentCname);
        origin += increment;
        Database.setCharacterInfo("luck", currentUname, currentUname, origin);
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
    //////////////////////////////////////////////////////////////////////////////// char, pet, trop 추가
    public void addCharacter(String cname) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        int date = Integer.parseInt(simpleDateFormat.format(cal.getTime()));
        Database.addCharacter(currentUname, currentCname, date);
    }
    public void addPet(int type) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        int pid = Integer.parseInt(simpleDateFormat.format(cal.getTime()));
        Database.addPet(pid, currentUname, type);
    }
    public void addTrophy(int type) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        int tid = Integer.parseInt(simpleDateFormat.format(cal.getTime()));
        Database.addPet(tid, currentUname, type);
    }
    ////////////////////////////////////////////////////////////////////////////////// get pet, trophy array
    public ArrayList<Integer> getPetArray() {
        return Database.getPetArray(currentUname);
    }
    public ArrayList<Integer> getTrophyArray() {
        return Database.getTrophyArray(currentUname);
    }
    ////////////////////////////////////////////////////////////////////////////////
}
