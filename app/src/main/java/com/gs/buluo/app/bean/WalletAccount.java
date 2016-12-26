package com.gs.buluo.app.bean;

import com.gs.buluo.app.bean.ResponseBody.IBaseResponse;

/**
 * Created by hjn on 2016/11/18.
 */
public class WalletAccount implements IBaseResponse {
    public enum  STATUS{
        NORMAL("NORMAL") ,LOCKED("LOCKED");
        String status;
        STATUS(String status) {
            this.status=status;
        }
    }

    public String id;
    public String balance;
    public STATUS state;
    public String lastTrading;
    public String password;
}
