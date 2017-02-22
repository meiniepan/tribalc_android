package com.gs.buluo.app.bean;

import com.gs.buluo.app.bean.ResponseBody.IBaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjn on 2016/12/20.
 */
public class StoreDetail implements IBaseResponse {
    public String id;
    public String name;
    public String linkman;
    public String phone;
    public String logo;
    public String subbranchName;
    public String desc;
    public StoreCategory category;                //Default FOOD From { FOOD, GIFT, OFFICE, LIVING, HOUSE, MAKEUP, PENETRATION, REPAST, HAIRDRESSING, FITNESS, ENTERTAINMENT, KEEPHEALTHY}
    public String otherPhone;        //其他电话
    public String province;
    public String city;
    public String district;
    public String address;
    public ArrayList<String> pictures;
    public List<String> facilities;              //辅助设施
    public String topics;
    public String personExpense;
    public List<String> cookingStyle;
    public String businessHours;
    public double[] coordinate;
    public String markPlace;
    public String collectionNum;
    public String popularValue;

    public enum StoreCategory {
        FOOD("食品"), GIFT("礼品"), OFFICE("办公用品"), LIVING("生活用品"), HOUSE("家居用品"), MAKEUP("化妆品"), PENETRATION("妇婴用品"),
        REPAST("餐饮"), HAIRDRESSING("美容"), FITNESS("健身"), ENTERTAINMENT("休闲娱乐"), KEEPHEALTHY("养身");
        String type;

        StoreCategory(String s) {
            type=s;
        }

        @Override
        public String toString() {
            return type;
        }
    }
}
