package com.gs.buluo.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hjn on 2016/11/24.
 */
public class OrderBean implements Serializable {
    public  String id;
    public String orderNum;
    public String ownerId;
    public String address;
    public String expressType;
    public String expressFee;
    public String totalFee;
    public String note;
    public  PayChannel payChannel;
    public OrderStatus status;
    public String createTime;
    public String settleTime;
    public String deliveryTime;
    public String receivedTime;
    public MarkStore store;
    public List<OrderItem> itemList;



    public enum  PayChannel {
        BALANCE("BALANCE") ,ALIPAY("ALIPAY"),WEICHAT("WEICHAT"),BANKCARD("BANKCARD");
        String status;

        PayChannel(String status) {
            this.status=status;
        }
    }

    public enum  OrderStatus{
        NO_SETTLE("NO_SETTLE") ,CANNEL("CANNEL"),SETTLE("SETTLE"),DELIVERY("DELIVERY"),RECEIVED("RECEIVED");
        String status;

        OrderStatus(String status) {
            this.status=status;
        }
    }

    public class OrderItem implements Serializable{
        public int amount;
        public ListGoods goods;
    }
}
