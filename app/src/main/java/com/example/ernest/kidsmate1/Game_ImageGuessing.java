package com.example.ernest.kidsmate1;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.naver.speech.clientapi.SpeechRecognitionResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class Game_ImageGuessing extends AppCompatActivity {
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

    private ScrollView scrollView_game;

    // 액티비티마다 다른 변수
    private String correctAnswer;
    private String correctAnsersMean;
    private boolean isRightAnswer;

    // 이미지 추출용 변수
    private Handler handler;
    private String imageURL;
    private URL url;
    private Bitmap bitmap;
    private static final String addr = "https://www.google.com/search?newwindow=1&prmd=ivmn&source=lnms&tbm=isch&sa=X&biw=360&bih=517&dpr=2&q=";
    private HttpsURLConnection conn = null;
    private BufferedInputStream bis = null;
    private BufferedReader reader = null;
    private StringBuffer st = null;
    private Document doc;
    private Elements elements;
    private Drawable drawable = null;

    // progress bar 변수
    private MyProgress myProgress;

    //함수 시작
    private String[] getWordAndMean() {
        return Database.getRandomWordMean();
    }

    private boolean makeQuiz(){
        imageURL = "";
        myProgress.show();
        String[] todayWord = getWordAndMean();
        correctAnswer = todayWord[0];
        correctAnsersMean = todayWord[1];
        isRightAnswer = false;
        //textView_word.setText(correctAnswer);
        handler = new Handler() {
            public void handleMessage(Message msg) {
                Bundle bun = msg.getData();
                imageURL = bun.getString("IMAGE_URL");
                //if (imageURL.equals(""));
                scrollView_game.setBackground(drawable);
                myProgress.dismiss();
            }
        };
        new Thread() {
            public void run() {
                String imageURL = "";
                try{
                    url = new URL(addr + correctAnswer.replaceAll(" ", "%20") + "#imgrc=6C2Fpt3z7jtfQM:");
                    conn = (HttpsURLConnection) url.openConnection();
                    conn.setRequestProperty("user-agent",
                            "Mozilla/5.0 (Linux; Android 4.3; Nexus 10 Build/JSS15Q) "
                                    + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2307.2 Mobile Safari/537.36"
                    );
                    conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                    SSLContext context = SSLContext.getInstance("TLS");
                    context.init(null, null, null);
                    conn.setSSLSocketFactory(context.getSocketFactory());
                    conn.connect();
                    conn.setInstanceFollowRedirects(true);


                    bis = new BufferedInputStream(conn.getInputStream());
                    reader = new BufferedReader(new InputStreamReader(bis));
                    st = new StringBuffer();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        st.append(line + "\n");
                    }
                }catch (Exception e) { e.printStackTrace();}
                //doc = Jsoup.parse(ResponseText);
                doc = Jsoup.parse(st.toString());

                elements = doc.select("div.rg_meta");
                for(Element element : elements) {
                    imageURL = imageURL + element.text();
                    imageURL = imageURL.substring(imageURL.indexOf("\"ou\"") + 6, imageURL.indexOf("\"ow\"") - 2);
                    break;
                }
                try {
                    url = new URL(imageURL);
                } catch (Exception e) { e.printStackTrace();}
                try {
                    bitmap = BitmapFactory.decodeStream(url.openStream());
                } catch (Exception e) {e.printStackTrace();}
                try {
                    drawable = new BitmapDrawable(bitmap);
                } catch (Exception e) { e.printStackTrace();}

                Bundle bun = new Bundle();
                bun.putString("IMAGE_URL", imageURL);
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
            }
        }.start();
        textView_word.setText(correctAnswer);
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
                if(!isRightAnswer && partialResult.toLowerCase().equals(correctAnswer.toLowerCase())){
                    isRightAnswer = true;
                    textView_debug.append("정답입니다.\n");
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
                    if(result.toLowerCase().equals(correctAnswer.toLowerCase())) {
                        isRightAnswer = true;
                        textView_debug.append("정답입니다.\n");
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
        scrollView_game = (ScrollView) findViewById(R.id.scrollView_game);
        myProgress = new MyProgress(this);
        myProgress.setCancelable(false);


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
                if(inputWord.toLowerCase().equals(correctAnswer.toLowerCase())){
                    isRightAnswer = true;
                    textView_debug.append("정답입니다.\n");
                    makeQuiz();
                }
            }
        });

        // 퀴즈를 생성한다.
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

    public static class EventHandler extends Handler {
        // 이벤트 핸들러 이너 클래스
        private final WeakReference<Game_ImageGuessing> mActivity;
        EventHandler(Game_ImageGuessing activity) {
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            Game_ImageGuessing activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }
}