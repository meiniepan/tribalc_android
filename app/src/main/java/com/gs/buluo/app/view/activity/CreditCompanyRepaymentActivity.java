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
import com.gs.buluo.app.bean.CreditBill;
import com.gs.buluo.app.bean.OrderPayment;
import com.gs.buluo.app.bean.Pay2MerchantRequest;
import com.gs.buluo.app.bean.PayChannel;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.app.view.widget.CustomAlertDialog;
import com.gs.buluo.app.view.widget.panel.NewPasswordPanel;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.LoadingDialog;

import java.util.ArrayList;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Soalng on 2017/7/28.
 */

public class CreditCompanyRepaymentActivity extends BaseActivity {

    @BindView(R.id.credit_repay_should)
    TextView tvShouldRepay;

    @BindView(R.id.credit_repay_input)
    EditText evRepay;
    @BindView(R.id.company_balance)
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
        CreditBill bill = getIntent().getParcelableExtra(Constant.CREDIT_BILL);
        shouldRepay = (bill.amount * 100 - bill.paidAmount * 100) / 100 + "";
        creditBillId = bill.id;
        tvShouldRepay.setText(shouldRepay);
        evRepay.setText(shouldRepay);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoadingDialog();
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getWallet(targetId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WalletAccount>>() {
                    @Override
                    public void onNext(BaseResponse<WalletAccount> response) {
                        tvBalance.setText(response.data.balance + "");
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
        Intent intent = new Intent(this, RechargeActivity.class);
        intent.putExtra(Constant.TARGET_ID, TribeApplication.getInstance().getUserInfo().getCompanyID());   //  企业or 个人
        startActivity(intent);
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
                            if (Float.parseFloat(evRepay.getText().toString().trim()) > (balance)) {
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
                        intent.putExtra(Constant.TARGET_ID, TribeApplication.getInstance().getUserInfo().getCompanyID());   //  企业or 个人
                        getCtx().startActivity(intent);
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
        NewPasswordPanel passwordPanel = new NewPasswordPanel(this, R.style.pay_dialog, password, new NewPasswordPanel.OnPwdFinishListener() {
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
        TribeRetrofit.getInstance().createApi(MoneyApis.class).doPay(targetId, "credit", request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderPayment>>() {
                    @Override
                    public void onNext(BaseResponse<OrderPayment> orderPaymentBaseResponse) {
                        ToastUtils.ToastMessage(getCtx(), "还款成功");
                        startActivity(new Intent(getCtx(), CompanyManagerActivity.class));
                    }
                });
    }
}
