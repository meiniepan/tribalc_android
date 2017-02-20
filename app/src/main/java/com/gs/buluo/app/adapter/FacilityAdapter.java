package com.gs.buluo.app.adapter;

import android.content.Context;
import android.content.pm.LabeledIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.view.activity.FacilityDetailActivity;

import java.util.ArrayList;

/**
 * Created by hjn on 2017/2/20.
 */
public class FacilityAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> datas;
    public FacilityAdapter(Context context, ArrayList<String> list) {
        this.context =context;
        datas =list;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.serve_detail_facility,parent,false);
        }
        ImageView iv= (ImageView) convertView.findViewById(R.id.facility_image);
        TextView tv= (TextView) convertView.findViewById(R.id.facility_text);

        String[] strings = datas.get(position).split(",");
        String name = strings[0];

        int res = Integer.parseInt(strings[1]);
        iv.setImageResource(res);
        tv.setText(name);

        return convertView;
    }
}
