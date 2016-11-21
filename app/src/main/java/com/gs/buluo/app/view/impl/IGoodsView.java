package com.gs.buluo.app.view.impl;

import com.gs.buluo.app.bean.ResponseBody.GoodResponseList;

/**
 * Created by hjn on 2016/11/16.
 */
public interface IGoodsView extends IBaseView{
    void getGoodsInfo(GoodResponseList responseList);
}
