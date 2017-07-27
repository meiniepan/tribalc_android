package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.baofoo.sdk.device.BaofooDeviceFingerPrint;
import com.baofoo.sdk.device.environment.Environment;
import com.baofoo.sdk.device.interfaces.ResultInterfaces;
import com.gs.buluo.app.BuildConfig;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.BankOrderResponse;
import com.gs.buluo.app.bean.ConfirmOrderRequest;
import com.gs.buluo.app.bean.PrepareOrderRequest;
import com.gs.buluo.app.bean.QueryOrderRequest;
import com.gs.buluo.app.bean.RequestBodyBean.PaySessionResponse;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.LoadingDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/4/26.
 */


public class BfRechargeVerifyCodePanel extends Dialog {
    private final Context mContext;
    private String mRechargeId;
    @Bind(R.id.et_verify_code)
    EditText etVerifyCode;
    @Bind(R.id.reGet_verify_code)
    TextView reGetVerifyCode;
    @Bind(R.id.tv_finish)
    TextView tvFinish;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    private RechargePanel mRechargePanel;
    private BankCard mBankCard;
    private BaofooDeviceFingerPrint baofooDeviceFingerPrint;
    private String rechargeAmount;


    public BfRechargeVerifyCodePanel(Context context, BankCard bankCard, String result, String money,  RechargePanel rechargePanel) {
        super(context, R.style.pay_dialog);
        mContext = context;
        mRechargeId = result;
        mRechargePanel = rechargePanel;
        mBankCard = bankCard;
        rechargeAmount = money;
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.verify_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = DensityUtils.dip2px(mContext, 450);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        timing();
        tvPhone.setText(mBankCard.phone.substring(0, 3) + "****" + mBankCard.phone.substring(7, 11));
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etVerifyCode.length() == 0) {
                    ToastUtils.ToastMessage(getContext(), R.string.input_verify);
                    return;
                }
                onFinish();
            }
        });

        rootView.findViewById(R.id.pwd_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        reGetVerifyCode.setClickable(false);
        reGetVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timing();
                doRecharge(rechargeAmount);
            }
        });
    }

    private void onFinish() {
        ConfirmOrderRequest request = new ConfirmOrderRequest();
        request.rechargeId = mRechargeId;
        request.vcode = etVerifyCode.getText().toString().trim();
        LoadingDialog.getInstance().show(mContext, "", true);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).confirmOrder(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<BankOrderResponse>>() {
                    @Override
                    public void onNext(BaseResponse<BankOrderResponse> bankOrderResponseBaseResponse) {
                        dealWithRechargeResult(bankOrderResponseBaseResponse);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        super.onFail(e);
                        etVerifyCode.setText("");
                    }
                });
    }

    private void dealWithRechargeResult(BaseResponse<BankOrderResponse> bankOrderResponseBaseResponse) {
        switch (bankOrderResponseBaseResponse.data.result) {
            case "1":
                ToastUtils.ToastMessage(mContext, R.string.success);
                jumpOnSuccess();
                break;
            case "2":
                ToastUtils.ToastMessage(mContext, R.string.wrong_verify);
                break;
            case "3":
                ToastUtils.ToastMessage(mContext, R.string.dealing);
                waitAndQueryAgain();
                break;
            case "4":
                ToastUtils.ToastMessage(mContext, R.string.unpay);
                break;
        }
    }

    private void waitAndQueryAgain() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                QueryOrderRequest request = new QueryOrderRequest();
                request.value = mRechargeId;
                LoadingDialog.getInstance().show(mContext, "", true);
                TribeRetrofit.getInstance().createApi(MoneyApis.class).queryOrder(request)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseSubscriber<BaseResponse<BankOrderResponse>>() {
                            @Override
                            public void onNext(BaseResponse<BankOrderResponse> bankOrderResponseBaseResponse) {
                                dealWithRechargeResult(bankOrderResponseBaseResponse);
                            }
                        });
            }
        }, 3000);
    }

    private void doRecharge(final String num) {
//        TribeApplication.showDialog("");
        LoadingDialog.getInstance().show(mContext, "", true);

        TribeRetrofit.getInstance().createApi(MoneyApis.class).getPrepareOrderInfo( new ValueRequestBody(null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<PaySessionResponse>>(false) {
                    @Override
                    public void onNext(BaseResponse<PaySessionResponse> response) {
                        doNextPrepare(response.data.result, num);
                    }
                });
    }

    private void doNextPrepare(final PaySessionResponse.PaySessionResult data, final String num) {
        if (BuildConfig.API_SERVER_URL.contains("dev")) {
            baofooDeviceFingerPrint = new BaofooDeviceFingerPrint(getContext(), data.sessionId, Environment.PRODUCT_DEVICE_SERVER);
        } else {
            baofooDeviceFingerPrint = new BaofooDeviceFingerPrint(getContext(), data.sessionId, Environment.PRODUCT_DEVICE_SERVER);
        }
        baofooDeviceFingerPrint.execute();
        baofooDeviceFingerPrint.onRespResult(new ResultInterfaces() {
            @Override
            public void respSuccess(String s) {
                doPrepare(num, data.paymentId);
            }

            @Override
            public void respError(String s) {
                Log.e("baofoo", "respError: " + s);
                ToastUtils.ToastMessage(getContext(), R.string.connect_fail);
                LoadingDialog.getInstance().dismissDialog();
            }
        });
    }

    private void doPrepare(String num, String paymentId) {
        PrepareOrderRequest prepareOrderRequest = new PrepareOrderRequest();
        prepareOrderRequest.bankCardId = mBankCard.id;
        prepareOrderRequest.totalFee = num;
        prepareOrderRequest.paymentId = paymentId;
        LoadingDialog.getInstance().show(mContext, "", true);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                prepareOrder(prepareOrderRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<BankOrderResponse>>(false) {
                    @Override
                    public void onNext(BaseResponse<BankOrderResponse> response) {
                        //this moment begin send verify code in deed
                        mRechargeId = response.data.result;
                    }
                });
    }

    private void timing() {
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                reGetVerifyCode.setClickable(false);
                reGetVerifyCode.setText("(" + millisUntilFinished / 1000 + ")重新获取");
            }

            @Override
            public void onFinish() {
                reGetVerifyCode.setText("重新获取");
                reGetVerifyCode.setClickable(true);
            }
        }.start();
    }

    private void jumpOnSuccess() {
        dismiss();
        mRechargePanel.dismiss();
    }

}