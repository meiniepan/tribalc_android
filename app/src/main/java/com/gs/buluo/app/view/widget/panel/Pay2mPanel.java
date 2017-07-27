package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import com.baofoo.sdk.device.environment.Environment;
import com.baofoo.sdk.device.interfaces.ResultInterfaces;
import com.gs.buluo.app.BuildConfig;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.BankOrderResponse;
import com.gs.buluo.app.bean.OrderPayment;
import com.gs.buluo.app.bean.Pay2MerchantRequest;
import com.gs.buluo.app.bean.PayChannel;
import com.gs.buluo.app.bean.PrepareOrderRequest;
import com.gs.buluo.app.bean.RequestBodyBean.PaySessionResponse;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.activity.UpdateWalletPwdActivity;
import com.gs.buluo.app.view.widget.CustomAlertDialog;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.LoadingDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/6/8.
 */
public class Pay2mPanel extends Dialog implements Pay2mPasswordPanel.OnPasswordPanelDismissListener, View.OnClickListener {
    private final OnPayPanelDismissListener onDismissListener;
    private Context mContext;
    @Bind(R.id.pay_way)
    TextView tvWay;
    @Bind(R.id.pay_money)
    TextView tvTotal;

    private PayChannel payWay = PayChannel.BALANCE;
    private String payWayString =PayChannel.BALANCE.toString();
    private String targetId;
    private View rootView;
    private String totalFee;
    private BankCard mBankCard;
    private BaofooDeviceFingerPrint baofooDeviceFingerPrint;
    private String apartmentCode;
    private String apartmentName;
    private String name;
    private int from;//1为向商家付款，2为付房租
    private AlertDialog dialog = null;

    public Pay2mPanel(Context context, OnPayPanelDismissListener onDismissListener) {
        super(context, R.style.my_dialog);
        mContext = context;
        this.onDismissListener = onDismissListener;
        initView();
    }

