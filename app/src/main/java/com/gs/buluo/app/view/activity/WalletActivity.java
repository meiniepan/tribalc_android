package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.eventbus.TopUpEvent;
import com.gs.buluo.app.model.MoneyModel;
import com.gs.buluo.app.network.TribeCallback;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.WalletPresenter;
import com.gs.buluo.app.utils.SharePreferenceManager;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.impl.IWalletView;
import com.gs.buluo.app.view.widget.panel.RechargePanel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/17.
 */
public class WalletActivity extends BaseActivity implements View.OnClickListener,IWalletView, DialogInterface.OnDismissListener {
    @Bind(R.id.wallet_integer)
    TextView mInterger;
    @Bind(R.id.wallet_float)
    TextView mFloat;
    Context mCtx;
    private String pwd;
    private RechargePanel panel;
    private String balance;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx=this;
        findViewById(R.id.wallet_scan).setOnClickListener(this);
        findViewById(R.id.wallet_bill).setOnClickListener(this);
        findViewById(R.id.wallet_card).setOnClickListener(this);
        findViewById(R.id.wallet_coupon).setOnClickListener(this);
        findViewById(R.id.wallet_financial).setOnClickListener(this);
        findViewById(R.id.wallet_pwd).setOnClickListener(this);
        findViewById(R.id.wallet_back).setOnClickListener(this);
        findViewById(R.id.wallet_recharge).setOnClickListener(this);

        ((WalletPresenter)mPresenter).getWalletInfo();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_wallet;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ((WalletPresenter)mPresenter).getWalletInfo();
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
                intent.setClass(mCtx,BankCardActivity.class);
                startActivity(intent);
                break;
            case R.id.wallet_coupon:
                break;
            case R.id.wallet_financial:
                break;
            case R.id.wallet_recharge:
                panel = new RechargePanel(this);
                panel.setData(balance);
                panel.show();
                panel.setOnDismissListener(this);
                break;
            case R.id.wallet_pwd:
                if (TextUtils.isEmpty(pwd)){
                    intent.setClass(mCtx,UpdateWalletPwdActivity.class);
                }else {
                    intent.putExtra(Constant.WALLET_PWD,pwd);
                    intent.setClass(mCtx,ConfirmActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.wallet_scan:
                intent.setClass(mCtx,CaptureActivity.class);
                startActivity(intent);
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
        pwd = account.password;
        balance = account.balance;
        setData(balance);
    }
    public void setData(String price) {
        if (price==null)return;
        String[] arrs = price.split("\\.");
        if (arrs.length > 1) {
            mInterger.setText(arrs[0]);
            mFloat.setText(arrs[1]);
        } else {
            mInterger.setText(price);
            mFloat.setText("00");
        }
    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(this,getString(res));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        ((WalletPresenter)mPresenter).getWalletInfo();
    }
}
