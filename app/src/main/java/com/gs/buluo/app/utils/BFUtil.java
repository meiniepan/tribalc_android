package com.gs.buluo.app.utils;

import android.content.Context;
import android.util.Log;

import com.baofoo.sdk.device.BaofooDeviceFingerPrint;
import com.baofoo.sdk.device.environment.Environment;
import com.baofoo.sdk.device.interfaces.ResultInterfaces;
import com.gs.buluo.app.BuildConfig;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.BankOrderResponse;
import com.gs.buluo.app.bean.OrderPayment;
import com.gs.buluo.app.bean.PrepareOrderRequest;
import com.gs.buluo.app.bean.RequestBodyBean.PaySessionResponse;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.widget.panel.NewBfPayVerifyCodePanel;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
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
        this.onBFPayStatusListener = onStatusListener;
        this.mCtx = context;
        this.bankCard = bankCard;

        TribeRetrofit.getInstance().createApi(MoneyApis.class).getPrepareOrderInfo(TribeApplication.getInstance().getUserInfo().getId(), new ValueRequestBody(data.id))
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
                        doNextPrepare(data, response.data.result);
                    }
                });
    }

    private void doNextPrepare(final OrderPayment data, final PaySessionResponse.PaySessionResult result) {
        if (BuildConfig.API_SERVER_URL.contains("dev")) {
            baofooDeviceFingerPrint = new BaofooDeviceFingerPrint(mCtx, result.sessionId, Environment.PRODUCT_DEVICE_SERVER);
        } else {
            baofooDeviceFingerPrint = new BaofooDeviceFingerPrint(mCtx, result.sessionId, Environment.PRODUCT_DEVICE_SERVER);
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
                ToastUtils.ToastMessage(mCtx, R.string.connect_fail);
                LoadingDialog.getInstance().dismissDialog();
                baofooDeviceFingerPrint.releaseResource();//释放资源；
            }
        });
    }

    private void doPrepare(final OrderPayment data) {
        PrepareOrderRequest prepareOrderRequest = new PrepareOrderRequest();
        prepareOrderRequest.bankCardId = bankCard.id;
        prepareOrderRequest.totalFee = data.totalAmount;
        prepareOrderRequest.paymentId = data.id;
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                prepareOrder(TribeApplication.getInstance().getUserInfo().getId(), prepareOrderRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<BankOrderResponse>>() {
                    @Override
                    public void onNext(BaseResponse<BankOrderResponse> response) {
                        showVerifyPanel(data, response.data.result);
                    }
                });
    }

    private void showVerifyPanel(OrderPayment data, String result) {
        NewBfPayVerifyCodePanel panel = new NewBfPayVerifyCodePanel(mCtx, bankCard, data, result, new NewBfPayVerifyCodePanel.onVerifyFinishListener() {
            @Override
            public void onFinish() {
                onBFPayStatusListener.onBFSuccess();
            }
        });
        panel.show();
    }
}