package com.gs.buluo.app.bean;

import java.util.ArrayList;

/**
 * Created by hjn on 2017/11/7.
 */

public class WelfareEntity {
    public String ownerName;
    public String ownerId;
    public String transSerialNo;
    public PayChannel payChannel;
    public long payDate;
    public String totalAmount;
    public String total;
    public ArrayList<WelfareMember> paymentRouterViews;

    public class WelfareMember {
        public String targetName;
        public String amount;
    }
}
