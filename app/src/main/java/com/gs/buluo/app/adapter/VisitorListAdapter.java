package com.gs.buluo.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.VisitorBean;

import java.util.ArrayList;

/**
 * Created by hjn on 2017/3/9.
 */
public class VisitorListAdapter extends BaseExpandableListAdapter {
    Context mCtx;
    ArrayList<VisitorBean> list;
    public VisitorListAdapter(Context ctx, ArrayList<VisitorBean> list) {
        this.mCtx =ctx;
        this.list=list;
    }

//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return list.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView==null){
//            convertView = LayoutInflater.from(mCtx).inflate(R.layout.visitor_list_item,parent,false);
//        }
//        TextView tv = (TextView) convertView.findViewById(R.id.item_door_name);
//
//        final VisitorBean visitorBean = list.get(position);
//        tv.setText(visitorBean.name);
//        convertView.findViewById(R.id.visitor_delete).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                list.remove(visitorBean);
//                notifyDataSetChanged();
//            }
//        });
//
//        return convertView;
//    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).door.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).door.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.visitor_list_item,parent,false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.item_door_name);
        ImageView sign = (ImageView) convertView.findViewById(R.id.visitor_arrow);
        ImageView icon = (ImageView) convertView.findViewById(R.id.visitor_icon);
        if (isExpanded){
            sign.setImageResource(R.mipmap.indicator_up);
            icon.setImageResource(R.mipmap.visitor_head_active);
        }else {
            sign.setImageResource(R.mipmap.indicator_down);
            icon.setImageResource(R.mipmap.visitor_head);
        }

        final VisitorBean visitorBean = list.get(groupPosition);
        tv.setText(visitorBean.name);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.visitor_child_view,parent,false);
        }
        TextView doorName = (TextView) convertView.findViewById(R.id.visitor_child_name);
        doorName.setText(list.get(groupPosition).door.get(childPosition));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
