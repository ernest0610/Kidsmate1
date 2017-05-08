package com.example.ernest.kidsmate1;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class Test_GameView extends View {
    Bitmap slime;

    int w, h;
    int x, y;
    int sw, sh;

    public Test_GameView(Context context) {
        super(context);
        slime = BitmapFactory.decodeResource(context.getResources(), R.drawable.progress);
        slime = Bitmap.createScaledBitmap(slime, 300, 300, true);
        sw = slime.getWidth() / 2;
        sh = slime.getHeight() / 2;
    }

    public Test_GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(slime, x - sw, y - sh, null);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.CENTER);
        String str = String.format("%d, %d", slime.getWidth(), slime.getHeight());
        canvas.drawText(str, x, y, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.w = w;
        this.h = h;

        x = w / 2;
        y = h / 2;
    }
}
