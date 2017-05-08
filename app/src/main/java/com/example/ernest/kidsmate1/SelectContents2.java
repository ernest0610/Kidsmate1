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
    StateManager mStateManager;

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

        mStateManager = StateManager.getInstance();

        mDatabaseTestStub = DatabaseTestStub.getInstance(); // // TODO: 2017-05-09 test 유닛 제거 
    }

    @Override
    protected void onResume(){
        super.onResume();
        Game_Result game_result = new Game_Result(this);
        String msg = "";
        
        if(mStateManager.getUserBG_count() >= mStateManager.getUserBG_bcount()){
            button_game_blankGuessing_bossBattle.setEnabled(true);
            msg = msg + "빈칸맞추기 보스전 플레이 가능!\n";
        }else{
            button_game_blankGuessing_bossBattle.setEnabled(false);
        }
        
        if(mStateManager.getUserIG_count() >= mStateManager.getUserIG_bcount()){
            button_game_imageGuessing_bossBattle.setEnabled(true);
            msg = msg + "그림맞추기 보스전 플레이 가능!\n";
        }else{
            button_game_imageGuessing_bossBattle.setEnabled(false);
        }
        
        if(mStateManager.getUserWC_count() >= mStateManager.getUserWC_bcount()){
            button_game_wordChain_bossBattle.setEnabled(true);
            msg = msg + ("끝말잇기 보스전 플레이 가능!\n");
        }else{
            button_game_wordChain_bossBattle.setEnabled(false);
        }

        // // TODO: 2017-05-09 트로피 지급의 예시를 보여주기 위해 간단하게 작성했지만, 추후에 조건을 변경해야 함.

        if(mStateManager.getUserBG_count() >= 3){
            // 빈칸맞추기 3회 플레이 트로피!
            mDatabaseTestStub.addTrophy("빈칸맞추기 3회 플레이 트로피!");
            msg = msg + ("빈칸맞추기 3회 플레이 트로피 획득!\n");
        }

        if(mStateManager.getUserIG_count() >= 3){
            // 그림맞추기 3회 플레이 트로피!
            mDatabaseTestStub.addTrophy("그림맞추기 3회 플레이 트로피!");
            msg = msg + ("그림맞추기 3회 플레이 트로피 획득!\n");
        }

        if(mStateManager.getUserWC_count() >= 3){
            // 끝말잇기 3회 플레이 트로피!
            mDatabaseTestStub.addTrophy("끝말잇기 3회 플레이 트로피!");
            msg = msg + ("끝말잇기 3회 플레이 트로피 획득!\n");
        }

        if(mStateManager.getCharacterLuck() >= 10){
            // 행운 스탯 10 달성 트로피!
            mDatabaseTestStub.addTrophy("행운 스탯 10 달성 트로피!");
            msg = msg + ("행운 스탯 10 달성 트로피 획득!\n");
        }

        if(mStateManager.getCharacterPower() >= 10){
            // 힘 스탯 10 달성 트로피!
            mDatabaseTestStub.addTrophy("힘 스탯 10 달성 트로피!");
            msg = msg + ("힘 스탯 10 달성 트로피 획득!\n");
        }

        if(mStateManager.getCharacterSmart() >= 10){
            // 지능 스탯 10 달성 트로피!
            mDatabaseTestStub.addTrophy("지능 스탯 10 달성 트로피!");
            msg = msg + ("지능 스탯 10 달성 트로피 획득!\n");
        }

        if(mStateManager.getUserTW_count() >= 3){
            // 오늘의 단어 3회 플레이 트로피!
            mDatabaseTestStub.addTrophy("오늘의 단어 3회 플레이 트로피!");
            msg = msg + ("오늘의 단어 3회 플레이 트로피 획득!\n");
        }

        // // TODO: 2017-05-09 펫 구현

        int tempSize = mDatabaseTestStub.getTrophyList().size();
        if(tempSize>=7){
            // 펫 추가
        }else if(tempSize>=3){
            // 펫 추가
        }else if(tempSize>=1){
            // 펫 추가
        }

        // // TODO: 2017-05-09 전직 구현 (레벨, 스탯 연관)
        
        // // TODO: 2017-05-09 알파벳 컬렉팅, 캐릭터 제공 구현 

        game_result.setGameResultText(msg);
        game_result.show();
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
}