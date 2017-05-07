package com.example.ernest.kidsmate1;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.naver.speech.clientapi.SpeechRecognitionResult;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Random;

public class Game_BlankGuessing extends AppCompatActivity {
    // 디버깅 메시지
    protected static final String TAG = Game_BlankGuessing.class.getSimpleName();

    // 모든 액티비티가 가지고 있어야 하는 요소.
    protected VoiceRecognizer mVoiceRecognizer; // 싱글톤
    protected InnerEventHandler mInnerEventHandler; // 각 액티비티 고유의 이벤트 핸들러
    protected VoiceSynthesizer mVoiceSynthesizer; // 음성 합성 API

    // test stub
    protected DatabaseTestStub mDatabaseTestStub;

    // 액티비티들 공통 UI
    protected TextView textView_word;
    protected TextView textView_mean;

    protected EditText editText_inputWord;
    protected Button button_inputWordAccept;

    protected Button button_start;
    protected Button button_next;
    protected Button button_playSound;

    // 퀴즈를 진행하기 위한 변수
    protected String correctAnswer; // 정답을 기록하는 변수
    protected String correctAnsersMean; // 정답의 의미를 기록하는 변수
    protected boolean isRightAnswer; // 정답을 맞췄는지 기록하는 변수
    protected Session_Admin session_admin; // 세션을 관리하는 변수
    protected int opportunity; // 발음할 수 있는 횟수를 제한하는 변수.

    protected int[] blankIndex = {0}; // 빈칸을 체크하는 변수
    protected boolean hintMean; // 힌트를 주었는지 체크하는 변수
    protected boolean hintSynthesizer;

    // 함수 시작
    protected boolean roundInit(){
        /*
        세션이 처음 시작될때 딱 한번 onCreate에서 호출되고, 그 이후에는 반드시 applyResult에서만 호출되어야 하는 함수.
         */
        // 문제를 얻어온다.
        String[] todayWord = Database.getRandomWordMean();
        correctAnswer = todayWord[0].toLowerCase();
        correctAnsersMean = todayWord[1];

        // 빈칸이 들어갈 위치 정하기.
        Random randomIndex = new Random();
        blankIndex[0] = randomIndex.nextInt(correctAnswer.length()-1);

        // 빈칸 만들어서 출력하기. 원본은 correctAnswer에 저장되어있고 변형되지 않음.
        StringBuffer mStringBuffer = new StringBuffer(correctAnswer);
        mStringBuffer.setCharAt(blankIndex[0], '_');
        textView_word.setText(mStringBuffer.toString());

        // 라운드 초기화.
        isRightAnswer = false;
        textView_mean.setText("");
        hintMean = false;
        hintSynthesizer = false;
        opportunity = 5;

        // 버튼 텍스트 초기화.
        button_start.setText("남은 기회는 " + Integer.toString(opportunity)+ "회.\n도전!");
        button_next.setText("현재 라운드: " + Integer.toString(session_admin.getCurrentRound())+ "\n패스(패배)");
        button_playSound.setText("발음 힌트");
        return true;
    }

    protected void checkAnswer(String word){
        /*
        정답을 체크하고, isRightAnswer의 값을 변경하는 함수. (결과를 데이터베이스에 반영하지는 않음)
        isRightAnswer의 값에 따라 이후 처리가 달라진다.
         */
        if(word.length()==1 && !isRightAnswer && word.toLowerCase().charAt(0) == correctAnswer.charAt(blankIndex[0])) {
            isRightAnswer = true;
            Log.d(TAG, "정답입니다.");
        }
    }

    protected void applyResult(Session_Admin.resultCode result){
        /*
        정답을 데이터베이스에 반영하고, 다음 문제를 출제하거나, 세션을 종료하는 함수.
         */
        session_admin.reportGameResult(result);
        if(session_admin.getCurrentRound() <= session_admin.getMaxRound()){
            roundInit();
        }else{
            endingSession();
        }
    }

    protected void endingSession(){
        button_next.setEnabled(false);
        button_start.setEnabled(true);
        button_playSound.setEnabled(false);
        button_inputWordAccept.setEnabled(false);

        sendResultToDatabase();
        showTotalResult();
    }

    protected void sendResultToDatabase(){
        /*
        데이터베이스에 결과를 전송
         */
        if(session_admin.getCorrectRound() >= session_admin.getGoalRound()){
            mDatabaseTestStub.addCharacterExp(mDatabaseTestStub.getEarnedExpWhenSuccess());
        }else{
            mDatabaseTestStub.addCharacterExp(mDatabaseTestStub.getEarnedExpWhenFailure());
        }
        mDatabaseTestStub.addStatBlankGuessing(session_admin.getCorrectRound());
    }

