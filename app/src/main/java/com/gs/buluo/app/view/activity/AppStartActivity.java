package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.baidu.mapapi.SDKInitializer;
import com.gs.buluo.app.R;
import com.gs.buluo.app.presenter.BasePresenter;

/**
 * Created by hjn on 2016/11/3.
 */
public class AppStartActivity extends BaseActivity{
    @Override
    protected void bindView(Bundle savedInstanceState) {
        SDKInitializer.initialize(getApplicationContext());  //map initialize
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
    protected int getContentLayout() {
        return R.layout.activity_start;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }
}
