package com.gs.buluo.app.bean;

import android.os.Parcelable;

/**
 * Created by hjn on 2016/11/28.
 */
public class CommunityPlate {
    public String id;
    public String name;
    public String address;
    public String phone;
    public String mainPicture;
    public String city;
    @Override
    public String toString() {
        return "CommunityPlate{" +
                "id='" + id + '\'' +
                ", companyName='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", mainPicture='" + mainPicture + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
