package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.app.R;
import com.gs.buluo.app.view.widget.MoneyTextView;

import butterknife.Bind;

/**
 * Created by hjn on 2017/7/21.
 */

public class CreditActivity extends BaseActivity {
    @Bind(R.id.credit_available)
    MoneyTextView tvAvailable;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        tvAvailable.setMoneyText("188.88");

        findViewById(R.id.credit_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getCtx(), CreditBillActivity.class));
            }
        });
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
