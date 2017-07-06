package com.gs.buluo.app.bean.RequestBodyBean;

/**
 * Created by Solang on 2017/7/5.
 * 缴租计划项
 */

public class RentPlanItem {
    public String id;
    public String protocolId;
    public long plannedTime;
    public String plannedRental;
    public long actualTime;
    public String actualPay;
    public String[] bankSerialNum;
    public boolean finished;
    public long startTime;
    public long endTime;
    public String num;
}
