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
import android.widget.Toast;

import com.naver.speech.clientapi.SpeechRecognitionResult;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Random;

public class Game_WordChain extends AppCompatActivity {
    // 디버깅 메시지
    protected static final String TAG = Game_WordChain.class.getSimpleName();

    // 모든 액티비티가 가지고 있어야 하는 요소.
    protected VoiceRecognizer mVoiceRecognizer; // 싱글톤
    protected InnerEventHandler mInnerEventHandler; // 각 액티비티 고유의 이벤트 핸들러
    protected VoiceSynthesizer mVoiceSynthesizer; // 음성 합성 API

    // 데이터베이스 테스트 stub
    //protected DatabaseTestStub mDatabaseTestStub = DatabaseTestStub.getInstance();

    // 데이터베이스 상태 매니저
    protected DatabaseStateManager mDatabaseStateManager;

    // 액티비티들 공통 UI
    protected TextView textView_word;
    protected TextView textView_mean;

    protected EditText editText_inputWord;
    protected Button button_inputWordAccept;

    protected Button button_start;
    protected Button button_next;
    protected Button button_playSound;

    // 퀴즈를 진행하기 위한 변수
    protected String givenWord; // 컴퓨터가 제시한 단어를 기록하는 변수.
    protected boolean isRightAnswer; // 적절한 단어를 말했는지 기록하는 변수.
    protected String RightAnswer; // 답한 단어를 기록하는 변수.
    protected Session_Admin session_admin; // 세션을 관리하는 변수
    protected int opportunity; // 발음할 수 있는 횟수를 제한하는 변수.

    // 결과물 출력을 위해 임시로 기록해두는 변수
    protected boolean isLevelUp = false;
    protected int increasedExp = 0;

    //함수 시작
    protected boolean roundInit() {
        /*
        세션이 처음 시작될때 딱 한번 onCreate에서 호출되는 함수.
         */
        // 문제를 얻어온다.
        Random random = new Random();
        String atoz = "abcdefghijklmnopqrstuvwxyz";
        roundInit(atoz.charAt(random.nextInt(26)));
        return true;
    }
    protected boolean roundInit(char ch) {
        /*
        applyResult에서만 호출되어야 하는 함수.
         */
        // 문제를 얻어온다.
        givenWord = Database.getRandomWordStartWith(String.valueOf(ch));
        textView_word.setText(givenWord);

        // 라운드 초기화.
        isRightAnswer = false;
        textView_mean.setText("");
        opportunity = 5;

        // 버튼 텍스트 초기화.
        button_start.setText("남은 기회는 " + Integer.toString(opportunity)+ "회.\n도전!");
        button_next.setText("현재 라운드: " + Integer.toString(session_admin.getCurrentRound())+ "\n패스(패배)");
        //button_playSound.setText("발음 힌트"); // 필요한가?
        return true;
    }

    protected void checkAnswer(String word){
        /*
        정답을 체크하고, isRightAnswer의 값을 변경하는 함수. (결과를 데이터베이스에 반영하지는 않음)
        isRightAnswer의 값에 따라 이후 처리가 달라진다.
         */
        if(word.length()>=2) {
            Log.d(TAG, "givenWord[n]:" + givenWord.toLowerCase().charAt(givenWord.length() - 1) +
                    ", result[0]:" + word.toLowerCase().charAt(0));
            if ((givenWord.toLowerCase().charAt(givenWord.length() - 1)) == word.toLowerCase().charAt(0)) {
                isRightAnswer = true;
                RightAnswer = new String(word.toLowerCase());
                Log.d(TAG, "정답입니다.");
            }else {
                Log.d(TAG, "오답입니다.");
            }
        }else {
            Log.d(TAG, "한글자는 답이 될 수 없습니다.");
        }
    }

