package com.example.ernest.kidsmate1;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

class Dialog_Simple_Message extends Dialog {
    private LinearLayout linearLayout_root;
    private TextView textView_gameResult;

    public Dialog_Simple_Message(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 제목
        setContentView(R.layout.dialog_game_result);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경을 투명하게

        textView_gameResult = (TextView) findViewById(R.id.textView_gameResult);
        linearLayout_root = (LinearLayout) findViewById(R.id.linearLayout_root);

        linearLayout_root.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Dialog_Simple_Message.this.onBackPressed();
            }
        });
    }
    public void setGameResultText (String text){
        textView_gameResult.setText(text);
    }
}
