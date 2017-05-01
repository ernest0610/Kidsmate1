package com.example.ernest.kidsmate1;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

public class VoiceSynthesizer{
    private static String clientId = "kOLPnFrgYQnbsiqsFsTn";//애플리케이션 클라이언트 아이디값";
    private static String clientSecret = "r38dkrGz8Z";//애플리케이션 클라이언트 시크릿값";

    private String arg;
    private boolean speaker;
    private int speed;
    private Context context;

    public VoiceSynthesizer(Context context) {
        this.context = context;
        this.arg = "";
        this.speaker = true;
        this.speed = 0;
    }

    /*
    public VoiceSynthesizer(Context context, String arg, boolean speaker, int speed) {
        this.context = context;
        this.arg = "";
        this.speaker = speaker;
        this.speed = speed;
    }
    public void setSpeaker(boolean speaker){
        this.speaker = speaker;
    }
    */

    public void setString(String arg){
        this.arg = arg;
    }

    public void doSynthsize() {
        new Thread() {
            public void run () {
                realSynthsizer(arg, speaker, speed);
            }
        }.start();
    }

    private void realSynthsizer(String arg, boolean mSpeaker, int mSpeed) {
        String speaker;
        String speed;
        try {
            String text = URLEncoder.encode(arg, "UTF-8"); // 13자
            String apiURL = "https://openapi.naver.com/v1/voice/tts.bin";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            // post request
        /*
        speaker : clara(여성) / matt(남성)
        speed : -5, -4, ... 4, 5 (0.5배 빠른, 0.4배 빠른 .... 0.4배 느린, 0.5배 느린)
        */
            if (mSpeaker) {
                speaker = URLEncoder.encode("clara", "UTF-8");
            } else {
                speaker = URLEncoder.encode("matt", "UTF-8");
            }
            if (mSpeed <= -5) {
                speed = URLEncoder.encode("-5", "UTF-8");
            } else if (mSpeed >= 5) {
                speed = URLEncoder.encode("5", "UTF-8");
            } else {
                speed = URLEncoder.encode(String.valueOf(mSpeed), "UTF-8");
            }
            String postParams = "speaker=" + speaker + "&speed=" + speed + "&text=" + text; // 파라메터 + 텍스트

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;

            if (responseCode == 200) { // 정상 호출
                InputStream is = con.getInputStream();
                int read = 0;
                byte[] bytes = new byte[1024];
                String tempname = Long.valueOf(new Date().getTime()).toString(); // 랜덤한 이름으로 mp3 파일 생성

                File f = new File(context.getCacheDir(), tempname + ".mp3");
                f.createNewFile();
                OutputStream outputStream = new FileOutputStream(f);
                while ((read = is.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                is.close();
                outputStream.flush();
                outputStream.close();

                //implement playing sound
                MediaPlayer pronounce = new MediaPlayer();
                pronounce.setDataSource(new FileInputStream(f).getFD());//예외 발생
                pronounce.prepare();
                pronounce.start();

            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                System.out.println(response.toString());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}