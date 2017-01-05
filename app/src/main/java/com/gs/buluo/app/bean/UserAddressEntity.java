package com.gs.buluo.app.bean;

import com.gs.buluo.app.bean.ResponseBody.IBaseResponse;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by hjn on 2016/11/11.
 */
@Table(name = "address_info")
public class UserAddressEntity implements Serializable, IBaseResponse {
    @Column(name = "id", isId = true)
    private int mid;
    @Column(name = "uid")
    private String uid;
    @Column(name = "address_id")
    private String id;        //地址ID
    @Column(name = "area")
    private String area;                     //收货省份
    @Column(name = "address")
    private String address;                     //详细地址
    @Column(name = "companyName")
    private String name;                        //收货人姓名
    @Column(name = "phone")
    private String phone;

    private String province;
    private String city;
    private String district;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvice() {
        return province;
    }

    public void setProvice(String province) {
        this.province = province;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String province,String city,String district) {
        this.area = province+"-"+city+"-"+district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
