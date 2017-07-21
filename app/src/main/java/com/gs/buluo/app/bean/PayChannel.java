package com.gs.buluo.app.bean;

/**
 * Created by hjn on 2017/7/21.
 */

public enum  PayChannel {
    BALANCE("余额支付") ,ALIPAY("支付宝"),WEICHAT("微信"),BF_BANKCARD("银行卡支付");
   public String value;

    PayChannel(String value) {
        this.value=value;
    }
}
