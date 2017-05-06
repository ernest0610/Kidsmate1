package com.example.ernest.kidsmate1;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by User on 2017-05-05.
 */

public class MyAdapter_test extends BaseAdapter {
    Context context;
    ArrayList<Item_test> items = new ArrayList<Item_test>();

    public MyAdapter_test(Context context) {
        super();
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public  void addItem(Item_test item) {
        items.add(item);
    }

    public void removeItem(int position) {
        items.remove(position);
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemView_test view = new ItemView_test(context);
        Item_test item = items.get(position);
        view.setTextView(item.getString());
        return view;
    }
}
