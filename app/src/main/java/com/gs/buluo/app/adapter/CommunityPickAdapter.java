package com.gs.buluo.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.CommunityPlate;


import java.util.List;

/**
 * Created by fs on 2016/12/8.
 */
public class CommunityPickAdapter extends BaseAdapter  {

    public static final int TYPE_CITY = 1;
    public static final int TYPE_COMMUNITY = 2;
    private final List<CommunityPlate> mList;
    private final Context mContext;
    private final LayoutInflater mInflater;
    private String LastCity = "";

    public CommunityPickAdapter(Context context, List list) {
        mList = list;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_pick_community, parent, false);
            ViewHolder holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.mTextView.setText(mList.get(position).city);

        if (LastCity.equals(mList.get(position).city)) {
            holder.mCityText.setVisibility(View.GONE);
        }

        String city = mList.get(position).city;
        holder.mCityText.setText(city);
        holder.mTextView.setText(mList.get(position).name);
        LastCity = city;

        return convertView;
    }


    public static class ViewHolder {
        public TextView mTextView;
        public TextView mCityText;

        public ViewHolder(View view) {
            mTextView = (TextView) view.findViewById(R.id.item_pick_community);
            mCityText = (TextView) view.findViewById(R.id.item_pick_city);
        }
    }
}
