package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.eventbus.DialogDismissEvent;
import com.gs.buluo.app.eventbus.DialogShowEvent;
import com.gs.buluo.app.presenter.LoginPresenter;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.WXUtils;
import com.gs.buluo.app.view.impl.ILoginView;
import com.gs.buluo.common.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by hjn on 2016/11/3.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, ILoginView {
    @BindView(R.id.login_username)
    EditText et_phone;
    @BindView(R.id.login_verify)
    EditText et_verify;
    @BindView(R.id.login_send_verify)
    Button reg_send;
    //wbn
    @BindView(R.id.login)
    Button bt_login;

    private HashMap<String, String> params;
    private CountDownTimer countDownTimer;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.login_back).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.login_send_verify).setOnClickListener(this);
        findViewById(R.id.login_protocol).setOnClickListener(this);
        findViewById(R.id.wx_login).setOnClickListener(this);
        if (getIntent().getBooleanExtra(Constant.RE_LOGIN, false)) { //登录冲突
            ToastUtils.ToastMessage(getCtx(), getString(R.string.login_again));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBuglyUpgradeDialogShow(DialogShowEvent event) {
        com.gs.buluo.common.utils.CommonUtils.backgroundAlpha(this, 0.7f);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBuglyUpgradeDialogDismiss(DialogDismissEvent event) {
        com.gs.buluo.common.utils.CommonUtils.backgroundAlpha(this, 1f);
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
        switch (v.getId()) {
            case R.id.login_back:
                finish();
                break;
            case R.id.login_send_verify:
                if (!CommonUtils.checkPhone("86", phone, this)) return;
                startCounter();
                ((LoginPresenter) mPresenter).doVerify(phone);
                et_verify.requestFocus();
                break;
            case R.id.login:
                if (!CommonUtils.checkPhone("86", phone, this)) return;
                showLoadingDialog();
                params = new HashMap<>();
                params.put(Constant.PHONE, phone);
                params.put(Constant.VERIFICATION, et_verify.getText().toString().trim());
                ((LoginPresenter) mPresenter).doLogin(params, bt_login);
                break;
            case R.id.login_protocol:
                startActivity(new Intent(getCtx(), WebActivity.class));
                break;
            case R.id.wx_login:
                WXUtils.getInstance().doLogin();
                break;
        }
    }

    @Override
    public void dealWithIdentify(int res, String displayMessage) {
        switch (res) {
            case 202:
                break;
            case 400:
            case 401:
            case 504:
                ToastUtils.ToastMessage(this, displayMessage);
                reg_send.setText("获取验证码");
                reg_send.setClickable(true);
                break;
            default:
                if (!TextUtils.isEmpty(displayMessage))
                    ToastUtils.ToastMessage(getCtx(), displayMessage);
                break;
        }
    }

    public void startCounter() {
        reg_send.setText("60s");
        reg_send.setClickable(false);
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                reg_send.setText(millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                reg_send.setText("获取验证码");
                reg_send.setClickable(true);
            }
        };
        countDownTimer.start();
    }

    @Override
    public void showError(int res) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer.onFinish();
        }
        ToastUtils.ToastMessage(this, res);
    }

    @Override
    public void actSuccess() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
