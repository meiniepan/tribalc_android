package com.gs.buluo.app.bean;

import com.gs.buluo.app.bean.ResponseBody.IBaseResponse;

import java.util.List;

/**
 * Created by hjn on 2016/11/29.
 */
public class DetailStoreSetMeal extends ListStoreSetMeal implements IBaseResponse {
    public String topics;
    public List<String> pictures;
    public String detailURL;
    public int collectNum;
    public int reservationNum;
    public String recommendedReason;
    public DetailStore detailStore;
}
