package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.view.widget.MoneyTextView;
import com.gs.buluo.app.view.widget.ProgressRing;

import butterknife.Bind;

/**
 * Created by hjn on 2017/7/21.
 */

public class CreditActivity extends BaseActivity {
    @Bind(R.id.credit_available)
    MoneyTextView tvAvailable;
    @Bind(R.id.credit_ring)
    ProgressRing mRing;
    @Bind(R.id.credit_bill)
    TextView tvBill;
    @Bind(R.id.credit_bill2)
    TextView tvBillDate;
    @Bind(R.id.credit_repayment)
    TextView tvRepayDate;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        WalletAccount account = getIntent().getParcelableExtra(Constant.WALLET);
        float leftCredit = (account.creditLimit * 100 - account.creditBalance * 100) / 100;
        tvAvailable.setMoneyText(leftCredit + "");
        mRing.setProgress((int) (leftCredit / account.creditLimit));

        findViewById(R.id.credit_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getCtx(), CreditBillActivity.class));
            }
        });

        tvBill.setText(account.creditBalance + "");
        tvRepayDate.setText("每月" + account.repayDay + "日");
        tvBillDate.setText("每月" + account.billDay + "日");
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_credit;
    }

    public void doRepayment(View view) {
        Intent intent = new Intent(getCtx(), CreditRepaymentActivity.class);
        startActivity(intent);
    }
}
