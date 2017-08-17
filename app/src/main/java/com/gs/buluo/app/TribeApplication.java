package com.gs.buluo.app;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.view.View;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.eventbus.DialogDismissEvent;
import com.gs.buluo.app.eventbus.DialogShowEvent;
import com.gs.buluo.app.view.activity.MainActivity;
import com.gs.buluo.app.view.activity.SettingActivity;
import com.gs.buluo.common.BaseApplication;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.ui.UILifecycleListener;

import org.greenrobot.eventbus.EventBus;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

/**
 * Created by hjn on 2016/11/1.
 */
public class TribeApplication extends BaseApplication {
    private static final String TAG = "TribeApplication";
    private static TribeApplication instance;
    private DbManager.DaoConfig daoConfig;
    private UserInfoEntity user;
    private LatLng position;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initDb();
        initCrash();
        initBuglyUpgrade();
        initPush();
        Fresco.initialize(this);
        SDKInitializer.initialize(this);  //map initialize
    }

    public void initPush() {
        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                Log.e("TPush", "注册成功，设备token为：" + data);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.e("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });
    }

    private void initCrash() {
//        if (Constant.Base.BASE_URL.contains("dev"))return;
        //CrashReport.initCrashReport(getApplicationContext(), "29add4efd5", Constant.Base.BASE_URL.contains("dev"));
        //bug和应用升级统一初始化方法  已经接入Bugly用户改用上面的初始化方法,不影响原有的crash上报功能; init方法会自动检测更新，不需要再手动调用Beta.checkUpgrade(), 如需增加自动检查时机可以使用Beta.checkUpgrade(false,false);

//        UserInfoEntity userInfo = TribeApplication.getInstance().getUserInfo();
//        CrashReport.putUserData(this, "userId", userInfo == null ? "un login" : userInfo.getId());
//        CrashReport.putUserData(this, "phone", userInfo == null ? "un login" : userInfo.getPhone());
    }

    private void initBuglyUpgrade() {
        //        if (Constant.Base.BASE_URL.contains("dev"))return;
        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog;
        Beta.canShowUpgradeActs.add(MainActivity.class);
        Beta.canShowUpgradeActs.add(SettingActivity.class);
        Beta.upgradeDialogLifecycleListener = new UILifecycleListener<UpgradeInfo>() {
            @Override
            public void onCreate(Context context, View view, UpgradeInfo upgradeInfo) {

            }

            @Override
            public void onStart(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onStart");
            }

            @Override
            public void onResume(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onResume");
                EventBus.getDefault().post(new DialogShowEvent());
            }

            @Override
            public void onPause(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onPause");
            }

            @Override
            public void onStop(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onStop");
                EventBus.getDefault().post(new DialogDismissEvent());
            }

            @Override
            public void onDestroy(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onDestory");
            }
        };
        Bugly.init(getApplicationContext(), "29add4efd5", false);
    }

    private void initDb() {
        x.Ext.init(this);//X utils初始化
        //        x.Ext.setDebug(BuildConfig.DEBUG);
        daoConfig = new DbManager.DaoConfig()
                .setDbName("tribe")
                .setDbVersion(2)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        db.getDatabase().enableWriteAheadLogging(); //WAL
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() { //更新数据库
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        if (oldVersion == 1) {
                            update1To2(db);
                        }
                    }
                });
    }

    private void update1To2(DbManager db) {
        try {
            db.addColumn(UserInfoEntity.class, "role");
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    public static synchronized TribeApplication getInstance() {
        return instance;
    }

    @Override
    public void onInitialize() {

    }

    @Override
    public String getFilePath() {
        return Constant.DIR_PATH;
    }

    public DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }

    public void setUserInfo(UserInfoEntity info) {
        user = info;
    }

    public UserInfoEntity getUserInfo() {
        if (user == null) {
            user = new UserInfoDao().findFirst();
        }
        return user;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public LatLng getPosition() {
        return position;
    }

    private boolean bf_recharge;
    private boolean bf_withdraw;

    public boolean isBf_recharge() {
        return bf_recharge;
    }

    public void setBf_recharge(boolean bf_recharge) {
        this.bf_recharge = bf_recharge;
    }

    public boolean isBf_withdraw() {
        return bf_withdraw;
    }

    public void setBf_withdraw(boolean bf_withdraw) {
        this.bf_withdraw = bf_withdraw;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
