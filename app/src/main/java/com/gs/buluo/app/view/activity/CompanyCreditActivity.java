package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.view.widget.MoneyTextView;
import com.gs.buluo.app.view.widget.ProgressRing;

import butterknife.Bind;

/**
 * Created by Solang on 2017/7/18.
 */

public class CompanyCreditActivity extends BaseActivity
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

    @Override
    protected int getContentLayout() {
        return R.layout.activity_company_credit;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mRing.setProgress((int) (80/100));
    }
}
