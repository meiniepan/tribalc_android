package com.gs.buluo.app.presenter;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.OrderBean;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.BaseResponse;
import com.gs.buluo.app.bean.ResponseBody.OrderResponse;
import com.gs.buluo.app.model.ShoppingModel;
import com.gs.buluo.app.network.ShoppingService;
import com.gs.buluo.app.network.TribeCallback;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.view.impl.IOrderView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/24.
 */
public class OrderPresenter extends BasePresenter<IOrderView> {
    private ShoppingModel model;
    private String status;
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
        model.getOrder(TribeApplication.getInstance().getUserInfo().getId(), status, 20 + "", nextSkip, new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.body() != null && response.body().code == 200) {
                    if (isAttach())mView.getOrderInfoSuccess(response.body().data);
                } else {
                    if (isAttach())mView.showError(R.string.connect_fail);
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                if (isAttach()) mView.showError(R.string.connect_fail);
            }
        });
    }

    public void updateOrderStatus(String orderId, final String status) {
        model.updateOrder(TribeApplication.getInstance().getUserInfo().getId(), new ValueRequestBody(status), orderId, new Callback<BaseResponse<OrderBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<OrderBean>> call, Response<BaseResponse<OrderBean>> response) {
                if (response.body() != null && response.body().code == 200) {
                    if (isAttach()) mView.updateSuccess(response.body().data);
                } else {
                    if (isAttach()) mView.showError(R.string.update_fail);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<OrderBean>> call, Throwable t) {
                if (isAttach())mView.showError(R.string.connect_fail);
            }
        });
    }

    public void getOrder(String orderId){
        TribeRetrofit.getInstance().createApi(ShoppingService.class).getOrder(orderId,TribeApplication.getInstance().getUserInfo().getId()).
                enqueue(new TribeCallback<OrderBean>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<OrderBean>> response) {
                        mView.getOrderDetail(response.body().data);
                    }

                    @Override
                    public void onFail(int responseCode, BaseResponse<OrderBean> body) {
                        mView.showError(R.string.connect_fail);
                    }
                });
    }
}
