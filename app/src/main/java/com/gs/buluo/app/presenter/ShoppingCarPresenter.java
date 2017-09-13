package com.gs.buluo.app.presenter;

import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ResponseBody.ShoppingCartResponse;
import com.gs.buluo.app.network.ShoppingApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.impl.IShoppingView;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/12/2.
 */
public class ShoppingCarPresenter extends BasePresenter<IShoppingView> {
    private String nextSkip;

    public void getShoppingListFirst() {
        TribeRetrofit.getInstance().createApi(ShoppingApis.class).
                getShoppingCarListFirst(TribeApplication.getInstance().getUserInfo().getId(), 100)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ShoppingCartResponse>>() {
                    @Override
                    public void onNext(BaseResponse<ShoppingCartResponse> response) {
                        if (isAttach()) mView.getShoppingCarInfoSuccess(response.data);
                        nextSkip = response.data.nextSkip;
                    }

                    @Override
                    public void onFail(ApiException e) {
                        mView.showError(e.getCode());
                    }
                });
    }

    public void getShoppingCarMore() {
        TribeRetrofit.getInstance().createApi(ShoppingApis.class).
                getShoppingCarList(TribeApplication.getInstance().getUserInfo().getId(), nextSkip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ShoppingCartResponse>>() {
                    @Override
                    public void onNext(BaseResponse<ShoppingCartResponse> response) {
                        if (isAttach()) mView.getShoppingCarInfoSuccess(response.data);
                        nextSkip = response.data.nextSkip;
                    }
                });
    }
}
