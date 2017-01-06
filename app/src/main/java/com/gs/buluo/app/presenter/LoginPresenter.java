package com.gs.buluo.app.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.ResponseBody.UserAddressListResponse;
import com.gs.buluo.app.bean.ResponseBody.UserBeanResponse;
import com.gs.buluo.app.bean.SipBean;
import com.gs.buluo.app.bean.UserAddressEntity;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.bean.ResponseBody.UserInfoResponse;
import com.gs.buluo.app.bean.UserSensitiveEntity;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.dao.UserSensitiveDao;
import com.gs.buluo.app.eventbus.SelfEvent;
import com.gs.buluo.app.model.MainModel;
import com.gs.buluo.app.triphone.LinphoneManager;
import com.gs.buluo.app.triphone.LinphonePreferences;
import com.gs.buluo.app.triphone.LinphoneUtils;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.view.impl.ILoginView;

import org.greenrobot.eventbus.EventBus;
import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneCoreException;
import org.linphone.core.LinphoneCoreFactory;

import java.util.List;
import java.util.Map;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/3.
 */
public class LoginPresenter extends BasePresenter<ILoginView> {
    private final MainModel mainModel;
    private String token;

    public LoginPresenter() {
        mainModel = new MainModel();
    }

    public void doLogin(Map<String, String> params) {
        mainModel.doLogin(params, new Callback<UserBeanResponse>() {
            @Override
            public void onResponse(Call<UserBeanResponse> call, Response<UserBeanResponse> response) {
                UserBeanResponse user = response.body();
                if (null != user && user.getCode() == 200 || null != user && user.getCode() == 201) {
                    Log.e("Login Result: userId ", "Retrofit Response: "+ response.body().getData().getAssigned());
                    String uid = user.getData().getAssigned();
                    token = response.body().getData().getToken();
                    getUserInfo(uid);
                    getSensitiveInfo(uid);
                    getAddressInfo(uid);
                } else if (user!=null&&user.getCode()==401){
                    if (isAttach()) mView.showError(R.string.wrong_verify);
                }
            }

            @Override
            public void onFailure(Call<UserBeanResponse> call, Throwable t) {
                if (isAttach())mView.showError(R.string.connect_fail);
            }
        });
    }



    public void doVerify(String phone) {
        mainModel.doVerify(phone, new Callback<BaseCodeResponse<CodeResponse>>() {
            @Override
            public void onResponse(Call<BaseCodeResponse<CodeResponse>> call, Response<BaseCodeResponse<CodeResponse>> response) {
                if (response.body()!=null){
                    BaseCodeResponse res = response.body();
                    if (res.code==202){
                        mView.dealWithIdentify(202);
                    }else {
                        mView.dealWithIdentify(400);
                    }
                }else {
                    if (null == mView) return;
                    mView.showError(R.string.connect_fail);
                }

            }

            @Override
            public void onFailure(Call<BaseCodeResponse<CodeResponse>> call, Throwable t) {
                if (null == mView) return;
                mView.showError(R.string.connect_fail);
            }
        });
    }

    public void getUserInfo(String uid) {
        mainModel.getUserInfo(uid, new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                UserInfoResponse info =response.body();
                if (null==info){
                    mView.showError(R.string.connect_fail);
                    return;
                }
                UserInfoEntity entity = info.getData();
                entity.setToken(token);
                if (entity.getDistrict()!=null)
                    entity.setArea(entity.getProvince()+"-"+entity.getCity()+"-"+entity.getDistrict());
                else
                    entity.setArea(entity.getProvince()+"-"+entity.getCity());

                TribeApplication.getInstance().setUserInfo(entity);
                UserInfoDao dao=new UserInfoDao();
                dao.saveBindingId(entity);
                EventBus.getDefault().post(new SelfEvent());
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
        mainModel.getSensitiveUserInfo(uid, new Callback<BaseCodeResponse<UserSensitiveEntity>>() {
            @Override
            public void onResponse(Call<BaseCodeResponse<UserSensitiveEntity>> call, Response<BaseCodeResponse<UserSensitiveEntity>> response) {
                UserSensitiveEntity data = response.body().data;
                data.setSipJson();
                if (!CommonUtils.isLibc64()){
                    SipBean sip = data.getSip();
                    saveCreatedAccount(sip.user,sip.password,null,null,sip.domain, LinphoneAddress.TransportType.LinphoneTransportUdp);
                }
                new UserSensitiveDao().saveBindingId(data);
            }

            @Override
            public void onFailure(Call<BaseCodeResponse<UserSensitiveEntity>> call, Throwable t) {
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
                    address.setArea(address.getProvice(),address.getCity(),address.getDistrict());
                    dao.saveBindingId(address);
                }
            }

            @Override
            public void onFailure(Call<UserAddressListResponse> call, Throwable t) {
                mView.showError(R.string.connect_fail);
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

        if(prefix != null){
            builder.setPrefix(prefix);
        }
        String forcedProxy = "";
        if (!TextUtils.isEmpty(forcedProxy)) {
            builder.setProxy(forcedProxy)
                    .setOutboundProxyEnabled(true)
                    .setAvpfRRInterval(5);
        }

        if(transport != null) {
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
