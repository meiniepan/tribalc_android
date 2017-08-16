package com.gs.buluo.app.view.impl;

import com.gs.buluo.app.bean.ResponseBody.ShoppingCartResponse;

/**
 * Created by hjn on 2016/12/2.
 */
public interface IShoppingView extends IBaseView {
    void getShoppingCarInfoSuccess(ShoppingCartResponse body);
}
