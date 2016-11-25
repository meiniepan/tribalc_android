package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.WalletPresenter;
import com.gs.buluo.app.utils.SharePreferenceManager;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.impl.IWalletView;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/17.
 */
public class WalletActivity extends BaseActivity implements View.OnClickListener,IWalletView {
    @Bind(R.id.wallet_money)
    TextView mMoney;

    Context mCtx;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx=this;
        setBarColor(R.color.common_dark);
        findViewById(R.id.wallet_scan).setOnClickListener(this);
        findViewById(R.id.wallet_bill).setOnClickListener(this);
        findViewById(R.id.wallet_card).setOnClickListener(this);
        findViewById(R.id.wallet_coupon).setOnClickListener(this);
        findViewById(R.id.wallet_financial).setOnClickListener(this);
        findViewById(R.id.wallet_pwd).setOnClickListener(this);
        findViewById(R.id.wallet_back).setOnClickListener(this);

        ((WalletPresenter)mPresenter).getWalletInfo();
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
                startActivity(intent);
                break;
            case R.id.wallet_card:
                startActivity(new Intent(mCtx,BankCardActivity.class));
                break;
            case R.id.wallet_coupon:
                break;
            case R.id.wallet_financial:
                break;
            case R.id.wallet_pwd:
                if (TextUtils.isEmpty(SharePreferenceManager.getInstance(getApplicationContext()).getStringValue(Constant.WALLET_PWD))){
                    intent.setClass(mCtx,UpdateWalletPwdActivity.class);
                }else {
                    intent.setClass(mCtx,ConfirmActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.wallet_scan:
                break;
            case R.id.wallet_back:
                finish();
                break;
        }
    }

    @Override
    protected BasePresenter getPresenter() {
        return new WalletPresenter();
    }

    @Override
    public void getWalletInfoFinished(WalletAccount account) {
        SharePreferenceManager.getInstance(getApplicationContext()).setValue(Constant.WALLET_PWD,account.password);
        mMoney.setText(account.balance);
    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(this,getString(res));
    }
}
