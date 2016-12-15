package com.gs.buluo.app.presenter;

import com.baidu.platform.comapi.map.E;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.ResponseBody.OrderResponse;
import com.gs.buluo.app.model.ShoppingModel;
import com.gs.buluo.app.view.impl.IOrderView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/24.
 */
public class OrderPresenter extends BasePresenter<IOrderView> {
    private ShoppingModel model;
    private OrderBean.OrderStatus status;
    private String nextSkip;

    public OrderPresenter() {
        model = new ShoppingModel();
    }

    public void getOrderListFirst(int pos) {
        setStatus(pos);
        model.getOrderFirst(TribeApplication.getInstance().getUserInfo().getId(), status, 20 + "", new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.body() != null && response.body().code == 200) {
                    nextSkip = response.body().data.nextSkip;
                    mView.getOrderInfoSuccess(response.body().data);
                } else {
                    mView.showError(R.string.connect_fail);
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }

    public void setStatus(int pos) {
        switch (pos){
            case 0:
                status = null;
                break;
            case 1:
                status = OrderBean.OrderStatus.NO_SETTLE;
                break;
            case 2:
                status = OrderBean.OrderStatus.DELIVERY;
                break;
            case 3:
                status = OrderBean.OrderStatus.RECEIVED;
                break;
        }
    }

    public void getOrderListMore() {
        model.getOrder(TribeApplication.getInstance().getUserInfo().getId(), status, 20 + "", nextSkip, new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.body() != null && response.body().code == 200) {
                    mView.getOrderInfoSuccess(response.body().data);
                } else {
                    mView.showError(R.string.connect_fail);
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }
}
