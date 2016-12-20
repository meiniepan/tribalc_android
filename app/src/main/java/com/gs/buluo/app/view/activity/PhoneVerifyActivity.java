package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.LoginPresenter;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.impl.ILoginView;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/9.
 */
public class PhoneVerifyActivity extends BaseActivity implements ILoginView {
    @Bind(R.id.bind_edit_phone)
    EditText mPhone;
    private String phone;

    @Override
    protected void bindView(Bundle savedInstanceState) {

        findViewById(R.id.phone_bind_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = mPhone.getText().toString().trim();
                if (!CommonUtils.checkPhone("86",phone,PhoneVerifyActivity.this))return;
                ((LoginPresenter)mPresenter).doVerify(phone);
            }
        });
        findViewById(R.id.bind_phone_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_phone1;
    }

    @Override
    protected BasePresenter getPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(this,getString(R.string.connect_fail));
    }
    @Override
    public void loginSuccess() {
    }
    @Override
    public  void dealWithIdentify(int res) {
        switch (res){
            case 202:
                Intent intent = new Intent(PhoneVerifyActivity.this, PhoneVerifyActivity2.class);
                intent.putExtra("phone",phone);
                startActivity(intent);
                break;
            case 400:
                ToastUtils.ToastMessage(this,getString(R.string.wrong_number));
                break;
        }
    }
}
