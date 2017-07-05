package com.gs.buluo.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.RequestBodyBean.RentPlanItem;

import java.util.ArrayList;

/**
 * Created by Solang on 2017/7/3.
 */

public class RentPlanListAdapter extends BaseAdapter {
    private Context mCtx;
    private ArrayList<RentPlanItem> datas;

    public RentPlanListAdapter(Context context, ArrayList<RentPlanItem> datas) {
        mCtx = context;
        this.datas = datas;
    }

    @Override

    public int getCount() {
        return datas.size();
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
        RentPlanItem data = datas.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.rent_plan_list_item, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.tvNum = (TextView) convertView.findViewById(R.id.tv_rent_plan_item_num);
            holder.tvPlannedTime = (TextView) convertView.findViewById(R.id.tv_rent_plan_item_pay_day);
            holder.tvRentTime = (TextView) convertView.findViewById(R.id.tv_rent_plan_item_rent_time);
            holder.tvPlannedRental = (TextView) convertView.findViewById(R.id.tv_rent_plan_item_should_pay);
            holder.llHavePay = (LinearLayout) convertView.findViewById(R.id.ll_rent_plan_have_pay);
            holder.tvHavePay = (TextView) convertView.findViewById(R.id.tv_rent_plan_item_have_pay);
            holder.tvPay = (TextView) convertView.findViewById(R.id.tv_rent_plan_item_pay);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.tvNum.setText(data.num);

        return convertView;
    }

    private class ViewHolder {
        private TextView tvNum;
        private TextView tvPlannedTime;
        private TextView tvRentTime;
        private TextView tvPlannedRental;
        private TextView tvActualPay;
        private ImageView ivFinished;
        private LinearLayout llHavePay;
        private TextView tvHavePay;
        private TextView tvPay;
    }
}
