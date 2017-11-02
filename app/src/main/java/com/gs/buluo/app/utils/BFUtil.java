package com.gs.buluo.app.utils;

import android.content.Context;
import android.util.Log;

import com.baofoo.sdk.device.BaofooDeviceFingerPrint;
import com.baofoo.sdk.device.constonts.Constents;
import com.baofoo.sdk.device.environment.Environment;
import com.baofoo.sdk.device.interfaces.ResultInterfaces;
import com.gs.buluo.app.BuildConfig;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.OrderPayment;
import com.gs.buluo.app.bean.PrepareOrderRequest;
import com.gs.buluo.app.bean.RequestBodyBean.PaySessionResponse;
import com.gs.buluo.app.bean.RequestBodyBean.ValueBody;
import com.gs.buluo.app.bean.ResultResponse;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.widget.panel.NewBfPayVerifyCodePanel;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.LoadingDialog;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2017/7/18.
 */

public class BFUtil {
    private BaofooDeviceFingerPrint baofooDeviceFingerPrint;
    private Context mCtx;
    private BankCard bankCard;

    private OnBFPayStatusListener onBFPayStatusListener;

    public interface OnBFPayStatusListener {
        void onBFSuccess();
    }

    public void doBFPay(Context context, final OrderPayment data, BankCard bankCard, final OnBFPayStatusListener onStatusListener) {
        LoadingDialog.getInstance().show(context, "", false);
        this.onBFPayStatusListener = onStatusListener;
        this.mCtx = context;
        this.bankCard = bankCard;
        TribeRetrofit.getInstance().createApi(MoneyApis.class).generateSessionId(new ValueBody(data.id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponse<PaySessionResponse>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.ToastMessage(mCtx, R.string.connect_fail);
                        LoadingDialog.getInstance().dismissDialog();
                    }

                    @Override
                    public void onNext(BaseResponse<PaySessionResponse> response) {
                        data.id = response.data.result.paymentId;
                        doNextPrepare(data, response.data.result);
                    }
                });
    }

    private void doNextPrepare(final OrderPayment data, final PaySessionResponse.PaySessionResult result) {
        if (BuildConfig.API_SERVER_URL.contains("dev")) {
            baofooDeviceFingerPrint = new BaofooDeviceFingerPrint(mCtx, result.sessionId, Environment.PRODUCT_DEVICE_SERVER, Constents.SyncBlockTimeOut);
        } else {
            baofooDeviceFingerPrint = new BaofooDeviceFingerPrint(mCtx, result.sessionId, Environment.PRODUCT_DEVICE_SERVER,Constents.SyncBlockTimeOut);
        }
        baofooDeviceFingerPrint.execute();
        baofooDeviceFingerPrint.onRespResult(new ResultInterfaces() {
            @Override
            public void respSuccess(String s) {
                doPrepare(data, result);
                baofooDeviceFingerPrint.destroyHandler();//释放资源；
            }

            @Override
            public void respError(String s) {
                Log.e("baofoo", "respError: " + s);
                ToastUtils.ToastMessage(mCtx, R.string.connect_fail);
                LoadingDialog.getInstance().dismissDialog();
                baofooDeviceFingerPrint.destroyHandler();//释放资源；
            }
        });
    }

    private void doPrepare(final OrderPayment data, PaySessionResponse.PaySessionResult result) {
        PrepareOrderRequest prepareOrderRequest = new PrepareOrderRequest();
        prepareOrderRequest.bankCardId = bankCard.id;
        prepareOrderRequest.totalFee = data.totalAmount;
        prepareOrderRequest.paymentId = result.paymentId;
        prepareOrderRequest.targetId = data.ownerAccountId;
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                prepareOrder(prepareOrderRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ResultResponse>>() {
                    @Override
                    public void onNext(BaseResponse<ResultResponse> response) {
                        showVerifyPanel(data, response.data.result);
                    }
                });
    }

    private void showVerifyPanel(OrderPayment data, String result) {
        final NewBfPayVerifyCodePanel panel = new NewBfPayVerifyCodePanel(mCtx, bankCard, data, result, new NewBfPayVerifyCodePanel.onVerifyFinishListener() {
            @Override
            public void onFinish() {
                onBFPayStatusListener.onBFSuccess();
            }
        });
        panel.show();
    }
}
