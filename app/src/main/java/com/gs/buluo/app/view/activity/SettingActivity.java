package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.AppConfigInfo;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.eventbus.DialogDismissEvent;
import com.gs.buluo.app.eventbus.DialogShowEvent;
import com.gs.buluo.app.network.MainApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.utils.AppManager;
import com.gs.buluo.app.utils.SharePreferenceManager;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.CustomAlertDialog;
import com.gs.buluo.common.UpdateEvent;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.CommonUtils;
import com.gs.buluo.common.utils.DataCleanManager;
import com.tencent.bugly.beta.Beta;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.tencent.android.tpush.XGPush4Msdk.registerPush;

/**
 * Created by hjn on 2016/11/7.
 */
public class SettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    @Bind(R.id.mine_switch)
    Switch mSwitch;
    @Bind(R.id.setting_cache_size)
    TextView tvCache;
    private UserInfoEntity info;
    private Context mCtx;
    private CustomAlertDialog customAlertDialog;
    private static final String TAG = "SettingActivity";

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx = this;
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        UserInfoDao dao = new UserInfoDao();
        info = dao.findFirst();
        setSwitch();
        mSwitch.setOnCheckedChangeListener(this);
        mSwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (info == null) {
                    startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                    return true;
                }
                return false;
            }
        });
        findViewById(R.id.setting_back).setOnClickListener(this);
        findViewById(R.id.exit).setOnClickListener(this);
        findViewById(R.id.setting_clear_cache).setOnClickListener(this);
        findViewById(R.id.setting_recall).setOnClickListener(this);
        findViewById(R.id.setting_update).setOnClickListener(this);
        findViewById(R.id.message_manager).setOnClickListener(this);

        String cacheSize = null;
        try {
            cacheSize = DataCleanManager.getTotalCacheSize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cacheSize != null)
            tvCache.setText(cacheSize);
    }

    private void setSwitch() {
        if (null == info) {
            mSwitch.setChecked(false);
        } else {
//            if (info.isNotify()){
//                mSwitch.setChecked(true);
//            }else {
//                mSwitch.setChecked(false);
//            }
        }
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
//                info.setNotify(true);
        } else {
//                info.setNotify(false);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.setting_back:
                finish();
                break;
            case R.id.setting_recall:
                intent.setClass(mCtx, FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_clear_cache:
                new CustomAlertDialog.Builder(this).setTitle("提示").setMessage("确定清除所有缓存?").setPositiveButton("清除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataCleanManager.clearAllCache(SettingActivity.this);
                        tvCache.setText("0K");
                    }
                }).setNegativeButton(mCtx.getString(R.string.cancel), null).create().show();
                break;
            case R.id.setting_update:
//                checkUpdate();
                Beta.checkUpgrade(true, false);
                break;
            case R.id.message_manager:
//                checkUpdate();
                intent.setClass(this,MessageManagerActivity.class);
                startActivity(intent);
                break;
            case R.id.exit:
                customAlertDialog = new CustomAlertDialog.Builder(this).setTitle(R.string.prompt).setMessage("您确定要退出登录吗?")
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout();
                            }
                        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                customAlertDialog.dismiss();
                            }
                        }).create();
                customAlertDialog.show();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBuglyUpgradeDialogShow(DialogShowEvent event) {
        CommonUtils.backgroundAlpha(this, 0.7f);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBuglyUpgradeDialogDismiss(DialogDismissEvent event) {
        CommonUtils.backgroundAlpha(this, 1f);
    }

    private void logout() {
        registerPush(getApplicationContext(),"", null);
        Intent intent = new Intent();
        SharePreferenceManager.getInstance(getApplicationContext()).clearValue(Constant.SIGN_IN);
        SharePreferenceManager.getInstance(getApplicationContext()).clearValue(Constant.CANCEL_UPDATE_VERSION);
        new UserInfoDao().clear();
        TribeApplication.getInstance().setUserInfo(null);
        intent.setClass(mCtx, LoginActivity.class);
        startActivity(intent);
        AppManager.getAppManager().finishActivity(MainActivity.class);
        finish();
    }

    private String versionName;

    private void checkUpdate() {
        showLoadingDialog();
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TribeRetrofit.getInstance().createApi(MainApis.class).getLastVersion(versionName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<AppConfigInfo>>() {
                    @Override
                    public void onNext(BaseResponse<AppConfigInfo> config) {
                        String[] nows = versionName.split("\\.");
                        String[] minis = config.data.minVersion.split("\\.");
                        for (int i = 0; i < minis.length; i++) {
                            if (Integer.parseInt(nows[i]) < Integer.parseInt(minis[i])) {
                                EventBus.getDefault().postSticky(new UpdateEvent(false, config.data.lastVersion, config.data.releaseNote));
                                return;
                            }
                        }

                        if (!TextUtils.equals(config.data.lastVersion.substring(0, config.data.lastVersion.lastIndexOf(".")), versionName.substring(0, config.data.lastVersion.lastIndexOf(".")))) {
                            EventBus.getDefault().postSticky(new UpdateEvent(true, config.data.lastVersion, config.data.releaseNote));
                        } else {
                            ToastUtils.ToastMessage(getCtx(), R.string.current_newest);
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
