package com.gs.buluo.app.bean;

/**
 * Created by hjn on 2016/11/11.
 */
public class UserAddressEntity {
    private String id;        //地址ID
    private String province;                    //收货省份
    private String city;                           //收货城市
    private String district;                       //收货城区
    private String address;     //详细地址
    private String name;                        //收货人姓名
    private String phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
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
