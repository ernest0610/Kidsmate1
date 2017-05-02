package com.example.ernest.kidsmate1;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by User on 2017-05-02.
 */
// 커스텀 progress bar
public class MyProgress extends Dialog {
    public MyProgress(Context context)
    {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 제목
        setContentView(R.layout.custom_progress);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //배경을 투명하게
    }
}
