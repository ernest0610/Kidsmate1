package com.example.ernest.kidsmate1;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.naver.speech.clientapi.SpeechRecognitionResult;

import java.util.List;
import java.util.Random;

public class Game_WordChain extends AppCompatActivity {
    // 모든 액티비티가 가지고 있어야 하는 요소.
    private VoiceRecognizer mVoiceRecognizer; // 싱글톤
    private EventHandler mEventHandler; // 각 액티비티 고유의 이벤트 핸들러
    private VoiceSynthesizer mVoiceSynthesizer; // 음성 합성 API

    // test stub
    private DatabaseTestStub mDatabaseTestStub;

    // 디버깅 메시지
    private static final String TAG = Game_WordChain.class.getSimpleName();

    // 액티비티들 공통 UI
    private TextView textView_word;
    private TextView textView_mean;

    private EditText editText_inputWord;
    private Button button_inputWordAccept;

    private Button button_start;
    private Button button_next;
    private Button button_playSound;

    // 퀴즈를 진행하기 위한 변수
    private String givenWord; // 컴퓨터가 제시한 단어를 기록하는 변수.
    private boolean isRightAnswer; // 적절한 단어를 말했는지 기록하는 변수.
    private String RightAnswer; // 답한 단어를 기록하는 변수.
    private Session_Admin session_admin; // 세션을 관리하는 변수
    private int opportunity; // 발음할 수 있는 횟수를 제한하는 변수.

    //함수 시작
    private boolean roundInit() {
        /*
        세션이 처음 시작될때 딱 한번 onCreate에서 호출되는 함수.
         */
        // 문제를 얻어온다.
        Random random = new Random();
        String atoz = "abcdefghijklmnopqrstuvwxyz";
        roundInit(atoz.charAt(random.nextInt(26)));
        return true;
    }
    private boolean roundInit(char ch) {
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

    private void checkAnswer(String word){
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
            }
        }
    }

    private void applyResult(Session_Admin.resultCode result){
        /*
        정답을 데이터베이스에 반영하고, 다음 문제를 출제하거나, 세션을 종료하는 함수.
         */
        session_admin.reportGameResult(result);
        if(session_admin.getCurrentRound() <= session_admin.getMaxRound()
                && result == Session_Admin.resultCode.CORRECT){
            roundInit(RightAnswer.toLowerCase().charAt(RightAnswer.length() - 1));
        }else{
            if(session_admin.getCorrectRound() >= session_admin.getGoalRound()){
                mDatabaseTestStub.addCharacterExp(mDatabaseTestStub.getEarnedExpWhenSuccess());
            }else{
                mDatabaseTestStub.addCharacterExp(mDatabaseTestStub.getEarnedExpWhenFailure());
            }
            mDatabaseTestStub.addStatWordChain(session_admin.getCorrectRound());

            button_next.setEnabled(false);
            button_start.setEnabled(true);
            button_playSound.setEnabled(false);
            button_inputWordAccept.setEnabled(false);

            button_start.setText("메인화면으로.");
            button_start.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    onBackPressed2();
                }
            });
        }
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady: //// TODO: 2017-05-06 준비 메시지가 늦게 도착하는 경우 메인화면으로 텍스트보다 나중에 갱신되어 이 메시지가 뜨는 경우가 있음.
                button_start.setText("연결됨");
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
        mEventHandler = new EventHandler(this);
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
                mVoiceSynthesizer.setString(givenWord);
                mVoiceSynthesizer.doSynthsize();
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
        mVoiceRecognizer.initialize(mEventHandler);
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
}