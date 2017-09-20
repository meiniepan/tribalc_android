package com.gs.buluo.app.presenter;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.GoodList;
import com.gs.buluo.app.network.GoodsApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.impl.IGoodsView;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/16.
 */
public class GoodsOfStorePresenter extends BasePresenter<IGoodsView>{
    private String nextSkip;

        public void getGoodsList(String storeId){
            TribeRetrofit.getInstance().createApi(GoodsApis.class).
                    getGoodsOfStoreFirst(storeId,20 + "")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<BaseResponse<GoodList>>() {
                        @Override
                        public void onNext(BaseResponse<GoodList> response) {
                            nextSkip = response.data.nextSkip;
                            if (isAttach())mView.getGoodsInfo(response.data);
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (isAttach())mView.showError(R.string.connect_fail);
                        }
                    });
    }

    public void loadMore(String storeId) {
        TribeRetrofit.getInstance().createApi(GoodsApis.class).
                getGoodsOfStore(storeId,20+"", nextSkip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<GoodList>>() {
                    @Override
                    public void onNext(BaseResponse<GoodList> response) {
                        nextSkip = response.data.nextSkip;
                        if (isAttach()) mView.getGoodsMore(response.data);
                    }
                    @Override
                    public void onError(Throwable e) {
                        if (isAttach())mView.loadMoreError(R.string.connect_fail);
                    }
                });
        }
}
