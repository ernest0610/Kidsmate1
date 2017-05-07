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

public class Feature_TodaysWord extends AppCompatActivity {
    // 모든 액티비티가 가지고 있어야 하는 요소.
    private VoiceRecognizer mVoiceRecognizer; // 싱글톤
    private EventHandler mEventHandler; // 각 액티비티 고유의 이벤트 핸들러
    private VoiceSynthesizer mVoiceSynthesizer; // 음성 합성 API

    // test stub
    private DatabaseTestStub mDatabaseTestStub;

    // 디버깅 메시지
    private static final String TAG = Feature_TodaysWord.class.getSimpleName();

    // 액티비티들 공통 UI
    private TextView textView_word;
    private TextView textView_mean;

    private EditText editText_inputWord;
    private Button button_inputWordAccept;

    private Button button_start;
    private Button button_next;
    private Button button_playSound;

    // 오늘의 단어를 진행하기 위한 변수
    private String correctAnswer;
    private String correctAnsersMean;
    private boolean isRightAnswer;
    private Session_Admin session_admin; // 세션을 관리하는 변수

    // 함수 시작
    private boolean roundInit(){
        /*
        세션이 처음 시작될때 딱 한번 onCreate에서 호출되고, 그 이후에는 반드시 applyResult에서만 호출되어야 하는 함수.
         */
        // 문제를 얻어온다.
        String[] todayWord = Database.getRandomWordMean();
        correctAnswer = todayWord[0];
        correctAnsersMean = todayWord[1];

        // 라운드 초기화.
        isRightAnswer = false;
        textView_word.setText(correctAnswer);
        textView_mean.setText(correctAnsersMean);

        // 버튼 텍스트 초기화.
        button_start.setText("발음 해보기");
        button_next.setText("다음으로 넘어가기");
        button_playSound.setText("발음 들어보기");
        return true;
    }

    private void checkAnswer(String word){
        /*
        정답을 체크하고, isRightAnswer의 값을 변경하는 함수. (결과를 데이터베이스에 반영하지는 않음)
        isRightAnswer의 값에 따라 이후 처리가 달라진다.
         */
        if(word.toLowerCase().equals(correctAnswer.toLowerCase())){
            isRightAnswer = true;
            Log.d(TAG, "정답입니다.");
        }
    }

    private void applyResult(Session_Admin.resultCode result){
        /*
        정답을 데이터베이스에 반영하고, 다음 문제를 출제하거나, 세션을 종료하는 함수.
         */
        session_admin.reportGameResult(result);
        if(session_admin.getCurrentRound() <= session_admin.getMaxRound()){
            roundInit();
        }else{
            if(session_admin.getCorrectRound() >= session_admin.getGoalRound()){
                mDatabaseTestStub.addCharacterExp(mDatabaseTestStub.getEarnedExpWhenSuccess());
            }else{
                mDatabaseTestStub.addCharacterExp(mDatabaseTestStub.getEarnedExpWhenFailure());
            }

            Random random = new Random();
            int whatStat = random.nextInt(3);
            int dice = random.nextInt(6)+1;

            switch(whatStat) {
                case(0):
                    mDatabaseTestStub.addStatBlankGuessing(dice);
                    break;
                case(1):
                    mDatabaseTestStub.addStatImageGuessing(dice);
                    break;
                case(2):
                    mDatabaseTestStub.addStatWordChain(dice);
                    break;
                default:
                    break;
                }

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
                button_start.setText("발음하세요.");
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
                    Log.d(TAG, "다시 발음 해 보세요.");
                }
                break;
            case R.id.recognitionError:
                MessageDialogFragment.newInstance("Error code : " + msg.obj.toString());
                button_start.setText("발음 해보기");
                button_start.setEnabled(true);
                break;
            case R.id.endPointDetectTypeSelected:
                break;
            case R.id.clientInactive:
                button_start.setText("발음 해보기");
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

        // UI 생성 (액티비티 공통)
        setContentView(R.layout.game_basic);

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
        button_inputWordAccept.setEnabled(false);

        // UI 리스너 구현
        button_start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!mVoiceRecognizer.isRunning()) {
                    button_start.setText("기다리세요.");
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
                    Log.d(TAG, "다시 입력 해 보세요.");
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