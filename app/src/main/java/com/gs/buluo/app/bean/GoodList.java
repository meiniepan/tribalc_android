package com.gs.buluo.app.bean;

import com.gs.buluo.app.bean.ResponseBody.IBaseResponse;

import java.util.List;

/**
 * Created by hjn on 2016/11/16.
 */
public class GoodList implements IBaseResponse {
    public String category;
    public String sort;
    public String prevSkip;
    public String nextSkip;
    public Boolean hasMore;
    public List<ListGoods> content;

}
