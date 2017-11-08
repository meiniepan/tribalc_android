package com.gs.buluo.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.WelfareEntity;

import java.util.ArrayList;

/**
 * Created by hjn on 2017/11/7.
 */

public class WelfareMemberAdapter extends BaseAdapter {
    Context context;
    ArrayList<WelfareEntity.WelfareMember> members;

    public WelfareMemberAdapter(Context context, ArrayList<WelfareEntity.WelfareMember> members) {
        this.context = context;
        this.members = members;
    }

    @Override
    public int getCount() {
        return members.size();
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_welfare_member, parent, false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.welfare_item_name);
        TextView tvAmount = (TextView) convertView.findViewById(R.id.welfare_item_amount);

        tvName.setText(members.get(position).targetName);
        tvAmount.setText(members.get(position).amount);

        return convertView;
    }
}
