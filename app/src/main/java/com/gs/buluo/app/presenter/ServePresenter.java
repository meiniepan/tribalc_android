package com.gs.buluo.app.presenter;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ResponseBody.ServeResponse;
import com.gs.buluo.app.network.ServeApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.impl.IServeView;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/29.
 */
public class ServePresenter extends BasePresenter<IServeView> {
    private String nextSkip;

    public void getServeListFirst(String category, String sort) {
        String coordinate;
        if (sort.contains(Constant.SORT_COORDINATE_DESC)) {
            coordinate = TribeApplication.getInstance().getPosition().longitude + "," + TribeApplication.getInstance().getPosition().latitude;
            TribeRetrofit.getInstance().createApi(ServeApis.class).
                    getServiceListFirst(category, 20, sort, coordinate)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<BaseResponse<ServeResponse>>() {
                        @Override
                        public void onNext(BaseResponse<ServeResponse> response) {
                            ServeResponse data = response.data;
                            nextSkip = data.nextSkip;
                            if (isAttach()) mView.getServerSuccess(response.data);
                        }
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            if (isAttach()) mView.showError(R.string.net_error);
                        }
                    });
        } else {
            TribeRetrofit.getInstance().createApi(ServeApis.class).
                    getServiceListFirst(category, 20, sort)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<BaseResponse<ServeResponse>>() {
                        @Override
                        public void onNext(BaseResponse<ServeResponse> response) {
                            ServeResponse data = response.data;
                            nextSkip = data.nextSkip;
                            if (isAttach()) mView.getServerSuccess(response.data);
                        }
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            if (isAttach()) mView.showError(R.string.net_error);
                        }
                    });
        }
    }

    public void getServeMore(String category, String sort) {
        String coordinate;
        if (sort.contains(Constant.SORT_COORDINATE_DESC)) {
            coordinate = TribeApplication.getInstance().getPosition().longitude + "," + TribeApplication.getInstance().getPosition().latitude;
            TribeRetrofit.getInstance().createApi(ServeApis.class).
                    getServiceList(category, 20, sort, coordinate)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<BaseResponse<ServeResponse>>() {
                        @Override
                        public void onNext(BaseResponse<ServeResponse> response) {
                            ServeResponse data = response.data;
                            nextSkip = data.nextSkip;
                            if (isAttach()) mView.getServerSuccess(data);
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            if (isAttach()) mView.showError(R.string.net_error);
                        }
                    });
        } else {
            TribeRetrofit.getInstance().createApi(ServeApis.class).getServiceList(category, 20, sort, nextSkip)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<BaseResponse<ServeResponse>>() {
                        @Override
                        public void onNext(BaseResponse<ServeResponse> response) {
                            ServeResponse data = response.data;
                            nextSkip = data.nextSkip;
                            if (isAttach()) mView.getServerSuccess(data);
                        }
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            if (isAttach()) mView.showError(R.string.net_error);
                        }
                    });
        }
    }
}
