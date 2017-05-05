package com.example.ernest.kidsmate1;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.naver.speech.clientapi.SpeechRecognitionResult;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Random;

public class Game_BlankGuessing extends AppCompatActivity {
    // 모든 액티비티가 가지고 있어야 하는 요소.
    private VoiceRecognizer mVoiceRecognizer; // 싱글톤
    private EventHandler mEventHandler; // 각 액티비티 고유의 이벤트 핸들러
    private VoiceSynthesizer mVoiceSynthesizer; // 음성 합성 API

    // 액티비티들 공통 UI
    private TextView textView_word;
    private TextView textView_mean;
    private TextView textView_debug;

    private EditText editText_inputWord;
    private Button button_inputWordAccept;

    private Button button_start;
    private Button button_next;
    private Button button_playSound;

    // 액티비티마다 다른 변수
    private String correctAnswer;
    private String correctAnsersMean;
    private boolean isRightAnswer;
    private int[] blankIndex = {0};

    // 함수 시작
    private String[] getWordAndMean() {
        return Database.getRandomWordMean();
    }

    private boolean makeQuiz(){
        String[] todayWord = getWordAndMean();
        correctAnswer = todayWord[0].toLowerCase();
        correctAnsersMean = todayWord[1];

        //빈칸이 들어갈 위치 정하기.
        Random randomIndex = new Random();
        blankIndex[0] = randomIndex.nextInt(correctAnswer.length()-1);

        isRightAnswer = false;

        //빈칸 만들어서 출력하기. 원본은 correctAnswer에 저장되어있고 변형되지 않음.
        StringBuffer mStringBuffer = new StringBuffer(correctAnswer);
        mStringBuffer.setCharAt(blankIndex[0], '_');
        textView_word.setText(mStringBuffer.toString());
        return true;
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
                textView_debug.append(partialResult+" ");
                if(partialResult.length()==1) {
                    if (!isRightAnswer && partialResult.toLowerCase().charAt(0)
                            == correctAnswer.charAt(blankIndex[0])) {
                        isRightAnswer = true;
                        textView_debug.append("정답입니다.\n");
                    }
                }
                textView_debug.append("\n");
                break;
            case R.id.endPointDetected:
                break;
            case R.id.finalResult:
                List<String> results = ((SpeechRecognitionResult)(msg.obj)).getResults();
                for (String result: results) {
                    if (isRightAnswer) break;
                    textView_debug.append(result+" ");
                    if(result.length()==1) {
                        if (!isRightAnswer && result.toLowerCase().charAt(0)
                                == correctAnswer.charAt(blankIndex[0])) {
                            isRightAnswer = true;
                            textView_debug.append("정답입니다.\n");
                        }
                    }
                }
                textView_debug.append("\n");
                if (isRightAnswer) {
                    makeQuiz();
                }else{
                    textView_debug.append("다시 발음 해 보세요.\n");
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

        textView_debug = (TextView) findViewById(R.id.textView_debug);

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
                makeQuiz();
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
                if(inputWord.length()==1 && inputWord.charAt(0) == correctAnswer.charAt(blankIndex[0])) {
                    isRightAnswer = true;
                    textView_debug.append("정답입니다.\n");
                    makeQuiz();
                }
            }
        });

        // 퀴즈를 만든다.
        makeQuiz();
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