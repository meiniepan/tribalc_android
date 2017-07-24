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
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.WalletPresenter;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.impl.IWalletView;
import com.gs.buluo.app.view.widget.MoneyTextView;
import com.gs.buluo.app.view.widget.panel.RechargePanel;
import com.gs.buluo.common.widget.LoadingDialog;

import java.text.NumberFormat;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/17.
 */
public class WalletActivity extends BaseActivity implements View.OnClickListener, IWalletView, DialogInterface.OnDismissListener {
    @Bind(R.id.wallet_balance)
    MoneyTextView mBalance;
    @Bind(R.id.wallet_credit_account)
    TextView tvCredit;
    @Bind(R.id.wallet_available_account)
    TextView tvAvaAccount;

    Context mCtx;
    private String pwd;
    private RechargePanel panel;
    private WalletAccount wallet;
//    private ArrayList<BankCard> list;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.custom_blue2);
        mCtx = this;
        findViewById(R.id.wallet_scan).setOnClickListener(this);
        findViewById(R.id.wallet_bill).setOnClickListener(this);
        findViewById(R.id.wallet_card).setOnClickListener(this);
        findViewById(R.id.wallet_coupon).setOnClickListener(this);
        findViewById(R.id.wallet_financial).setOnClickListener(this);
        findViewById(R.id.wallet_pwd).setOnClickListener(this);
        findViewById(R.id.wallet_credit).setOnClickListener(this);
        findViewById(R.id.wallet_recharge).setOnClickListener(this);
        findViewById(R.id.wallet_withdraw).setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_wallet;
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoadingDialog();
        ((WalletPresenter) mPresenter).getWalletInfo();
    }


    @Override
    public void onClick(View v) {
        if (wallet == null) {
            ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
            return;
        }
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.wallet_bill:
                intent.setClass(WalletActivity.this, BillActivity.class);
                startActivity(intent);
                break;
            case R.id.wallet_card:
                intent.setClass(mCtx, BankCardActivity.class);
                startActivity(intent);
                break;
            case R.id.wallet_coupon:
                ToastUtils.ToastMessage(getCtx(), R.string.no_function);
                break;
            case R.id.wallet_financial:
                ToastUtils.ToastMessage(getCtx(), R.string.no_function);
                break;
            case R.id.wallet_recharge:
                if (!TribeApplication.getInstance().isBf_recharge()) {
                    ToastUtils.ToastMessage(getCtx(), R.string.no_function);
                    break;
                }
                panel = new RechargePanel(this);
                panel.setData(wallet.balance);
                panel.show();
                panel.setOnDismissListener(this);
                break;
            case R.id.wallet_withdraw:
                if (!TribeApplication.getInstance().isBf_withdraw()) {
                    ToastUtils.ToastMessage(getCtx(), R.string.no_function);
                    break;
                }
                intent.putExtra(Constant.WALLET, wallet);
                intent.setClass(getCtx(), CashActivity.class);
                startActivity(intent);
                break;
            case R.id.wallet_pwd:
                if (TextUtils.isEmpty(pwd)) {
                    intent.setClass(mCtx, UpdateWalletPwdActivity.class);
                } else {
                    intent.putExtra(Constant.WALLET_PWD, pwd);
                    intent.setClass(mCtx, ConfirmActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.wallet_scan:
                intent.setClass(mCtx, CaptureActivity.class);
                startActivity(intent);
                break;
            case R.id.wallet_credit:
                intent.setClass(mCtx, CreditActivity.class);
                intent.putExtra(Constant.WALLET, wallet);
                startActivity(intent);
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
        this.wallet = account;
        setData(account);
    }

    public void setData(WalletAccount account) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(true);
        String format = nf.format(account.balance);
        mBalance.setMoneyText(format);
        tvCredit.setText(account.creditLimit + "");
        tvAvaAccount.setText((account.creditLimit * 100 - account.creditBalance * 100) / 100 + "");
    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(this, getString(res));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        ((WalletPresenter) mPresenter).getWalletInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoadingDialog.getInstance().dismissDialog();
    }
}
