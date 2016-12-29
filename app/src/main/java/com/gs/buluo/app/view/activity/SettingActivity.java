package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.dao.UserSensitiveDao;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.utils.DataCleanManager;
import com.gs.buluo.app.utils.SharePreferenceManager;
import com.gs.buluo.app.view.widget.CustomAlertDialog;

import butterknife.Bind;

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
    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx =this;
        UserInfoDao dao=new UserInfoDao();
        info = dao.findFirst();
        setSwitch();
        mSwitch.setOnCheckedChangeListener(this);
        mSwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (info==null){
                    startActivity(new Intent(SettingActivity.this,LoginActivity.class));
                    return true;
                }
                return false;
            }
        });
        findViewById(R.id.setting_back).setOnClickListener(this);
        findViewById(R.id.exit).setOnClickListener(this);
        findViewById(R.id.setting_clear_cache).setOnClickListener(this);
        findViewById(R.id.setting_recall).setOnClickListener(this);

        String cacheSize = null;
        try {
            cacheSize = DataCleanManager.getTotalCacheSize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cacheSize!=null)
            tvCache.setText(cacheSize);
    }

    private void setSwitch() {
        if (null==info){
            mSwitch.setChecked(false);
        }else {
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
            if (isChecked){
//                info.setNotify(true);
            }else {
//                info.setNotify(false);
            }
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()){
            case R.id.setting_back:
                finish();
                break;
            case R.id.setting_recall:
                intent.setClass(mCtx,FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_clear_cache:
                new CustomAlertDialog.Builder(this).setTitle("提示").setMessage("确定清除所有缓存?").setPositiveButton("清除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataCleanManager.clearAllCache(SettingActivity.this);
                        tvCache.setText("0K");
                    }
                }).setNegativeButton(mCtx.getString(R.string.cancel),null).create().show();
                break;
            case R.id.exit:
                SharePreferenceManager.getInstance(getApplicationContext()).clearValue(Constant.WALLET_PWD);
                new UserInfoDao().clear();
                new UserSensitiveDao().clear();
                new AddressInfoDao().clear();
                TribeApplication.getInstance().setUserInfo(null);
                intent.setClass(mCtx,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }


}
