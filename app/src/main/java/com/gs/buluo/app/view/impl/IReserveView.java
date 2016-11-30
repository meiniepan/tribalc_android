package com.gs.buluo.app.view.impl;

import com.gs.buluo.app.bean.ResponseBody.ReserveResponse;

/**
 * Created by hjn on 2016/11/29.
 */
public interface IReserveView extends IBaseView {

    void getReserveSuccess(ReserveResponse.ReserveResponseBody data);
}
