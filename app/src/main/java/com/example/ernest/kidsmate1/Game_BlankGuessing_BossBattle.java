package com.example.ernest.kidsmate1;

import android.content.DialogInterface;

import java.util.ArrayList;
import java.util.Random;

public class Game_BlankGuessing_BossBattle extends Game_BlankGuessing {
    protected char selectedChar;
    protected StringBuilder finalState;

    @Override
    protected void sendResultToDatabase(){
        /*
        데이터베이스에 결과를 전송
         */
        if(session_admin.getCorrectRound() >= session_admin.getGoalRound()){
            //mDatabaseTestStub.addCharacterExp(mDatabaseTestStub.getEarnedExpWhenSuccess());
            mStateManager.addCharacterExp(mStateManager.getEarnedExpWhenSuccess());

            setStringPuzzle();

        }else{
            //mDatabaseTestStub.addCharacterExp(mDatabaseTestStub.getEarnedExpWhenFailure());
            mStateManager.addCharacterExp(mStateManager.getEarnedExpWhenFailure());
        }
        //mDatabaseTestStub.addStatBlankGuessing(session_admin.getCorrectRound());
        mStateManager.addCharacterLuck(session_admin.getCorrectRound());
    }

    @Override
    protected void showTotalResult(){
        /*
        모든 라운드가 끝나고 세션의 결과를 표시
         */
        Game_Result game_result = new Game_Result(this);
        game_result.setOnCancelListener(new DialogInterface.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialog){
                Game_BlankGuessing_BossBattle.this.finish();
            }
        });
        game_result.setGameResultText(
                "CurrentRound: "+session_admin.getCurrentRound()+
                        "\nCorrectRound: "+session_admin.getCorrectRound()+
                        //"\nCurrentExp: "+mDatabaseTestStub.getCurrentExp()+
                        //"\nLevelUpExp: "+mDatabaseTestStub.getLevelUpExp()
                        "\nCurrentExp: "+mStateManager.getCharacterExp()+
                        "\nLevelUpExp: "+mStateManager.getLevelUpExp()+
                        "\nselectedChar: "+selectedChar+
                        "\nfinalState: "+finalState.toString()
        );
        game_result.show();
    }

    protected void setStringPuzzle(){
        // string 처리
        Random random = new Random();
        String target = mDatabaseTestStub.getUserAlpha_pyramid_fullString();
        String state = mDatabaseTestStub.getUserAlpha_pyramid();
        finalState = new StringBuilder();
        ArrayList<Integer> indexOfString = new ArrayList();
        int index = 0;
        for(;index < target.length() && index < state.length(); index++){
            if(target.charAt(index) != state.charAt(index)){
                finalState.append("_");
                indexOfString.add(index);
            }else{
                finalState.append(target.charAt(index));
            }
        }
        if(index < target.length()){
            for(;index < target.length(); index++){
                finalState.append("_");
                indexOfString.add(index);
            }
        }
        int selectedIndex = random.nextInt(indexOfString.size());
        char selectedChar = target.charAt(selectedIndex);
        finalState.setCharAt(selectedIndex, selectedChar);

        mDatabaseTestStub.setUserAlpha_pyramid(finalState.toString());
    }
}