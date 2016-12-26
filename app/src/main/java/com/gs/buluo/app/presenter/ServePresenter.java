package com.gs.buluo.app.presenter;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ResponseBody.ServeResponse;
import com.gs.buluo.app.model.ServeModel;
import com.gs.buluo.app.view.impl.IServeView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/29.
 */
public class ServePresenter extends BasePresenter<IServeView> {
    private ServeModel model;
    private String nextSkip;

    public ServePresenter(){
        model=new ServeModel();
    }

    public void getServeListFirst(String category,String sort){
        model.getServeListFirst(category, 20, sort, new Callback<ServeResponse>() {
            @Override
            public void onResponse(Call<ServeResponse> call, Response<ServeResponse> response) {
                if (response.body()!=null&&response.body().code==200&&response.body().data!=null){
                    ServeResponse.ServeResponseBody data = response.body().data;
                    nextSkip= data.nextSkip;
                    mView.getServerSuccess(response.body().data);
                }
            }

            @Override
            public void onFailure(Call<ServeResponse> call, Throwable t) {
                    mView.showError(R.string.connect_fail);
            }
        });
    }
    public void getServeMore(String category,String sort){
        model.getServeList(category, 20, sort,nextSkip, new Callback<ServeResponse>() {
            @Override
            public void onResponse(Call<ServeResponse> call, Response<ServeResponse> response) {
                if (response.body().code==200&&response.body().data!=null){
                    mView.getServerSuccess(response.body().data);
                }
            }

            @Override
            public void onFailure(Call<ServeResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }
}
