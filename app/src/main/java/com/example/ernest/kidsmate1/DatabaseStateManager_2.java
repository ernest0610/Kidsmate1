package com.example.ernest.kidsmate1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseStateManager_2 {
    protected static final String TAG = DatabaseStateManager_2.class.getSimpleName();
    private static final int DB_MAX_SIZE = 3017;

    private static DatabaseStateManager_2 mDatabaseStateManager = null;
    private String currentUname = "";
    private String currentCname = "";
    private DatabaseHelper_2 databaseHelper;
    private SQLiteDatabase database;
    private Cursor cursor;
    private Object result;
    private Context context;

    private DatabaseStateManager_2(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper_2(context);
    }

    public static DatabaseStateManager_2 getInstance(Context context) { /// constructor
        if(mDatabaseStateManager == null) {
            synchronized (DatabaseStateManager_2.class) {
                if(mDatabaseStateManager == null) {
                    mDatabaseStateManager = new DatabaseStateManager_2(context);
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

    /////////////////////////////////////////user get set
    public Object getUser(String column) {
        result = null;
        database = databaseHelper.openDatabase();
        cursor = database.rawQuery("SELECT " + column + " FROM user WHERE unmae = '" + currentUname + "';", null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            if(isText(column))
                result = cursor.getString(0);
            else
                result = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return result;
    }

    public void setUser(String column, Object value) {
        database = databaseHelper.openDatabase();
        if(isText(column))
            database.execSQL("UPDATE user SET " + column + " = '" + (String)value + "' WHERE uname = '" + currentUname + "';");
        else
            database.execSQL("UPDATE user SET " + column + " = " + (int)value + " WHERE uname = '" + currentUname + "';");
        database.close();
    }

    public void addUser(String uname) {
        database = databaseHelper.openDatabase();
        database.execSQL("INSERT INTO user (uname) VALUES ('" + uname + "');");
        database.close();
    }

    public void delUser(String uname) {
        database = databaseHelper.openDatabase();
        database.execSQL("DELETE FROM user WHERE uname = '" + uname + "';");
        database.close();
    }
    /////////////////////////////////////////////
    /////////////////////////////////////////char get set
    public Object getCharacter(String column) {
        result = null;
        database = databaseHelper.openDatabase();
        cursor = database.rawQuery("SELECT " + column + " FROM character WHERE unmae = '" + currentUname + "' AND cname = '" + currentCname + ";", null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            if(isText(column))
                result = cursor.getString(0);
            else
                result = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return result;
    }

    public void setCharacter(String column, Object value) {
        database = databaseHelper.openDatabase();
        if(isText(column))
            database.execSQL("UPDATE character SET " + column + " = '" + (String)value + "' WHERE uname = '" + currentUname + "' AND cname = '" + currentCname + "';");
        else
            database.execSQL("UPDATE character SET " + column + " = " + (int)value + " WHERE uname = '" + currentUname + "' AND cname = '" + currentCname + "';");
        database.close();
    }

    public void addCharacter(String cname) {
        database = databaseHelper.openDatabase();
        database.execSQL("INSERT INTO character (uname, cname) VALUES ('" + currentUname + "', '" + cname + "');");
        database.close();
    }

    public void delCharacter(String cname) {
        database = databaseHelper.openDatabase();
        database.execSQL("DELETE FROM character WHERE uname = '" + currentUname + "' AND cname = '" + cname + "';");
        database.close();
    }
    //////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////characterinfo get
    public Object getCharacterInfo(String column, String job) {
        result = null;
        database = databaseHelper.openDatabase();
        cursor = database.rawQuery("SELECT " + column + " FROM characterinfo WHERE job = '" + job + "';", null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            if(isText(column))
                result = cursor.getString(0);
            else
                result = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return result;
    }
    //////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////dictionary get
    public Object getDictionaray(String column, String word) {
        result = null;
        database = databaseHelper.openDatabase();
        cursor = database.rawQuery("SELECT " + column + " FROM characterinfo WHERE word = '" + word + "';", null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            if(isText(column))
                result = cursor.getString(0);
            else
                result = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return result;
    }

    public String getRandomWord() {
        database = databaseHelper.openDatabase();
        int id = (int)(Math.random() * Integer.MAX_VALUE) % DB_MAX_SIZE + 1;
        String word = "";
        Cursor cursor = database.rawQuery("SELECT word FROM dictionary WHERE id = " + id, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast())
            word = cursor.getString(0);
        cursor.close();
        database.close();
        return word;
    }

    public String[] getRandomWordMean() {
        database = databaseHelper.openDatabase();
        int id = (int)(Math.random() * Integer.MAX_VALUE) % DB_MAX_SIZE + 1;
        String[] word = new String[]{"", ""};
        Cursor cursor = database.rawQuery("SELECT word, mean FROM dictionary WHERE id = " + id, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            word[0] = cursor.getString(0);
            word[1] = cursor.getString(1);
        }
        cursor.close();
        database.close();
        return word;
    }

    public String getRandomWordStartWith(String start) {
        database = databaseHelper.openDatabase();
        String word = "";
        Cursor cursor = database.rawQuery("SELECT word FROM dictionary WHERE word LIKE '" + start + "%' COLLATE NOCASE", null);
        cursor.moveToFirst();
        int id = 0;
        if(!cursor.isAfterLast()) {
            id = (int) (Math.random() * Integer.MAX_VALUE) % cursor.getCount();
            cursor.move(id);
            word = cursor.getString(0);
        }
        cursor.close();
        database.close();
        return word;
    }

    public String[] getRandomWordMeanStartWith(String start) {
        database = databaseHelper.openDatabase();
        String[] word = new String[]{"", ""};
        Cursor cursor = database.rawQuery("SELECT word, mean FROM dictionary WHERE word LIKE '" + start + "%' COLLATE NOCASE", null);
        int id = (int)(Math.random() * Integer.MAX_VALUE) % cursor.getCount();
        cursor.move(id);
        if(!cursor.isAfterLast()) {
            word[0] = cursor.getString(0);
            word[1] = cursor.getString(1);
        }
        cursor.close();
        database.close();
        return word;
    }
    ////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////game get set
    public Object getGame(String column, String gametype) {
        result = null;
        database = databaseHelper.openDatabase();
        cursor = database.rawQuery("SELECT " + column + " FROM game WHERE gametype = '" + gametype + "';", null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            if(isText(column))
                result = cursor.getString(0);
            else
                result = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return result;
    }

    public void setGame(String column, String gametype, Object value) {
        database = databaseHelper.openDatabase();
        if(isText(column))
            database.execSQL("UPDATE game SET " + column + " = '" + (String)value + "' WHERE gametype = '" + gametype + "';");
        else
            database.execSQL("UPDATE game SET " + column + " = " + (int)value + " WHERE gametype = '" + gametype + "';");
        database.close();
    }

    public void addGame(String gametype) {
        database = databaseHelper.openDatabase();
        database.execSQL("INSERT INTO gmae (gametype) VALUES ('" + gametype + "');");
        database.close();
    }

    public void delGame(String gametype) {
        database = databaseHelper.openDatabase();
        database.execSQL("DELETE FROM game WHERE gametype = '" + gametype + "';");
        database.close();
    }
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////leveltable get
    public Object getLeveltable(String column, int level) {
        result = null;
        database = databaseHelper.openDatabase();
        cursor = database.rawQuery("SELECT " + column + " FROM leveltable WHERE level = " + level + ";", null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            if(isText(column))
                result = cursor.getString(0);
            else
                result = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return result;
    }
    /////////////////////////////////////////////////////////////////////
    public Object getPet(String column, String pid) {
        result = null;
        database = databaseHelper.openDatabase();
        cursor = database.rawQuery("SELECT " + column + " FROM pet WHERE uname = '" + currentUname + "' AND pid = '" + pid + "';", null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            if(isText(column))
                result = cursor.getString(0);
            else
                result = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return result;
    }

    public void setPet(String column, String pid, Object value) {
        database = databaseHelper.openDatabase();
        if(isText(column))
            database.execSQL("UPDATE pet SET " + column + " = '" + (String)value + "' WHERE uname = '" + currentUname + "' AND pid = '" + pid + "';");
        else
            database.execSQL("UPDATE pet SET " + column + " = " + (int)value + " WHERE uname = '" + currentUname + "' AND pid = '" + pid + "';");
        database.close();
    }

    public void addPet(String pid) {
        database = databaseHelper.openDatabase();
        database.execSQL("INSERT INTO pet (uname, pid) VALUES ('" + currentUname + "', '" + pid + "');");
        database.close();
    }

    public void delPet(String pid) {
        database = databaseHelper.openDatabase();
        database.execSQL("DELETE FROM pet WHERE uname = '" + currentUname + "' AND pid = '" + pid + "';");
        database.close();
    }
    /////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////petinfo get
    public Object getPetInfo(String column, String pettype) {
        result = null;
        database = databaseHelper.openDatabase();
        cursor = database.rawQuery("SELECT " + column + " FROM petinfo WHERE pettype = '" + pettype + "';", null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            if(isText(column))
                result = cursor.getString(0);
            else
                result = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return result;
    }
    ///////////////////////////////////////////////////////////
    //////////////////////////////////////////////////trophy get set
    public Object getTrophy(String column, String tid) {
        result = null;
        database = databaseHelper.openDatabase();
        cursor = database.rawQuery("SELECT " + column + " FROM trophy WHERE uname = '" + currentUname + "' AND tid = '" + tid + "';", null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            if(isText(column))
                result = cursor.getString(0);
            else
                result = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return result;
    }

    public void setTrophy(String column, String tid, Object value) {
        database = databaseHelper.openDatabase();
        if(isText(column))
            database.execSQL("UPDATE trophy SET " + column + " = '" + (String)value + "' WHERE uname = '" + currentUname + "' AND tid = '" + tid + "';");
        else
            database.execSQL("UPDATE trophy SET " + column + " = " + (int)value + " WHERE uname = '" + currentUname + "' AND tid = '" + tid + "';");
        database.close();
    }

    public void addTrophy(String tid) {
        database = databaseHelper.openDatabase();
        database.execSQL("INSERT INTO trophy (uname, tid) VALUES ('" + currentUname + "', '" + tid + "');");
        database.close();
    }

    public void delTrophy(String tid) {
        database = databaseHelper.openDatabase();
        database.execSQL("DELETE FROM trophy WHERE uname = '" + currentUname + "' AND tid = '" + tid + "';");
        database.close();
    }
    ///////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////trophyinfo get
    public Object getTrophyInfo(String column, String trophytype) {
        result = null;
        database = databaseHelper.openDatabase();
        cursor = database.rawQuery("SELECT " + column + " FROM trophyinfo WHERE trophytype = '" + trophytype + "';", null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            if(isText(column))
                result = cursor.getString(0);
            else
                result = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return result;
    }
    ///////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////userdictionary get set
    public Object getUserDictionary(String column, int id) {
        result = null;
        database = databaseHelper.openDatabase();
        cursor = database.rawQuery("SELECT " + column + " FROM userdictionary WHERE uname = '" + currentUname + "' AND id = " + id + ";", null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            if(isText(column))
                result = cursor.getString(0);
            else
                result = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return result;
    }

    public void setUserDictionary(String column, int id, Object value) {
        database = databaseHelper.openDatabase();
        if(isText(column))
            database.execSQL("UPDATE userdictionary SET " + column + " = '" + (String)value + "' WHERE uname = '" + currentUname + "' AND id = " + id + ";");
        else
            database.execSQL("UPDATE userdictionary SET " + column + " = " + (int)value + " WHERE uname = '" + currentUname + "' AND id = " + id + ";");
        database.close();
    }

    public void addUserDictionary(int id) {
        database = databaseHelper.openDatabase();
        database.execSQL("INSERT INTO userdictionary (uname, id) VALUES ('" + currentUname + "', " + id + ");");
        database.close();
    }

    public void delUserDictionary(String id) {
        database = databaseHelper.openDatabase();
        database.execSQL("DELETE FROM userdictionary WHERE uname = '" + currentUname + "' AND id = " + id + ";");
        database.close();
    }
    ////////////////////////////////////////////////////////////////////////////////

    /*// 전직
    public void setCharacterJob(String job) {
        Database.setCharacterJob(currentUname, currentCname, job);
    }
    public String getChracterJob(){
        return Database.getCurrentJob(currentUname, currentCname);
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

        //다음 레벨업에 필요한 경험치를 얻는다.

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
    }*/
    private Boolean isText(String column) {
        if(column.equals("uname") || column.equals("TW_lastplaydate") || column.equals("alpha_kerberos")
                || column.equals("alpha_griffon") || column.equals("alpha_pyramid") || column.equals("cname")
                || column.equals("job") || column.equals("data") || column.equals("name") || column.equals("word")
                || column.equals("mean") || column.equals("gametype") || column.equals("fullalpha") || column.equals("pid")
                || column.equals("pettype") || column.equals("tid") || column.equals("trophytype") || column.equals("description")
                || column.equals("addeddate"))
            return true;
        else
            return false;
    }
}
