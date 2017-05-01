package com.example.ernest.kidsmate1;

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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.ref.WeakReference;
import java.util.List;

public class Feature_Dictionary extends AppCompatActivity {
    // 모든 액티비티가 가지고 있어야 하는 요소.
    private VoiceRecognizer mVoiceRecognizer; // 싱글톤
    private EventHandler mEventHandler; // 각 액티비티 고유의 이벤트 핸들러

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
    private String question;
    private String questionMean;

    // Http Request Parsing 용도
    private String ResponseText;
    private HttpClient client;
    private HttpGet get;
    private HttpResponse response;
    private HttpEntity resEntity;
    private Document doc;
    private Elements elements1;
    private Elements elements2;
    private static final String addr = "http://m.endic.naver.com/search.nhn?searchOption=all&query=";
    private String mean;
    private Handler handler;

    //함수 시작
    private void setWordMean(String word) {
        final String tmpWord = word;
        handler = new Handler() {
            public void handleMessage(Message msg) {
                Bundle bun = msg.getData();
                mean = bun.getString("HTML_DATA");
                if (mean.equals(""))
                    mean = "'" + tmpWord + "'에 대한 결과가 없습니다";
                textView_mean.setText(mean);
            }
        };
        new Thread() {
            public void run() {
                String mean = "";
                client = new DefaultHttpClient();
                get = new HttpGet(addr + tmpWord.replaceAll(" ", "%20"));
                try {
                    response = client.execute(get);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                resEntity = response.getEntity();
                if (resEntity != null) {
                    try {
                        ResponseText = EntityUtils.toString(resEntity, "UTF-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                doc = Jsoup.parse(ResponseText);
                elements1 = doc.select("div.entry_search_word");
                for (Element element1 : elements1) {
                    elements2 = element1.select("strong");
                    /*if(elements2.select("strong").size() == 2)
                        elements2 = element1.select("strong strong");*/
                    mean = mean + elements2.get(0).text() + "\n";
                    elements2 = element1.select("li");
                    for (Element element2 : elements2) {
                        mean = mean + element2.text() + "\n";
                    }
                    elements2 = element1.select("p.example_stc");
                    for (Element element2 : elements2) {
                        mean = mean + "\nEx)" + element2.text() + "\n";
                    }
                    elements2 = element1.select("p.example_mean");
                    for (Element element2 : elements2) {
                        mean = mean + element2.text() + "\n";
                    }
                    mean = mean + "\n";
                }
                Bundle bun = new Bundle();
                bun.putString("HTML_DATA", mean);
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
            }
        }.start();
    }

    private boolean showWordMean(String word){
        question = word;
        textView_word.setText(word);
        setWordMean(word);
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
                break;
            case R.id.endPointDetected:
                break;
            case R.id.finalResult:
                List<String> results = ((SpeechRecognitionResult)(msg.obj)).getResults();

                for (String result: results) {
                    textView_debug.append(result+" ");
                }
                textView_debug.append("\n");

                showWordMean(results.get(0));
                editText_inputWord.setText(results.get(0));
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
        button_start.setEnabled(true);
        button_next.setEnabled(false);
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

        button_playSound.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            }
        });

        button_inputWordAccept.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showWordMean(editText_inputWord.getText().toString());
            }
        });
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

    public static class EventHandler extends Handler {
        // 이벤트 핸들러 이너 클래스
        private final WeakReference<Feature_Dictionary> mActivity;
        EventHandler(Feature_Dictionary activity) {
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            Feature_Dictionary activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }
}