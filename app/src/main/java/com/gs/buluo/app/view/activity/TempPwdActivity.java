package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;

import butterknife.Bind;

/**
 * Created by hjn on 2017/6/30.
 */

public class TempPwdActivity extends BaseActivity {
    @Bind(R.id.temp_pwd)
    TextView tempPwd;
    @Bind(R.id.temp_pwd_name)
    TextView tvName;
    @Bind(R.id.temp_pwd_number)
    TextView tvNum;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        String name = getIntent().getStringExtra(Constant.DEPARTMENT_NAME);
        String number = getIntent().getStringExtra(Constant.DEPARTMENT_NUMBER);
        tvName.setText(name);
        tvNum.setText(number);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_temp_pwd;
    }
}
