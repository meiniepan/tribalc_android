package com.gs.buluo.app.presenter;

import android.text.TextUtils;
import android.widget.Button;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.RequestBodyBean.LoginBody;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.UserBeanEntity;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.eventbus.SelfEvent;
import com.gs.buluo.app.model.MainModel;
import com.gs.buluo.app.network.MainApis;
import com.gs.buluo.app.network.RxApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.triphone.LinphoneManager;
import com.gs.buluo.app.triphone.LinphonePreferences;
import com.gs.buluo.app.triphone.LinphoneUtils;
import com.gs.buluo.app.view.impl.ILoginView;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import org.greenrobot.eventbus.EventBus;
import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneCoreException;
import org.linphone.core.LinphoneCoreFactory;

import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/3.
 */
public class LoginPresenter extends BasePresenter<ILoginView> {
    private final MainModel mainModel;
    private String token;

    public LoginPresenter() {
        mainModel = new MainModel();
    }

    public void doLogin(Map<String, String> params, final Button button) {
        //wbn
        setButtonFalse(button);
        LoginBody bean = new LoginBody();
        bean.phone = params.get(Constant.PHONE);
        bean.verificationCode = params.get(Constant.VERIFICATION);
        TribeRetrofit.getInstance().createApi(RxApis.class).
                doLogin(bean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<UserBeanEntity>>() {
                    @Override
                    public void onCompleted() {
                        setButtonTrue(button);
                    }

                    @Override
                    public void onFail(ApiException e) {
                        setButtonTrue(button);
                        if (isAttach())mView.dealWithIdentify(e.getCode());
                    }

                    @Override
                    public void onNext(BaseResponse<UserBeanEntity> userBeanResponse) {
                        doOnLogin(userBeanResponse.data);
                    }
                });
    }

    private void doOnLogin(UserBeanEntity user) {
        String uid = user.getAssigned();
        token = user.getToken();
        UserInfoEntity entity = new UserInfoEntity();
        entity.setId(uid);
        entity.setToken(token);
        TribeApplication.getInstance().setUserInfo(entity);
        getUserInfo(uid);
        getAddressInfo(uid);
    }

    private void setButtonFalse(Button button) {
        button.setClickable(false);
        button.setText(R.string.sign_in_ing);
    }

    private void setButtonTrue(Button button) {
        button.setText(R.string.sign_in);
        button.setClickable(true);
    }

    public void doVerify(String phone) {
        TribeRetrofit.getInstance().createApi(MainApis.class).
                doVerify(new ValueRequestBody(phone))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CodeResponse>>() {
                    @Override
                    public void onNext(BaseResponse<CodeResponse> response) {
                        if (isAttach()) mView.dealWithIdentify(response.code);
                    }
                });
    }

    public void getUserInfo(String uid) {
        TribeRetrofit.getInstance().createApi(MainApis.class).
                getUser(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<UserInfoEntity>>() {
                    @Override
                    public void onNext(BaseResponse<UserInfoEntity> response) {
                        UserInfoEntity entity = response.data;
                        entity.setToken(token);
                        if (entity.getDistrict() != null)
                            entity.setArea(entity.getProvince() + "-" + entity.getCity() + "-" + entity.getDistrict());
                        else
                            entity.setArea(entity.getProvince() + "-" + entity.getCity());

                        TribeApplication.getInstance().setUserInfo(entity);
                        UserInfoDao dao = new UserInfoDao();
                        dao.saveBindingId(entity);
                        EventBus.getDefault().post(new SelfEvent());
                        if (isAttach()) {
                            mView.loginSuccess();
                        }
                    }
                });
    }

    private void getAddressInfo(String assigned) {

        TribeRetrofit.getInstance().createApi(MainApis.class).
                getDetailAddressList(assigned)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<List<UserAddressEntity>>>() {
                    @Override
                    public void onNext(BaseResponse<List<UserAddressEntity>> response) {
                        List<UserAddressEntity> list = response.data;
                        AddressInfoDao dao = new AddressInfoDao();
                        for (UserAddressEntity address : list) {
                            address.setUid(TribeApplication.getInstance().getUserInfo().getId());
                            address.setArea(address.getProvice(), address.getCity(), address.getDistrict());
                            dao.saveBindingId(address);
                        }
                    }
                });
    }

    private void saveCreatedAccount(String username, String password, String prefix, String ha1, String domain, LinphoneAddress.TransportType transport) {
        username = LinphoneUtils.getDisplayableUsernameFromAddress(username);
        domain = LinphoneUtils.getDisplayableUsernameFromAddress(domain);

        String identity = "sip:" + username + "@" + domain;
        try {
            LinphoneAddress address = LinphoneCoreFactory.instance().createLinphoneAddress(identity);
        } catch (LinphoneCoreException e) {
            org.linphone.mediastream.Log.e(e);
        }

        LinphonePreferences.AccountBuilder builder = new LinphonePreferences.AccountBuilder(LinphoneManager.getLc())
                .setUsername(username)
                .setDomain(domain)
                .setHa1(ha1)
                .setPassword(password);

        if (prefix != null) {
            builder.setPrefix(prefix);
        }
        String forcedProxy = "";
        if (!TextUtils.isEmpty(forcedProxy)) {
            builder.setProxy(forcedProxy)
                    .setOutboundProxyEnabled(true)
                    .setAvpfRRInterval(5);
        }

        if (transport != null) {
            builder.setTransport(transport);
        }

//        if (getResources().getBoolean(R.bool.enable_push_id)) {
//            String regId = mPrefs.getPushNotificationRegistrationID();
//            String appId = getString(R.string.push_sender_id);
//            if (regId != null && mPrefs.isPushNotificationEnabled()) {
//                String contactInfos = "app-id=" + appId + ";pn-type=google;pn-tok=" + regId;
//                builder.setContactParameters(contactInfos);
//            }
//        }

        try {
            builder.saveNewAccount();
//            if(!newAccount) {
//                displayRegistrationInProgressDialog();
//            }
//            accountCreated = true;
        } catch (LinphoneCoreException e) {
            org.linphone.mediastream.Log.e(e);
        }
    }
}
