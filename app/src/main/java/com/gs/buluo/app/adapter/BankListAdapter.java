package com.gs.buluo.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gs.buluo.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fs on 2016/12/22.
 */

public class BankListAdapter extends BaseAdapter {

    private final Context mContext;
    private LayoutInflater mInflater;
    private List<String> mList=new ArrayList<>();

    public BankListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<String> list){
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
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
        if (convertView==null){
            convertView = mInflater.inflate(R.layout.item_pick_bank, parent, false);
            ViewHolder holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.mTextView.setText(mList.get(position));
        return convertView;
    }

    public static class ViewHolder{

        public TextView mTextView;

        public ViewHolder(View view) {
            mTextView = (TextView) view.findViewById(R.id.bank_name);
        }

    }

}
