package com.gs.buluo.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.app.R;


/**
 * Created by hjn on 2016/11/16.
 */
public class ServeSortGridAdapter extends BaseAdapter {
    Context mCtx;
    int[] icons = {R.mipmap.average_low, R.mipmap.average_high, R.mipmap.most_popular, R.mipmap.most_near, R.mipmap.most_comment};
    int[] iconsSelc = {R.mipmap.average_low_selc, R.mipmap.average_high_selc, R.mipmap.most_popular_selc, R.mipmap.most_near_selc, R.mipmap.most_comment_selc};
    int[] names = {R.string.average_low, R.string.average_high, R.string.most_popular, R.string.most_near, R.string.most_comment};
    private int mPos = -1;
    private ImageView iv;
    private TextView tv;

    public ServeSortGridAdapter(Context context) {
        mCtx = context;
    }

    @Override
    public int getCount() {
        return 5;
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
        View view = LayoutInflater.from(mCtx).inflate(R.layout.sort_item, null);
        if (position==2){
            view.findViewById(R.id.sort_divider).setVisibility(View.GONE);
        }
        iv = (ImageView) view.findViewById(R.id.sort_icon);
        tv = (TextView) view.findViewById(R.id.sort_name);
//        iv.setImageResource(icons[position]);
        tv.setText(names[position]);
        if (position == mPos) {
            tv.setTextColor(0xff50c7d1);
            iv.setImageResource(iconsSelc[position]);
        } else {
            tv.setTextColor(0xff9a9a9a);
            iv.setImageResource(icons[position]);
        }
        return view;
    }

    public void setPos(int pos) {
        mPos = pos;
    }
}
