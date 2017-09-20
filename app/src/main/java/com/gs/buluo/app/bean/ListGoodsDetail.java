package com.gs.buluo.app.bean;

import android.annotation.SuppressLint;

import com.gs.buluo.app.bean.ResponseBody.IBaseResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjn on 2016/11/22.
 */
@SuppressLint("ParcelCreator")
public class ListGoodsDetail extends ListGoods implements IBaseResponse {
    public String title;
    public String standardId;
//    public String standardSnapshot;
    public boolean snapshot;
    public boolean published;
    public String categorty;
    public List<String> pictures;
    public String thumbnail;
    public ArrayList<String> detail;
    public int repertory;
    public String note;
    public String originCountry;
    public String dispatchPlace;
    public MarkStore tMarkStore;
    public int dailyLimit;//单日限量
    public int dailySaled;//单日已售出
}
