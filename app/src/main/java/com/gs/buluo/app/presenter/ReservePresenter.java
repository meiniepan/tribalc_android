package com.gs.buluo.app.presenter;

import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ResponseBody.ReserveResponseBody;
import com.gs.buluo.app.network.ReserveApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.impl.IReserveView;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/29.
 */
public class ReservePresenter extends BasePresenter<IReserveView> {
    private String nextSkip;

    public void getReserveListFirst(String category) {
        TribeRetrofit.getInstance().createApi(ReserveApis.class).
                getReserveListFirst(TribeApplication.getInstance().getUserInfo().getId(), 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ReserveResponseBody>>() {
                    @Override
                    public void onNext(BaseResponse<ReserveResponseBody> response) {
                        nextSkip = response.data.nextSkip;
                        if (isAttach()) mView.getReserveSuccess(response.data);
                    }
                });
    }

    public void getReserveMore(String category) {
        TribeRetrofit.getInstance().createApi(ReserveApis.class).
                getReserveList(TribeApplication.getInstance().getUserInfo().getId(),20, nextSkip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ReserveResponseBody>>() {
                    @Override
                    public void onNext(BaseResponse<ReserveResponseBody> response) {
                        if (isAttach()) mView.getReserveSuccess(response.data);
                    }
                });
    }
}
