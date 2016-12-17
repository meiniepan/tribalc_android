package com.gs.buluo.app.view.impl;

import com.gs.buluo.app.bean.ResponseBody.OrderResponse;

/**
 * Created by hjn on 2016/11/24.
 */
public interface IOrderView extends IBaseView{
    void getOrderInfoSuccess(OrderResponse.OrderResponseBean data);
    void updateSuccess();
}
