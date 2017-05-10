package com.example.ernest.kidsmate1;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

class Dialog_Get_String extends Dialog {
    private LinearLayout linearLayout_root;
    private TextView textView_gameResult;

    public Dialog_Get_String(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 제목
        setContentView(R.layout.dialog_get_string);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경을 투명하게
    }
}
