package com.gs.buluo.app.bean;

import com.gs.buluo.app.bean.ResponseBody.IBaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjn on 2016/11/18.
 */
public class WalletAccount implements IBaseResponse {
    private enum WalletStatus {
        NORMAL("NORMAL"), LOCKED("LOCKED");
        String status;

        WalletStatus(String status) {
            this.status = status;
        }
    }

    private enum AccountType {
        CARD, //会员卡模式，仅用于商户，此时"商户余额"计算公式为 balance - creditBalance，其他额度信息无效,
        PROTOCOL    //(协议模式，通用，各项余额正常显示)
    }

    private enum CreditStatus {
        NONE("未开通"), NORMAL("正常"), OVERDUE("预期"), DISABLED("停用");

        CreditStatus(String status) {

        }
    }

    public float withdrawCharge; //手续费
    public String id;
    public String balance;
    public WalletStatus state;
    public String lastTrading;
    public String password;

    public float limitedBalance;
    public float creditBalance;
    public float creditLimit;
    public CreditStatus creditStatus;
    public ArrayList<BankCard> bankCards;
}
