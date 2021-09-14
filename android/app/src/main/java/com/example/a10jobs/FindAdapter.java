package com.example.a10jobs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FindAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<FindItem> data;
    private int layout;

    public FindAdapter(Context context, int layout, ArrayList<FindItem> data){
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position).getTitle();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view == null){
            view = inflater.inflate(layout, viewGroup, false);
        }
        FindItem item = data.get(position);

        TextView title = (TextView) view.findViewById(R.id.findObject);
        title.setText(item.getTitle());


        return view;
    }
}