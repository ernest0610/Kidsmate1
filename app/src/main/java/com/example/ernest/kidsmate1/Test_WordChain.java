package com.example.ernest.kidsmate1;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.naver.speech.clientapi.SpeechRecognitionResult;

import java.util.List;
import java.util.Random;

/**
 * Created by User on 2017-05-05.
 */

public class Test_WordChain extends AppCompatActivity {
    // 모든 액티비티가 가지고 있어야 하는 요소.
    private VoiceRecognizer mVoiceRecognizer; // 싱글톤
    private EventHandler mEventHandler; // 각 액티비티 고유의 이벤트 핸들러
    private VoiceSynthesizer mVoiceSynthesizer; // 음성 합성 API

    // 액티비티들 공통 UI
    private LinearLayout linearLayout;
    private TextView textView_word;
    private TextView textView_mean;

    private EditText editText_inputWord;
    private Button button_inputWordAccept;

    private Button button_start;
    private Button button_next;
    private Button button_playSound;

    //private ProgressBar progressBar;
    //private Thread thread;

    // 액티비티마다 다른 변수
    private String givenWord;
    private boolean isRightAnswer;
    private String RightAnswerString;

    //함수 시작
    private static final String TAG = Test_WordChain.class.getSimpleName();

    private String getWordStartWith(char ch){
        return Database.getRandomWordStartWith(String.valueOf(ch));
    }

    private boolean makeRandomQuiz(){
        Random random = new Random();
        String atoz = "abcdefghijklmnopqrstuvwxyz";
        char ch = atoz.charAt(random.nextInt(26));
        while(Database.getRandomWordStartWith(String.valueOf(ch)).equals("")) {
            ch = atoz.charAt(random.nextInt(26));
        }
        makeQuiz(ch);
        return true;
    }
    private boolean makeQuiz(char ch){
        //progressBar.setProgress(100);
        givenWord = getWordStartWith(ch);
        isRightAnswer = false;
        textView_word.setText(givenWord);
        return true;
    }

    private void checkAnswer(String word){
        if(word.length()>=2) {
            Log.d(TAG, "givenWord[n]:" + givenWord.toLowerCase().charAt(givenWord.length()-1) +
                    ", partialResult[0]:" + word.toLowerCase().charAt(0));
            if (!isRightAnswer && (givenWord.toLowerCase().charAt(givenWord.length()-1)) == word.toLowerCase().charAt(0)) {
                isRightAnswer = true;
                RightAnswerString = new String(word.toLowerCase());
                Log.d(TAG, "정답입니다.");
            }
        }
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady:
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
                for (String result: results) {
                    Log.d(TAG, "results: " + result);
                    checkAnswer(result);
                    if(isRightAnswer) break;
                }
                if (isRightAnswer) {
                    makeQuiz(RightAnswerString.charAt(RightAnswerString.length()-1));
                }else{
                    Log.d(TAG, "다시 발음 해 보세요.");
                }
                break;
            case R.id.recognitionError:
                MessageDialogFragment.newInstance("Error code : " + msg.obj.toString());
                button_start.setText("시작");
                button_start.setEnabled(true);
                break;
            case R.id.endPointDetectTypeSelected:
                break;
            case R.id.clientInactive:
                button_start.setText("시작");
                button_start.setEnabled(true);
                break;
            /*case R.id.progressbarClock:
                progressBar.incrementProgressBy(-5);
                break;*/
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
        setContentView(R.layout.game_basic2_test);

        textView_word = (TextView) findViewById(R.id.textView_word_test);
        textView_mean = (TextView) findViewById(R.id.textView_mean_test);

        button_start = (Button) findViewById(R.id.button_start_test);
        button_next = (Button) findViewById(R.id.button_next_test);
        button_playSound = (Button) findViewById(R.id.button_playSound_test);

        editText_inputWord = (EditText) findViewById(R.id.editText_inputWord_test);
        button_inputWordAccept = (Button) findViewById(R.id.button_inputWordAccept_test);

        linearLayout = (LinearLayout) findViewById(R.id.LinearLayout_root_test);
        linearLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.testwordchain));

        //progressBar = (ProgressBar) findViewById(R.id.progressBar_test);

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
                    button_start.setText("연결중");
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
                makeRandomQuiz();
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
                if (isRightAnswer) {
                    makeQuiz(RightAnswerString.charAt(RightAnswerString.length()-1));
                }else{
                    Log.d(TAG, "다시 발음 해 보세요.");
                }
            }
        });

        // 퀴즈를 생성한다.
        makeRandomQuiz();
        /*new Thread() {
            @Override
            public void run() {
                try {
                    //for(int i=0; i<20; i++){
                    for(;;){
                        sleep(1000);
                        Message msg = Message.obtain(mEventHandler, R.id.progressbarClock);
                        msg.sendToTarget();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 액티비티 시작시 반드시 음성인식 기능을 초기화 하여야 함.
        mVoiceRecognizer.initialize(mEventHandler);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 액티비티 종료시 반드시 음성인식 기능을 릴리즈 하여야 함.
        mVoiceRecognizer.release();
    }
}
