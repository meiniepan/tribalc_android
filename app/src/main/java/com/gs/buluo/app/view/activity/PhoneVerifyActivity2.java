package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.RequestBodyBean.PhoneUpdateBody;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.network.MainApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.AppManager;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by hjn on 2016/11/9.
 */
public class PhoneVerifyActivity2 extends BaseActivity {
    @Bind(R.id.verify_phone2)
    TextView mPhone;
    @Bind(R.id.bind_verify)
    EditText mVerify;
    @Bind(R.id.second_counts)
    TextView reg_send;

    @Bind(R.id.phone_code_title)
    TextView title;
    private String phone;
    private boolean fromPwd;
    private UserInfoEntity infoEntity;
    private UserInfoDao dao;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        fromPwd = getIntent().getBooleanExtra("for_security", false);
        if (fromPwd) {
            dao = new UserInfoDao();
            infoEntity = dao.findFirst();
            phone = infoEntity.getPhone();
            title.setText("安全校验");
            mPhone.setText(phone);
            reg_send.setClickable(false);
        } else {
            phone = getIntent().getStringExtra("phone");
            mPhone.setText(phone);
            reg_send.setClickable(false);
            startCounter();
        }
        doVerify();

        findViewById(R.id.phone_bind_next_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkVerify();
            }
        });

        reg_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doVerify();
            }
        });
    }

    private void doVerify() {
        TribeRetrofit.getInstance().createApi(MainApis.class).
                doVerify(new ValueRequestBody(phone))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CodeResponse>>() {
                    @Override
                    public void onNext(BaseResponse<CodeResponse> response) {
                        startCounter();
                    }

                    @Override
                    public void onFail(ApiException e) {
                        super.onFail(e);
                        reg_send.setText("获取验证码");
                        reg_send.setClickable(true);
                        findViewById(R.id.text_behind).setVisibility(View.GONE);
                    }
                });
    }

    private void startCounter() {
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                reg_send.setClickable(false);
                reg_send.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                reg_send.setText("获取验证码");
                findViewById(R.id.text_behind).setVisibility(View.GONE);
                reg_send.setClickable(true);
            }
        }.start();
    }

    private void checkVerify() {
        String verify = mVerify.getText().toString().trim();
        if (TextUtils.isEmpty(verify)) {
            ToastUtils.ToastMessage(getCtx(), R.string.verify_not_empty);
            return;
        }
        if (fromPwd) { //忘记密码
            Intent intent = new Intent(this, UpdateWalletPwdActivity.class);
            intent.putExtra(Constant.VCODE, verify);
            startActivity(intent);
            AppManager.getAppManager().finishActivity(ConfirmActivity.class);
            finish();
        } else {
            PhoneUpdateBody body = new PhoneUpdateBody();
            body.phone = phone;
            body.verificationCode = verify;
            TribeRetrofit.getInstance().createApi(MainApis.class).
                    updatePhone(TribeApplication.getInstance().getUserInfo().getId(), body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<BaseResponse<CodeResponse>>() {
                        @Override
                        public void onNext(BaseResponse<CodeResponse> response) {
                            infoEntity.setPhone(phone);
                            dao.update(infoEntity);
                            finish();
                            AppManager.getAppManager().finishActivity(PhoneVerifyActivity.class);
                        }

                        @Override
                        public void onFail(ApiException e) {
                            ToastUtils.ToastMessage(getCtx(), R.string.wrong_verify);
                        }
                    });
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_phone2;
    }

}
