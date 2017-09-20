package com.gs.buluo.app.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.CreditRepayAdapter;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.CreditBill;
import com.gs.buluo.app.bean.OrderPayment;
import com.gs.buluo.app.bean.Pay2MerchantRequest;
import com.gs.buluo.app.bean.PayChannel;
import com.gs.buluo.app.bean.RequestBodyBean.WxPayRequest;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.BFUtil;
import com.gs.buluo.app.utils.WXUtils;
import com.gs.buluo.app.view.widget.CustomAlertDialog;
import com.gs.buluo.app.view.widget.panel.NewPasswordPanel;
import com.gs.buluo.app.view.widget.panel.PayChoosePanel;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.LoadingDialog;

import butterknife.Bind;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/7/21.
 */

public class CreditRepaymentActivity extends BaseActivity implements BFUtil.OnBFPayStatusListener, PayChoosePanel.onChooseFinish {
    @Bind(R.id.credit_repay_pay_icon)
    ImageView creditRepayPayIcon;
    @Bind(R.id.credit_repay_pay_method)
    TextView creditRepayPayMethod;
    @Bind(R.id.credit_repay_pay_method_note)
    TextView creditRepayPayMethodNote;
    @Bind(R.id.credit_repay_input)
    EditText evRepay;

    private CreditRepayAdapter adapter;

    @Bind(R.id.credit_repay_should)
    TextView tvShouldRepay;

    BankCard mBankCard;
    private PayChannel payChannel = PayChannel.BALANCE;

    private String creditBillId;
    private String shouldRepay;
    private PayChoosePanel panel;
    private double balance;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.white);
        CreditBill bill = getIntent().getParcelableExtra(Constant.CREDIT_BILL);
        balance = getIntent().getDoubleExtra(Constant.BALANCE, 0);
        shouldRepay = (bill.amount * 100 - bill.paidAmount * 100) / 100 + "";
        creditBillId = bill.id;
        tvShouldRepay.setText(shouldRepay);
        evRepay.setText(shouldRepay);
        evRepay.setSelection(shouldRepay.length());

        creditRepayPayMethodNote.setText("可用余额" + balance);
        findViewById(R.id.credit_repay_choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panel.show();
            }
        });
        panel = new PayChoosePanel(this, balance, this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_credit_repayment;
    }

    public void doCreditRepay(View view) {
        if (evRepay.length() == 0) {
            ToastUtils.ToastMessage(getCtx(), "请输入还款金额");
            return;
        }
        switch (payChannel) {
            case BALANCE:
                getWalletInfo();
                break;
            case BF_BANKCARD:
                createPayment(null);
                break;
            case WEICHAT:

                break;
        }
    }

    @Override
    public void onChoose(PayChannel payChannel, BankCard bankCard, String bankName) {
        this.payChannel = payChannel;
        mBankCard = bankCard;
        setChannelView(bankName);
    }

    private void setChannelView(String bankName) {
        switch (payChannel) {
            case BALANCE:
                creditRepayPayMethod.setText(payChannel.value);
                creditRepayPayMethodNote.setVisibility(View.VISIBLE);
                creditRepayPayIcon.setImageResource(R.mipmap.pay_balance);
                break;
            case BF_BANKCARD:
                creditRepayPayMethod.setText(bankName);
                creditRepayPayMethodNote.setVisibility(View.GONE);
                creditRepayPayIcon.setImageResource(mBankCard.bankIcon);
                break;
            case WEICHAT:
                creditRepayPayMethod.setText(payChannel.value);
                creditRepayPayMethodNote.setVisibility(View.GONE);
                creditRepayPayIcon.setImageResource(R.mipmap.pay_wechat);
                break;
        }
    }

    private void getWalletInfo() {
        LoadingDialog.getInstance().show(getCtx(), "", true);
        String id = TribeApplication.getInstance().getUserInfo().getId();
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getWallet(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WalletAccount>>() {
                    @Override
                    public void onNext(BaseResponse<WalletAccount> response) {
                        String password = response.data.password;
                        float balance = (float) response.data.balance;
                        if (password == null) {
                            showAlert();
                        } else {
                            if (Float.parseFloat(shouldRepay) > (balance)) {
                                showNotEnough(balance);
                            } else {
                                showPasswordPanel(password);
                            }
                        }
                    }
                });
    }

    private void showNotEnough(final float balance) {
        new CustomAlertDialog.Builder(getCtx()).setTitle(getString(R.string.prompt)).setMessage(getString(R.string.lack_to_recharge))
                .setPositiveButton(getString(R.string.to_recharge), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getCtx(), RechargeActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton(getResources().getString(R.string.cancel), null).create().show();
    }

    private void showAlert() {
        new CustomAlertDialog.Builder(getCtx()).setTitle(getString(R.string.prompt)).setMessage(getString(R.string.not_set_pwd))
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getCtx().startActivity(new Intent(getCtx(), UpdateWalletPwdActivity.class));
                    }
                }).setNegativeButton(getResources().getString(R.string.cancel), null).create().show();
    }

    private void showPasswordPanel(final String password) {
        NewPasswordPanel passwordPanel = new NewPasswordPanel(this, R.style.sheet_dialog, password, new NewPasswordPanel.OnPwdFinishListener() {
            @Override
            public void onPwdFinishListener(String strPassword) {
                createPayment(strPassword);
            }

            @Override
            public void onPwdPanelDismiss() {

            }
        });
        passwordPanel.show();
    }

    private void createPayment(final String password) {
        LoadingDialog.getInstance().show(getCtx(), "", true);
        Pay2MerchantRequest request = new Pay2MerchantRequest();
        request.payChannel = payChannel.name();
        if (password != null) request.password = password;
        request.targetId = creditBillId;
        request.totalFee = evRepay.getText().toString().trim();
        TribeRetrofit.getInstance().createApi(MoneyApis.class).doPay(TribeApplication.getInstance().getUserInfo().getId(), "credit", request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<OrderPayment>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.ToastMessage(getCtx(),R.string.connect_fail);
                    }

                    @Override
                    public void onNext(BaseResponse<OrderPayment> orderPaymentBaseResponse) {
                        setStatus(password, orderPaymentBaseResponse.data);
                    }
                });
    }

    public void setStatus(String password, final OrderPayment data) {
        switch (payChannel) {
            case BALANCE:
                if (data.status == OrderPayment.PayStatus.FINISHED || data.status == OrderPayment.PayStatus.PAYED) {
                    ToastUtils.ToastMessage(getCtx(), getString(R.string.repay_success));
                    ToastUtils.ToastMessage(getCtx(), R.string.pay_success);
                    getCtx().startActivity(new Intent(getCtx(), WalletActivity.class));
                    finish();
                } else {
                    ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
                }
                break;
            case WEICHAT:
                WxPayRequest request = new WxPayRequest();
                WXUtils.getInstance().payInWechat(request);
                break;
            case BF_BANKCARD:
                new BFUtil().doBFPay(getCtx(), data, mBankCard, this);
                break;
        }
    }

    @Override
    public void onBFSuccess() {
        ToastUtils.ToastMessage(getCtx(), getString(R.string.repay_success));
        Intent intent = new Intent();
        intent.setClass(getCtx(), WalletActivity.class);
        startActivity(intent);
        finish();
    }
}
