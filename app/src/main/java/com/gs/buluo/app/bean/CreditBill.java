package com.gs.buluo.app.bean;

/**
 * Created by hjn on 2017/7/24.
 */

public class CreditBill {
    public String id;
    public String ownerId;
    public float amount;
    public float paidAmount;
    public long zeroDate;
    public long billDate;
    public long repayDate;
    public CreditBillStatus status;

    enum CreditBillStatus {
        CREATED("创建"), PAID("已还清"), OVERDUE("逾期"), FREEZING("逾期后冰洁，无法继续还款");
        public String status;

        CreditBillStatus(String s) {
            status = s;
        }
    }
}
