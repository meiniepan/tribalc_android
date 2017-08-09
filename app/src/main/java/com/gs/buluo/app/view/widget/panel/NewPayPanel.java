package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.OrderPayment;
import com.gs.buluo.app.bean.Pay2MerchantRequest;
import com.gs.buluo.app.bean.PayChannel;
import com.gs.buluo.app.bean.WalletAccount;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.BFUtil;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.activity.UpdateWalletPwdActivity;
import com.gs.buluo.app.view.widget.CustomAlertDialog;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.LoadingDialog;

import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/12/7.
 */
public class NewPayPanel extends Dialog implements View.OnClickListener, BFUtil.OnBFPayStatusListener {
    private final OnPayFinishListener onFinishListener;
    private Context mContext;
    @Bind(R.id.pay_way)
    TextView tvWay;
    @Bind(R.id.pay_money)
    TextView tvTotal;
    @Bind(R.id.pay_choose_area)
    View chooseArea;
    @Bind(R.id.pay_choose)
    View arrow;

    private PayChannel payWay = PayChannel.BALANCE;
    private String payWayString = PayChannel.BALANCE.toString();
    private View rootView;
    private String totalFee;
    private String targetId;
    private BankCard mBankCard;
    private String paymentType;
    private WalletAccount data;

    public NewPayPanel(Context context, OnPayFinishListener onDismissListener) {
        super(context, R.style.my_dialog);
        mContext = context;
        this.onFinishListener = onDismissListener;
        initView();
    }

    public void setData(String price, String targetId, String type) {
        tvWay.setText(payWay.value);
        this.totalFee = price;
        tvTotal.setText(price);
        this.targetId = targetId;
        paymentType = type;
        initCompanyPayRent();
    }

    private void initCompanyPayRent() {
        if (paymentType.equals("rent")){
            chooseArea.setEnabled(false);
            arrow.setVisibility(View.INVISIBLE);
        }
        getWalletInfo();
    }

    private String oldMoney;

    //商家买单打折前的数额
    public void setPayBeforeDiscount(String money) {
        oldMoney = money;
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
        chooseArea.setOnClickListener(this);
    }


    private void getWalletInfo() {
        String id = TribeApplication.getInstance().getUserInfo().getId();
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getWallet(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<WalletAccount>>() {
                    @Override
                    public void onNext(BaseResponse<WalletAccount> response) {
                        data = response.data;
                    }
                });
    }

