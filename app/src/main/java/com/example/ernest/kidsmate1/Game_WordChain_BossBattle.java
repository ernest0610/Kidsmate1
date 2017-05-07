package com.example.ernest.kidsmate1;

import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Random;

public class Game_WordChain_BossBattle extends Game_WordChain {
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
        //mDatabaseTestStub.addStatWordChain(session_admin.getCorrectRound());
        mStateManager.addCharacterSmart(session_admin.getCorrectRound());
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
                Game_WordChain_BossBattle.this.finish();
            }
        });
        game_result.setGameResultText(
                "CurrentRound: "+(session_admin.getCurrentRound()-1)+
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

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        finalState = new StringBuilder();
    }

    protected void setStringPuzzle(){
        // string 처리
        Random random = new Random();
        String target = mDatabaseTestStub.getUserAlpha_griffin_fullString();
        String state = mDatabaseTestStub.getUserAlpha_griffin();
        ArrayList<Integer> indexOfString = new ArrayList<Integer>();
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
        selectedChar = target.charAt(selectedIndex);
        finalState.setCharAt(selectedIndex, selectedChar);

        mDatabaseTestStub.setUserAlpha_pyramid(finalState.toString());
    }
}