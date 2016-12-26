package com.gs.buluo.app.bean;

import com.gs.buluo.app.bean.ResponseBody.IBaseResponse;

import java.util.List;
import java.util.Map;

/**
 * Created by hjn on 2016/11/22.
 */
public class GoodsStandard implements IBaseResponse {
    public String id;
    public Map<String,ListGoodsDetail>  goodsIndexes;
    public GoodsDescription descriptions;

    public  class GoodsDescription{
        public   GoodsType primary;
        public   GoodsType secondary;
    }


    public  class GoodsType{
        public  String label;
        public   List<String> types;
    }
}
