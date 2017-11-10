package com.gs.buluo.app.bean;

/**
 * Created by hjn on 2017/7/19.
 */

public enum HomeMessageEnum {
    ACCOUNT_WALLET_PAYMENT("钱包助手", "付款通知"),
    ACCOUNT_WALLET_RECHARGE("钱包助手", "充值到账"),
    CREDIT_ENABLE("授信助手", "开通授信"),
    CREDIT_DISABLE("授信助手", "授信额度冻结"),
    CREDIT_BILL_GENERATION("授信助手", "账单生成"),
    CREDIT_BILL_PAYMENT("授信助手", "账单支付"),
    RENT_CHECK_IN("公寓管家", "入住通知"),
    RENT_BILL_GENERATION("公寓管家", "缴租提醒"),
    RENT_BILL_PAYMENT("公寓管家", "租金缴纳"),
    COMPANIES_ADMIN("企业办公", "管理变更"),
    COMPANIES_RENT_BILL_GENERATION("企业办公", "缴租提醒"),
    ACCOUNT_REGISTER("账户推送", "欢迎登陆"),
    ORDER_DELIVERY("订单详情", "发货提醒"),
    ORDER_SETTLE("订单详情", "买货提醒"),
    ORDER_REFUND("订单详情", "订单退款"),
    ACCOUNT_WALLET_WITHDRAW("钱包助手", "提现到账"),
    TENANT_RECHARGE("商户助手", "收款到账"),
    TENANT_WITHDRAW("商户助手", "提现到账"),
    COMPANIES_WALLET_WITHDRAW("企业办公", "提现到账"),
    WELFARE("福利", "福利"),
    WELFARE_PAYMENT("福利", "福利付款"),
    COMPANIES_RENT_BILL_PAYMENT("企业办公", "租金缴纳"),
    ACCOUNT_AUTHENTICATION("账户推送", "认证变更"),
    CONFERENCE_RESERVATION_REMIND("企业办公", "会议室预定"),
    CONFERENCE_RESERVATION_SUCCESS("企业办公", "会议室预定");


    public String owner;
    public String type;

    HomeMessageEnum(String owner, String type) {
        this.owner = owner;
        this.type = type;
    }
}
