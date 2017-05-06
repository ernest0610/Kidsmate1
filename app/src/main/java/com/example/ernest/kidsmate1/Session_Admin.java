package com.example.ernest.kidsmate1;

import java.util.ArrayList;

class Session_Admin {
    private int maxRound;
    private int currentRound;
    private int goalRound;

    public enum resultCode {CORRECT, WRONG, PASSED, ERROR}

    private int correctRound;
    private int wrongRound;
    private int passedRound;
    private int errorRound;

    private ArrayList<resultCode> roundResultArray;

    public Session_Admin(int maxRound, int goalRound){
        this.maxRound = maxRound;
        this.currentRound = 1;
        this.goalRound = goalRound;

        this.correctRound = 0;
        this.wrongRound = 0;
        this.passedRound = 0;
        this.errorRound = 0;

        roundResultArray = new ArrayList(maxRound);
    }

    public void reportGameResult(resultCode result){
        roundResultArray.add(result);
        currentRound++;

        switch (result){
            case CORRECT:
                correctRound++;
                break;
            case WRONG:
                wrongRound++;
                break;
            case PASSED:
                passedRound++;
                break;
            case ERROR:
                errorRound++;
                break;
            default:
                break;
        }
    }

    public int getCorrectRound(){
        return this.correctRound;
    }

    public int getCurrentRound(){
        return this.currentRound;
    }

    public int getMaxRound(){
        return this.maxRound;
    }

    public int getGoalRound(){
        return this.goalRound;
    }

    public ArrayList<resultCode> getroundResultArray(){
        return roundResultArray;
    }

}
