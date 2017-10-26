package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.CreditBill;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.widget.MoneyTextView;
import com.gs.buluo.app.view.widget.ProgressRing;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/7/21.
 */

public class CreditActivity extends BaseActivity {
    @BindView(R.id.credit_available)
    MoneyTextView tvAvailable;
    @BindView(R.id.credit_ring)
    ProgressRing mRing;
    @BindView(R.id.credit_bill)
    TextView tvBill;
    @BindView(R.id.credit_bill2)
    TextView tvBillDate;
    @BindView(R.id.credit_repayment)
    TextView tvRepayDate;
    @BindView(R.id.credit_limit)
    TextView tvLimit;
    @BindView(R.id.button_repay)
    Button btRepay;
    @BindView(R.id.credit_icon)
    ImageView ivStatusIcon;
    private CreditBill creditBill;
    private double walletBalance;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.white);
        WalletAccount account = getIntent().getParcelableExtra(Constant.WALLET);
        walletBalance = account.balance ;
        float leftCredit = (account.creditLimit * 100 - account.creditBalance * 100) / 100;
        tvAvailable.setMoneyText(leftCredit + "");
        int progress = (int) (leftCredit / account.creditLimit * 100);
        if (progress < 100 && progress > 98) {
            progress = 98;
        }
        if (account.creditStatus == WalletAccount.CreditStatus.OVERDUE) {
            mRing.setPaintColor(0xfff43731, 0xfffc8e57, 0xfff43731);
            btRepay.setBackgroundColor(0xfff43731);
            ivStatusIcon.setImageResource(R.mipmap.credit_overdue_icon);
            findViewById(R.id.credit_overdue_mark).setVisibility(View.VISIBLE);
        }
        mRing.setProgress(progress);

        findViewById(R.id.credit_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getCtx(), CreditBillActivity.class);
                intent.putExtra(Constant.TARGET_ID, TribeApplication.getInstance().getUserInfo().getId());
                startActivity(intent);
            }
        });

        tvLimit.setText(account.creditLimit + "");
        tvRepayDate.setText("每月 " + account.repayDay + " 日");
        tvBillDate.setText("每月 " + account.billDay + " 日");
        showLoadingDialog(false);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).getCurrentCreditBill(TribeApplication.getInstance().getUserInfo().getId())
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

    @Override
    protected int getContentLayout() {
        return R.layout.activity_credit;
    }

    public void doRepayment(View view) {
        Intent intent = new Intent(getCtx(), CreditRepaymentActivity.class);
        intent.putExtra(Constant.CREDIT_BILL, creditBill);
        intent.putExtra(Constant.BALANCE, walletBalance);
        startActivity(intent);
    }

    public void setCreditData(CreditBill creditData) {
        if (creditData == null) {
            tvBill.setText("本期账单已还清");
            tvBill.setTextColor(getResources().getColor(R.color.custom_blue2));
            btRepay.setEnabled(false);
            return;
        }
        this.creditBill = creditData;
        tvBill.setText(creditData.amount + "");
        if (creditData.status == CreditBill.CreditBillStatus.PAID) {
            tvBill.setText("本期账单已还清");
            tvBill.setTextColor(getResources().getColor(R.color.custom_blue2));
            btRepay.setEnabled(false);
            btRepay.setText("已还清");
        }
        if (creditData.status == CreditBill.CreditBillStatus.OVERDUE) {
            mRing.setPaintColor(0xfff43731, 0xfffc8e57, 0xfff43731);
            btRepay.setBackgroundColor(0xfff43731);
            ivStatusIcon.setImageResource(R.mipmap.credit_overdue_icon);
            findViewById(R.id.credit_overdue_mark).setVisibility(View.VISIBLE);
        }
    }
}
