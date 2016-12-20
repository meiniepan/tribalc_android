package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.baidu.mapapi.SDKInitializer;
import com.gs.buluo.app.R;
import com.gs.buluo.app.utils.FreImageLoader;

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(AppStartActivity.this,MainActivity.class));
                AppStartActivity.this.finish();
            }
        },2000);
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
}
