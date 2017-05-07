package com.example.ernest.kidsmate1;

import android.content.DialogInterface;

import java.util.Random;

class Game_BlankGuessing_BossBattle extends Game_BlankGuessing {
    protected char presentedChar;

    @Override
    protected void sendResultToDatabase(){
        /*
        데이터베이스에 결과를 전송
         */
        if(session_admin.getCorrectRound() >= session_admin.getGoalRound()){
            mDatabaseTestStub.addCharacterExp(mDatabaseTestStub.getEarnedExpWhenSuccess());

            Random random = new Random();
            String atoz = "abcdefghijklmnopqrstuvwxyz";
            presentedChar = atoz.charAt(random.nextInt(atoz.length()));
            this.mDatabaseTestStub.addAlphabet(presentedChar);

        }else{
            mDatabaseTestStub.addCharacterExp(mDatabaseTestStub.getEarnedExpWhenFailure());
        }
        mDatabaseTestStub.addStatBlankGuessing(session_admin.getCorrectRound());
    }

    @Override
    protected void showTotalResult(){
        /*
        모든 라운드가 끝나고 세션의 결과를 표시
         */
        Game_Result game_result = new Game_Result(this);
        game_result.setGameResultText(
                "CurrentRound: "+session_admin.getCurrentRound()+
                        "\nCorrectRound: "+session_admin.getCorrectRound()+
                        "\nCurrentExp: "+mDatabaseTestStub.getCurrentExp()+
                        "\nLevelUpExp: "+mDatabaseTestStub.getLevelUpExp()+
                        "\npresentedChar: "+presentedChar
        );
        game_result.setOnCancelListener(new DialogInterface.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialog){
                Game_BlankGuessing_BossBattle.this.finish();
            }
        });
        game_result.show();
    }
}