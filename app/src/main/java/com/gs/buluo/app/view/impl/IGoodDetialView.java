package com.gs.buluo.app.view.impl;

import com.gs.buluo.app.bean.ListGoodsDetail;
import com.gs.buluo.app.bean.GoodsStandard;

/**
 * Created by hjn on 2016/11/22.
 */
public interface IGoodDetialView extends IBaseView{
    void getDetailSuccess(ListGoodsDetail entity);
    void getStandardSuccess(GoodsStandard standard);
    void addSuccess();
}
