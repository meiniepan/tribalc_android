package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.common.utils.AppManager;

import butterknife.Bind;

/**
 * Created by Solang on 2017/6/8.
 */

public class Pay2MerchantSuccessActivity extends BaseActivity {
    @Bind(R.id.tv_pay2m_success_name)
    TextView tvMerchantName;
    @Bind(R.id.tv_pay2m_success_money)
    TextView tvMoney;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        tvMerchantName.setText(getIntent().getStringExtra("name"));
        tvMoney.setText(getIntent().getStringExtra("money"));
        findViewById(R.id.pay2m_success_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFinish();
            }
        });
        findViewById(R.id.btn_pay2m_success_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFinish();
            }
        });
    }

    private void myFinish() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
        AppManager.getAppManager().finishActivity(Pay2MerchantActivity.class);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.pay2merchant_success;
    }
}
