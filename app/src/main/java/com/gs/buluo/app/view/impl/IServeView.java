package com.gs.buluo.app.view.impl;

import com.gs.buluo.app.bean.DetailStoreSetMeal;
import com.gs.buluo.app.bean.ResponseBody.ServeResponse;

/**
 * Created by hjn on 2016/11/29.
 */
public interface IServeView extends IBaseView {
    void getServerSuccess(ServeResponse.ServeResponseBody body);
}
