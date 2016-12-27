package com.gs.buluo.app.view.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.RequestBodyBean.OpenDoorRequestBody;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
import com.gs.buluo.app.network.OpenDoorService;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.triphone.LinphoneManager;
import com.gs.buluo.app.triphone.LinphonePreferences;
import com.gs.buluo.app.triphone.LinphoneUtils;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.RippleView;

import org.linphone.core.LinphoneAccountCreator;
import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCoreException;
import org.linphone.core.LinphoneCoreFactory;
import org.linphone.core.LinphoneCoreListenerBase;
import org.linphone.core.LinphoneProxyConfig;
import org.linphone.mediastream.Log;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gs.buluo.app.R.string.address;
import static com.gs.buluo.app.triphone.LinphonePreferences.*;

public class OpenDoorActivity extends BaseActivity implements RippleView.RippleStateListener, View.OnClickListener,LinphoneAccountCreator.LinphoneAccountCreatorListener {

    @Bind(R.id.open_door_rippleView)
    RippleView mRippleView;
    @Bind(R.id.open_door_text)
    TextView mTextView;
    @Bind(R.id.open_door_down)
    ImageView mImageView;
    @Bind(R.id.open_door_lock)
    ImageView mLockImg;
    private View mRootView;
    private View lockView;

    private LinphoneCoreListenerBase mListener;
    private LinphoneAccountCreator accountCreator;

