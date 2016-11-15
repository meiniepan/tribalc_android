package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.dao.AddressInfoDao;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.dao.UserSensitiveDao;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.utils.ToastUtils;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/7.
 */
public class SettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    @Bind(R.id.mine_switch)
    Switch mSwitch;
    private UserInfoEntity info;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        UserInfoDao dao=new UserInfoDao();
        info = dao.findFirst();
        setSwitch();
        mSwitch.setOnCheckedChangeListener(this);
        mSwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (info==null){
                    ToastUtils.ToastMessage(SettingActivity.this,"没登录呢");
                    return true;
                }
                return false;
            }
        });
        findViewById(R.id.setting_back).setOnClickListener(this);
        findViewById(R.id.exit).setOnClickListener(this);
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
        switch (v.getId()){
            case R.id.setting_back:
                finish();
                break;
            case R.id.exit:
                new UserInfoDao().clear();
                new UserSensitiveDao().clear();
                TribeApplication.getInstance().setUserInfo(null);
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
//                intent.putExtra(Constant.LOGIN,false);
                startActivity(intent);
                finish();
                break;
        }
    }
}
