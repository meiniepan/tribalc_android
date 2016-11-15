package com.gs.buluo.app.view.impl;

import com.gs.buluo.app.bean.UserAddressEntity;

/**
 * Created by hjn on 2016/11/15.
 */
public interface IAddAddressView extends IBaseView{
    void addAddressSuccess(UserAddressEntity entity);
    void updateAddressSuccess(UserAddressEntity entity);
}
