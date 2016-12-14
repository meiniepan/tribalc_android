package com.gs.buluo.app.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by hjn on 2016/11/11.
 */
@Table(name = "user_sensitive")
public class UserSensitiveEntity {
    @Column(name = "id", isId = true)
    private int mid;
    @Column(name="uid")
    private String id;
    @Column(name="name")
    private String name;
    @Column(name="phone")
    private String phone;
    @Column(name="id_no")
    private String idNo;
    @Column(name="address_id")
    private String addressID;

    @Override
    public String toString() {
        return "UserSensitiveEntity{" +
                "mid=" + mid +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
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
