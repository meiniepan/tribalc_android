package com.gs.buluo.app.view.impl;

import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.ResponseBody.OrderResponse;

/**
 * Created by hjn on 2016/11/24.
 */
public interface IOrderView extends IBaseView{
    void getOrderInfoSuccess(OrderResponse data);
    void updateSuccess(OrderBean status);

    void getOrderDetail(OrderBean data);
}
