package com.gs.buluo.app.presenter;

import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.HomeMessageResponse;
import com.gs.buluo.app.network.HomeMessagesApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.impl.IMainFragmentView;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/8/10.
 */

public class MainFragmentPresenter extends BasePresenter<IMainFragmentView> {
    public void getHomeMessage() {
        TribeRetrofit.getInstance().createApi(HomeMessagesApis.class).getMessage(TribeApplication.getInstance().getUserInfo().getId(), 5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<HomeMessageResponse>>() {
                    @Override
                    public void onNext(BaseResponse<HomeMessageResponse> response) {
                        if (isAttach()) mView.getHomeMessageSuccess(response.data);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        if (isAttach()) mView.getHomeMessageFail(e);
                    }
                });
    }

    public void getRefreshMessage(long createTime) {
        TribeRetrofit.getInstance().createApi(HomeMessagesApis.class).getMessageMore(TribeApplication.getInstance().getUserInfo().getId(),
                8, createTime
                , true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<HomeMessageResponse>>() {
                    @Override
                    public void onNext(BaseResponse<HomeMessageResponse> response) {
                        if (isAttach()) mView.getRefreshMessageSuccess(response.data);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        if (isAttach()) mView.getRefreshMessageFail(e);
                    }
                });
    }

    public void getMoreMessage(long createTime) {
        TribeRetrofit.getInstance().createApi(HomeMessagesApis.class).getMessageMore(TribeApplication.getInstance().getUserInfo().getId(),
                5, createTime
                , false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<HomeMessageResponse>>() {
                    @Override
                    public void onNext(BaseResponse<HomeMessageResponse> response) {
                        if (isAttach()) mView.getMoreMessageSuccess(response.data);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        if (isAttach()) mView.getMoreMessageFail(e);
                    }
                });
    }
}
