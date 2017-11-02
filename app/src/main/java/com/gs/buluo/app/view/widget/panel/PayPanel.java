package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.baofoo.sdk.device.BaofooDeviceFingerPrint;
import com.baofoo.sdk.device.constonts.Constents;
import com.baofoo.sdk.device.environment.Environment;
import com.baofoo.sdk.device.interfaces.ResultInterfaces;
import com.gs.buluo.app.BuildConfig;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.OrderPayment;
import com.gs.buluo.app.bean.PayChannel;
import com.gs.buluo.app.bean.PrepareOrderRequest;
import com.gs.buluo.app.bean.RequestBodyBean.NewPaymentRequest;
import com.gs.buluo.app.bean.RequestBodyBean.PaySessionResponse;
import com.gs.buluo.app.bean.RequestBodyBean.ValueBody;
import com.gs.buluo.app.bean.ResultResponse;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.WXUtils;
import com.gs.buluo.app.view.activity.RechargeActivity;
import com.gs.buluo.app.view.activity.UpdateWalletPwdActivity;
import com.gs.buluo.app.view.widget.CustomAlertDialog;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.LoadingDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/12/7.
 */
public class PayPanel extends Dialog implements PasswordPanel.OnPasswordPanelDismissListener, View.OnClickListener, PayChoosePanel.onChooseFinish {
    private final OnPayPanelDismissListener onDismissListener;
    private Context mContext;
    @BindView(R.id.pay_way)
    TextView tvWay;
    @BindView(R.id.pay_money)
    TextView tvTotal;

    private PayChannel payWay = PayChannel.BALANCE;
    private List<String> orderId;
    private View rootView;
    private String price;
    private String type;
    private BankCard mBankCard;
    private BaofooDeviceFingerPrint baofooDeviceFingerPrint;
    private WalletAccount walletAccount;
    private PayChoosePanel payChoosePanel;

    public PayPanel(Context context, OnPayPanelDismissListener onDismissListener) {
        super(context, R.style.my_dialog);
        mContext = context;
        this.onDismissListener = onDismissListener;
        initView();
        getWalletInfo();
    }

    public void setData(String price, List<String> orderId, String type) {
        tvWay.setText(payWay.value);
        this.price = price;
        tvTotal.setText(price);
        this.orderId = orderId;
        this.type = type;
    }

    private void initView() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.pay_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = DensityUtils.dip2px(mContext, 400);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

