package com.gs.buluo.app.bean.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.gs.buluo.app.bean.UserInfoEntity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by hjn on 2016/11/10.
 */
public class UserInfoResponse {
    private int iid;
    private int code;
    private UserInfoEntity data;
    private String message;

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(UserInfoEntity data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public UserInfoEntity getData() {
        if (data == null) {
            data = new UserInfoEntity();
        }
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
    }
}
