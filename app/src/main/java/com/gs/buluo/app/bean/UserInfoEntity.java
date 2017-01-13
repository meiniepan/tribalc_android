package com.gs.buluo.app.bean;



import com.alibaba.fastjson.JSON;
import com.gs.buluo.app.bean.ResponseBody.IBaseResponse;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by hjn on 2016/11/10.
 */
@Table(name = "user_info")
public class UserInfoEntity implements IBaseResponse {
    @Column(name = "id", isId = true)
    private int mid;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Column(name = "uid")
    private String id;

    @Column(name = "birthday")
    private String birthday;
    @Column(name = "area")
    private String area;

    @Column(name ="sex")
    private String sex;
    @Column(name = "picture")
    private String picture;

    @Column(name="emotion")
    private String emotion;

    private String coordinate;

    @Column(name="service_level")
    private String serviceLeve;

    @Column(name="community_id")
    private String communityID;


    @Column(name ="community_name")
    private String communityName;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Column(name="cover")
    private String cover;

    private String province;
    private String district;

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
        sipInfo= JSON.toJSONString(sip);
    }

    public enum AuthorityStatus{
        NOT_START,PROCESSING,SUCCESS,FAILURE
    }

    public UserInfoEntity.AuthorityStatus getEnumStatus() {
        switch (authorizedStatus){
            case "NOT_START":
                return UserInfoEntity.AuthorityStatus.NOT_START;
            case "PROCESSING":
                return UserInfoEntity.AuthorityStatus.PROCESSING;
            case "SUCCESS":
                return UserInfoEntity.AuthorityStatus.SUCCESS;
            case "FAILURE":
                return UserInfoEntity.AuthorityStatus.FAILURE;
        }
        return UserInfoEntity.AuthorityStatus.NOT_START;
    }

    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public void setSip(SipBean bean){
        sip = bean;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
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

    public SipBean getSip(){

        return JSON.parseObject(sipInfo,SipBean.class);
    }


    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    private String city;
    private String registrationDate;

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    private String detailAddress;


    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getServiceLeve() {
        return serviceLeve;
    }

    public void setServiceLeve(String serviceLeve) {
        this.serviceLeve = serviceLeve;
    }

    public String getArea() {
        if (area.contains("null"))
            return "";
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public String getId() {
//        return "5824287f0cf210fc9cef5e42";
//        return "58326c550cf2fb2e9e989db5";
//        return "584e4f8c1c3e73d1bc07e6ea";
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getCommunityID() {
        return communityID;
    }

    public void setCommunityID(String communityID) {
        this.communityID = communityID;
    }

    public enum Gender{
        MALE("MALE"),FEMALE("FEMALE");
        public String sex;
        Gender(String s) {
            sex=s;
        }
    }

}
