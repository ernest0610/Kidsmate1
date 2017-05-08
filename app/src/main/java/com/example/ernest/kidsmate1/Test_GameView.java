package com.example.ernest.kidsmate1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class Test_GameView extends View {
    /////////////////////////////////////// 객체 목록
    private MyImageView myCharacter;
    private MyButton mic;
    //////////////////////////////////////

    /////////////////////////////////////////////// myCharacter 정보
    private final int myCharacterRatioInView = 40;
    private int myCharacterRatioChange;
    private int myCharacterX = 0, myCharacterY = 0;
    private int myCharacterWidth = 0, myCharacterHeight = 0;
    private Bitmap myCharacterResizedImage;
    ////////////////////////////////////////////////

    /////////////////////////////////////////////// mic 정보
    private final int micRatioInView = 10;
    private int micRatioChange;
    private int micX = 0, micY = 0;
    private int micWidth = 0, micHeight = 0;
    private Bitmap micResizedImage;
    ///////////////////////////////////////////////

    //////////////////////////////////////////// view 정보
    private int view_width, view_height;
    ///////////////////////////////////////////

    ///////////////////////////////////////////////// 생성자
    public Test_GameView(Context context) {
        super(context);
        myCharacter = new MyImageView(context, this);
        mic = new MyButton(context, this);
    }
    /////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////////// 화면 그림
    @Override
    protected void onDraw(Canvas canvas) {
        // myCharacter
        if(myCharacterResizedImage != null && view_width != 0 && view_height != 0) {
            canvas.drawBitmap(myCharacterResizedImage, myCharacterX, myCharacterY, null);
        }
        if(micResizedImage != null && view_width != 0 && view_height != 0) {
            canvas.drawBitmap(micResizedImage, micX, micY, null);
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////// 화면 바뀔때 resizing
    @Override
    protected void onSizeChanged(int view_width, int view_height, int old_view_width, int old_view_height) {
        super.onSizeChanged(view_width, view_height, old_view_width, old_view_height);
        this.view_width = view_width;
        this.view_height = view_height;

        if(myCharacter.getImage() != null && view_width != 0 && view_height != 0) {
            myCharacterResize();
        }

        if(mic.getImage() != null && view_width != 0 && view_height != 0) {
            micResize();
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////// touch envent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isTouch = false;

        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                isTouch = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                isTouch = false;
                break;
            default:
                return true;
        }

        int ptrIdx = MotionEventCompat.getActionIndex(event);
        int id = MotionEventCompat.getPointerId(event, ptrIdx);

        float x = MotionEventCompat.getX(event, ptrIdx);
        float y = MotionEventCompat.getY(event, ptrIdx);

        //////////////////////////////////////////////////////// 버튼 action
        mic.action(id, isTouch, x, y);
        /////////////////////////////////////////////////////////

        return true;
    }
    ///////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////// 객체 resizing
    private void myCharacterResize() {
        if(myCharacter.getImage().getHeight() > myCharacter.getImage().getWidth()) {
            myCharacterHeight = view_height * myCharacterRatioInView / 100;
            myCharacterRatioChange = myCharacterHeight * 100 / myCharacter.getImage().getHeight();
            myCharacterWidth = myCharacter.getImage().getWidth() * myCharacterRatioChange / 100;
        }
        else {
            myCharacterWidth = view_width * myCharacterRatioInView / 100;
            myCharacterRatioChange = myCharacterWidth * 100 / myCharacter.getImage().getWidth();
            myCharacterHeight = myCharacter.getImage().getHeight() * myCharacterRatioChange / 100;
        }
        myCharacterX = view_width / 2 - myCharacterWidth / 2;
        myCharacterY = view_height - view_height / 8 - myCharacterHeight;
        myCharacterResizedImage = Bitmap.createScaledBitmap(myCharacter.getImage(), myCharacterWidth, myCharacterHeight, true);
    }
    private void micResize() {
        if(mic.getImage().getHeight() > mic.getImage().getWidth()) {
            micHeight = view_height * micRatioInView / 100;
            micRatioChange = micHeight * 100 / mic.getImage().getHeight();
            micWidth = mic.getImage().getWidth() * micRatioChange / 100;
        }
        else {
            micWidth = view_width * micRatioInView / 100;
            micRatioChange = micWidth * 100 / mic.getImage().getWidth();
            micHeight = mic.getImage().getHeight() * micRatioChange / 100;
        }
        micX = myCharacterX + myCharacterWidth - micWidth / 2;
        micY = myCharacterY + myCharacterHeight - micHeight / 2;
        micResizedImage = Bitmap.createScaledBitmap(mic.getImage(), micWidth, micHeight, true);
        mic.setRectF(new RectF(micX, micY, micX + micWidth, micY + micHeight));
    }
    ////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////// 객체 반환
    public MyImageView getMyCharacter() {
        return myCharacter;
    }
    public MyButton getMic() {
        return mic;
    }
    ////////////////////////////////////////////////////////////////
}
