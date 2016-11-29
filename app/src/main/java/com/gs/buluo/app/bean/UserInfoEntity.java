package com.gs.buluo.app.bean;

import android.text.TextUtils;

import com.gs.buluo.app.Constant;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by hjn on 2016/11/10.
 */
@Table(name = "user_info")
public class UserInfoEntity {
    @Column(name = "id", isId = true)
    private int mid;

    @Column(name = "nickname")
    private String nickname;

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
    @Column(name="enterprise_id")
    private String enterpriseID;

    @Column(name="community_id")
    private String communityID;



    private String province;
    private String district;

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
        return "5824287f0cf210fc9cef5e42";
//        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnterpriseID() {
        return enterpriseID;
    }

    public void setEnterpriseID(String enterpriseID) {
        this.enterpriseID = enterpriseID;
    }

    public String getCommunityID() {
        return communityID;
    }

    public void setCommunityID(String communityID) {
        this.communityID = communityID;
    }
}
