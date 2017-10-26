package com.gs.buluo.app.view.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.CreditBill;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.app.view.widget.MoneyTextView;
import com.gs.buluo.app.view.widget.ProgressRing;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/7/18.
 */

public class CompanyCreditActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.company_credit_available)
    MoneyTextView tvAvailable;
    @BindView(R.id.company_credit_ring)
    ProgressRing mRing;
    @BindView(R.id.company_credit_bill)
    TextView tvBill;
    @BindView(R.id.company_credit_bill2)
    TextView tvBillDate;
    @BindView(R.id.company_credit_repayment)
    TextView tvRepayDate;
    @BindView(R.id.company_credit_limit)
    TextView tvLimit;
    @BindView(R.id.company_button_repay)
    Button btRepay;
    private Context mCtx;
    private String billAmount;
    private CreditBill creditBill;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_company_credit;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx = getCtx();
        findViewById(R.id.company_credit_history).setOnClickListener(this);
        btRepay.setOnClickListener(this);
        WalletAccount account = getIntent().getParcelableExtra(Constant.WALLET);
        float leftCredit = (account.creditLimit * 100 - account.creditBalance * 100) / 100;
        tvAvailable.setMoneyText(leftCredit + "");
        mRing.setProgress((int) (leftCredit * 100 / account.creditLimit));
        tvLimit.setText(account.creditLimit + "");
        tvRepayDate.setText("每月" + account.repayDay + "日");
        tvBillDate.setText("每月" + account.billDay + "日");
        showLoadingDialog(false);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).getCurrentCreditBill(TribeApplication.getInstance().getUserInfo().getCompanyID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CreditBill>>() {
                    @Override
                    public void onNext(BaseResponse<CreditBill> creditBillBaseResponse) {
                        setCreditData(creditBillBaseResponse.data);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        ToastUtils.ToastMessage(getCtx(), e.getDisplayMessage());
                    }
                });
    }

    public void setCreditData(CreditBill creditData) {
        if (creditData == null) {
            tvBill.setText(0 + "");
            btRepay.setEnabled(false);
            tvBillDate.setVisibility(View.GONE);
            tvRepayDate.setVisibility(View.GONE);
            return;
        }
        billAmount = creditData.amount + "";
        this.creditBill = creditData;
        tvBill.setText(billAmount);
        if (creditData.status == CreditBill.CreditBillStatus.PAID) {
            btRepay.setEnabled(false);
            btRepay.setText("已还清");
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.company_credit_history:
                intent.setClass(mCtx, CreditBillActivity.class);
                intent.putExtra(Constant.TARGET_ID, TribeApplication.getInstance().getUserInfo().getCompanyID());
                startActivity(intent);
                break;
            case R.id.company_button_repay:
                intent = new Intent(getCtx(), CreditCompanyRepaymentActivity.class);
                intent.putExtra(Constant.CREDIT_BILL, creditBill);
                startActivity(intent);
                break;

        }
    }
}
