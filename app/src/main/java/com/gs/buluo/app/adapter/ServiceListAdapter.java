package com.gs.buluo.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by hjn on 2016/11/24.
 */
public class ServiceListAdapter extends BaseAdapter{
    List mList;
    Context mCtx;
    public ServiceListAdapter(Context context) {
        mCtx=context;
    }

    public void setData(List list){
        mList=list;
    }

    @Override
    public int getCount() {
        return mList.size();
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
        return null;
    }
}
