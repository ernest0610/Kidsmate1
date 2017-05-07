package com.example.ernest.kidsmate1;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

// 커스텀 progress bar
class Game_Result extends Dialog {
    public Game_Result(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 제목
        setContentView(R.layout.game_result);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경을 투명하게
    }
}
