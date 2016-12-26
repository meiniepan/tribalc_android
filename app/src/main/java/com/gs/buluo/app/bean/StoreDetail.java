package com.gs.buluo.app.bean;

import com.gs.buluo.app.bean.ResponseBody.IBaseResponse;

import java.util.List;

/**
 * Created by hjn on 2016/12/20.
 */
public class StoreDetail implements IBaseResponse {
    public String id;
    public String name;
    public String logo;
    public String phone;
    public String brand;
    public String category;
    public String mainPicture;
    public String thumbnail;
    public List<String> faclities;
    public String discount;
    public String markPlace;
    public List<String> tags;
    public List<String> pictures;
    public String address;
    public String collectNum;
    public String popularValue;
    public String businessHours;
}
