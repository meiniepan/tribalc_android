package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.VerifyBody;
import com.gs.buluo.app.network.MainApis;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.activity.BankCardActivity;
import com.gs.buluo.app.view.widget.PwdEditText;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Solang on 2017/4/19.
 */

public class VerifyCodePanel extends Dialog {
    private final Context mContext;
    private final String mCardId;
    private final String mPhone;
    @Bind(R.id.pwd_board_pet)
    PwdEditText pwdEditText;
    @Bind(R.id.reGet_verify_code)
    TextView reGetVerifyCode;

    public VerifyCodePanel(Context context, String carId, String phone) {
        super(context, R.style.pay_dialog);
        mContext = context;
        mCardId = carId;
        mPhone = phone;
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.verify_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = DensityUtils.dip2px(mContext, 450);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        pwdEditText.showKeyBoard();

        pwdEditText.setInputCompleteListener(new PwdEditText.InputCompleteListener() {
            @Override
            public void inputComplete() {
                String strPassword = pwdEditText.getStrPassword();
                VerifyBody verifyBody = new VerifyBody();
                verifyBody.value = strPassword;
                TribeRetrofit.getInstance().createApi(MoneyApis.class).
                        uploadVerify(TribeApplication.getInstance().getUserInfo().getId(), mCardId, verifyBody).
                        subscribeOn(Schedulers.io()).
                        observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseSubscriber<BaseResponse<CodeResponse>>() {
                            @Override
                            public void onNext(BaseResponse<CodeResponse> codeResponseBaseResponse) {
                                mContext.startActivity(new Intent(mContext, BankCardActivity.class));
                                dismiss();
                            }

                            @Override
                            public void onFail(ApiException e) {
                                ToastUtils.ToastMessage(getContext(), R.string.verify_error);
                                pwdEditText.clear();
                            }
                        });

            }
        });
        rootView.findViewById(R.id.pwd_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        reGetVerifyCode.setClickable(false);
        reGetVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerifyCode(mPhone);
            }
        });
    }


    private void sendVerifyCode(String phone) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.ToastMessage(mContext, R.string.phone_not_empty);
            return;
        }
        TribeRetrofit.getInstance().createApi(MainApis.class).
                doVerify(new ValueRequestBody(phone))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<CodeResponse>>() {
                    @Override
                    public void onNext(BaseResponse<CodeResponse> response) {
                        dealWithIdentify(response.code);
                    }
                });
    }


    private void dealWithIdentify(int code) {
        switch (code) {
            case 202:
                reGetVerifyCode.setText("60s");
                new CountDownTimer(60000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        reGetVerifyCode.setClickable(false);
                        reGetVerifyCode.setText(millisUntilFinished / 1000 + "秒");
                    }

                    @Override
                    public void onFinish() {
                        reGetVerifyCode.setText("获取验证码");
                        reGetVerifyCode.setClickable(true);
                    }
                }.start();
                break;
            case 400:
                ToastUtils.ToastMessage(mContext, mContext.getString(R.string.wrong_number));
                break;
        }
    }

}
