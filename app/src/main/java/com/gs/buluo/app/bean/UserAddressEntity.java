package com.gs.buluo.app.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by hjn on 2016/11/11.
 */
@Table(name = "address_info")
public class UserAddressEntity implements Serializable{
    @Column(name = "id", isId = true)
    private int mid;

    @Column(name = "uid")
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Column(name = "address_id")
    private String id;        //地址ID
    @Column(name = "area")
    private String area;                     //收货省份
    @Column(name = "address")
    private String address;                     //详细地址
    @Column(name = "name")
    private String name;                        //收货人姓名
    @Column(name = "phone")
    private String phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDetailAddress() {
        return address;
    }

    public void setDetailAddress(String address) {
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
