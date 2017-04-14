package com.gs.buluo.app.network;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.app.bean.ResponseBody.IBaseResponse;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.eventbus.LogoutEvent;
import com.gs.buluo.app.utils.AppManager;
import com.gs.buluo.app.utils.SharePreferenceManager;
import com.gs.buluo.app.view.activity.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/12/28.
 */

public abstract class TribeCallback<T extends IBaseResponse> implements Callback<BaseResponse<T>> {
    private static final String TAG = "TribeCallback";

    @Override
    public void onResponse(Call<BaseResponse<T>> call, Response<BaseResponse<T>> response) {
        if (response == null) {
            onFail(500, null);
            return;
        }
        BaseResponse responseBody = response.body();
        if (responseBody == null) {
            if (response.code() == 401) {
                logout();
            }
        } else if (responseBody.code >= 400) {
            onFail(responseBody.code, response.body());
        } else {
            onSuccess(response);
        }
    }

    private void logout() {
        EventBus.getDefault().post(new LogoutEvent());
        SharePreferenceManager.getInstance(TribeApplication.getInstance().getApplicationContext()).clearValue(Constant.WALLET_PWD);
        new AddressInfoDao().clear();
        new UserInfoDao().clear();
        TribeApplication.getInstance().setUserInfo(null);
    }

    @Override
    public void onFailure(Call<BaseResponse<T>> call, Throwable t) {
        onFail(500, null);
    }


    public abstract void onSuccess(Response<BaseResponse<T>> response);

    public abstract void onFail(int responseCode, BaseResponse<T> body);

}
