package com.example.ernest.kidsmate1;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by User on 2017-05-09.
 */

public class MyTextView {
    private Context context;
    private Test_GameView parentView;

    private String text;

    public MyTextView(Context context, Test_GameView parentView) {
        this.context = context;
        this.parentView = parentView;
        text = "";
    }

    public void setText(String text) {
        this.text = text;
        parentView.invalidate();
    }

    public String getText() {
        return text;
    }
}
