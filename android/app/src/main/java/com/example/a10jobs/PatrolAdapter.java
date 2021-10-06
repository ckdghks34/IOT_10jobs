package com.example.a10jobs;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PatrolAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<PatrolItem> sample;

    public PatrolAdapter(Context context, ArrayList<PatrolItem> data){
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public PatrolItem getItem(int position) {
        return sample.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View converView, ViewGroup viewGroup) {
        View view = mLayoutInflater.inflate(R.layout.patrol_item, null);

        TextView title = (TextView) view.findViewById(R.id.logDate);
        ImageView img = (ImageView) view.findViewById(R.id.logImg);

        title.setText(sample.get(position).getDate());
        img.setImageBitmap(sample.get(position).getImg());



        return view;
    }
}
