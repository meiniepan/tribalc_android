package com.gs.buluo.app.bean;

import android.annotation.SuppressLint;

import com.gs.buluo.app.bean.ResponseBody.IBaseResponse;

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
    public String detailURL;
    public int repertory;
    public String note;
    public List<String> tags;
    public String originCountry;
    public String dispatchPlace;
    public MarkStore tMarkStore;
}
