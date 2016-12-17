package com.gs.buluo.app.bean;

/**
 * Created by hjn on 2016/12/17.
 */

public class OrderPayment {
    public String id;
    public String createTime;
    public String ownerAccountId;
    public PayStatus status;
    public String payChannel;
    public String totalAmount;
    public String note;

    public enum PayStatus{
        CREATED("CREATED"),PAYED("PAYED"),FINISHED("FINISHED"),FAILURE("FAILURE");

        PayStatus(String status) {
        }

    }
}
