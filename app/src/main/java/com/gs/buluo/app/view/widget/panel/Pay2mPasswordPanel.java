package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.OrderPayment;
import com.gs.buluo.app.bean.Pay2MerchantRequest;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.AppManager;
import com.gs.buluo.app.view.activity.Pay2MerchantActivity;
import com.gs.buluo.app.view.activity.Pay2MerchantSuccessActivity;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.LoadingDialog;
import com.gs.buluo.common.widget.PwdEditText;

import org.xutils.common.util.MD5;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/6/8.
 */
public class Pay2mPasswordPanel extends Dialog {
    private final String name;
    private OnPasswordPanelDismissListener onPasswordPanelDismissListener;
    private String targetId;
    private Context mContext;
    @Bind(R.id.pwd_board_pet)
    PwdEditText pwdEditText;
    private final String myPwd;
    private OrderBean.PayChannel payChannel;
    private boolean doSuccess;
    private String totalFee;
    private AlertDialog dialog = null;


    public Pay2mPasswordPanel(Context context, String pwd, String targetId, String totalFee, OrderBean.PayChannel channel, String name, OnPasswordPanelDismissListener onPasswordPanelDismissListener) {
        super(context, R.style.pay_dialog);
        mContext = context;
        myPwd = pwd;
        this.targetId = targetId;
        this.payChannel = channel;
        this.totalFee = totalFee;
        this.name = name;
        this.onPasswordPanelDismissListener = onPasswordPanelDismissListener;
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
                    payMoney();
                } else {
                    ToastUtils.ToastMessage(getContext(), R.string.wrong_pwd);
                    pwdEditText.clear();
                }
            }
        });
    }

    private void payMoney() {
        Pay2MerchantRequest request = new Pay2MerchantRequest();
        request.targetId = targetId;
        request.payChannel = payChannel.name();
        request.totalFee = totalFee;
        request.password = pwdEditText.getStrPassword();
        LoadingDialog.getInstance().show(mContext, "", true);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).pay2Merchant(TribeApplication.getInstance().getUserInfo().getId(), request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderPayment>>() {
                    @Override
                    public void onNext(BaseResponse<OrderPayment> response) {
                            if (response.data.status.toString().equals("PAYED") || response.data.status.toString().equals("FINISHED")){
                                startSuccessActivity();
                            }else if (response.data.status.toString().equals("CREATED")){
                                TribeRetrofit.getInstance().createApi(MoneyApis.class).getPaymentStatus(TribeApplication.getInstance().getUserInfo().getId(), response.data.id)
                                        .subscribeOn(Schedulers.io()).delay(1, TimeUnit.SECONDS)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new BaseSubscriber<BaseResponse<OrderPayment>>() {
                                            @Override
                                            public void onNext(BaseResponse<OrderPayment> response) {
                                                if (response.data.status.toString().equals("PAYED") || response.data.status.toString().equals("FINISHED")){
                                                    startSuccessActivity();
                                                }else {
                                                    LoadingDialog.getInstance().dismissDialog();
                                                    showDialog();
                                                }
                                            }
                                        });
                            }else{
                                LoadingDialog.getInstance().dismissDialog();
                                showDialog();
                            }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoadingDialog.getInstance().dismissDialog();
                        showDialog();
                    }
                });
    }

    private void startSuccessActivity() {
        Intent intent = new Intent(mContext, Pay2MerchantSuccessActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("money", totalFee);
        mContext.startActivity(intent);
        AppManager.getAppManager().finishActivity(Pay2MerchantActivity.class);
    }


    public interface OnPasswordPanelDismissListener {
        void onPasswordPanelDismiss(boolean successful);
    }

    @Override
    public void dismiss() {
        if (onPasswordPanelDismissListener != null) {
            onPasswordPanelDismissListener.onPasswordPanelDismiss(doSuccess);
        }
        super.dismiss();
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
                dismiss();
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = DensityUtils.dip2px(mContext, 229);
        params.height = DensityUtils.dip2px(mContext, 213);
        dialog.getWindow().setAttributes(params);
    }


}
