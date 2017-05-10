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

public class Feature_TodaysWord extends AppCompatActivity {
    // 디버깅 메시지
    private static final String TAG = Feature_TodaysWord.class.getSimpleName();

    // 모든 액티비티가 가지고 있어야 하는 요소.
    private VoiceRecognizer mVoiceRecognizer; // 싱글톤
    private InnerEventHandler mInnerEventHandler; // 각 액티비티 고유의 이벤트 핸들러
    private VoiceSynthesizer mVoiceSynthesizer; // 음성 합성 API

    // 데이터베이스 테스트 stub
    //private DatabaseTestStub mDatabaseTestStub;

    // 데이터베이스 상태 매니저
    protected DatabaseStateManager mDatabaseStateManager;

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

    // 결과물 출력을 위해 임시로 기록해두는 변수
    protected boolean isLevelUp = false;
    protected int increasedExp = 0;
    protected int whatStat = -1;
    protected int dice = -1;

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
        }else {
            Log.d(TAG, "오답입니다.");
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
            endingSession();
        }
    }

    protected void endingSession(){
        button_next.setEnabled(false);
        button_start.setEnabled(false);
        button_playSound.setEnabled(false);
        button_inputWordAccept.setEnabled(false);

        sendResultToDatabase();
        mDatabaseStateManager.addUserTW_count(1);
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

        Random random = new Random();
        whatStat = random.nextInt(3);
        dice = random.nextInt(6)+1;

        switch(whatStat) {
            case(0):
                mDatabaseStateManager.addCharacterLuck(dice);
                break;
            case(1):
                mDatabaseStateManager.addCharacterPower(dice);
                break;
            case(2):
                mDatabaseStateManager.addCharacterSmart(dice);
                break;
            default:
                break;
        }
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
                Feature_TodaysWord.this.finish();
            }
        });
        if(isLevelUp) {temp = temp + "레벨업 하였습니다!\n";}
        temp = temp + "정답률: " + session_admin.getCorrectRound()  + "/" + (session_admin.getCurrentRound()-1) + "\n";
        switch(whatStat) {
            case(0):
                temp = temp + "행운 스탯 상승: ";
                break;
            case(1):
                temp = temp + "힘 스탯 상승: ";
                break;
            case(2):
                temp = temp + "지능 스탯 상승: ";
                break;
            default:
                break;
        }
        temp = temp + dice + "\n";
        temp = temp + "오른 경험치: " + increasedExp + "\n"; // // TODO: 2017-05-09  목표 도달시에 경험치가 두배 상승했음을 보여줄 필요가 있음.
        temp = temp + "현재 경험치: " + mDatabaseStateManager.getCharacterExp() + "\n";
        temp = temp + "다음 레벨 까지 경험치: " + mDatabaseStateManager.getLevelUpExp() + "\n";
        game_result.setGameResultText(temp);
        game_result.show();
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady:
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
                    Toast.makeText(getApplicationContext(), "정답입니다.", Toast.LENGTH_SHORT).show();
                    applyResult(Session_Admin.resultCode.CORRECT);
                }else{
                    Toast.makeText(getApplicationContext(), "오답입니다.", Toast.LENGTH_SHORT).show();
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
        mInnerEventHandler = new InnerEventHandler(this);
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
        session_admin = new Session_Admin(mDatabaseStateManager.getMaxRound(), mDatabaseStateManager.getGoalRound());
        //session_admin = new Session_Admin(mDatabaseTestStub.getMaxRound(), mDatabaseTestStub.getGoalRound());
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
        private final WeakReference<Feature_TodaysWord> mActivity;
        InnerEventHandler(Feature_TodaysWord activity) {
            mActivity = new WeakReference(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            Feature_TodaysWord activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }
}