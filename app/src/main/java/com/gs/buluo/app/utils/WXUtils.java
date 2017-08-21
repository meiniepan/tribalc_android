package com.gs.buluo.app.utils;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.WxPayResponse;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.UUID;

/**
 * Created by hjn on 2016/12/30.
 */

public class WXUtils {
    private static WXUtils wxPayUtils;
    private static IWXAPI msgApi = null;


    private WXUtils() {
        msgApi = WXAPIFactory.createWXAPI(TribeApplication.getInstance().getApplicationContext(), null);
        // 将该app注册到微信
        msgApi.registerApp(Constant.Base.WX_ID);
    }

    public static WXUtils getInstance() {
        if (wxPayUtils == null) {
            wxPayUtils = new WXUtils();
        }
        return wxPayUtils;
    }


    public void doPay(WxPayResponse data) {
        PayReq request = new PayReq();
        request.appId = Constant.Base.WX_ID;
        request.partnerId = data.partnerid;
        request.prepayId = data.prepayid;
        request.packageValue = "Sign=WXPay";
        request.nonceStr = data.noncestr;
        request.timeStamp = data.timestamp;
        request.sign = data.sign;
        msgApi.sendReq(request);
    }

    public void doLogin(){
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = UUID.randomUUID().toString();
        msgApi.sendReq(req);
    }
}
