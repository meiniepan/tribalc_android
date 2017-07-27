package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.view.widget.MoneyTextView;
import com.gs.buluo.app.view.widget.ProgressRing;

import butterknife.Bind;

/**
 * Created by Solang on 2017/7/18.
 */

public class CompanyCreditActivity extends BaseActivity implements View.OnClickListener
{
    @Bind(R.id.company_credit_available)
    MoneyTextView tvAvailable;
    @Bind(R.id.company_credit_ring)
    ProgressRing mRing;
    @Bind(R.id.company_credit_bill)
    TextView tvBill;
    @Bind(R.id.company_credit_bill2)
    TextView tvBillDate;
    @Bind(R.id.company_credit_repayment)
    TextView tvRepayDate;
    private Context mCtx;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_company_credit;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx = getCtx();
        findViewById(R.id.company_credit_history).setOnClickListener(this);
        mRing.setProgress(80);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.company_credit_history:
                intent.setClass(mCtx, BillActivity.class);
                startActivity(intent);
                break;
        }
    }
}
