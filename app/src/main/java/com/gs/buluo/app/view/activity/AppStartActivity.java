package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.triphone.LinphoneManager;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.SharePreferenceManager;

/**
 * Created by hjn on 2016/11/3.
 */
public class AppStartActivity extends BaseActivity{
    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.transparent);

        beginActivity();
    }

    private void beginActivity() {
      if (SharePreferenceManager.getInstance(this).getBooeanValue("guide"+getVersionCode())){
          SharePreferenceManager.getInstance(this).setValue("guide"+getVersionCode(), false);
          new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                  startActivity(new Intent(AppStartActivity.this, GuideActivity.class));
                  finish();
              }
          },1000);
      }else {
          new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                  startActivity(new Intent(AppStartActivity.this,MainActivity.class));
                  finish();
              }
          },1000);
      }
    }

    @Override
    protected void init() {
        if (!CommonUtils.isLibc64()){
            LinphoneManager.createAndStart(TribeApplication.getInstance().getApplicationContext());
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_start;
    }


    public int getVersionCode(){
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
}
