package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.MoneyTextView;
import com.gs.buluo.app.view.widget.ProgressRing;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/7/21.
 */

public class CreditActivity extends BaseActivity {
    @Bind(R.id.credit_available)
    MoneyTextView tvAvailable;
    @Bind(R.id.credit_ring)
    ProgressRing mRing;
    @Bind(R.id.credit_bill)
    TextView tvBill;
    @Bind(R.id.credit_bill2)
    TextView tvBillDate;
    @Bind(R.id.credit_repayment)
    TextView tvRepayDate;
    @Bind(R.id.credit_limit)
    TextView tvLimit;
    @Bind(R.id.button_repay)
    Button btRepay;

    private String creditBillId;
    private String billAmount;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        WalletAccount account = getIntent().getParcelableExtra(Constant.WALLET);
        float leftCredit = (account.creditLimit * 100 - account.creditBalance * 100) / 100;
        tvAvailable.setMoneyText(leftCredit + "");
        mRing.setProgress((int) (leftCredit / account.creditLimit));

        findViewById(R.id.credit_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getCtx(), CreditBillActivity.class));
            }
        });

        tvLimit.setText(account.creditLimit + "");
        tvRepayDate.setText("每月" + account.repayDay + "日");
        tvBillDate.setText("每月" + account.billDay + "日");
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
        intent.putExtra(Constant.CREDIT_BALANCE, billAmount);
        intent.putExtra(Constant.CREDIT_BILL_ID,creditBillId);
        startActivity(intent);
    }

    public void setCreditData(CreditBill creditData) {
        billAmount = creditData.amount + "";
        tvBill.setText(billAmount);
        creditBillId = creditData.id;
        if (creditData.status == CreditBill.CreditBillStatus.PAID){
            btRepay.setEnabled(false);
            btRepay.setText("已还清");
        }
    }
}
