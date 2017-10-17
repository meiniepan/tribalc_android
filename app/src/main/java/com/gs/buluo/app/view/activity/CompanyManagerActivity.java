package com.gs.buluo.app.view.activity;

import android.content.Context;
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
import com.gs.buluo.app.presenter.CompanyManagerPresenter;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.app.view.impl.ICompanyManagerView;
import com.gs.buluo.app.view.widget.MoneyTextView;
import com.gs.buluo.common.widget.LoadingDialog;

import butterknife.BindView;

/**
 * Created by Solang on 2017/7/24.
 */

public class CompanyManagerActivity extends BaseActivity implements View.OnClickListener, ICompanyManagerView {
    @BindView(R.id.company_balance)
    MoneyTextView mBalance;
    @BindView(R.id.company_credit_limit)
    TextView tvCredit;
    @BindView(R.id.company_available_limit)
    TextView tvAvaAccount;
    @BindView(R.id.tv_name)
    TextView tvName;
    Context mCtx;
    private String companyId;
    private WalletAccount account;
    private String pwd;
    private double balance;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_company_manager;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx = this;
        tvName.setText(TribeApplication.getInstance().getUserInfo().getCompanyName());
        companyId = TribeApplication.getInstance().getUserInfo().getCompanyID();
        findViewById(R.id.company_recharge).setOnClickListener(this);
        findViewById(R.id.company_bill).setOnClickListener(this);
        findViewById(R.id.company_credit).setOnClickListener(this);
        findViewById(R.id.company_pay_password).setOnClickListener(this);
        findViewById(R.id.company_pay_rent).setOnClickListener(this);
        tvName.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoadingDialog();
        ((CompanyManagerPresenter) mPresenter).getCompanyInfo(companyId);
    }

    @Override
    protected BasePresenter getPresenter() {
        return new CompanyManagerPresenter();
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tv_name:
                tvName.setVisibility(View.GONE);
                break;
            case R.id.company_recharge:
                if (!TribeApplication.getInstance().isBf_recharge()) {
                    ToastUtils.ToastMessage(getCtx(), R.string.no_function);
                    break;
                }
                intent.setClass(this,RechargeActivity.class);
                intent.putExtra(Constant.TARGET_ID,TribeApplication.getInstance().getUserInfo().getCompanyID());
                startActivity(intent);
                break;
            case R.id.company_bill:
                intent.setClass(mCtx, BillActivity.class);
                intent.putExtra(Constant.TARGET_ID, TribeApplication.getInstance().getUserInfo().getCompanyID());
                startActivity(intent);
                break;
            case R.id.company_credit:
                intent.setClass(mCtx, CompanyCreditActivity.class);
                intent.putExtra(Constant.WALLET, account);
                startActivity(intent);
                break;
            case R.id.company_pay_password:
                if (TextUtils.isEmpty(pwd)) {
                    intent.setClass(mCtx, UpdateWalletPwdActivity.class);
                    intent.putExtra(Constant.TARGET_ID, TribeApplication.getInstance().getUserInfo().getCompanyID());
                } else {
                    intent.putExtra(Constant.WALLET_PWD, pwd);
                    intent.setClass(mCtx, ConfirmActivity.class);
                    intent.putExtra(Constant.TARGET_ID, TribeApplication.getInstance().getUserInfo().getCompanyID());
                }
                startActivity(intent);
                break;
            case R.id.company_pay_rent:
                intent.setClass(mCtx, CompanyPayRentActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(this, getString(res));
    }

    @Override
    public void getCompanyInfoFinished(WalletAccount account) {
        this.account = account;
        pwd = account.password;
        balance = account.balance;
        mBalance.setMoneyText(account.balance + "");
        tvCredit.setText(account.creditLimit + "");
        tvAvaAccount.setText((account.creditLimit * 100 - account.creditBalance * 100) / 100 + "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoadingDialog.getInstance().dismissDialog();
    }
}
