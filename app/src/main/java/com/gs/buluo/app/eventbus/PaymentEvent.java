package com.gs.buluo.app.eventbus;
/**
 * Created by hjn on 2016/12/20.
 */
public class PaymentEvent {

    private String orderId;

    public PaymentEvent() {
    }

    public PaymentEvent(String orderId) {
        this.orderId=orderId;
    }
}
