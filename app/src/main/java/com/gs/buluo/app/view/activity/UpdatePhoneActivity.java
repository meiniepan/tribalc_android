package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.LoginPresenter;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.impl.ILoginView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by hjn on 2016/11/9.
 */
public class UpdatePhoneActivity extends BaseActivity implements ILoginView {
    @Bind(R.id.tv_old_phone)
    TextView tvOldPhone;
    @Bind(R.id.et_new_phone)
    EditText etNewPhone;
    @Bind(R.id.tv_send_verify)
    TextView tvSendVerify;
    @Bind(R.id.et_update_verify)
    EditText etVerify;

    private String phone;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        phone = TribeApplication.getInstance().getUserInfo().getPhone();
        tvOldPhone.setText(phone);

        tvSendVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCounter();
                ((LoginPresenter) mPresenter).doVerify(etNewPhone.getText().toString().trim());
            }
        });
    }

    private Subscriber<Long> subscriber;

    private void startCounter() {
        final int startTime = 60;
        tvSendVerify.setClickable(false);
        subscriber = new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                tvSendVerify.setText("获取验证码");
                tvSendVerify.setClickable(true);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Long aLong) {
                tvSendVerify.setText(aLong + "秒");
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
    protected int getContentLayout() {
        return R.layout.activity_phone1;
    }

    @Override
    protected BasePresenter getPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(this, getString(R.string.connect_fail));
    }

    @Override
    public void actSuccess() {
        ToastUtils.ToastMessage(getCtx(), R.string.update_success);
        finish();
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
                tvSendVerify.setText("获取验证码");
                tvSendVerify.setClickable(true);
                break;
        }
    }

    public void changePhone(View view) {
        String phone = etNewPhone.getText().toString().trim();
        if (!CommonUtils.checkPhone("", phone, this)) {
            ToastUtils.ToastMessage(getCtx(), R.string.wrong_number);
            return;
        }
        if (etVerify.length() == 0) {
            ToastUtils.ToastMessage(getCtx(), R.string.input_verify);
            return;
        }
        showLoadingDialog();
        ((LoginPresenter) mPresenter).updatePhone(phone, etVerify.getText().toString().trim());
    }
}
