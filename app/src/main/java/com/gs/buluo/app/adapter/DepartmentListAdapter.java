package com.gs.buluo.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.RentProtocol;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.activity.DepartmentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjn on 2017/6/23.
 */

public class DepartmentListAdapter extends BaseAdapter implements View.OnClickListener {
    Context mCtx ;
    List<RentProtocol> datas;
    public DepartmentListAdapter(Context context, ArrayList<RentProtocol> datas) {
        mCtx = context;
        this.datas = datas;
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
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.department_item,parent,false);
        }

        TextView dId = (TextView) convertView.findViewById(R.id.department_item_id);
        TextView dName = (TextView) convertView .findViewById(R.id.department_item_name);
        convertView.findViewById(R.id.department_payment).setOnClickListener(this);
        convertView.findViewById(R.id.department_update_pwd).setOnClickListener(this);
        convertView.findViewById(R.id.department_temp_pwd).setOnClickListener(this);
        convertView.findViewById(R.id.department_power).setOnClickListener(this);
        convertView.findViewById(R.id.department_contract).setOnClickListener(this);
        convertView.findViewById(R.id.department_repayment).setOnClickListener(this);

        RentProtocol rentProtocol = datas.get(position);
        dId.setText(rentProtocol.sourceNum);
        dName.setText(rentProtocol.sourceName);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.department_payment:
                ToastUtils.ToastMessage(mCtx,"点了");
                break;
            case R.id.department_update_pwd:
                break;
            case R.id.department_temp_pwd:
                break;
            case R.id.department_power:
                break;
            case R.id.department_contract:
                break;
            case R.id.department_repayment:
                break;
        }
    }
}
