package com.gs.buluo.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.RentProtocol;
import com.gs.buluo.app.view.activity.ContractActivity;
import com.gs.buluo.app.view.activity.HousePaymentActivity;
import com.gs.buluo.app.view.activity.RentPaymentPlanActivity;
import com.gs.buluo.app.view.activity.TempPwdActivity;
import com.gs.buluo.app.view.activity.UpdateDepartmentPwdActivity;
import com.gs.buluo.common.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjn on 2017/6/23.
 */

public class DepartmentListAdapter extends BaseAdapter {
    Context mCtx;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.department_item, parent, false);
        }

        TextView dId = (TextView) convertView.findViewById(R.id.department_item_id);
        TextView dName = (TextView) convertView.findViewById(R.id.department_item_name);
        convertView.findViewById(R.id.department_payment).setOnClickListener(new CustomOnClickListener(position));
        convertView.findViewById(R.id.department_update_pwd).setOnClickListener(new CustomOnClickListener(position));
        convertView.findViewById(R.id.department_temp_pwd).setOnClickListener(new CustomOnClickListener(position));
        convertView.findViewById(R.id.department_power).setOnClickListener(new CustomOnClickListener(position));
        convertView.findViewById(R.id.department_contract).setOnClickListener(new CustomOnClickListener(position));
        convertView.findViewById(R.id.department_repayment).setOnClickListener(new CustomOnClickListener(position));

        RentProtocol rentProtocol = datas.get(position);
        dId.setText(rentProtocol.sourceNum);
        dName.setText(rentProtocol.sourceName);
        return convertView;
    }

    private class CustomOnClickListener implements View.OnClickListener {
        private int mIndex;

        public CustomOnClickListener(int index) {
            mIndex = index;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            RentProtocol protocol = datas.get(mIndex);
            switch (v.getId()) {
                case R.id.department_payment:
                    Intent intent1 = new Intent(mCtx, HousePaymentActivity.class);
                    intent1.putExtra(Constant.RENT_PROTOCOL_ID, protocol.id);
                    intent1.putExtra(Constant.RENT_APARTMENT_CODE, protocol.sourceNum);
                    intent1.putExtra(Constant.RENT_APARTMENT_NAME, protocol.sourceName);
                    mCtx.startActivity(intent1);
                    break;
                case R.id.department_update_pwd:
                    intent.setClass(mCtx,UpdateDepartmentPwdActivity.class);
                    intent.putExtra(Constant.DEPARTMENT_ID,protocol.sourceId);
                    intent.putExtra(Constant.DEPARTMENT_NAME,protocol.sourceName);
                    intent.putExtra(Constant.DEPARTMENT_NUMBER,protocol.sourceNum);
                    intent.putExtra(Constant.DEPARTMENT_SN,protocol.sn);
                    mCtx.startActivity(intent);
                    break;
                case R.id.department_temp_pwd:
                    intent.setClass(mCtx,TempPwdActivity.class);
                    intent.putExtra(Constant.DEPARTMENT_ID,protocol.sourceId);
                    intent.putExtra(Constant.DEPARTMENT_NAME,protocol.sourceName);
                    intent.putExtra(Constant.DEPARTMENT_NUMBER,protocol.sourceNum);
                    intent.putExtra(Constant.DEPARTMENT_SN,protocol.sn);
                    mCtx.startActivity(intent);
                    break;
                case R.id.department_power:
                    ToastUtils.ToastMessage(mCtx,R.string.no_function);
                    break;
                case R.id.department_contract:
                    intent.setClass(mCtx, ContractActivity.class);
                    intent.putExtra(Constant.CONTRACT, protocol.pictures);
                    mCtx.startActivity(intent);
                    break;
                case R.id.department_repayment:
                    Intent repayIntent = new Intent(mCtx, RentPaymentPlanActivity.class);
                    repayIntent.putExtra(Constant.RENT_PROTOCOL_ID, protocol.id);
                    repayIntent.putExtra(Constant.RENT_APARTMENT_CODE, protocol.sourceNum);
                    repayIntent.putExtra(Constant.RENT_APARTMENT_NAME, protocol.sourceName);
                    mCtx.startActivity(repayIntent);
                    break;
            }
        }
    }

}
