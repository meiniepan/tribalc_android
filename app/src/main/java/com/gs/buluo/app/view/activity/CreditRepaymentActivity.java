package com.gs.buluo.app.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
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
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.BFUtil;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.CustomAlertDialog;
import com.gs.buluo.app.view.widget.panel.NewPasswordPanel;
import com.gs.buluo.app.view.widget.panel.RechargePanel;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/7/21.
 */

public class CreditRepaymentActivity extends BaseActivity implements BFUtil.OnBFPayStatusListener {
    @Bind(R.id.credit_repay_card_list)
    ListView listView;
    private CreditRepayAdapter adapter;

    @Bind(R.id.credit_pay_balance)
    RadioButton radioButton;
    @Bind(R.id.ll_add__bank_card)
    View addView;
    @Bind(R.id.credit_repay_should)
    TextView tvShouldRepay;

    @Bind(R.id.credit_repay_input)
    EditText evRepay;
    BankCard mBankCard;

    private PayChannel payChannel = PayChannel.BALANCE;

    ArrayList<BankCard> list = new ArrayList<>();
    private String creditBillId;
    private String shouldRepay;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        CreditBill bill = getIntent().getParcelableExtra(Constant.CREDIT_BILL);
        shouldRepay = (bill.amount * 100 - bill.paidAmount * 100) / 100 + "";
        creditBillId = bill.id;
        tvShouldRepay.setText(shouldRepay);
        adapter = new CreditRepayAdapter(getCtx(), list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                radioButton.setChecked(false);
                mBankCard = list.get(position);
                payChannel = PayChannel.BF_BANKCARD;
                adapter.setPos(position);
            }
        });

        findViewById(R.id.ll_balance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payChannel = PayChannel.BALANCE;
                radioButton.setChecked(true);
                adapter.setPos(-1);
            }
        });
        addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getCtx(), AddBankCardActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        TribeRetrofit.getInstance().createApi(MoneyApis.class).getCardList(TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<List<BankCard>>>() {
                    @Override
                    public void onNext(BaseResponse<List<BankCard>> listBaseResponse) {
                        list.clear();
                        list.addAll(listBaseResponse.data);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_credit_repayment;
    }

    public void repayMoney(View view) {
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
                        RechargePanel panel = new RechargePanel(getCtx(), TribeApplication.getInstance().getUserInfo().getId());
                        panel.setData(balance);
                        panel.show();
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
        request.totalFee = evRepay.getText().toString().trim();
        TribeRetrofit.getInstance().createApi(MoneyApis.class).doPay(TribeApplication.getInstance().getUserInfo().getId(), "credit", request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderPayment>>() {
                    @Override
                    public void onFail(ApiException e) {
                        ToastUtils.ToastMessage(getCtx(), e.getDisplayMessage() == null ? getString(R.string.connect_fail) : e.getDisplayMessage());
                    }

                    @Override
                    public void onNext(BaseResponse<OrderPayment> orderPaymentBaseResponse) {
                        setStatus(password, orderPaymentBaseResponse.data);
                    }
                });
    }

    public void setStatus(String password, final OrderPayment data) {
        if (password == null) {
            new BFUtil().doBFPay(getCtx(), data, mBankCard, this);
        } else {
            if (data.status == OrderPayment.PayStatus.FINISHED || data.status == OrderPayment.PayStatus.PAYED) {
                ToastUtils.ToastMessage(getCtx(), getString(R.string.repay_success));
                ToastUtils.ToastMessage(getCtx(), R.string.pay_success);
                getCtx().startActivity(new Intent(getCtx(), WalletActivity.class));
                finish();
            }
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