    private void showNotEnough(final float balance) {
        new CustomAlertDialog.Builder(mContext).setTitle(R.string.prompt).setMessage(mContext.getString(R.string.lack_to_recharge))
                .setPositiveButton(mContext.getString(R.string.to_recharge), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RechargePanel panel = new RechargePanel(mContext, TribeApplication.getInstance().getUserInfo().getId());
                        panel.setData(balance);
                        panel.show();
                        dismiss();
                    }
                }).setNegativeButton(mContext.getResources().getString(R.string.cancel), null).create().show();
    }

    private void showAlert() {
        new CustomAlertDialog.Builder(getContext()).setTitle(R.string.prompt).setMessage(R.string.not_set_pwd)
                .setPositiveButton("去设置", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getContext().startActivity(new Intent(getContext(), UpdateWalletPwdActivity.class));
                        dismiss();
                    }
                }).setNegativeButton(mContext.getString(R.string.cancel), null).create().show();
    }

    private void showPasswordPanel(final String password) {
        NewPasswordPanel passwordPanel = new NewPasswordPanel(mContext, password, new NewPasswordPanel.OnPwdFinishListener() {
            @Override
            public void onPwdFinishListener(String strPassword) {
                createPayment(strPassword);
            }

            @Override
            public void onPwdPanelDismiss() {
                dismiss();
            }
        });
        passwordPanel.show();
        TranslateAnimation animation = new TranslateAnimation(0, -CommonUtils.getScreenWidth(mContext), 0, 0);
        animation.setDuration(500);
        animation.setFillAfter(true);
        animation.start();
        rootView.startAnimation(animation);
    }

    private void createPayment(final String password) {
        LoadingDialog.getInstance().show(mContext, "", true);
        Pay2MerchantRequest request = new Pay2MerchantRequest();
        request.payChannel = payWay.name();
        if (password != null) request.password = password;
        request.targetId = targetId;
        request.totalFee = oldMoney == null ? totalFee : oldMoney;
        TribeRetrofit.getInstance().createApi(MoneyApis.class).doPay(TribeApplication.getInstance().getUserInfo().getId(), paymentType, request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<OrderPayment>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoadingDialog.getInstance().dismissDialog();
                        dismiss();
                        if (e instanceof ApiException) {
                            onFinishListener.onPayFail((ApiException) e);
                        }
                    }

                    @Override
                    public void onNext(BaseResponse<OrderPayment> orderPaymentBaseResponse) {
                        setStatus(password, orderPaymentBaseResponse.data);
                    }
                });
    }

    private void setStatus(String password, final OrderPayment data) {
        if (password == null) {
            new BFUtil().doBFPay(mContext, data, mBankCard, this);
        } else {
            if (data.status == OrderPayment.PayStatus.FINISHED || data.status == OrderPayment.PayStatus.PAYED) {
                ToastUtils.ToastMessage(getContext(), R.string.pay_success);
                onFinishListener.onPaySuccess();
                dismiss();
            } else {
                rootView.postDelayed(new TimerTask() {
                    @Override
                    public void run() {
                        getPaymentInfo(data);
                    }
                }, 1000);
            }
        }
    }

    public void getPaymentInfo(OrderPayment data) {
        LoadingDialog.getInstance().show(mContext, "", true);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).getPaymentStatus(TribeApplication.getInstance().getUserInfo().getId(), data.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderPayment>>() {
                    @Override
                    public void onNext(BaseResponse<OrderPayment> orderPaymentBaseResponse) {
                        setStatusAgain(orderPaymentBaseResponse.data);
                    }

                    @Override
                    public void onFail(ApiException e) {
                    }
                });
    }

    public void setStatusAgain(final OrderPayment data) {
        if (data.status == OrderPayment.PayStatus.FINISHED || data.status == OrderPayment.PayStatus.PAYED) {
            ToastUtils.ToastMessage(getContext(), R.string.pay_success);
            onFinishListener.onPaySuccess();
        } else {
            ToastUtils.ToastMessage(getContext(), data.note);
        }
        dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_close:
                dismiss();
                break;
            case R.id.pay_finish:
                switch (payWay) {
                    case BALANCE:
                        if (data == null) {
                            ToastUtils.ToastMessage(getContext(), R.string.connect_fail);
                            return;
                        }
                        String password = data.password;
                        float balance = (float) data.balance;
                        if (password == null) {
                            showAlert();
                        } else {
                            if (Float.parseFloat(totalFee) > getAvailableBalance(data)) {
                                showNotEnough(balance);
                            } else {
                                showPasswordPanel(password);
                            }
                        }
                        break;
                    case BF_BANKCARD:
                        createPayment(null);
                        break;
                    default:
                        break;
                }
                break;
            case R.id.pay_choose_area:
                PayChoosePanel payChoosePanel = new PayChoosePanel(mContext, new PayChoosePanel.onChooseFinish() {
                    @Override
                    public void onChoose(String payChannel, BankCard bankCard) {
                        payWayString = payChannel;
                        if (payChannel.contains("储蓄卡")) {
                            payWay = PayChannel.BF_BANKCARD;
                            tvWay.setText(payWayString);
                        } else if (payChannel.contains("BALANCE")) {
                            payWay = PayChannel.BALANCE;
                            tvWay.setText(payWay.value);
                        }
                        mBankCard = bankCard;
                    }
                });
                payChoosePanel.show();
                break;
        }
    }

    @Override
    public void onBFSuccess() {
        dismiss();
        onFinishListener.onPaySuccess();
    }

    private double getAvailableBalance(WalletAccount data) {
        if (data.creditStatus == WalletAccount.CreditStatus.NORMAL) {
            return (data.balance + data.creditLimit - data.creditBalance);
        } else {
            return data.balance;
        }
    }

    public interface OnPayFinishListener {
        void onPaySuccess();

        void onPayFail(ApiException e);
    }
}