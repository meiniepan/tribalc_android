package com.gs.buluo.app.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by hjn on 2016/11/16.
 */
public class GoodsEntity implements Serializable{
    public String id;
    public String creatTime;
    public String category;
    public String name;
    public String brand;
    public String mainPicture;
    public List<String> pictures;
    public String thumbnail;
    public String detailURL;
    public String originalPrice;
    public String salePrice;
    public StandardRepo standardDesc;
    public Map<String ,String >  standardRepertory;
    public String saleQuantity;
    public String note;
    public List<String> tags;
    public String originCountry;
    public String dispatch;

    public class StandardRepo implements Serializable{
        public List<String> level1;
        public List<String> level2;
    }
}
