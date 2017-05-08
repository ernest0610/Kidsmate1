package com.example.ernest.kidsmate1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by User on 2017-05-08.
 */

public class MyButton {
    private Bitmap image;
    private String text;
    private boolean enabled;

    private int ptrId = -1;
    private RectF rectF;

    private boolean isTouch;

    private View.OnClickListener onClickListener;
    private Context context;
    private Test_GameView parentView;

    public MyButton(Context context, Test_GameView parentView) {
        this.context = context;
        this.parentView = parentView;
        enabled = true;
    }

    public void action(int id, boolean isDown, float x, float y) {
        if(isDown && rectF.contains(x, y)) {
            isTouch = true;
            ptrId = id;
            if(onClickListener != null)
                onClickListener.onClick(parentView);
        }

        if (!isDown && id == ptrId) {
            isTouch = false;
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setImage(int id) {
        image = BitmapFactory.decodeResource(context.getResources(), id);
        parentView.invalidate();
    }

    public void setText(String text) {
        this.text = text;
        parentView.invalidate();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        parentView.invalidate();
    }

    public Bitmap getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

    public void setRectF(RectF rectF) {
        this.rectF = rectF;
    }
}
