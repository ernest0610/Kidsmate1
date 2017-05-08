package com.example.ernest.kidsmate1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by User on 2017-05-08.
 */

public class MyImageView {
    private Context context;
    private Test_GameView parentView;

    private Bitmap image;

    public MyImageView(Context context, Test_GameView parentView) {
        this.context = context;
        this.parentView = parentView;
    }

    public void setImage(int id) {
        image = BitmapFactory.decodeResource(context.getResources(), id);
        parentView.invalidate();
    }

    public void resetImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }
}
