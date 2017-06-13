package com.gs.buluo.app;

import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.common.BaseApplication;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import org.greenrobot.eventbus.EventBus;
import org.xutils.DbManager;
import org.xutils.x;

/**
 * Created by hjn on 2016/11/1.
 */
public class TribeApplication extends BaseApplication {
    private static TribeApplication instance;
    private DbManager.DaoConfig daoConfig;
    private UserInfoEntity user;
    private LatLng position;

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);  //map initialize
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
        instance=this;
        x.Ext.init(this);//X utils初始化
        Fresco.initialize(this);
//        x.Ext.setDebug(BuildConfig.DEBUG);
        initDb();

        EventBus.getDefault();
    }

    private void initDb() {
        daoConfig=new DbManager.DaoConfig()
                .setDbName("tribe")
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        db.getDatabase().enableWriteAheadLogging(); //WAL
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() { //更新数据库
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    }
                });
    }

    public static synchronized TribeApplication getInstance(){
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

    public void setUserInfo(UserInfoEntity info){
        user = info;
    }

    public UserInfoEntity getUserInfo(){
        if (user==null){
            user = new UserInfoDao().findFirst();
        }
        return user;
    }

    public void setPosition(LatLng positon) {
        this.position = positon;
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


}
