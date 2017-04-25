package com.gs.buluo.app.presenter;

import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.DetailReservation;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.network.ReserveApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.impl.IDetailReserveView;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/30.
 */
public class DetailReservationPresenter extends BasePresenter<IDetailReserveView> {
    public void getReserveDetail(String id) {
        TribeRetrofit.getInstance().createApi(ReserveApis.class).
                getReserveDetail(id, TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<DetailReservation>>() {
                    @Override
                    public void onNext(BaseResponse<DetailReservation> response) {
                        if (isAttach()) mView.getDetailSuccess(response.data);
                    }
                });
    }

    public void cancelReserve(String id, String key) {
        TribeRetrofit.getInstance().createApi(ReserveApis.class).
                cancelReserve(id, TribeApplication.getInstance().getUserInfo().getId(), new ValueRequestBody(key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CodeResponse>>() {
                    @Override
                    public void onNext(BaseResponse<CodeResponse> response) {
                        if (isAttach()) mView.cancelSuccess();
                    }
                });
    }
}