    public void setData(int from,String totalFee, String targetId, String name,String apartmentCode,String apartmentName) {
        tvWay.setText(payWay.toString());
        this.totalFee = totalFee;
        tvTotal.setText(totalFee);
        this.targetId = targetId;
        this.name = name;
        this.from = from;
        this.apartmentCode = apartmentCode;
        this.apartmentName = apartmentName;
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
                        String password = response.data.password;
                        float balance =(float) response.data.balance;
                        if (password == null) {
                            showAlert();
                        } else {
                            if (Float.parseFloat(totalFee) > balance) {
                                showNotEnough(balance);
                            } else {
                                showPasswordPanel(password);
                            }
                        }
                    }
                });
    }

    private void showNotEnough(final float balance) {
        new CustomAlertDialog.Builder(getContext()).setTitle("提示").setMessage("您账户余额不足，请先去充值")
                .setPositiveButton("去充值", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RechargePanel panel = new RechargePanel(mContext);
                        panel.setData(balance);
                        panel.show();
                    }
                }).setNegativeButton("取消", null).create().show();
    }

    private void showAlert() {
        new CustomAlertDialog.Builder(getContext()).setTitle("提示").setMessage("您还没有设置支付密码，请先去设置密码")
                .setPositiveButton("去设置", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getContext().startActivity(new Intent(getContext(), UpdateWalletPwdActivity.class));
                    }
                }).setNegativeButton("取消", null).create().show();
    }

    private void showPasswordPanel(String password) {
        Pay2mPasswordPanel passwordPanel = new Pay2mPasswordPanel(from,mContext, password, targetId, totalFee, payWay, name, apartmentCode,apartmentName,this);
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
                if (payWayString.equals(PayChannel.BALANCE.toString()))
                    getWalletInfo();
                else if (!payWayString.equals("")) {
                    Pay2MerchantRequest request = new Pay2MerchantRequest();
                    request.targetId = targetId;
                    request.payChannel = PayChannel.BF_BANKCARD.name();
                    request.totalFee = totalFee;
                    LoadingDialog.getInstance().show(mContext, "", true);
                    if (from == 1)
                    m2mPay(request);
                    if (from == 2)
                        rentPay(request);
                }
                break;
            case R.id.pay_choose_area:
                PayChoosePanel payChoosePanel = new PayChoosePanel(mContext, new PayChoosePanel.onChooseFinish() {
                    @Override
                    public void onChoose(String payChannel, BankCard bankCard) {
                        payWayString = payChannel;
                        tvWay.setText(payWayString);
                        if (payChannel.contains("储蓄卡")) {
                            payWay = PayChannel.BF_BANKCARD;
                        }
                        mBankCard = bankCard;
                    }
                });
                payChoosePanel.show();
                LoadingDialog.getInstance().show(mContext,"",true);
                break;
        }
    }

    private void rentPay(Pay2MerchantRequest request) {
        TribeRetrofit.getInstance().createApi(MoneyApis.class).payRent(TribeApplication.getInstance().getUserInfo().getId(), request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderPayment>>() {
                    @Override
                    public void onNext(BaseResponse<OrderPayment> response) {
                        doBFPrepare(response.data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoadingDialog.getInstance().dismissDialog();
                        showDialog();
                    }
                });
    }

    private void m2mPay(Pay2MerchantRequest request) {
        TribeRetrofit.getInstance().createApi(MoneyApis.class).pay2Merchant(TribeApplication.getInstance().getUserInfo().getId(), request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderPayment>>() {
                    @Override
                    public void onNext(BaseResponse<OrderPayment> response) {
                        doBFPrepare(response.data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoadingDialog.getInstance().dismissDialog();
                        showDialog();
                    }
                });
    }

    private void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.myCorDialog);
        View view = View
                .inflate(mContext, R.layout.pay2merchant_error, null);
        builder.setView(view);
        builder.setCancelable(true);
        view.findViewById(R.id.btn_pay2m_error_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = com.gs.buluo.common.utils.DensityUtils.dip2px(mContext, 229);
        params.height = com.gs.buluo.common.utils.DensityUtils.dip2px(mContext, 213);
        dialog.getWindow().setAttributes(params);
    }

    private void doBFPrepare(final OrderPayment data) {
        LoadingDialog.getInstance().show(mContext, "", true);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).getPrepareOrderInfo( new ValueRequestBody(data.id))
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
        LoadingDialog.getInstance().show(mContext, "", true);
        if (BuildConfig.API_SERVER_URL.contains("dev")) {
            baofooDeviceFingerPrint = new BaofooDeviceFingerPrint(getContext(), result.sessionId, Environment.PRODUCT_DEVICE_SERVER);
        } else {
            baofooDeviceFingerPrint = new BaofooDeviceFingerPrint(getContext(), result.sessionId, Environment.PRODUCT_DEVICE_SERVER);
        }
        baofooDeviceFingerPrint.execute();
        baofooDeviceFingerPrint.onRespResult(new ResultInterfaces() {
            @Override
            public void respSuccess(String s) {
                doPrepare(data);
                baofooDeviceFingerPrint.releaseResource();//释放资源；
            }

            @Override
            public void respError(String s) {
                Log.e("baofoo", "respError: " + s);
                ToastUtils.ToastMessage(getContext(), R.string.connect_fail);
                LoadingDialog.getInstance().dismissDialog();
                baofooDeviceFingerPrint.releaseResource();//释放资源；
            }
        });
    }

    private void doPrepare(final OrderPayment data) {
        LoadingDialog.getInstance().show(mContext, "", true);
        PrepareOrderRequest prepareOrderRequest = new PrepareOrderRequest();
        prepareOrderRequest.bankCardId = mBankCard.id;
        prepareOrderRequest.totalFee = data.totalAmount;
        prepareOrderRequest.paymentId = data.id;
        LoadingDialog.getInstance().show(mContext, "", true);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                prepareOrder( prepareOrderRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<BankOrderResponse>>() {
                    @Override
                    public void onNext(BaseResponse<BankOrderResponse> response) {
                        if (from == 1)
                        new BfPayVerifyCodePanel(mContext, mBankCard, response.data.result, data, Pay2mPanel.this, name, totalFee, 1,null,null,null).show();
                        if (from == 2)
                            new BfPayVerifyCodePanel(mContext, mBankCard, response.data.result, data, Pay2mPanel.this, name, totalFee, 1,targetId,apartmentCode,apartmentName).show();
                    }
                });
    }

    @Override
    public void onPasswordPanelDismiss(boolean successful) {
        dismiss();
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