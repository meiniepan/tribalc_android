package com.gs.buluo.app.bean.RequestBodyBean;

/**
 * Created by hjn on 2017/5/9.
 */

public class PaySessionResponse {

    public PaySessionResult result;

    public class PaySessionResult{
        public String sessionId;
        public String paymentId;
    }
}
