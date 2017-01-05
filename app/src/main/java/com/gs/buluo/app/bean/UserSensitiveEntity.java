package com.gs.buluo.app.bean;

import com.alibaba.fastjson.JSON;
import com.gs.buluo.app.bean.ResponseBody.IBaseResponse;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by hjn on 2016/11/11.
 */
@Table(name = "user_sensitive")
public class UserSensitiveEntity implements IBaseResponse {
    @Column(name = "id", isId = true)
    private int mid;
    @Column(name="uid")
    private String id;
    @Column(name="companyName")
    private String name;
    @Column(name="phone")
    private String phone;
    @Column(name="id_no")
    private String idNo;
    @Column(name="address_id")
    private String addressID;
    @Column(name="company_id")
    private String companyID;
    @Column(name="company_name")
    private String companyName;
    @Column(name="sip_info")
    private String sipInfo;
    @Column(name="authorized_status")
    private String authorizedStatus;

    private SipBean sip;

    public void setSipJson(){
        sipInfo=JSON.toJSONString(sip);
    }

    public enum AuthorityStatus{
        NOT_START,PROCESSING,SUCCESS,FAILURE
    }

    public AuthorityStatus getEnumStatus() {
        switch (authorizedStatus){
            case "NOT_START":
                return AuthorityStatus.NOT_START;
            case "PROCESSING":
                return AuthorityStatus.PROCESSING;
            case "SUCCESS":
                return AuthorityStatus.SUCCESS;
            case "FAILURE":
                return AuthorityStatus.FAILURE;
        }
        return AuthorityStatus.NOT_START;
    }
//
//    public void setAuthorizedStatus(String authorizedStatus) {
//        this.authorizedStatus = authorizedStatus;
//    }

    public void setSip(SipBean bean){
        sip = bean;
    }

    public SipBean getSip(){
        return JSON.parseObject(sipInfo,SipBean.class);
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }
    @Override
    public String toString() {
        return "UserSensitiveEntity{" +
                "mid=" + mid +
                ", id='" + id + '\'' +
                ", companyName='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", idNo='" + idNo + '\'' +
                ", addressID='" + addressID + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getAddressID() {
        return addressID;
    }

    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }
}
