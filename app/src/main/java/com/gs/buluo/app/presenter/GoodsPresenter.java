package com.gs.buluo.app.presenter;

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
public class GoodsPresenter extends BasePresenter<IGoodsView>{
    private String nextSkip;

        public void getGoodsList(){

            TribeRetrofit.getInstance().createApi(GoodsApis.class).
                    getGoodsListFirst(20 + "")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<BaseResponse<GoodList>>() {
                        @Override
                        public void onNext(BaseResponse<GoodList> response) {
                            nextSkip = response.data.nextSkip;
                            if (isAttach())mView.getGoodsInfo(response.data);
                        }
                    });
    }

    public void loadMore() {
        TribeRetrofit.getInstance().createApi(GoodsApis.class).
                getGoodsList(20, nextSkip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<GoodList>>() {
                    @Override
                    public void onNext(BaseResponse<GoodList> response) {
                        mView.getGoodsInfo(response.data);
                    }
                });
        }
}
