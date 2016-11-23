package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.BillEntity;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.TribeDateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/21.
 */
public class BillDetailActivity extends BaseActivity{
    @Bind(R.id.bill_detail_number)
    TextView mNumber;
    @Bind(R.id.bill_detail_money)
    TextView mMoney;
    @Bind(R.id.bill_detail_name)
    TextView mName;
    @Bind(R.id.bill_detail_time)
    TextView mTime;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        BillEntity entity= (BillEntity) getIntent().getSerializableExtra(Constant.BILL);
        mNumber.setText(entity.id);
        String amount = entity.amount;
        if (amount.contains("-")){
            mMoney.setText("支出"+amount.substring(1,amount.length()));
        }else {
            mMoney.setText("收入"+amount);
        }

        mName.setText(entity.annotherAccountId);

        long createTime = Long.parseLong(entity.createTime);
        Date date = new Date(createTime);
        mTime.setText(TribeDateUtils.dateFormat9(date));

        findViewById(R.id.bill_detail_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bill_detail;
    }
}
