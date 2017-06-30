package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.utils.ToastUtils;

import butterknife.Bind;

/**
 * Created by hjn on 2017/6/30.
 */

public class UpdateDepartmentPwdActivity extends BaseActivity {
    @Bind(R.id.department_pwd_name)
    TextView tvName;
    @Bind(R.id.department_pwd_number)
    TextView tvNum;
    @Bind(R.id.department_input_pwd)
    EditText etPwd;
    @Bind(R.id.department_input_pwd_again)
    EditText etPwdAgain;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        String name = getIntent().getStringExtra(Constant.DEPARTMENT_NAME);
        String number = getIntent().getStringExtra(Constant.DEPARTMENT_NUMBER);
        tvName.setText(name);
        tvNum.setText(number);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_department_update_pwd;
    }

    public void confirmUpdate(View view) {
        if (etPwd.length()<6){
            ToastUtils.ToastMessage(getCtx(),"密码长度不能小于6位");
            return;
        }
        if (TextUtils.equals(etPwd.getText().toString().trim(), etPwdAgain.getText().toString().trim())) {
            //TODO  to update
            ToastUtils.ToastMessage(getCtx(), "click");
        }
    }
}
