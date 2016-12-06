package com.gs.buluo.app.view.activity;

import android.os.Bundle;

import com.gs.buluo.app.bean.ResponseBody.UserBeanResponse;
import com.gs.buluo.app.model.MainModel;
import com.gs.buluo.app.presenter.BasePresenter;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fs on 2016/12/5.
 */
public class TextActivity extends BaseActivity implements Callback<UserBeanResponse> {

    @Override
    protected void bindView(Bundle savedInstanceState) {
        MainModel mainModel=new MainModel();
        mainModel.doLogin(null,this);
    }

    @Override
    protected int getContentLayout() {
        return 0;
    }

    @Override
    public void onResponse(Call<UserBeanResponse> call, Response<UserBeanResponse> response) {

    }

    @Override
    public void onFailure(Call<UserBeanResponse> call, Throwable t) {

    }
}
