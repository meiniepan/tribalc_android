package com.gs.buluo.app.presenter;

import android.util.Log;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.UserAddressListResponse;
import com.gs.buluo.app.bean.ResponseBody.UserBeanResponse;
import com.gs.buluo.app.bean.ResponseBody.UserSensitiveResponse;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.bean.ResponseBody.UserInfoResponse;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.dao.UserSensitiveDao;
import com.gs.buluo.app.eventbus.NameEvent;
import com.gs.buluo.app.model.MainModel;
import com.gs.buluo.app.view.impl.ILoginView;

import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/3.
 */
public class LoginPresenter extends BasePresenter<ILoginView> {
    public String TAG=  "LoginPresenter";
    private final MainModel mainModel;

    public LoginPresenter() {
        mainModel = new MainModel();
    }

    public void doLogin(Map<String, String> params) {
        mainModel.doLogin(params, new Callback<UserBeanResponse>() {
            @Override
            public void onResponse(Call<UserBeanResponse> call, Response<UserBeanResponse> response) {
                Log.d(TAG, "Retrofit Response: "+ response.body().toString());
                UserBeanResponse user = response.body();
                if (null != user && user.getCode() == 200 || null != user && user.getCode() == 201) {
                    UserInfoEntity entity = new UserInfoEntity();
                    entity.setId(user.getData().getAssigned());
                    TribeApplication.getInstance().setUserInfo(entity);

                    String assigned = user.getData().getAssigned();
                    getUserInfo(assigned);
                    getSensitiveInfo(assigned);
                    getAddressInfo(assigned);
                } else {
                    if (null == mView) return;
                    mView.showError(R.string.wrong_verify);
                }
            }

            @Override
            public void onFailure(Call<UserBeanResponse> call, Throwable t) {
                if (null == mView) return;
                mView.showError(R.string.connect_fail);
            }
        });
    }



    public void doVerify(String phone) {
        mainModel.doVerify(phone, new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                CodeResponse res = response.body();
                if (res.code==202){
                    mView.dealWithIdentify(202);
                }else {
                    mView.dealWithIdentify(400);
                }
            }

            @Override
            public void onFailure(Call<CodeResponse> call, Throwable t) {
                if (null == mView) return;
                mView.showError(R.string.connect_fail);
            }
        });
    }

    public void getUserInfo(String uid) {
        mainModel.getUserInfo(uid, new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                Log.d(TAG, "Retrofit Response: "+ response.body().toString());
                UserInfoResponse info =response.body();
                if (null==info)return;
                UserInfoEntity entity = info.getData();
                entity.setArea(entity.getProvince()+"-"+entity.getCity()+"-"+entity.getDistrict());
                TribeApplication.getInstance().setUserInfo(entity);
                UserInfoDao dao=new UserInfoDao();
                dao.saveBindingId(entity);
                EventBus.getDefault().post(new NameEvent(entity.getNickname()));
                if (isAttach()){
                    mView.loginSuccess();
                }
            }
            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                if (isAttach()){
                    mView.showError(R.string.connect_fail);
                }
            }
        });
    }
    public void getSensitiveInfo(String uid){
        mainModel.getSensitiveUserInfo(uid, new Callback<UserSensitiveResponse>() {
            @Override
            public void onResponse(Call<UserSensitiveResponse> call, Response<UserSensitiveResponse> response) {
                UserSensitiveResponse body = response.body();
                new UserSensitiveDao().saveBindingId(body.data);
            }

            @Override
            public void onFailure(Call<UserSensitiveResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
            }
        });
    }
    private void getAddressInfo(String assigned) {
        mainModel.getAddressList(assigned, new Callback<UserAddressListResponse>() {
            @Override
            public void onResponse(Call<UserAddressListResponse> call, Response<UserAddressListResponse> response) {
                List<UserAddressEntity > list=response.body().data;
                AddressInfoDao dao=new AddressInfoDao();
                for (UserAddressEntity address:list){
                    address.setUid(TribeApplication.getInstance().getUserInfo().getId());
                    dao.saveBindingId(address);
                }
            }

            @Override
            public void onFailure(Call<UserAddressListResponse> call, Throwable t) {
            }
        });
    }
}
