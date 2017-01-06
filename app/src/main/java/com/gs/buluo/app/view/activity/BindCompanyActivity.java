package com.gs.buluo.app.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.ResponseCode;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.CompanyPlate;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.SipBean;
import com.gs.buluo.app.bean.UserSensitiveEntity;
import com.gs.buluo.app.dao.UserSensitiveDao;
import com.gs.buluo.app.model.MainModel;
import com.gs.buluo.app.network.CompanyService;
import com.gs.buluo.app.network.TribeCallback;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.triphone.LinphoneManager;
import com.gs.buluo.app.triphone.LinphonePreferences;
import com.gs.buluo.app.triphone.LinphoneUtils;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneCoreException;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BindCompanyActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.tv_company_name)
    TextView mCompanyName;
    @Bind(R.id.et_user_name)
    EditText mUsername;
    @Bind(R.id.et_part_name)
    EditText mPartName;
    @Bind(R.id.et_position_name)
    EditText mPositionName;
    @Bind(R.id.et_work_number)
    EditText mWorkNumber;
    private CompanyPlate mCompanyPlate;
    private Context mContext;
    private UserSensitiveEntity sensitiveEntity;
    private UserSensitiveDao dao;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.bind_company).setOnClickListener(this);
        findViewById(R.id.bind_company_back).setOnClickListener(this);
        mCompanyName.setOnClickListener(this);
        EventBus.getDefault().register(this);
        mContext = this;
        checkIsVerify();
    }

    private void checkIsVerify() {
        dao = new UserSensitiveDao();
        sensitiveEntity = dao.findFirst();
        String name = sensitiveEntity.getName();
        if (TextUtils.isEmpty(name)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("您好").setMessage("请先进行个人实名认证")
                    .setPositiveButton("去认证", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            startActivity(new Intent(mContext, VerifyActivity.class));
                        }
                    });
            builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setCancelable(false);
            builder.create().show();
        } else {
            mUsername.setText(name);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveCompanyPanel(CompanyPlate companyPlate) {
        mCompanyPlate = companyPlate;
        mCompanyName.setText(companyPlate.companyName);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bind_company;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bind_company:
                if (mCompanyPlate == null) {
                    ToastUtils.ToastMessage(mContext, "请选择公司");
                } else {
                    String companyName = mCompanyName.getText().toString().trim();
                    if (!TextUtils.isEmpty(companyName)) {
                        bindCompany(mCompanyPlate.id);
                    }
                }
                break;
            case R.id.bind_company_back:
                finish();
                break;
            case R.id.tv_company_name:
                startActivity(new Intent(this, PickCommunityActivity.class));
                break;
        }
    }

    public void bindCompany(String id) {
        showLoadingDialog();
        TribeRetrofit.getInstance().createApi(CompanyService.class).bindCompany(TribeApplication.getInstance().getUserInfo().getId(),
                new ValueRequestBody(id)).enqueue(new TribeCallback<CodeResponse>() {
            @Override
            public void onSuccess(Response<BaseCodeResponse<CodeResponse>> response) {
                dismissDialog();
                ToastUtils.ToastMessage(mContext, "绑定成功");
                sensitiveEntity.setCompanyID(mCompanyPlate.id);
                sensitiveEntity.setCompanyName(mCompanyPlate.companyName);
                dao.update(sensitiveEntity);
                getSipInfo();
                finish();
            }

            @Override
            public void onFail(int responseCode, BaseCodeResponse<CodeResponse> body) {
                if (responseCode== ResponseCode.WRONG_PARAMETER){
                    ToastUtils.ToastMessage(mContext,"公司无此员工信息");
                }else {
                    ToastUtils.ToastMessage(mContext,R.string.connect_fail);
                }
                dismissDialog();
            }
        });
    }


    public void getSipInfo() {
            new MainModel().getSensitiveUserInfo(TribeApplication.getInstance().getUserInfo().getId(), new Callback<BaseCodeResponse<UserSensitiveEntity>>() {
                @Override
                public void onResponse(Call<BaseCodeResponse<UserSensitiveEntity>> call, Response<BaseCodeResponse<UserSensitiveEntity>> response) {
                    UserSensitiveEntity data = response.body().data;
                    data.setSipJson();
                    if (!CommonUtils.isLibc64()){
                        SipBean sip = data.getSip();
                        saveCreatedAccount(sip.user,sip.password,null,null,sip.domain, LinphoneAddress.TransportType.LinphoneTransportUdp);
                    }
                    data.setMid(sensitiveEntity.getMid());
                    new UserSensitiveDao().update(data);
                }

                @Override
                public void onFailure(Call<BaseCodeResponse<UserSensitiveEntity>> call, Throwable t) {
                    ToastUtils.ToastMessage(mContext,R.string.connect_fail);
                }
            });
        }
    public void saveCreatedAccount(String username, String password, String prefix, String ha1, String domain, LinphoneAddress.TransportType transport) {
        username = LinphoneUtils.getDisplayableUsernameFromAddress(username);
        domain = LinphoneUtils.getDisplayableUsernameFromAddress(domain);

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
