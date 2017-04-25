package com.gs.buluo.app.presenter;

import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.OrderResponse;
import com.gs.buluo.app.network.ShoppingApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.impl.IOrderView;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/24.
 */
public class OrderPresenter extends BasePresenter<IOrderView> {
    private String status;
    private String nextSkip;

    public void getOrderListFirst(int pos) {
        setStatus(pos);
        TribeRetrofit.getInstance().createApi(ShoppingApis.class).
                getOrderFirst(TribeApplication.getInstance().getUserInfo().getId(), 20, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderResponse>>() {
                    @Override
                    public void onNext(BaseResponse<OrderResponse> response) {
                        nextSkip = response.data.nextSkip;
                        if (isAttach()) mView.getOrderInfoSuccess(response.data);
                    }
                });
    }

    public void setStatus(int pos) {
        switch (pos) {
            case 0:
                status = null;
                break;
            case 1:
                status = OrderBean.OrderStatus.NO_SETTLE.name();
                break;
            case 2:
                status = OrderBean.OrderStatus.DELIVERY.name();
                break;
            case 3:
                status = OrderBean.OrderStatus.RECEIVED.name();
                break;
        }
    }

    public void getOrderListMore() {
        TribeRetrofit.getInstance().createApi(ShoppingApis.class).
                getOrderList(TribeApplication.getInstance().getUserInfo().getId(), 20, nextSkip, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderResponse>>() {
                    @Override
                    public void onNext(BaseResponse<OrderResponse> response) {
                        if (isAttach()) mView.getOrderInfoSuccess(response.data);
                    }
                });
    }

    public void updateOrderStatus(String orderId, final String status) {
        TribeRetrofit.getInstance().createApi(ShoppingApis.class).
                updateOrderStatus(orderId, TribeApplication.getInstance().getUserInfo().getId(), new ValueRequestBody(status))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderBean>>() {
                    @Override
                    public void onNext(BaseResponse<OrderBean> response) {
                        if (isAttach()) mView.updateSuccess(response.data);
                    }
                });
    }

    public void getOrder(String orderId) {
        TribeRetrofit.getInstance().createApi(ShoppingApis.class).getOrder(orderId, TribeApplication.getInstance().getUserInfo().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<OrderBean>>() {
                    @Override
                    public void onNext(BaseResponse<OrderBean> response) {
                        mView.getOrderDetail(response.data);
                    }
                });
    }
}
