package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.common.utils.AppManager;

import butterknife.BindView;

/**
 * Created by Solang on 2017/7/3.
 */

public class RentPaySuccessActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.ll_rent_pay_success_all_plan)
    LinearLayout llAllPlan;
    @BindView(R.id.btn_rent_pay_success_back)
    Button btnBack;
    @BindView(R.id.tv_rent_pay_success_num)
    TextView tvNum;
    private String num;
    private String apartmentCode;
    private String apartmentName;
    private String protocolId;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        num = getIntent().getStringExtra(Constant.RENT_PAYED_NUM);
        apartmentCode = getIntent().getStringExtra(Constant.RENT_APARTMENT_CODE);
        apartmentName = getIntent().getStringExtra(Constant.RENT_APARTMENT_NAME);
        protocolId = getIntent().getStringExtra(Constant.RENT_PROTOCOL_ID);
        tvNum.setText(num);
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
                intent.putExtra(Constant.RENT_APARTMENT_CODE,apartmentCode);
                intent.putExtra(Constant.RENT_APARTMENT_NAME,apartmentName);
                intent.putExtra(Constant.RENT_PROTOCOL_ID,protocolId);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_rent_pay_success_back:
                finish();
                break;
        }
    }
}