    private Bitmap mBitmap;
    public Context mContext;
    public Handler mHandler=new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            if (msg.what==1) {
                mLockImg.setVisibility(View.INVISIBLE);
                mTextView.setVisibility(View.VISIBLE);
                mRippleView.stopRipple();
            }
        }
    };

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mRippleView.setRippleStateListener(this);
        mTextView.setOnClickListener(this);
        mImageView.setOnClickListener(this);
        mRootView = getRootView();
        mContext=this;
        lockView=findViewById(R.id.lock_view);
        LinphoneManager.createAndStart(this);
        LinphoneManager.getInstance().changeStatusToOnline();

        Intent intent = getIntent();
        byte[] bytes = intent.getByteArrayExtra(Constant.PICTURE);
        mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        mRootView.setBackground(new BitmapDrawable(mBitmap));

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        int leftMargin = CommonUtils.getScreenWidth(this)/2 - DensityUtils.dip2px(this,50);
        int topMargin =  (int)(CommonUtils.getScreenHeight(this)/4*3) - DensityUtils.dip2px(this,50);
        lp.setMargins(leftMargin,topMargin, 0, 0);
        lockView.setLayoutParams(lp);

        accountCreator = LinphoneCoreFactory.instance().createAccountCreator(LinphoneManager.getLc(), LinphonePreferences.instance().getXmlrpcUrl());
        accountCreator.setDomain(getResources().getString(R.string.default_domain));
        accountCreator.setListener(this);

        mListener = new LinphoneCoreListenerBase() {
            @Override
            public void configuringStatus(LinphoneCore lc, final LinphoneCore.RemoteProvisioningState state, String message) {
                if (state == LinphoneCore.RemoteProvisioningState.ConfiguringSuccessful) {
                    ToastUtils.ToastMessage(OpenDoorActivity.this,"success");
                } else if (state == LinphoneCore.RemoteProvisioningState.ConfiguringFailed) {
                    Toast.makeText(OpenDoorActivity.this, getString(R.string.connect_fail), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void registrationState(LinphoneCore lc, LinphoneProxyConfig cfg, LinphoneCore.RegistrationState state, String smessage) {
                        if (state == LinphoneCore.RegistrationState.RegistrationOk) {
                            if (LinphoneManager.getLc().getDefaultProxyConfig() != null) {
                                accountCreator.isAccountUsed();
                            }
                        } else if (state == LinphoneCore.RegistrationState.RegistrationFailed) {

                        } else if(!(state == LinphoneCore.RegistrationState.RegistrationProgress)) {

                        }
                }
        };
        setLinphoneCoreListener();

        saveCreatedAccount("10005","3Q@110PA",null,null,"dyc.bj.buluo-gs.com", LinphoneAddress.TransportType.LinphoneTransportUdp);
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_open_door;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_door_text:
                mRippleView.startRipple();

                LinphoneManager.getInstance().newOutgoingCall("gate_01","jack");

               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       LinphoneCore lc = LinphoneManager.getLc();
                       lc.stopDtmf();
                       char c = '#';
                       if (lc.isIncall()) {
                           lc.sendDtmf(c);
                       }
                   }
               },2000);
                break;
            case R.id.open_door_down:
                finish();
                overridePendingTransition(R.anim.around_alpha, R.anim.around_alpha_out);
                break;
        }
    }

    public void saveCreatedAccount(String username, String password, String prefix, String ha1, String domain, LinphoneAddress.TransportType transport) {
        username = LinphoneUtils.getDisplayableUsernameFromAddress(username);
        domain = LinphoneUtils.getDisplayableUsernameFromAddress(domain);

        String identity = "sip:" + username + "@" + domain;
        try {
            LinphoneAddress address = LinphoneCoreFactory.instance().createLinphoneAddress(identity);
        } catch (LinphoneCoreException e) {
            Log.e(e);
        }

        AccountBuilder builder = new AccountBuilder(LinphoneManager.getLc())
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
            Log.e(e);
        }
    }
    public void setLinphoneCoreListener() {
        LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
        if (lc != null) {
            lc.addListener(mListener);

            LinphoneProxyConfig lpc = lc.getDefaultProxyConfig();
            if (lpc != null) {
                mListener.registrationState(lc, lpc, lpc.getState(), null);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.around_alpha, R.anim.around_alpha_out);
    }

    @Override
    public void startRipple() {

    }

    @Override
    public void stopRipple() {

    }

    @Override
    public void onRippleUpdate(ValueAnimator animation) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();

        LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
        if (lc != null) {
            lc.addListener(mListener);
        }
    }

    @Override
    protected void onPause() {
        LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
        if (lc != null) {
            lc.removeListener(mListener);
        }

        super.onPause();
    }

    @Override
    public void onAccountCreatorIsAccountUsed(LinphoneAccountCreator linphoneAccountCreator, LinphoneAccountCreator.Status status) {
        if(status.equals(LinphoneAccountCreator.Status.AccountExistWithAlias)){
//            success();
        }
    }

    @Override
    public void onAccountCreatorAccountCreated(LinphoneAccountCreator linphoneAccountCreator, LinphoneAccountCreator.Status status) {

    }

    @Override
    public void onAccountCreatorAccountActivated(LinphoneAccountCreator linphoneAccountCreator, LinphoneAccountCreator.Status status) {

    }

    @Override
    public void onAccountCreatorAccountLinkedWithPhoneNumber(LinphoneAccountCreator linphoneAccountCreator, LinphoneAccountCreator.Status status) {

    }

    @Override
    public void onAccountCreatorPhoneNumberLinkActivated(LinphoneAccountCreator linphoneAccountCreator, LinphoneAccountCreator.Status status) {

    }

    @Override
    public void onAccountCreatorIsAccountActivated(LinphoneAccountCreator linphoneAccountCreator, LinphoneAccountCreator.Status status) {

    }

    @Override
    public void onAccountCreatorPhoneAccountRecovered(LinphoneAccountCreator linphoneAccountCreator, LinphoneAccountCreator.Status status) {

    }

    @Override
    public void onAccountCreatorIsAccountLinked(LinphoneAccountCreator linphoneAccountCreator, LinphoneAccountCreator.Status status) {

    }

    @Override
    public void onAccountCreatorIsPhoneNumberUsed(LinphoneAccountCreator linphoneAccountCreator, LinphoneAccountCreator.Status status) {

    }

    @Override
    public void onAccountCreatorPasswordUpdated(LinphoneAccountCreator linphoneAccountCreator, LinphoneAccountCreator.Status status) {

    }
}
