package com.gs.buluo.app.view.impl;

import com.gs.buluo.app.bean.ResponseBody.BillResponseData;

/**
 * Created by hjn on 2016/11/18.
 */
public interface IBillView extends IBaseView{
    void getBillSuccess(BillResponseData billList);
}
