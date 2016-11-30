package com.gs.buluo.app.view.impl;

import com.gs.buluo.app.bean.DetailReservation;

/**
 * Created by hjn on 2016/11/30.
 */
public interface IDetailReserveView extends IBaseView{
    void getDetailSuccess(DetailReservation reservation);
    void cancelSuccess();
    void cancelFailure();
}