        rootView.findViewById(R.id.pay_ask).setOnClickListener(this);
        rootView.findViewById(R.id.pay_close).setOnClickListener(this);
        rootView.findViewById(R.id.pay_finish).setOnClickListener(this);
        rootView.findViewById(R.id.pay_choose_area).setOnClickListener(this);
    }


    public void getWalletInfo() {
        LoadingDialog.getInstance().show(getContext(), "", true);
        String id = TribeApplication.getInstance().getUserInfo().getId();
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getWallet(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WalletAccount>>() {
                    @Override
                    public void onNext(BaseResponse<WalletAccount> response) {
                        setWalletData(response);
                    }
                });
    }

    private void setWalletData(BaseResponse<WalletAccount> response) {
        walletAccount = response.data;
        payChoosePanel = new PayChoosePanel(mContext, getAvailableBalance(walletAccount), this);
    }

    private double getAvailableBalance(WalletAccount data) {
        if (data.creditStatus == WalletAccount.CreditStatus.NORMAL) {
            return (data.balance + data.creditLimit - data.creditBalance);
        } else {
            return data.balance;
        }
    }

    private void showNotEnough(final float balance) {
        new CustomAlertDialog.Builder(getContext()).setTitle("提示").setMessage("您账户余额不足，请先去充值")
                .setPositiveButton("去充值", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getContext().startActivity(new Intent(getContext(), RechargeActivity.class));
                    }
                }).setNegativeButton("取消", null).create().show();
    }

    private void showAlert() {
        new CustomAlertDialog.Builder(getContext()).setTitle("提示").setMessage("您还没有设置支付密码，请先去设置密码")
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getContext().startActivity(new Intent(getContext(), UpdateWalletPwdActivity.class));
                    }
                }).setNegativeButton("取消", null).create().show();
    }

    private void showPasswordPanel(String password) {
        PasswordPanel passwordPanel = new PasswordPanel(mContext, password, orderId, payWay, type, this);
        passwordPanel.show();
        TranslateAnimation animation = new TranslateAnimation(0, -CommonUtils.getScreenWidth(mContext), 0, 0);
        animation.setDuration(500);
        animation.setFillAfter(true);
        animation.start();
        rootView.startAnimation(animation);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_close:
                dismiss();
                break;
            case R.id.pay_finish:
                if (walletAccount == null) {
                    ToastUtils.ToastMessage(getContext(), R.string.connect_fail);
                    return;
                }
                if (payWay == PayChannel.BALANCE) {
                    String password = walletAccount.password;
                    float balance = (float) walletAccount.balance;
                    if (password == null) {
                        showAlert();
                    } else {
                        if (Float.parseFloat(price) > getAvailableBalance(walletAccount)) {
                            showNotEnough(balance);
                        } else {
                            showPasswordPanel(password);
                        }
                    }
                } else if (payWay == PayChannel.WECHAT) {
                    payInWx();
                } else if (payWay == PayChannel.BF_BANKCARD) {
                    applyBankCardPay();
                } else {
                    dismiss();
                }
                break;
            case R.id.pay_choose_area:
                if (walletAccount == null) {
                    ToastUtils.ToastMessage(getContext(), R.string.connect_fail);
                    return;
                }
                payChoosePanel.show();
                break;
        }
    }

    private void payInWx() {
        LoadingDialog.getInstance().show(mContext, "", true);
        NewPaymentRequest request = new NewPaymentRequest();
        request.orderIds = orderId;
        request.payChannel = payWay.name();
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                createPayment(TribeApplication.getInstance().getUserInfo().getId(), type, request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<OrderPayment>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.ToastMessage(getContext(), R.string.connect_fail);
                        LoadingDialog.getInstance().dismissDialog();
                    }

                    @Override
                    public void onNext(BaseResponse<OrderPayment> response) {
                        WXUtils.getInstance().payInWechat(response.data);
                    }
                });

    }

    private void applyBankCardPay() {
        LoadingDialog.getInstance().show(mContext, "", true);
        NewPaymentRequest request = new NewPaymentRequest();
        request.orderIds = orderId;
        request.payChannel = payWay.name();
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                createPayment(TribeApplication.getInstance().getUserInfo().getId(), type, request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<OrderPayment>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.ToastMessage(getContext(), R.string.connect_fail);
                        LoadingDialog.getInstance().dismissDialog();
                    }

                    @Override
                    public void onNext(BaseResponse<OrderPayment> response) {
                        doBFPrepare(response.data);
                    }
                });
    }

    private void doBFPrepare(final OrderPayment data) {
        TribeRetrofit.getInstance().createApi(MoneyApis.class).generateSessionId(new ValueBody(data.id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<PaySessionResponse>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.ToastMessage(getContext(), R.string.connect_fail);
                        LoadingDialog.getInstance().dismissDialog();
                    }

                    @Override
                    public void onNext(BaseResponse<PaySessionResponse> response) {
                        doNextPrepare(data, response.data.result);
                    }
                });
    }

    private void doNextPrepare(final OrderPayment data, final PaySessionResponse.PaySessionResult result) {
        if (BuildConfig.API_SERVER_URL.contains("dev")) {
            baofooDeviceFingerPrint = new BaofooDeviceFingerPrint(getContext(), result.sessionId, Environment.PRODUCT_DEVICE_SERVER, Constents.SyncBlockTimeOut);
        } else {
            baofooDeviceFingerPrint = new BaofooDeviceFingerPrint(getContext(), result.sessionId, Environment.PRODUCT_DEVICE_SERVER,Constents.SyncBlockTimeOut);
        }
        baofooDeviceFingerPrint.execute();
        baofooDeviceFingerPrint.onRespResult(new ResultInterfaces() {
            @Override
            public void respSuccess(String s) {
                doPrepare(data);
                baofooDeviceFingerPrint.destroyHandler();//释放资源；
            }

            @Override
            public void respError(String s) {
                Log.e("baofoo", "respError: " + s);
                ToastUtils.ToastMessage(getContext(), R.string.connect_fail);
                LoadingDialog.getInstance().dismissDialog();
                baofooDeviceFingerPrint.destroyHandler();//释放资源；
            }
        });
    }

    private void doPrepare(final OrderPayment data) {
        PrepareOrderRequest prepareOrderRequest = new PrepareOrderRequest();
        prepareOrderRequest.bankCardId = mBankCard.id;
        prepareOrderRequest.totalFee = data.totalAmount;
        prepareOrderRequest.paymentId = data.id;
        LoadingDialog.getInstance().show(mContext, "", true);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                prepareOrder(prepareOrderRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ResultResponse>>() {
                    @Override
                    public void onNext(BaseResponse<ResultResponse> response) {
                        new BfPayVerifyCodePanel(mContext, mBankCard, response.data.result, data, PayPanel.this, null, null, 0, null, null, null).show();
                    }

                    @Override
                    public void onFail(ApiException e) {
                        ToastUtils.ToastMessage(getContext(), R.string.connect_fail);
                    }
                });
    }

    @Override
    public void onPasswordPanelDismiss(boolean successful) {
        dismiss();
    }

    @Override
    public void onChoose(PayChannel payChannel, BankCard bankCard, String bankName) {
        payWay = payChannel;
        if (bankCard != null) {
            tvWay.setText(bankName);
            mBankCard = bankCard;
        } else {
            tvWay.setText(payWay.value);
        }
    }

    public interface OnPayPanelDismissListener {
        void onPayPanelDismiss();
    }

    @Override
    public void dismiss() {
        if (onDismissListener != null) {
            onDismissListener.onPayPanelDismiss();
        }
        super.dismiss();
    }
}