package com.gs.buluo.app.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.CreditBill;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.app.view.widget.recyclerHelper.BaseQuickAdapter;
import com.gs.buluo.common.utils.TribeDateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by hjn on 2017/7/25.
 */

public class CreditBillAdapter extends BaseQuickAdapter<CreditBill, BaseHolder> {

    public CreditBillAdapter(@LayoutRes int layoutResId, @Nullable List<CreditBill> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseHolder helper, CreditBill item) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(item.billDate));
        int month = calendar.get(Calendar.MONTH) + 1;
        Log.e(TAG, "convert: " + month);
        if (helper.getAdapterPosition() == 0 || month == 12) {  //12月要显示年份
            helper.setText(R.id.bill_item_year, calendar.get(Calendar.YEAR) + "年");
            helper.getView(R.id.bill_item_year).setVisibility(View.VISIBLE);
        }
        helper.setText(R.id.credit_bill_title, month + "月账单");
        helper.setText(R.id.credit_bill_detail, TribeDateUtils.dateFormat5(new Date(item.zeroDate)) + "-" + TribeDateUtils.dateFormat5(new Date(item.billDate)));
        helper.setText(R.id.credit_bill_money, "¥" + item.amount);
    }
}
