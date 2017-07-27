package com.gs.buluo.app.presenter;

import com.gs.buluo.app.bean.ResponseBody.BillResponseData;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.impl.IBillView;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/18.
 */
public class BillPresenter extends BasePresenter<IBillView>{

    private String nextSkip;


    public void getBillListFirst(String targetId){

        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getBillListFirst(targetId,"20")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<BillResponseData>>() {
                    @Override
                    public void onNext(BaseResponse<BillResponseData> response) {
                        if (isAttach())  mView.getBillSuccess(response.data);
                        nextSkip =  response.data.nextSkip;
                    }
                });
    }

    public void loadMoreBill(String targetId) {
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                getBillList(targetId,"20", nextSkip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<BillResponseData>>() {
                    @Override
                    public void onNext(BaseResponse<BillResponseData> response) {
                        nextSkip= response.data.nextSkip;
                        if (isAttach()) mView.getBillSuccess(response.data);
                    }
                });
    }
}
