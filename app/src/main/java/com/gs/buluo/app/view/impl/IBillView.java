package com.gs.buluo.app.view.impl;

import com.gs.buluo.app.bean.BillEntity;
import com.gs.buluo.app.bean.ResponseBody.BillResponse;

import java.util.List;

/**
 * Created by hjn on 2016/11/18.
 */
public interface IBillView extends IBaseView{
    void getBillSuccess(BillResponse.BillResponseData billList);
}
