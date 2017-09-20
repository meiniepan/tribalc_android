package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.OrderPayment;
import com.gs.buluo.app.bean.PayChannel;
import com.gs.buluo.app.bean.RequestBodyBean.NewPaymentRequest;
import com.gs.buluo.app.eventbus.PaymentEvent;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.LoadingDialog;
import com.gs.buluo.common.widget.PwdEditText;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.MD5;

import java.util.List;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/12/13.
 */
public class PasswordPanel extends Dialog {
    private OnPasswordPanelDismissListener onPasswordPanelDismissListener;
    private List<String> orderId;
    private Context mContext;
    @Bind(R.id.pwd_board_pet)
    PwdEditText pwdEditText;
    private final String myPwd;
    private PayChannel payChannel;
    private String type;

    private int payType;
    private boolean doSuccess;

    public PasswordPanel(Context context, String pwd, List<String> orderId, PayChannel channel, String type, OnPasswordPanelDismissListener onPasswordPanelDismissListener) {
        super(context, R.style.pay_dialog);
        mContext = context;
        myPwd = pwd;
        this.orderId = orderId;
        this.payChannel = channel;
        this.type = type;
        this.onPasswordPanelDismissListener = onPasswordPanelDismissListener;
        payType = 0;  //订单支付
        initView();
    }

    public PasswordPanel(Context context, String pwd, OnPasswordPanelDismissListener onPasswordPanelDismissListener) {
        super(context, R.style.pay_dialog);
        mContext = context;
        myPwd = pwd;
        this.onPasswordPanelDismissListener = onPasswordPanelDismissListener;
        payType = 1;  //提现
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.pwd_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = com.gs.buluo.common.utils.DensityUtils.dip2px(mContext, 450);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        pwdEditText.showKeyBoard();
        rootView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        pwdEditText.setInputCompleteListener(new com.gs.buluo.common.widget.PwdEditText.InputCompleteListener() {
            @Override
            public void inputComplete() {
                if (TextUtils.equals(MD5.md5(pwdEditText.getStrPassword()), myPwd)) {
                    doSuccess = true;
                    doDeal();
                } else {
                    ToastUtils.ToastMessage(getContext(), R.string.wrong_pwd);
                    pwdEditText.clear();
                }
            }
        });
    }

    private void doDeal() {
        if (payType == 0) {
            payMoney();     //支付
        } else {
            dismiss();      //提现
        }
    }

    private void payMoney() {
        NewPaymentRequest request = new NewPaymentRequest();
        request.orderIds = orderId;
        request.payChannel = payChannel.name();
        request.password = pwdEditText.getStrPassword();
        LoadingDialog.getInstance().show(mContext, "", true);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).createPayment(TribeApplication.getInstance().getUserInfo().getId(), type, request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderPayment>>() {
                    @Override
                    public void onNext(BaseResponse<OrderPayment> orderPaymentBaseResponse) {
                        setStatus(orderPaymentBaseResponse.data);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        if (e.getDisplayMessage() != null) {
                            ToastUtils.ToastMessage(getContext(), e.getDisplayMessage());
                        } else {
                            ToastUtils.ToastMessage(getContext(), R.string.connect_fail);
                        }
                    }
                });
    }

    public void setStatus(final OrderPayment data) {
        if (data.status == OrderPayment.PayStatus.FINISHED || data.status == OrderPayment.PayStatus.PAYED) {
            LoadingDialog.getInstance().dismissDialog();
            com.gs.buluo.common.utils.ToastUtils.ToastMessage(getContext(), R.string.pay_success);
            EventBus.getDefault().post(new PaymentEvent());
            dismiss();
        } else {
            new Handler().postDelayed(new TimerTask() {
                @Override
                public void run() {
                    getPaymentInfo(data);
                }
            }, 1000);
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
                });
    }

    public void setStatusAgain(final OrderPayment data) {
        if (data.status == OrderPayment.PayStatus.FINISHED || data.status == OrderPayment.PayStatus.PAYED) {
            com.gs.buluo.common.utils.ToastUtils.ToastMessage(getContext(), R.string.pay_success);
            EventBus.getDefault().post(new PaymentEvent());
            dismiss();
        } else {
            ToastUtils.ToastMessage(getContext(), data.note);
        }
    }

    public interface OnPasswordPanelDismissListener {
        void onPasswordPanelDismiss(boolean successful);
    }

    @Override
    public void dismiss() {
        if (onPasswordPanelDismissListener != null) {
            onPasswordPanelDismissListener.onPasswordPanelDismiss(doSuccess);
        }
        pwdEditText.dismissKeyBoard();
        super.dismiss();
    }
}
