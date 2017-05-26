package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.bean.RequestBodyBean.ValueRequestBody;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.view.activity.BankCardActivity;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.widget.LoadingDialog;

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
    private final BankCard mBankCard;
    @Bind(R.id.et_verify_code)
    EditText etCode;
    @Bind(R.id.reGet_verify_code)
    TextView reGetVerifyCode;
    @Bind(R.id.tv_phone)
    TextView tvPhone;

    public VerifyCodePanel(Context context, BankCard bankCard) {
        super(context, R.style.pay_dialog);
        mContext = context;
        mCardId = bankCard.id;
        mPhone = bankCard.phone;
        mBankCard = bankCard;
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
        tvPhone.setText(mPhone.substring(0, 3) + "****" + mPhone.substring(7, 11));
        rootView.findViewById(R.id.pwd_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.tv_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doConfirm();

            }
        });
        timing();
        reGetVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerifyCode();
            }
        });
    }

    private void doConfirm() {
        ValueRequestBody verifyBody = new ValueRequestBody(etCode.getText().toString().trim());
        LoadingDialog.getInstance().show(mContext, "", true);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                confirmAddBankCard(TribeApplication.getInstance().getUserInfo().getId(), mCardId, verifyBody).
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
                        switch (e.getCode()) {
                            case 404:
                                com.gs.buluo.common.utils.ToastUtils.ToastMessage(getContext(), R.string.cannot_find_bankinfo);
                                break;
                            case 424:
                                com.gs.buluo.common.utils.ToastUtils.ToastMessage(getContext(), R.string.bind_error);
                                break;
                            default:
                                com.gs.buluo.common.utils.ToastUtils.ToastMessage(getContext(), R.string.verify_error);
                                break;
                        }
                    }
                });
    }

    private void sendVerifyCode() {
        LoadingDialog.getInstance().show(mContext,"",true);
        TribeRetrofit.getInstance().createApi(MoneyApis.class).
                prepareAddBankCard(TribeApplication.getInstance().getUserInfo().getId(), mBankCard).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<BankCard>>() {
                               @Override
                               public void onNext(BaseResponse<BankCard> bankCardBaseResponse) {
                                   timing();
                               }

                               @Override
                               public void onFail(ApiException e) {
                                   switch (e.getCode()) {
                                       case 400:
                                           com.gs.buluo.common.utils.ToastUtils.ToastMessage(getContext(), R.string.phone_format_error);
                                           break;
                                       case 403:
                                           com.gs.buluo.common.utils.ToastUtils.ToastMessage(getContext(), R.string.bankcard_owner_error);
                                           break;
                                       case 409:
                                           com.gs.buluo.common.utils.ToastUtils.ToastMessage(getContext(), R.string.bank_card_binded);
                                           break;
                                       case 412:
                                           com.gs.buluo.common.utils.ToastUtils.ToastMessage(getContext(), R.string.un_auth);
                                           break;
                                       case 424:
                                           com.gs.buluo.common.utils.ToastUtils.ToastMessage(getContext(), R.string.bind_error);
                                           break;
                                       default:
                                           com.gs.buluo.common.utils.ToastUtils.ToastMessage(getContext(), R.string.net_error);
                                           break;
                                   }
                               }
                           }
                );
    }

    private void timing() {
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
    }

}
