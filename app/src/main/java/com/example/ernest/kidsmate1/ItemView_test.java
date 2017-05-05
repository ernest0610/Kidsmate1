package com.example.ernest.kidsmate1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by User on 2017-05-05.
 */

public class ItemView_test extends LinearLayout {
    TextView textView;

    public ItemView_test(Context context) {
        super(context);
        init(context);
    }

    public ItemView_test(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_test, this, true);

        textView = (TextView) findViewById(R.id.textView_item_test);
    }

    public void setTextView(String string) {
        textView.setText(string);
    }
}
