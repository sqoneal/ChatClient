package com.liebao.zzj.chatclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kszen on 5/24/2016.
 */
public class MyAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<String> mData;
    private Context mcontext;

    public MyAdapter(Context context, ArrayList<String> lData) {
        mInflater = LayoutInflater.from(context);
        this.mData = lData;
        this.mcontext = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (position % 2 == 0) {
            if (vh == null) {
                convertView = mInflater.inflate(R.layout.listlayout, null);
                vh = new ViewHolder();
                vh.mimageView = (ImageView) convertView.findViewById(R.id.imageview1);
                vh.mtextView = (TextView) convertView.findViewById(R.id.textview1);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
        } else {
            if (vh == null) {
                convertView = mInflater.inflate(R.layout.listlayout2, null);
                vh = new ViewHolder();
                vh.mimageView = (ImageView) convertView.findViewById(R.id.imageview2);
                vh.mtextView = (TextView) convertView.findViewById(R.id.textview2);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
        }
        vh.mimageView.setBackgroundResource(R.mipmap.ic_launcher);
        RotateAnimation ra = new RotateAnimation(0f,360f,Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        ra.setDuration(3000);
        ra.setRepeatCount(Animation.INFINITE);
        //ra.setRepeatMode(Animation.RESTART);
//        ra.setStartOffset(2000);
        vh.mimageView.setAnimation(ra);
        ra.startNow();
        vh.mtextView.setText(position + "--->" + mData.get(position));
        return convertView;
    }

    public final class ViewHolder {
        public ImageView mimageView;
        public TextView mtextView;
    }
}
