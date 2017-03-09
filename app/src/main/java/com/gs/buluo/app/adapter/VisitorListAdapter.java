package com.gs.buluo.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.VisitorBean;

import java.util.ArrayList;

/**
 * Created by hjn on 2017/3/9.
 */
public class VisitorListAdapter extends BaseAdapter {
    Context mCtx;
    ArrayList<VisitorBean> list;
    public VisitorListAdapter(Context ctx, ArrayList list) {
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
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.visitor_list_item,parent,false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.item_door_name);

        final VisitorBean visitorBean = list.get(position);
        tv.setText(visitorBean.name);
        convertView.findViewById(R.id.visitor_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(visitorBean);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

}
