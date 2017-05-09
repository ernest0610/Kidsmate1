package com.example.ernest.kidsmate1;

import android.content.DialogInterface;
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
import android.widget.Toast;

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
    // 디버깅 메시지
    protected static final String TAG = Game_ImageGuessing.class.getSimpleName();

    // 모든 액티비티가 가지고 있어야 하는 요소.
    protected VoiceRecognizer mVoiceRecognizer; // 싱글톤
    protected InnerEventHandler mInnerEventHandler; // 각 액티비티 고유의 이벤트 핸들러
    protected VoiceSynthesizer mVoiceSynthesizer; // 음성 합성 API

    // 데이터베이스 테스트 stub
    protected DatabaseTestStub mDatabaseTestStub = DatabaseTestStub.getInstance();

    // 데이터베이스 상태 매니저
    protected StateManager mStateManager;

    // 액티비티들 공통 UI
    protected TextView textView_word;
    protected TextView textView_mean;

    protected EditText editText_inputWord;
    protected Button button_inputWordAccept;

    protected Button button_start;
    protected Button button_next;
    protected Button button_playSound;

    // 퀴즈를 진행하기 위한 변수
    protected String correctAnswer; // 정답을 기록하는 변수
    protected String correctAnsersMean; // 정답의 의미를 기록하는 변수
    protected boolean isRightAnswer; // 정답을 맞췄는지 기록하는 변수
    protected Session_Admin session_admin; // 세션을 관리하는 변수
    protected int opportunity; // 발음할 수 있는 횟수를 제한하는 변수.

    // 결과물 출력을 위해 임시로 기록해두는 변수
    protected boolean isLevelUp = false;
    protected int increasedExp = 0;

    // 그림 맞추기 전용 변수
    protected ScrollView scrollView_game; //테스트중 17.05.09

    // 이미지 추출용 변수
    protected Handler handler;
    protected String imageURL;
    protected URL url;
    protected Bitmap bitmap;
    protected static final String addr = "https://www.google.com/search?newwindow=1&prmd=ivmn&source=lnms&tbm=isch&sa=X&biw=360&bih=517&dpr=2&q=";
    protected HttpsURLConnection conn = null;
    protected BufferedInputStream bis = null;
    protected BufferedReader reader = null;
    protected StringBuffer st = null;
    protected Document doc;
    protected Elements elements;
    protected Drawable drawable = null;

    // progress bar 변수
    protected MyProgress myProgress;

    // 함수 시작
    protected boolean roundInit(){
        /*
        세션이 처음 시작될때 딱 한번 onCreate에서 호출되고, 그 이후에는 반드시 applyResult에서만 호출되어야 하는 함수.
         */
        // 로딩 메시지를 띄움
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
                myProgress.dismiss(); // 로딩 메시지를 닫음.
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

    protected void checkAnswer(String word){
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

    protected void applyResult(Session_Admin.resultCode result){
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
        button_start.setEnabled(true);
        button_playSound.setEnabled(false);
        button_inputWordAccept.setEnabled(false);

        sendResultToDatabase();
        mStateManager.addUserIG_count(1);
        showTotalResult();
    }

    protected void sendResultToDatabase(){
        /*
        데이터베이스에 결과를 전송
         */
        if(session_admin.getCorrectRound() >= session_admin.getGoalRound()){
            increasedExp = mStateManager.getEarnedExpWhenSuccess();
        }else if(session_admin.getCorrectRound() > 0){
            increasedExp = mStateManager.getEarnedExpWhenFailure();
        }else{
            increasedExp = 0; // // TODO: 2017-05-09  0문제를 맞춰도 경험치가 5 상승하는건 불합리하므로, 최소 한문제를 맞춰야 경험치가 올라가도록 조정.
        }
        isLevelUp = mStateManager.addCharacterExp(increasedExp);
        mStateManager.addCharacterPower(session_admin.getCorrectRound());
    }

    protected void showTotalResult(){
        /*
        모든 라운드가 끝나고 세션의 결과를 표시
         */
        String temp = "";
        Game_Result game_result = new Game_Result(this);
        game_result.setOnCancelListener(new DialogInterface.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialog){
                Game_ImageGuessing.this.finish();
            }
        });
        if(isLevelUp) {temp = temp + "레벨업 하였습니다!\n";}
        temp = temp + "정답률: " + session_admin.getCorrectRound()  + "/" + (session_admin.getCurrentRound()-1) + "\n";
        temp = temp + "체력 스탯 상승: " + session_admin.getCorrectRound() + "\n";
        temp = temp + "오른 경험치: " + increasedExp + "\n"; // // TODO: 2017-05-09  목표 도달시에 경험치가 두배 상승했음을 보여줄 필요가 있음.
        temp = temp + "현재 경험치: " + mStateManager.getCharacterExp() + "\n";
        temp = temp + "다음 레벨 까지 경험치: " + mStateManager.getLevelUpExp() + "\n";
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
        mStateManager = StateManager.getInstance();

        // UI 생성 (액티비티 공통)
        setContentView(R.layout.game_basic2);

        textView_word = (TextView) findViewById(R.id.textView_word);       // custom view 테스트중 17.05.09
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
        myProgress.setCancelable(false); // // TODO: 2017-05-09 인터넷 상태가 좋지 않은 경우 뒤로가기로도 취소를 할 수 없어 영원히 로딩상태가 되는 경우가 있음.

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
        session_admin = new Session_Admin(mStateManager.getMaxRound(), mStateManager.getGoalRound());
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
        private final WeakReference<Game_ImageGuessing> mActivity;
        InnerEventHandler(Game_ImageGuessing activity) {
            mActivity = new WeakReference(activity);
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