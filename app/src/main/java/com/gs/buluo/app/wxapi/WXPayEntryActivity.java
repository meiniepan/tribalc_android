package com.gs.buluo.app.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.RequestBodyBean.ValueBody;
import com.gs.buluo.app.eventbus.PaymentEvent;
import com.gs.buluo.app.eventbus.WXPayEvent;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.widget.LoadingDialog;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by hjn on 2016/12/29.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constant.Base.WX_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.e(TAG, "onReq: " + baseReq.toString());
        if (baseReq.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

        }
        LoadingDialog.getInstance().dismissDialog();
        finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.e(TAG, "onResp22222222: " + baseResp.toString());
        if (baseResp instanceof PayResp) {
            getPayResult(((PayResp) baseResp).prepayId);
        }
        finish();
    }

    private void getPayResult(String prepayId) {
        LoadingDialog.getInstance().show(this, "", true);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).getWXPayResult(new ValueBody(prepayId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onFail(ApiException e) {
                        if (!TextUtils.isEmpty(e.getDisplayMessage())) {
                            ToastUtils.ToastMessage(TribeApplication.getInstance().getApplicationContext(), e.getDisplayMessage());
                        } else {
                            ToastUtils.ToastMessage(TribeApplication.getInstance().getApplicationContext(), R.string.pay_cancel);
                        }
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        EventBus.getDefault().post(new WXPayEvent());
                        EventBus.getDefault().post(new PaymentEvent());
                    }
                });


    }
}
