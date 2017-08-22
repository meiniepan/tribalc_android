package com.gs.buluo.app.view.impl;

import com.gs.buluo.app.bean.HomeMessageResponse;
import com.gs.buluo.common.network.ApiException;

/**
 * Created by Solang on 2017/8/10.
 */

public interface IMainFragmentView extends IBaseView {
    void getHomeMessageSuccess(HomeMessageResponse messageResponse);

    void getHomeMessageFail(ApiException e);

    void getRefreshMessageSuccess(HomeMessageResponse messageResponse);

    void getRefreshMessageFail(ApiException e);

    void getMoreMessageSuccess(HomeMessageResponse messageResponse);

    void getMoreMessageFail(ApiException e);
}
