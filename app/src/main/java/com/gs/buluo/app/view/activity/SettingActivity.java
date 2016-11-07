package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.UserInfo;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.utils.ToastUtils;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/7.
 */
public class SettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    @Bind(R.id.mine_switch)
    Switch mSwitch;
    private UserInfo info;

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
    }

    private void setSwitch() {
        if (null==info){
            mSwitch.setChecked(false);
        }else {
            if (info.isNotify()){
                mSwitch.setChecked(true);
            }else {
                mSwitch.setChecked(false);
            }
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
                info.setNotify(true);
            }else {
                info.setNotify(false);
            }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_back:
                finish();
                break;
        }
    }
}
