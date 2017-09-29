package com.gs.buluo.app.bean;

import java.io.Serializable;

/**
 * Created by hjn on 2016/11/17.
 */
public class BillEntity implements Serializable {
    public enum  TradingType{

        PAY("付款") ,RECEIPT("收款"),RECHARGE("充值"),WITHDRAW("提现"),REFUND("退款");
        public String status;

        TradingType(String status) {
            this.status=status;
        }
    }

    public String id;
    public String createTime;
    public String title;
    public String ownerId;
    public String anotherId;
    public String amount;
    public String balance;
    public TradingType tradingType;
    public PayChannel payChannel;
    public String associatedOrderId;
    public String note;
    public String displayName;
}
