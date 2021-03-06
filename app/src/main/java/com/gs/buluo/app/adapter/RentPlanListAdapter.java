package com.gs.buluo.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.PayRentEvent;
import com.gs.buluo.app.bean.RequestBodyBean.RentPlanItem;
import com.gs.buluo.app.view.activity.RentPaySuccessActivity;
import com.gs.buluo.app.view.widget.panel.NewPayPanel;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by Solang on 2017/7/3.
 */

public class RentPlanListAdapter extends BaseAdapter {
    private Context mCtx;
    private ArrayList<RentPlanItem> datas;
    private boolean first = true;
    private int firstUnfinished = 0;
    private String protocolId;
    private String apartmentCode;
    private String apartmentName;
    private int type = 0;

    public RentPlanListAdapter(Context context, ArrayList<RentPlanItem> datas, String protocolId, String apartmentCode, String apartmentName) {
        mCtx = context;
        this.datas = datas;
        this.protocolId = protocolId;
        this.apartmentCode = apartmentCode;
        this.apartmentName = apartmentName;
    }

    public void setData(ArrayList<RentPlanItem> listData, String protocolId, String s, String companyName) {
        this.datas = listData;
        this.protocolId = protocolId;
        this.apartmentCode = s;
        this.apartmentName = companyName;
    }

    public RentPlanListAdapter(Context mCtx, ArrayList<RentPlanItem> listData) {
        this.mCtx = mCtx;
        this.datas = listData;
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
        final RentPlanItem data = datas.get(position);
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
            holder.ivFinished = (ImageView) convertView.findViewById(R.id.iv_rent_plan_item_finished);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.tvNum.setText(data.num);
        holder.tvPlannedTime.setText(TribeDateUtils.SDF5.format(data.plannedTime));
        holder.tvRentTime.setText(TribeDateUtils.SDF5.format(data.startTime) + " 至 " + TribeDateUtils.SDF5.format(data.endTime));
        holder.tvPlannedRental.setText(data.plannedRental);
        if (data.finished) {
            holder.llHavePay.setVisibility(View.VISIBLE);
            holder.tvHavePay.setText(data.actualPay);
            holder.ivFinished.setImageResource(R.mipmap.rent_plan_finished);
            holder.tvPay.setVisibility(View.GONE);
        } else {
            if (first) {
                firstUnfinished = position;
                first = false;
            }
            if (position == firstUnfinished) {
                holder.tvPay.setVisibility(View.VISIBLE);
                holder.tvPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doPay(data);
                    }
                });
                if (System.currentTimeMillis() > data.plannedTime)
                    holder.ivFinished.setImageResource(R.mipmap.rent_plan_overdue);
            }
        }


        return convertView;
    }

    public void doPay(final RentPlanItem data) {
        if (type == 0) { //公司缴费
            NewPayPanel payPanel = new NewPayPanel(mCtx, new NewPayPanel.OnPayFinishListener() {
                @Override
                public void onPaySuccess() {
                    EventBus.getDefault().post(new PayRentEvent());
                }

                @Override
                public void onPayFail(ApiException e) {
                    ToastUtils.ToastMessage(mCtx, e.getDisplayMessage());
                }
            });
            payPanel.setData(data.plannedRental, protocolId, "rent", true);
            payPanel.show();
        } else {//个人缴费
//            Pay2mPanel payBoard = new Pay2mPanel(mCtx, null);
//            payBoard.setData(2, data.plannedRental, protocolId, data.num, apartmentCode, apartmentName);
//            payBoard.show();
            NewPayPanel payPanel = new NewPayPanel(mCtx, new NewPayPanel.OnPayFinishListener() {
                @Override
                public void onPaySuccess() {
                    Intent intent = new Intent(mCtx, RentPaySuccessActivity.class);
                    intent.putExtra(Constant.RENT_PAYED_NUM, data.num);
                    intent.putExtra(Constant.RENT_APARTMENT_CODE, apartmentCode);
                    intent.putExtra(Constant.RENT_APARTMENT_NAME, apartmentName);
                    intent.putExtra(Constant.RENT_PROTOCOL_ID, protocolId);
                    mCtx.startActivity(intent);
                }

                @Override
                public void onPayFail(ApiException e) {
                    ToastUtils.ToastMessage(mCtx, e.getDisplayMessage());
                }
            });
            payPanel.setData(data.plannedRental, protocolId, "rent");
            payPanel.show();
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    public void refresh() {
        first = true;
        notifyDataSetChanged();
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
