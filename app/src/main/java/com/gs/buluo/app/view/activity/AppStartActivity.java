package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
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
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/3.
 */
public class AppStartActivity extends BaseActivity {
    public DetailLocationListener myListener = new DetailLocationListener();

    @Bind(R.id.version_name)
    TextView version;
    @Bind(R.id.app_start_bg)
    ImageView viewBg;
    @Bind(R.id.start_second)
    TextView tvSecond;
    @Bind(R.id.start_jump_area)
    View secondView;
    private LocationClient mLocClient;
    private String versionName;
    private Handler handler;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.transparent);
        handler = new Handler();
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            version.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        mLocClient.start();


        handler.postDelayed(r,2000);
        String uid = TribeApplication.getInstance().getUserInfo() == null ? null : TribeApplication.getInstance().getUserInfo().getId();
        TribeRetrofit.getInstance().createApi(MainApis.class).getConfig(uid, versionName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<ConfigInfo>>(false) {
                    @Override
                    public void onNext(BaseResponse<ConfigInfo> response) {
                        setData(response.data);
                    }
                });
    }
    Runnable r = new Runnable() {
        @Override
        public void run() {
            beginActivity();
        }
    };

    private void beginActivity() {
        if (SharePreferenceManager.getInstance(this).getBooeanValue("guide" + getVersionCode())) {
            SharePreferenceManager.getInstance(this).setValue("guide" + getVersionCode(), false);
            startActivity(new Intent(AppStartActivity.this, GuideActivity.class));
            finish();
        } else {
            startActivity(new Intent(AppStartActivity.this, MainActivity.class));
            finish();
        }
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

    public void setData(ConfigInfo data) {
        if (data.promotions.canSkip){
            secondView.setVisibility(View.VISIBLE);
            secondView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    beginActivity();
                }
            });
        }

        handler.removeCallbacks(r);
        Glide.with(this).load(data.promotions.url).into(viewBg);
        startTime = data.promotions.skipSeconds;
        startCounter();
    }

    int startTime = 1;

    private void startCounter() {
        tvSecond.setText(startTime + "");
        Subscriber<Long> subscriber = new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                beginActivity();
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Long aLong) {
                tvSecond.setText(aLong +"");
            }
        };
        Observable.interval(0, 1, TimeUnit.SECONDS).take(startTime + 1)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long time) {
                        return startTime - time;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
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

}
