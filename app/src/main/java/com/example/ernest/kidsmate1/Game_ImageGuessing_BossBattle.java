package com.example.ernest.kidsmate1;

import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Random;

public class Game_ImageGuessing_BossBattle extends Game_ImageGuessing {
    // 데이터베이스 테스트 stub
    protected DatabaseTestStub mDatabaseTestStub = DatabaseTestStub.getInstance(); // // TODO: 2017-05-09 test 유닛 제거

    protected char selectedChar;
    protected StringBuilder finalState;

    @Override
    protected void endingSession(){
        button_next.setEnabled(false);
        button_start.setEnabled(true);
        button_playSound.setEnabled(false);
        button_inputWordAccept.setEnabled(false);

        sendResultToDatabase();
        int tempInt = mDatabaseStateManager.getUserIG_count();
        mDatabaseStateManager.addUserIG_count(-tempInt); // // TODO: 2017-05-09  게임 플레이 카운트를 초기화하는 방식으로 대응하고 있지만, 게임 플레이 카운트가 누적되도록 변경해야 함.
        showTotalResult();
    }

    @Override
    protected void sendResultToDatabase(){
        /*
        데이터베이스에 결과를 전송
         */
        if(session_admin.getCorrectRound() >= session_admin.getGoalRound()){
            increasedExp = mDatabaseStateManager.getEarnedExpWhenSuccess();
            setStringPuzzle();
        }else if(session_admin.getCorrectRound() > 0){
            increasedExp = mDatabaseStateManager.getEarnedExpWhenFailure();
        }else{
            increasedExp = 0; // // TODO: 2017-05-09  0문제를 맞춰도 경험치가 5 상승하는건 불합리하므로, 최소 한문제를 맞춰야 경험치가 올라가도록 조정.
        }
        isLevelUp = mDatabaseStateManager.addCharacterExp(increasedExp);
        mDatabaseStateManager.addCharacterPower(session_admin.getCorrectRound());
    }

    @Override
    protected void showTotalResult(){
        /*
        모든 라운드가 끝나고 세션의 결과를 표시
         */
        String temp = "";
        Dialog_Game_Result game_result = new Dialog_Game_Result(this);
        game_result.setOnCancelListener(new DialogInterface.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialog){
                Game_ImageGuessing_BossBattle.this.finish();
            }
        });
        if(isLevelUp) {temp = temp + "레벨업 하였습니다!\n";}
        temp = temp + "정답률: " + session_admin.getCorrectRound()  + "/" + (session_admin.getCurrentRound()-1) + "\n";
        temp = temp + "행운 스탯 상승: " + session_admin.getCorrectRound() + "\n";
        temp = temp + "오른 경험치: " + increasedExp + "\n"; // // TODO: 2017-05-09  목표 도달시에 경험치가 두배 상승했음을 보여줄 필요가 있음.
        temp = temp + "현재 경험치: " + mDatabaseStateManager.getCharacterExp() + "\n";
        temp = temp + "다음 레벨 까지 경험치: " + mDatabaseStateManager.getLevelUpExp() + "\n";
        if(selectedChar == '0'){temp = temp + "모은 문자: 없음\n";}
        else{temp = temp + "모은 문자: " + selectedChar + "\n";}
        temp = temp + "지금까지 모은 문자들: " + finalState.toString() + "\n";
        game_result.setGameResultText(temp);
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
        String target = mDatabaseTestStub.getUserAlpha_kerberos_fullString(); // // TODO: 2017-05-09 test 유닛 제거
        String state = mDatabaseTestStub.getUserAlpha_kerberos(); // // TODO: 2017-05-09 test 유닛 제거
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

        mDatabaseTestStub.setUserAlpha_kerberos(finalState.toString()); // // TODO: 2017-05-09 test 유닛 제거
    }
}