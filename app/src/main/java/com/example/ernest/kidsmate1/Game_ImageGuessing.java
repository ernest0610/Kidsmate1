package com.example.ernest.kidsmate1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class Game_ImageGuessing extends AppCompatActivity {
    // 모든 액티비티가 가지고 있어야 하는 요소.
    private VoiceRecognizer mVoiceRecognizer; // 싱글톤
    private EventHandler mEventHandler; // 각 액티비티 고유의 이벤트 핸들러
    private VoiceSynthesizer mVoiceSynthesizer; // 음성 합성 API

    // test stub
    private DatabaseTestStub mDatabaseTestStub;

    // 디버깅 메시지
    private static final String TAG = Game_ImageGuessing.class.getSimpleName();

    // 액티비티들 공통 UI
    private TextView textView_word;
    private TextView textView_mean;

    private EditText editText_inputWord;
    private Button button_inputWordAccept;

    private Button button_start;
    private Button button_next;
    private Button button_playSound;

    // 퀴즈를 진행하기 위한 변수
    private String correctAnswer; // 정답을 기록하는 변수
    private String correctAnsersMean; // 정답의 의미를 기록하는 변수
    private boolean isRightAnswer; // 정답을 맞췄는지 기록하는 변수
    private Session_Admin session_admin; // 세션을 관리하는 변수
    private int opportunity; // 발음할 수 있는 횟수를 제한하는 변수.

    // 그림 맞추기 전용 변수
    private ScrollView scrollView_game;

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

    // 함수 시작
    private boolean roundInit(){
        /*
        세션이 처음 시작될때 딱 한번 onCreate에서 호출되고, 그 이후에는 반드시 applyResult에서만 호출되어야 하는 함수.
         */
        imageURL = "";
        myProgress.show();

        // 문제를 얻어온다.
        String[] todayWord = Database.getRandomWordMean();
        correctAnswer = todayWord[0];
        correctAnsersMean = todayWord[1];

        //라운드 초기화.
        isRightAnswer = false;
        //textView_mean.setText("");
        //hintMean = false;
        //hintSynthesizer = false;
        opportunity = 5;

        // 버튼 텍스트 초기화.
        button_start.setText("남은 기회는 " + Integer.toString(opportunity)+ "회.\n도전!");
        button_next.setText("현재 라운드: " + Integer.toString(session_admin.getCurrentRound())+ "\n패스(패배)");
        button_playSound.setText("발음 힌트");

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
                    imageURL = element.text();
                    imageURL = imageURL.substring(imageURL.indexOf("\"ou\"") + 6, imageURL.indexOf("\"ow\"") - 2);
                    try {
                        url = new URL(imageURL);
                    } catch (Exception e) { e.printStackTrace();}
                    try {
                        bitmap = BitmapFactory.decodeStream(url.openStream());
                    } catch (Exception e) {e.printStackTrace();}
                    try {
                        drawable = new BitmapDrawable(bitmap);
                    } catch (Exception e) { e.printStackTrace();}
                    if(bitmap != null)
                        break;
                }

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
            mDatabaseTestStub.addStatImageGuessing(session_admin.getCorrectRound());

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

        // imageGuessing 이미지 로딩 구현
        scrollView_game = (ScrollView) findViewById(R.id.scrollView_game);
        myProgress = new MyProgress(this);
        myProgress.setCancelable(false);

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