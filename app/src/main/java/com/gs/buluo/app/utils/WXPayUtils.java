package com.gs.buluo.app.utils;

import android.os.SystemClock;

import com.facebook.common.internal.Ints;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.TribeApplication;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by hjn on 2016/12/30.
 */

public class WXPayUtils {
    private static WXPayUtils wxPayUtils;
    private static IWXAPI msgApi = null;


    private WXPayUtils(){
        msgApi = WXAPIFactory.createWXAPI(TribeApplication.getInstance().getApplicationContext(), null);
        // 将该app注册到微信
        msgApi.registerApp(Constant.Base.WX_ID);
    }

    public static WXPayUtils getInstance(){
        if (wxPayUtils ==null){
            wxPayUtils = new WXPayUtils();
        }
        return wxPayUtils;
    }

    public static void doPay(String prepayId){
        PayReq request = new PayReq();
        request.appId = Constant.Base.WX_ID;
        request.partnerId = Constant.Base.WX_SHOP_ID;
        request.prepayId= prepayId;
        request.packageValue = "Sign=WXPay";
        request.nonceStr= CommonUtils.getRandomString(32);
        request.timeStamp= SystemClock.currentThreadTimeMillis()/1000+"";
        request.sign= Constant.Base.WX_SIGN;
        msgApi.sendReq(request);
    }
}
