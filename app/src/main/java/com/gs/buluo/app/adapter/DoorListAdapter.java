package com.gs.buluo.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.LockEquip;

import java.util.ArrayList;

/**
 * Created by hjn on 2017/3/9.
 */
public class DoorListAdapter extends BaseAdapter {
    Context mCtx;
    ArrayList<LockEquip> list;
    public DoorListAdapter(Context ctx, ArrayList<LockEquip> list) {
        this.mCtx =ctx;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.door_list_item,parent,false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.item_door_name);
        LockEquip equip = list.get(position);
        tv.setText(equip.name);
        return convertView;
    }
}
