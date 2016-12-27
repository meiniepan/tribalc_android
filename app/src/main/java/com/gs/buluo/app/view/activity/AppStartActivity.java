package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import com.gs.buluo.app.R;
import com.gs.buluo.app.utils.FreImageLoader;
import com.gs.buluo.app.utils.SharePreferenceManager;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;

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
          },1500);
      }else {
          new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                  startActivity(new Intent(AppStartActivity.this,MainActivity.class));
                  finish();
              }
          },1500);
      }
    }

    @Override
    protected void init() {
        initGallery();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_start;
    }

    private void initGallery() {
        ThemeConfig theme = new ThemeConfig.Builder()
                .build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setForceCrop(true)
                .setForceCropEdit(true)
                .setEnablePreview(true)
                .build();

        //配置 imageloader
        ImageLoader imageloader = new FreImageLoader(this);
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageloader, theme)
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(coreConfig);
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