    protected void applyResult(Session_Admin.resultCode result){
        /*
        정답을 데이터베이스에 반영하고, 다음 문제를 출제하거나, 세션을 종료하는 함수.
         */
        session_admin.reportGameResult(result);
        if(session_admin.getCurrentRound() <= session_admin.getMaxRound()
                && result == Session_Admin.resultCode.CORRECT){
            roundInit(RightAnswer.toLowerCase().charAt(RightAnswer.length() - 1));
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
        mDatabaseStateManager.addUserWC_count(1);
        showTotalResult();
    }

    protected void sendResultToDatabase(){
        /*
        데이터베이스에 결과를 전송
         */
        if(session_admin.getCorrectRound() >= session_admin.getGoalRound()){
            increasedExp = mDatabaseStateManager.getEarnedExpWhenSuccess();
        }else if(session_admin.getCorrectRound() > 0){
            increasedExp = mDatabaseStateManager.getEarnedExpWhenFailure();
        }else{
            increasedExp = 0; // // TODO: 2017-05-09  0문제를 맞춰도 경험치가 5 상승하는건 불합리하므로, 최소 한문제를 맞춰야 경험치가 올라가도록 조정.
        }
        isLevelUp = mDatabaseStateManager.addCharacterExp(increasedExp);
        mDatabaseStateManager.addCharacterSmart(session_admin.getCorrectRound());
    }

    protected void showTotalResult(){
        /*
        모든 라운드가 끝나고 세션의 결과를 표시
         */
        String temp = "";
        Dialog_Game_Result game_result = new Dialog_Game_Result(this);
        game_result.setOnCancelListener(new DialogInterface.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialog){
                Game_WordChain.this.finish();
            }
        });
        if(isLevelUp) {temp = temp + "레벨업 하였습니다!\n";}
        temp = temp + "정답률: " + session_admin.getCorrectRound()  + "/" + (session_admin.getCurrentRound()-1) + "\n";
        temp = temp + "지능 스탯 상승: " + session_admin.getCorrectRound() + "\n";
        temp = temp + "오른 경험치: " + increasedExp + "\n"; // // TODO: 2017-05-09  목표 도달시에 경험치가 두배 상승했음을 보여줄 필요가 있음.
        temp = temp + "현재 경험치: " + mDatabaseStateManager.getCharacterExp() + "\n";
        temp = temp + "다음 레벨 까지 경험치: " + mDatabaseStateManager.getLevelUpExp() + "\n";
        game_result.setGameResultText(temp);
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
                for (int index = results.size()-1; index>=0; index--) {
                    /*
                    다른 퀴즈들과는 다르게 여기서는 순서를 거꾸로 하여 체크해야한다.
                    checkAnswer은 적합한 답을 발견하면 변수의 값을 교체하는데,
                    부정확한 답부터 순서대로 체크하여야, 가장 마지막에 가장 정확한 값이 남기 때문이다.
                     */
                    Log.d(TAG, "results: " + results.get(index));
                    checkAnswer(results.get(index));

                }
                if (isRightAnswer) {
                    Toast.makeText(getApplicationContext(), "정답입니다.", Toast.LENGTH_SHORT).show();
                    applyResult(Session_Admin.resultCode.CORRECT);
                }else{
                    if(opportunity>0){
                        opportunity--;
                        Toast.makeText(getApplicationContext(), "오답입니다.", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "다시 발음 해 보세요.");
                    }else {
                        Toast.makeText(getApplicationContext(), "라운드 패배.", Toast.LENGTH_SHORT).show();
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
        // 데이터베이스를 다루기 위한 객체
        mDatabaseStateManager = DatabaseStateManager.getInstance();

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
                mVoiceSynthesizer.setString(givenWord);
                mVoiceSynthesizer.doSynthsize();
            }
        });

        button_inputWordAccept.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String inputWord = editText_inputWord.getText().toString();
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
        //session_admin = new Session_Admin(mDatabaseTestStub.getMaxRound(), mDatabaseTestStub.getGoalRound());
        session_admin = new Session_Admin(mDatabaseStateManager.getMaxRound(), mDatabaseStateManager.getGoalRound());
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
        private final WeakReference<Game_WordChain> mActivity;
        InnerEventHandler(Game_WordChain activity) {
            mActivity = new WeakReference(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            Game_WordChain activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }
}