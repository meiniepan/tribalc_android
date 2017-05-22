package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.model.LatLng;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ConfigInfo;
import com.gs.buluo.app.network.MainApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.SharePreferenceManager;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import java.io.File;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/3.
 */
public class AppStartActivity extends BaseActivity {
    public DetailLocationListener myListener = new DetailLocationListener();

    @Bind(R.id.version_name)
    TextView version;
    private LocationClient mLocClient;
    private String versionName;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.transparent);
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            version.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        mLocClient.start();

//        String uid = TribeApplication.getInstance().getUserInfo()==null? null : TribeApplication.getInstance().getUserInfo().getId();
//        TribeRetrofit.getInstance().createApi(MainApis.class).getConfig(uid,versionName)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseSubscriber<BaseResponse<ConfigInfo>>(false) {
//                    @Override
//                    public void onNext(BaseResponse<ConfigInfo> response) {
//
//                    }
//                });

        beginActivity();

    }

    private void beginActivity() {
        start();
    }

    @Override
    protected void init() {
        File file = new File(Constant.DIR_PATH);
        if (!file.exists()) file.mkdirs();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_start;
    }


    public int getVersionCode() {
        PackageManager manager;

        PackageInfo info = null;

        manager = this.getPackageManager();
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public class DetailLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location != null) {
                LatLng myPos = new LatLng(location.getLatitude(),
                        location.getLongitude());

                TribeApplication.getInstance().setPosition(myPos);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocClient.stop();
    }


    private void start() {
        if (SharePreferenceManager.getInstance(this).getBooeanValue("guide" + getVersionCode())) {
            SharePreferenceManager.getInstance(this).setValue("guide" + getVersionCode(), false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(AppStartActivity.this, GuideActivity.class));
                    finish();
                }
            }, 300);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(AppStartActivity.this, MainActivity.class));
                    finish();
                }
            }, 300);
        }
    }

}
