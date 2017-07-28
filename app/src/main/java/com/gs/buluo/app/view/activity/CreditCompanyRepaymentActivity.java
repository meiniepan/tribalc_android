package com.gs.buluo.app.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.OrderPayment;
import com.gs.buluo.app.bean.Pay2MerchantRequest;
import com.gs.buluo.app.bean.PayChannel;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.CustomAlertDialog;
import com.gs.buluo.app.view.widget.panel.NewPasswordPanel;
import com.gs.buluo.app.view.widget.panel.RechargePanel;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.LoadingDialog;

import java.util.ArrayList;

import butterknife.Bind;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Soalng on 2017/7/28.
 */

public class CreditCompanyRepaymentActivity extends BaseActivity {

    @Bind(R.id.credit_repay_should)
    TextView tvShouldRepay;

    @Bind(R.id.credit_repay_input)
    EditText evRepay;
    @Bind(R.id.company_balance)
    TextView tvBalance;

    private PayChannel payChannel = PayChannel.BALANCE;

    ArrayList<BankCard> list = new ArrayList<>();
    private String creditBillId;
    private String shouldRepay;
    private String targetId;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_credit_company_repayment;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        targetId = TribeApplication.getInstance().getUserInfo().getCompanyID();
        shouldRepay = getIntent().getStringExtra(Constant.CREDIT_BALANCE);
        creditBillId = getIntent().getStringExtra(Constant.CREDIT_BILL_ID);

        tvShouldRepay.setText(shouldRepay);

    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadingDialog.getInstance().show(getCtx(), "", true);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getWallet(targetId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WalletAccount>>() {
                    @Override
                    public void onNext(BaseResponse<WalletAccount> response) {
                        tvBalance.setText(response.data.balance+"");

                    }
                });
    }

    public void repayMoney(View view) {
        if (evRepay.length() == 0) {
            ToastUtils.ToastMessage(getCtx(), "请输入还款金额");
            return;
        }
                getWalletInfo();
    }
    public void recharge(View view) {
        RechargePanel panel = new RechargePanel(getCtx(), TribeApplication.getInstance().getUserInfo().getCompanyID());
        panel.show();
    }

    private void getWalletInfo() {
        LoadingDialog.getInstance().show(getCtx(), "", true);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getWallet(targetId)
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
                        RechargePanel panel = new RechargePanel(getCtx(), targetId);
                        panel.setData(balance);
                        panel.show();
                    }
                }).setNegativeButton(getResources().getString(R.string.cancel), null).create().show();
    }

    private void showAlert() {
        new CustomAlertDialog.Builder(getCtx()).setTitle("提示").setMessage("您还没有设置支付密码，请先去设置密码")
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getCtx().startActivity(new Intent(getCtx(), UpdateWalletPwdActivity.class));
                    }
                }).setNegativeButton("取消", null).create().show();
    }

    private void showPasswordPanel(final String password) {
        NewPasswordPanel passwordPanel = new NewPasswordPanel(this, password, new NewPasswordPanel.OnPwdFinishListener() {
            @Override
            public void onPwdFinishListener(String strPassword) {
                createPayment(strPassword);
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
        request.totalFee = shouldRepay;
        TribeRetrofit.getInstance().createApi(MoneyApis.class).pay2Merchant(targetId, request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<OrderPayment>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoadingDialog.getInstance().dismissDialog();
                        if (e instanceof ApiException && ((ApiException) e).getDisplayMessage() != null) {
                            ToastUtils.ToastMessage(getCtx(), ((ApiException) e).getDisplayMessage());
                        } else {
                            ToastUtils.ToastMessage(getCtx(), R.string.connect_fail);
                        }
                    }

                    @Override
                    public void onNext(BaseResponse<OrderPayment> orderPaymentBaseResponse) {
                        ToastUtils.ToastMessage(getCtx(), "还款成功");
                        finish();
                    }
                });
    }
}
