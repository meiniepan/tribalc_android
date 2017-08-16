package com.gs.buluo.app.presenter;

import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.GoodsStandard;
import com.gs.buluo.app.bean.ListGoodsDetail;
import com.gs.buluo.app.bean.RequestBodyBean.NewOrderBean;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.network.GoodsApis;
import com.gs.buluo.app.network.ShoppingApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.impl.IGoodDetialView;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/22.
 */
public class GoodsDetailPresenter extends BasePresenter<IGoodDetialView> {

    public void getGoodsDetail(String id) {
        TribeRetrofit.getInstance().createApi(GoodsApis.class).
                getGoodsDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ListGoodsDetail>>() {
                    @Override
                    public void onNext(BaseResponse<ListGoodsDetail> response) {
                        if (isAttach()) mView.getDetailSuccess(response.data);
                    }
                });
    }

    public void getGoodsStandard(String id) {

        TribeRetrofit.getInstance().createApi(GoodsApis.class).
                getGoodsStandard(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<GoodsStandard>>() {
                    @Override
                    public void onNext(BaseResponse<GoodsStandard> response) {
                        if (isAttach()) mView.getStandardSuccess(response.data);
                    }
                });
    }

    public void addCartItem(String id, int num) {
        NewOrderBean item = new NewOrderBean();
        item.goodsId = id;
        item.amount = num;
        TribeRetrofit.getInstance().createApi(ShoppingApis.class).
                addCartItem(TribeApplication.getInstance().getUserInfo().getId(), item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CodeResponse>>() {
                    @Override
                    public void onNext(BaseResponse<CodeResponse> response) {
                        if (isAttach()) mView.addSuccess();
                    }
                });

    }
}
