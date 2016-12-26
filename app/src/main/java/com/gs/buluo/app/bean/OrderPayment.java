package com.gs.buluo.app.bean;

import com.gs.buluo.app.bean.ResponseBody.IBaseResponse;

/**
 * Created by hjn on 2016/12/17.
 */

public class OrderPayment implements IBaseResponse {
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
