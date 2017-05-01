package com.example.ernest.kidsmate1;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.naver.speech.clientapi.SpeechRecognitionResult;

import java.lang.ref.WeakReference;
import java.util.List;

public class Game_WordChain extends AppCompatActivity {
    private VoiceRecognizer mVoiceRecognizer;
    private EventHandler mEventHandler;

    private String givenWord;
    private String givenWordMean;
    private boolean isRightAnswer;

    private TextView textView_word;
    private TextView textView_mean;
    private TextView textView_debug;

    private Button button_start;
    private Button button_next;

    private String[] getWord() {
        String[] result = new String[]{"", ""};
        result[0] = "apple";
        result[1] = "사과";
        return result;
    }

    private boolean makeQuiz(){
        String[] targetWord = getWord();
        givenWord = targetWord[0];
        givenWordMean = targetWord[1];
        isRightAnswer = false;
        textView_word.setText(givenWord);
        textView_mean.setText(givenWordMean);
        return true;
    }

    private void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady:
                button_start.setText("연결됨");
                break;
            case R.id.audioRecording:
                break;
            case R.id.partialResult:
                String partialResult = (String) msg.obj;
                textView_debug.append(partialResult+" ");
                if(partialResult.length()>=2) {
                    if (!isRightAnswer && partialResult.toLowerCase().charAt(0) == (givenWord.toLowerCase().lastIndexOf(0))) {
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
                    if(result.length()>=2) {
                        if (result.toLowerCase().charAt(0) == (givenWord.toLowerCase().lastIndexOf(0))) {
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
        mEventHandler = new EventHandler(this);

        setContentView(R.layout.game_basic);

        mVoiceRecognizer = VoiceRecognizer.getInstance(this);

        textView_word = (TextView) findViewById(R.id.textView_word);
        textView_mean = (TextView) findViewById(R.id.textView_mean);
        textView_debug = (TextView) findViewById(R.id.textView_debug);

        button_next = (Button) findViewById(R.id.button_next);
        button_start = (Button) findViewById(R.id.button_start);

        button_next.setEnabled(true);
        button_start.setEnabled(true);

        button_next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                makeQuiz();
            }
        });

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

        makeQuiz();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mVoiceRecognizer.initialize(mEventHandler);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVoiceRecognizer.release();
    }

    public static class EventHandler extends Handler {
        private final WeakReference<Game_WordChain> mActivity;
        EventHandler(Game_WordChain activity) {
            mActivity = new WeakReference<>(activity);
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