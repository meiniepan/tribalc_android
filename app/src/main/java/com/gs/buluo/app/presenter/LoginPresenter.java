package com.gs.buluo.app.presenter;

import android.util.Log;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.UserBeanResponse;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.bean.UserInfoResponse;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.model.LoginModel;
import com.gs.buluo.app.view.impl.ILoginView;

import org.xutils.common.Callback.CommonCallback;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/3.
 */
public class LoginPresenter extends BasePresenter<ILoginView> {
    private final LoginModel loginModel;

    public LoginPresenter() {
        loginModel = new LoginModel();
    }

    public void doLogin(Map<String, String> params) {
        loginModel.doLogin(params, new Callback<UserBeanResponse>() {
            @Override
            public void onResponse(Call<UserBeanResponse> call, Response<UserBeanResponse> response) {
                UserBeanResponse user = response.body();
                if (null != user && user.getCode() == 200 || null != user && user.getCode() == 201) {
                    UserInfoResponse userInfo = new UserInfoResponse();
                    UserInfoEntity entity = new UserInfoEntity();
                    userInfo.setData(entity);
                    userInfo.setUserJson(user.getData());
                    entity.setId(user.getData().getAssigned());

                    TribeApplication.getInstance().setUserInfo(userInfo.getData());
                    getUserInfo(user.getData().getAssigned());
//                    RequestParams params = new RequestParams(Constant.BASE_URL + "persons/"+ user.getData().getAssigned());
//                    params.setHeader("Content-Type", "application/json");
//                    params.setHeader("Accept", "application/json");
//                    params.setAsJsonContent(true);
//                    params.addParameter("id", user.getData().getAssigned());
//                    x.http().get(params, new CommonCallback<String>() {
//                        @Override
//                        public void onSuccess(String result) {
//                            Log.e("aaa", "onSuccess: " + result);
//                        }
//
//                        @Override
//                        public void onError(Throwable ex, boolean isOnCallback) {
//                            Log.e("aaa", "onError: " + ex.toString());
//                        }
//
//                        @Override
//                        public void onCancelled(CancelledException cex) {
//
//                        }
//
//                        @Override
//                        public void onFinished() {
//
//                        }
//                    });
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
        loginModel.doVerify(phone, new Callback<UserBeanResponse>() {
            @Override
            public void onResponse(Call<UserBeanResponse> call, Response<UserBeanResponse> response) {
                UserBeanResponse user = response.body();
            }

            @Override
            public void onFailure(Call<UserBeanResponse> call, Throwable t) {
                if (null == mView) return;
                mView.showError(R.string.connect_fail);
            }
        });
    }

    public void getUserInfo(String uid) {
        loginModel.getUserInfo(uid, new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                UserInfoResponse info =response.body();
                if (null==info)return;

                UserInfoEntity entity = info.getData();
                entity.setNickname("哈哈哈哈");

                TribeApplication.getInstance().setUserInfo(entity);
                UserInfoDao dao=new UserInfoDao();
                dao.saveBindingId(entity);
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

    public void  updateUser(String key,String value){
        loginModel.updateUser(TribeApplication.getInstance().getUserInfo().getId(), key, value, new CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("cccccc",result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("cccccc",ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
