package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.app.R;

/**
 * Created by hjn on 2016/11/17.
 */
public class WalletActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.wallet_scan).setOnClickListener(this);
        findViewById(R.id.wallet_bill).setOnClickListener(this);
        findViewById(R.id.wallet_card).setOnClickListener(this);
        findViewById(R.id.wallet_coupon).setOnClickListener(this);
        findViewById(R.id.wallet_financial).setOnClickListener(this);
        findViewById(R.id.wallet_pwd).setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_wallet;
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()){
            case R.id.wallet_bill:
                intent.setClass(WalletActivity.this,BillActivity.class);
                break;
            case R.id.wallet_card:
                break;
            case R.id.wallet_coupon:
                break;
            case R.id.wallet_financial:
                break;
            case R.id.wallet_pwd:
                break;
            case R.id.wallet_scan:
                break;
        }
    }
}