    protected void showTotalResult(){
        /*
        모든 라운드가 끝나고 세션의 결과를 표시
         */
        Game_Result game_result = new Game_Result(this);
        game_result.setGameResultText(
                "CurrentRound: "+session_admin.getCurrentRound()+
                        "\nCorrectRound: "+session_admin.getCorrectRound()+
                        "\nCurrentExp: "+mDatabaseTestStub.getCurrentExp()+
                        "\nLevelUpExp: "+mDatabaseTestStub.getLevelUpExp()
        );
        game_result.setOnCancelListener(new DialogInterface.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialog){
                Game_BlankGuessing.this.finish();
            }
        });
        game_result.show();
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady:
                button_start.setText("남은 기회는 " + Integer.toString(this.opportunity)+ "회.\n발음하세요.");
                break;
            case R.id.audioRecording:
                break;
            case R.id.partialResult:
                String partialResult = (String) msg.obj;
                Log.d(TAG, "partialResult: " + partialResult);
                checkAnswer(partialResult);
                break;
            case R.id.endPointDetected:
                break;
            case R.id.finalResult:
                List<String> results = ((SpeechRecognitionResult)(msg.obj)).getResults();
                for (String result: results) {
                    Log.d(TAG, "results: " + result);
                    checkAnswer(result);
                    if (isRightAnswer) break;
                }
                if (isRightAnswer) {
                    applyResult(Session_Admin.resultCode.CORRECT);
                }else{
                    if(opportunity>0){
                        opportunity--;
                        Log.d(TAG, "다시 발음 해 보세요.");
                    }else {
                        applyResult(Session_Admin.resultCode.WRONG);
                    }
                }
                break;
            case R.id.recognitionError:
                Log.d(TAG, "Error code : " + msg.obj.toString());
                button_start.setText("남은 기회는 " + Integer.toString(this.opportunity)+ "회.\n도전!");
                button_start.setEnabled(true);
                break;
            case R.id.endPointDetectTypeSelected:
                break;
            case R.id.clientInactive:
                button_start.setText("남은 기회는 " + Integer.toString(this.opportunity)+ "회.\n도전!");
                button_start.setEnabled(true);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 음성인식 API의 이벤트를 받을 핸들러 생성
        mInnerEventHandler = new InnerEventHandler(this);
        // 음성인식 API의 인스턴스를 받아옴.
        mVoiceRecognizer = VoiceRecognizer.getInstance(this);
        // 음성합성 API를 사용하기 위한 객체 생성.
        mVoiceSynthesizer = new VoiceSynthesizer(this);

        mDatabaseTestStub = DatabaseTestStub.getInstance();

        // UI 생성 (액티비티 공통)
        setContentView(R.layout.game_basic2);

        textView_word = (TextView) findViewById(R.id.textView_word);
        textView_mean = (TextView) findViewById(R.id.textView_mean);

        button_start = (Button) findViewById(R.id.button_start);
        button_next = (Button) findViewById(R.id.button_next);
        button_playSound = (Button) findViewById(R.id.button_playSound);

        editText_inputWord = (EditText) findViewById(R.id.editText_inputWord);
        button_inputWordAccept = (Button) findViewById(R.id.button_inputWordAccept);

        // UI 환경 설정 (액티비티마다 다름)
        button_next.setEnabled(true);
        button_start.setEnabled(true);
        button_playSound.setEnabled(true);
        button_inputWordAccept.setEnabled(true);

        // UI 리스너 구현
        button_start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!mVoiceRecognizer.isRunning()) {
                    button_start.setText("남은 기회는 " + Integer.toString(opportunity)+ "회.\n기다리세요.");
                    mVoiceRecognizer.recognize();
                } else {
                    button_start.setEnabled(false);
                    mVoiceRecognizer.stop();
                }
            }
        });

        button_next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                applyResult(Session_Admin.resultCode.PASSED);
            }
        });

        button_playSound.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mVoiceSynthesizer.setString(correctAnswer);
                mVoiceSynthesizer.doSynthsize();
                if(hintMean == false) {
                    if (hintSynthesizer == false) {
                        hintSynthesizer = true;
                        button_playSound.setText("의미 힌트");
                    } else {
                        textView_mean.setText(correctAnsersMean);
                        hintMean = true;
                        button_playSound.setText("발음 힌트");
                    }
                }
            }
        });

        button_inputWordAccept.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String inputWord = editText_inputWord.toString();
                editText_inputWord.setText("");
                checkAnswer(inputWord);
                Log.d(TAG, "input: " + inputWord);

                if (isRightAnswer) {
                    applyResult(Session_Admin.resultCode.CORRECT);
                }else{
                    if(opportunity>0){
                        opportunity--;
                        Log.d(TAG, "다시 입력 해 보세요.");
                    }else {
                        applyResult(Session_Admin.resultCode.WRONG);
                    }
                }
            }
        });

        // 세션 초기화, 퀴즈 생성
        session_admin = new Session_Admin(mDatabaseTestStub.getMaxRound(), mDatabaseTestStub.getGoalRound());
        roundInit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 액티비티 시작시 반드시 음성인식 기능을 초기화 하여야 함.
        mVoiceRecognizer.initialize(mInnerEventHandler);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 액티비티 종료시 반드시 음성인식 기능을 릴리즈 하여야 함.
        mVoiceRecognizer.release();
    }

    protected void onBackPressed2(){
        super.onBackPressed();
    }

    protected static class InnerEventHandler extends Handler {
        // 이벤트 핸들러 이너 클래스
        private final WeakReference<Game_BlankGuessing> mActivity;
        InnerEventHandler(Game_BlankGuessing activity) {
            mActivity = new WeakReference(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            Game_BlankGuessing activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }
}