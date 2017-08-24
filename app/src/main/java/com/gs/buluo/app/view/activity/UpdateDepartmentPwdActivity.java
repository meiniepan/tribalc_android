package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.LockPwdRequestBody;
import com.gs.buluo.app.network.DepartmentApi;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    private String sourceId;
    private String sn;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String name = intent.getStringExtra(Constant.DEPARTMENT_NAME);
        String number = intent.getStringExtra(Constant.DEPARTMENT_NUMBER);
        sourceId = intent.getStringExtra(Constant.DEPARTMENT_ID);
        sn = intent.getStringExtra(Constant.DEPARTMENT_SN);
        tvName.setText(name);
        tvNum.setText(number);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_department_update_pwd;
    }

    public void confirmUpdate(View view) {
        if (etPwd.length() < 6) {
            ToastUtils.ToastMessage(getCtx(), "密码长度不能小于6位");
            return;
        }
        String pwd = etPwd.getText().toString().trim();
        if (TextUtils.equals(pwd, etPwdAgain.getText().toString().trim())) {
            updatePwd(pwd);
        } else {
            ToastUtils.ToastMessage(getCtx(), "密码两次输入不一致");
        }
    }

    private void updatePwd(String pwd) {
        showLoadingDialog();
        LockPwdRequestBody body = new LockPwdRequestBody();
        body.sn = sn;
        body.password = pwd;
        TribeRetrofit.getInstance().createApi(DepartmentApi.class).updateLockPwd(TribeApplication.getInstance().getUserInfo().getId(), sourceId, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        ToastUtils.ToastMessage(getCtx(), R.string.update_success);
                        finish();
                    }

                    @Override
                    public void onFail(ApiException e) {
                        ToastUtils.ToastMessage(getCtx(), "修改错误，错误码：" + e.getCode());
                    }
                });
    }
}
