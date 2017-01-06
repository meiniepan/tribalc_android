package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.presenter.LoginPresenter;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.view.impl.ILoginView;
import com.gs.buluo.app.utils.ToastUtils;

import java.util.HashMap;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/3.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener ,ILoginView {
    @Bind(R.id.login_username)
    EditText et_phone;
    @Bind(R.id.login_verify)
    EditText et_verify;
    @Bind(R.id.login_send_verify)
    Button reg_send;
    private HashMap<String, String> params;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.login_back).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.login_send_verify).setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginPresenter getPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void onClick(View v) {
        String phone = et_phone.getText().toString().trim();
        switch (v.getId()){
            case R.id.login_back:
                finish();
                break;
            case R.id.login_send_verify:
                if (!CommonUtils.checkPhone("86",phone,this))return;
                ((LoginPresenter)mPresenter).doVerify(phone);
                break;
            case R.id.login:
                if (!CommonUtils.checkPhone("86",phone,this))return;
                params = new HashMap<>();
                params.put(Constant.PHONE, phone);
                params.put(Constant.VERIFICATION,et_verify.getText().toString().trim());
                ((LoginPresenter)mPresenter).doLogin(params);
                break;
        }
    }
    @Override
    public  void dealWithIdentify(int res) {
        switch (res){
            case 202:
                reg_send.setText("60s");
                new CountDownTimer(60000,1000){
                    @Override
                    public void onTick(long millisUntilFinished) {
                        reg_send.setClickable(false);
                        reg_send.setText(millisUntilFinished/1000+"秒");
                    }
                    @Override
                    public void onFinish() {
                        reg_send.setText("获取验证码");
                        reg_send.setClickable(true);
                    }
                }.start();
                break;
            case 400:
                ToastUtils.ToastMessage(this,getString(R.string.wrong_number));
                break;
        }
    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(this,res);
    }

    @Override
    public void loginSuccess() {
        finish();
    }
}
