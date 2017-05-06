package com.example.ernest.kidsmate1;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;

public class EventHandler extends Handler {
    // 이벤트 핸들러 이너 클래스
    private final WeakReference<AppCompatActivity> mActivity;
    public EventHandler(AppCompatActivity activity) {
        mActivity = new WeakReference<>(activity);
    }
    @Override
    public void handleMessage(Message msg) {
        AppCompatActivity activity = mActivity.get();
        if (activity != null) {
            if (activity instanceof Feature_Dictionary)
                ((Feature_Dictionary)activity).handleMessage(msg);
            else if (activity instanceof Feature_TodaysWord)
                ((Feature_TodaysWord)activity).handleMessage(msg);
            else if (activity instanceof Game_BlankGuessing)
                ((Game_BlankGuessing)activity).handleMessage(msg);
            else if (activity instanceof Game_ImageGuessing)
                ((Game_ImageGuessing)activity).handleMessage(msg);
            else if (activity instanceof Game_WordChain)
                ((Game_WordChain)activity).handleMessage(msg);
            else if (activity instanceof Test_WordChain)
                ((Test_WordChain)activity).handleMessage(msg);
        }
    }
}
