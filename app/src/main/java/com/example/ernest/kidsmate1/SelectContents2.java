package com.example.ernest.kidsmate1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class SelectContents2 extends AppCompatActivity {
    Button button_game_blankGuessing_bossBattle;
    Button button_game_imageGuessing_bossBattle;
    Button button_game_wordChain_bossBattle;
    LinearLayout linearLayout_buttonLayer;
    DatabaseStateManager mDatabaseStateManager;

    DatabaseTestStub mDatabaseTestStub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_contents2);
        button_game_blankGuessing_bossBattle = (Button) findViewById(R.id.button_game_blankGuessing_bossBattle);
        button_game_imageGuessing_bossBattle = (Button) findViewById(R.id.button_game_imageGuessing_bossBattle);
        button_game_wordChain_bossBattle = (Button) findViewById(R.id.button_game_wordChain_bossBattle);
        //linearLayout_buttonLayer = (LinearLayout) findViewById(R.id.linearLayout_buttonLayer);

        //linearLayout_buttonLayer.removeView(button_game_blankGuessing_bossBattle);
        //linearLayout_buttonLayer.removeView(button_game_imageGuessing_bossBattle);
        //linearLayout_buttonLayer.removeView(button_game_wordChain_bossBattle);

        button_game_blankGuessing_bossBattle.setEnabled(false);
        button_game_imageGuessing_bossBattle.setEnabled(false);
        button_game_wordChain_bossBattle.setEnabled(false);

        mDatabaseStateManager = DatabaseStateManager.getInstance();

        mDatabaseTestStub = DatabaseTestStub.getInstance(); // // TODO: 2017-05-09 test 유닛 제거 
    }

    @Override
    protected void onResume(){
        super.onResume();
        Dialog_Game_Result game_result = new Dialog_Game_Result(this);
        String msg = "";
        
        if(mDatabaseStateManager.getUserBG_count() >= 10 /*mDatabaseStateManager.getUserBG_bcount()*/){
            button_game_blankGuessing_bossBattle.setEnabled(true);
            msg = msg + "빈칸맞추기 보스전 플레이 가능!\n";
        }else{
            button_game_blankGuessing_bossBattle.setEnabled(false);
        }
        
        if(mDatabaseStateManager.getUserIG_count() >= 10 /*mDatabaseStateManager.getUserIG_bcount()*/){
            button_game_imageGuessing_bossBattle.setEnabled(true);
            msg = msg + "그림맞추기 보스전 플레이 가능!\n";
        }else{
            button_game_imageGuessing_bossBattle.setEnabled(false);
        }
        
        if(mDatabaseStateManager.getUserWC_count() >= 10 /*mDatabaseStateManager.getUserWC_bcount()*/){
            button_game_wordChain_bossBattle.setEnabled(true);
            msg = msg + ("끝말잇기 보스전 플레이 가능!\n");
        }else{
            button_game_wordChain_bossBattle.setEnabled(false);
        }

        // // TODO: 2017-05-09 트로피 지급의 예시를 보여주기 위해 간단하게 작성했지만, 추후에 조건을 변경해야 함.

        // // TODO: 2017-05-09 트로피 중복 지급 문제가 있음. 

        if(mDatabaseTestStub.isTrophyIssued(0)==false && mDatabaseStateManager.getUserBG_count() >= 3){
            // 빈칸맞추기 3회 플레이 트로피!
            msg = msg + mDatabaseTestStub.getTrophyString(0) + " 획득!\n";
            mDatabaseTestStub.setTrophyAchieved(0);
            mDatabaseTestStub.setTrophyIssued(0);
        }

        if(mDatabaseTestStub.isTrophyIssued(1)==false && mDatabaseStateManager.getUserIG_count() >= 3){
            // 그림맞추기 3회 플레이 트로피!
            msg = msg + mDatabaseTestStub.getTrophyString(1) + " 획득!\n";
            mDatabaseTestStub.setTrophyAchieved(1);
            mDatabaseTestStub.setTrophyIssued(1);
        }

        if(mDatabaseTestStub.isTrophyIssued(2)==false && mDatabaseStateManager.getUserWC_count() >= 3){
            // 끝말잇기 3회 플레이 트로피!
            msg = msg + mDatabaseTestStub.getTrophyString(2) + " 획득!\n";
            mDatabaseTestStub.setTrophyAchieved(2);
            mDatabaseTestStub.setTrophyIssued(2);
        }

        if(mDatabaseTestStub.isTrophyIssued(3)==false && mDatabaseStateManager.getCharacterLuck() >= 10){
            // 행운 스탯 10 달성 트로피!
            msg = msg + mDatabaseTestStub.getTrophyString(3) + " 획득!\n";
            mDatabaseTestStub.setTrophyAchieved(3);
            mDatabaseTestStub.setTrophyIssued(3);
        }

        if(mDatabaseTestStub.isTrophyIssued(4)==false && mDatabaseStateManager.getCharacterPower() >= 10){
            // 힘 스탯 10 달성 트로피!
            msg = msg + mDatabaseTestStub.getTrophyString(4) + " 획득!\n";
            mDatabaseTestStub.setTrophyAchieved(4);
            mDatabaseTestStub.setTrophyIssued(4);
        }

        if(mDatabaseTestStub.isTrophyIssued(5)==false && mDatabaseStateManager.getCharacterSmart() >= 10){
            // 지능 스탯 10 달성 트로피!
            msg = msg + mDatabaseTestStub.getTrophyString(5) + " 획득!\n";
            mDatabaseTestStub.setTrophyAchieved(5);
            mDatabaseTestStub.setTrophyIssued(5);
        }

        if(mDatabaseTestStub.isTrophyIssued(6)==false && mDatabaseStateManager.getUserTW_count() >= 3){
            // 오늘의 단어 3회 플레이 트로피!
            msg = msg + mDatabaseTestStub.getTrophyString(6) + " 획득!\n";
            mDatabaseTestStub.setTrophyAchieved(6);
            mDatabaseTestStub.setTrophyIssued(6);
        }

        int trophyCount = 0;
        int trophyListSize = mDatabaseTestStub.getTrophyListSize();
        for(int index=0; index < trophyListSize; index++){
            if(mDatabaseTestStub.isTrophyAchieved(index)==true){
                trophyCount++;
            }
        }

        if(mDatabaseTestStub.isPetAchieved(0)==false && trophyCount>=1){
            // 코리안 숏헤어 고양이
            msg = msg + mDatabaseTestStub.getPetString(0) + " 획득!\n";
            mDatabaseTestStub.setPetAchieved(0);
            mDatabaseTestStub.setPetIssued(0);
        }

        if(mDatabaseTestStub.isPetAchieved(1)==false && trophyCount>=3){
            // 페르시안 고양이
            msg = msg + mDatabaseTestStub.getPetString(1) + " 획득!\n";
            mDatabaseTestStub.setPetAchieved(1);
            mDatabaseTestStub.setPetIssued(1);
        }

        if(mDatabaseTestStub.isPetAchieved(2)==false && trophyCount>=7){
            // 샴 고양이
            msg = msg + mDatabaseTestStub.getPetString(2) + " 획득!\n";
            mDatabaseTestStub.setPetAchieved(2);
            mDatabaseTestStub.setPetIssued(2);
        }

        // // TODO: 2017-05-09 전직 구현 (레벨, 스탯 연관)
        
        // // TODO: 2017-05-09 알파벳 컬렉팅, 캐릭터 제공 구현 

        game_result.setGameResultText(msg);
        if(msg.length()>0) {
            game_result.show();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void clicked_Feature_Dictionary(View v) {
        Intent intent = new Intent(SelectContents2.this, Feature_Dictionary.class);
        startActivity(intent);
    }

    protected void clicked_Feature_TodaysWord(View v) {
        Intent intent = new Intent(SelectContents2.this, Feature_TodaysWord.class);
        startActivity(intent);
    }

    protected void clicked_Game_BlankGuessing(View v) {
        Intent intent = new Intent(SelectContents2.this, Game_BlankGuessing.class);
        startActivity(intent);
    }

    protected void clicked_Game_ImageGuessing(View v) {
        Intent intent = new Intent(SelectContents2.this, Game_ImageGuessing.class);
        startActivity(intent);
    }

    protected void clicked_Game_WordChain(View v) {
        Intent intent = new Intent(SelectContents2.this, Game_WordChain.class);
        startActivity(intent);
    }

    protected void clicked_Test_WordChain(View v) {
        Intent intent = new Intent(SelectContents2.this, Test_WordChain.class);
        startActivity(intent);
    }

    protected void clicked_Character_Info(View v) {
        Intent intent = new Intent(SelectContents2.this, Character_Info.class);
        startActivity(intent);
    }

    protected void clicked_Game_BlankGuessing_BossBattle(View v) {
        Intent intent = new Intent(SelectContents2.this, Game_BlankGuessing_BossBattle.class);
        startActivity(intent);
    }

    protected void clicked_Game_ImageGuessing_BossBattle(View v) {
        Intent intent = new Intent(SelectContents2.this, Game_ImageGuessing_BossBattle.class);
        startActivity(intent);
    }

    protected void clicked_Game_WordChain_BossBattle(View v) {
        Intent intent = new Intent(SelectContents2.this, Game_WordChain_BossBattle.class);
        startActivity(intent);
    }

    protected void clicked_Layout_Test(View v) {
        Intent intent = new Intent(SelectContents2.this, Layout_Test.class);
        startActivity(intent);
    }

    protected void clicked_Game_ImageGuessing_TestView(View v) {
        Intent intent = new Intent(SelectContents2.this, Game_ImageGuessing_TestView.class);
        startActivity(intent);
    }

    protected void clicked_Game_BlankGuessing_TestView(View v) {
        Intent intent = new Intent(SelectContents2.this, Game_BlankGuessing_TestView.class);
        startActivity(intent);
    }

    protected void clicked_Game_WordChain_TestView(View v) {
        Intent intent = new Intent(SelectContents2.this, Game_WordChain_TestView.class);
        startActivity(intent);
    }
}