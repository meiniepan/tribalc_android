package com.gs.buluo.app.presenter;

import android.widget.Button;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.RequestBodyBean.LoginBody;
import com.gs.buluo.app.bean.RequestBodyBean.ThirdLoginRequest;
import com.gs.buluo.app.bean.RequestBodyBean.ValueBody;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.UserBeanEntity;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.eventbus.SelfEvent;
import com.gs.buluo.app.network.MainApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.SharePreferenceManager;
import com.gs.buluo.app.view.impl.ILoginView;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.TribeDateUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/3.
 */
public class LoginPresenter extends BasePresenter<ILoginView> {
    private String token;

    public void doLogin(Map<String, String> params, final Button button) {
        button.setClickable(false);
        LoginBody bean = new LoginBody();
        bean.phone = params.get(Constant.PHONE);
        bean.verificationCode = params.get(Constant.VERIFICATION);
        TribeRetrofit.getInstance().createApi(MainApis.class).
                doLogin(bean)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<BaseResponse<UserBeanEntity>, Observable<BaseResponse<UserInfoEntity>>>() {
                    @Override
                    public Observable<BaseResponse<UserInfoEntity>> call(BaseResponse<UserBeanEntity> response) {
                        UserBeanEntity data = response.data;
                        String uid = data.getAssigned();
                        token = data.getToken();
                        if (data.getActivities().signin != null) {
                            long lastTimestamp = data.getActivities().signin.lastTimestamp; //last time of signing up
                            SharePreferenceManager.getInstance(TribeApplication.getInstance().getApplicationContext()).
                                    setValue(Constant.SIGN_IN, TribeDateUtils.dateFormat5(new Date(lastTimestamp)));
                        }

                        UserInfoEntity entity = new UserInfoEntity();
                        entity.setId(uid);
                        entity.setToken(token);
                        TribeApplication.getInstance().setUserInfo(entity);
                        getAddressInfo(uid);
                        return TribeRetrofit.getInstance().createApi(MainApis.class).
                                getUser(uid);
                    }
                })
                .doOnNext(new Action1<BaseResponse<UserInfoEntity>>() {
                    @Override
                    public void call(BaseResponse<UserInfoEntity> response) {
                        saveUserInfo(response);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<UserInfoEntity>>() {
                    @Override
                    public void onNext(BaseResponse<UserInfoEntity> userBeanResponse) {
                        mView.loginSuccess();
                    }

                    @Override
                    public void onFail(ApiException e) {
                        mView.dealWithIdentify(e.getCode(), e.getDisplayMessage());
                        button.setClickable(true);
                    }

                });
    }

    private void saveUserInfo(BaseResponse<UserInfoEntity> response) {
        UserInfoEntity entity = response.data;
        entity.setToken(token);
        entity.setRole(response.data.roles);
        if (entity.getDistrict() != null)
            entity.setArea(entity.getProvince() + "-" + entity.getCity() + "-" + entity.getDistrict());
        else
            entity.setArea(entity.getProvince() + "-" + entity.getCity());

        TribeApplication.getInstance().setUserInfo(entity);
        UserInfoDao dao = new UserInfoDao();
        dao.saveBindingId(entity);
        EventBus.getDefault().post(new SelfEvent());
    }

    public void doVerify(String phone) {
        TribeRetrofit.getInstance().createApi(MainApis.class).
                doVerify(new ValueBody(phone))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CodeResponse>>(false) {
                    @Override
                    public void onNext(BaseResponse<CodeResponse> response) {
                        if (isAttach()) mView.dealWithIdentify(response.code, "");
                    }

                    @Override
                    public void onFail(ApiException e) {
                        mView.dealWithIdentify(e.getCode(), e.getDisplayMessage());
                    }
                });
    }

    private AddressInfoDao dao = new AddressInfoDao();

    private void getAddressInfo(String assigned) {
        TribeRetrofit.getInstance().createApi(MainApis.class).
                getDetailAddressList(assigned)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<BaseResponse<List<UserAddressEntity>>, Observable<UserAddressEntity>>() {
                    @Override
                    public Observable<UserAddressEntity> call(BaseResponse<List<UserAddressEntity>> listBaseResponse) {
                        return Observable.from(listBaseResponse.data);
                    }
                })
                .subscribe(new BaseSubscriber<UserAddressEntity>() {
                    @Override
                    public void onNext(UserAddressEntity address) {
                        address.setUid(TribeApplication.getInstance().getUserInfo().getId());
                        address.setArea(address.getProvice(), address.getCity(), address.getDistrict());
                        dao.saveBindingId(address);
                    }
                });
    }

    public void doThirdLogin(HashMap<String, String> params, final String wxCode) {
        LoginBody bean = new LoginBody();
        bean.phone = params.get(Constant.PHONE);
        bean.verificationCode = params.get(Constant.VERIFICATION);
        TribeRetrofit.getInstance().createApi(MainApis.class).
                doLogin(bean)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<BaseResponse<UserBeanEntity>, Observable<BaseResponse<UserInfoEntity>>>() {
                    @Override
                    public Observable<BaseResponse<UserInfoEntity>> call(BaseResponse<UserBeanEntity> response) {
                        UserBeanEntity data = response.data;
                        String uid = data.getAssigned();
                        token = data.getToken();
                        if (data.getActivities().signin != null) {
                            long lastTimestamp = data.getActivities().signin.lastTimestamp; //last time of signing up
                            SharePreferenceManager.getInstance(TribeApplication.getInstance().getApplicationContext()).
                                    setValue(Constant.SIGN_IN, TribeDateUtils.dateFormat5(new Date(lastTimestamp)));
                        }

                        UserInfoEntity entity = new UserInfoEntity();
                        entity.setId(uid);
                        entity.setToken(token);
                        TribeApplication.getInstance().setUserInfo(entity);
                        getAddressInfo(uid);
                        bindThird(uid, wxCode);
                        return TribeRetrofit.getInstance().createApi(MainApis.class).
                                getUser(uid);
                    }
                })
                .doOnNext(new Action1<BaseResponse<UserInfoEntity>>() {
                    @Override
                    public void call(BaseResponse<UserInfoEntity> response) {
                        saveUserInfo(response);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<UserInfoEntity>>() {
                    @Override
                    public void onNext(BaseResponse<UserInfoEntity> userBeanResponse) {
                        mView.loginSuccess();
                    }

                    @Override
                    public void onFail(ApiException e) {
                        mView.dealWithIdentify(e.getCode(), e.getDisplayMessage());
                    }

                });
    }

    private void bindThird(String uid, String wxCode) {
        ThirdLoginRequest request = new ThirdLoginRequest();
        request.memberType = "PERSON";
        request.memberId = uid;
        request.code = wxCode;
        TribeRetrofit.getInstance().createApi(MainApis.class).bindThirdLogin(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                    }
                });
    }
}
