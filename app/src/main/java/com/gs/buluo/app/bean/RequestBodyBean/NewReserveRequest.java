package com.gs.buluo.app.bean.RequestBodyBean;

import com.gs.buluo.app.bean.UserInfoEntity;

/**
 * Created by hjn on 2016/11/30.
 */
public class NewReserveRequest {
    public String personNum;
    public String linkman;
    public String phone;
    public String note;
    public long appointTime;
    public String vcode;
    public UserInfoEntity.Gender sex;
    public String storeSetMealId;
}
