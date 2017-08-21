package com.gs.buluo.app.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gs.buluo.app.Constant;
import com.gs.buluo.common.widget.LoadingDialog;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by hjn on 2016/12/29.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXEntryActivity";

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
        Log.e(TAG, "onReq: "+ baseReq.toString());
        if(baseReq.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){

        }
        LoadingDialog.getInstance().dismissDialog();
        finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.e(TAG, "onReq: "+ baseResp.toString());
//        EventBus.getDefault().post(new TopupEvent());
//        LoadingDialog.getInstance().dismissDialog();WXPayEntryActivity
//        finish();
    }
}
