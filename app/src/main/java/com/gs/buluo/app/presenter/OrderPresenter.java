package com.gs.buluo.app.presenter;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.ResponseBody.OrderResponse;
import com.gs.buluo.app.model.OrderModel;
import com.gs.buluo.app.view.impl.IOrderView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/24.
 */
public class OrderPresenter extends BasePresenter<IOrderView> {
    private OrderModel model;
    public OrderPresenter(){
        model=new OrderModel();
    }

    public void getOrderListFirst(){
        model.getOrderFirst(TribeApplication.getInstance().getUserInfo().getId(), OrderBean.OrderStatus.NO_SETTLE, 20 + "", new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.body()!=null&&response.body().code==200){
                    mView.getOrderInfoSuccess(response.body().data);
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }

    public void getOrderListMore(String sortSkip){
        model.getOrder(TribeApplication.getInstance().getUserInfo().getId(), OrderBean.OrderStatus.NO_SETTLE, 20 + "",sortSkip, new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.body()!=null&&response.body().code==200){
                    mView.getOrderInfoSuccess(response.body().data);
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }
}
