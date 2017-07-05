package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.gs.buluo.app.R;

import butterknife.Bind;

/**
 * Created by Solang on 2017/7/3.
 */

public class RentPaySuccessActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_rent_pay_success_all_plan)
    LinearLayout llAllPlan;
    @Bind(R.id.btn_rent_pay_success_back)
    Button btnBack;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        llAllPlan.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_rent_success;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_rent_pay_success_all_plan:
                Intent intent = new Intent(RentPaySuccessActivity.this,RentPaymentPlanActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_rent_pay_success_back:
                finish();
                break;
        }
    }
}
