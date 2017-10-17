package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.presenter.LoginPresenter;
import com.gs.buluo.app.view.impl.ILoginView;
import com.gs.buluo.common.utils.AppManager;
import com.gs.buluo.common.utils.CommonUtils;
import com.gs.buluo.common.utils.ToastUtils;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by hjn on 2016/11/3.
 */
public class ThirdLoginActivity extends BaseActivity implements View.OnClickListener, ILoginView {
    @BindView(R.id.et_bind_login_phone)
    EditText etPhone;
    @BindView(R.id.et_bind_verify)
    EditText etVerify;
    @BindView(R.id.third_send_verify)
    TextView tvSend;

    private String wxCode;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        wxCode = getIntent().getStringExtra(Constant.WX_CODE);
        tvSend.setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bind_login;
    }

    @Override
    protected LoginPresenter getPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void onClick(View v) {
        String phone = etPhone.getText().toString().trim();
        switch (v.getId()) {
            case R.id.third_send_verify:
                if (!CommonUtils.checkPhone("86", phone, this)) return;
                startCounter();
                ((LoginPresenter) mPresenter).doVerify(phone);
                etVerify.requestFocus();
                break;
        }
    }

    private Subscriber<Long> subscriber;

    private void startCounter() {
        final int startTime = 60;
        tvSend.setClickable(false);
        subscriber = new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                tvSend.setText("获取验证码");
                tvSend.setClickable(true);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Long aLong) {
                tvSend.setText(aLong + "秒后重新发送");
            }
        };
        Observable.interval(0, 1, TimeUnit.SECONDS).take(startTime + 1)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long time) {
                        return startTime - time;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(this, res);
    }

    @Override
    public void actSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        AppManager.getAppManager().finishActivity(LoginActivity.class);
    }

    @Override
    public void dealWithIdentify(int res, String displayMessage) {
        switch (res) {
            case 504:
                ToastUtils.ToastMessage(this, getString(R.string.frequency_code));
                break;
            case 400:
                ToastUtils.ToastMessage(this, getString(R.string.wrong_number));
                subscriber.unsubscribe();
                tvSend.setText("获取验证码");
                tvSend.setClickable(true);
                break;
            case 401:
                ToastUtils.ToastMessage(this, R.string.wrong_verify);
                break;
        }
    }


    public void bindPhone(View view) {
        if (etVerify.length() == 0) {
            ToastUtils.ToastMessage(this, R.string.please_input_verify);
            return;
        }
        showLoadingDialog();
        ((LoginPresenter) mPresenter).doThirdLogin(etPhone.getText().toString().trim(), etVerify.getText().toString().trim(), wxCode);
    }
}
