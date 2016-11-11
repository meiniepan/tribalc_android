package com.gs.buluo.app.bean;

import com.alibaba.fastjson.JSON;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by hjn on 2016/11/10.
 */
@Table(name = "user")
public class UserInfoResponse {
    @Column(name = "id", isId = true)
    private int iid;

    @Column(name = "user_json")
    private String userJson;

    @Column(name = "user_info_json")
    private String infoJson;


    public UserInfoEntity getUserInfoEntity() {
        return JSON.parseObject(userJson,UserInfoEntity.class);
    }

    public void setUserInfoJson(UserInfoEntity user) {
        this.userJson = JSON.toJSONString(user);
    }

//    public UserBeanResponse.UserBeanEntity getUserBeanEntity() {
//        return JSON.parseObject(userJson, UserBeanResponse.UserBeanEntity.class);
//    }

    public void setUserJson(UserBeanResponse.UserBeanEntity user) {
        this.userJson = JSON.toJSONString(user);
    }

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
