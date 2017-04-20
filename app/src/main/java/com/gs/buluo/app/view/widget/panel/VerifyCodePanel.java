package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.ResponseBody.CodeResponse;
import com.gs.buluo.app.bean.VerifyBody;
import com.gs.buluo.app.network.MoneyApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.PwdEditText;
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
    @Bind(R.id.pwd_board_pet)
    PwdEditText pwdEditText;

    public VerifyCodePanel(Context context,String carId) {
        super(context, R.style.pay_dialog);
        mContext = context;
        mCardId = carId;
        initView();
    }
    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.verify_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = DensityUtils.dip2px(mContext,450);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        pwdEditText.showKeyBoard();

        pwdEditText.setInputCompleteListener(new PwdEditText.InputCompleteListener() {
            @Override
            public void inputComplete() {
                String strPassword = pwdEditText.getStrPassword();
                if (true) {
                    VerifyBody verifyBody = new VerifyBody();
                    verifyBody.value = strPassword;
                    TribeRetrofit.getInstance().createApi(MoneyApis.class).
                            uploadVerify(TribeApplication.getInstance().getUserInfo().getId(), mCardId,verifyBody).
                            subscribeOn(Schedulers.io()).
                            observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new BaseSubscriber<BaseResponse<CodeResponse>>() {
                                @Override
                                public void onNext(BaseResponse<CodeResponse> codeResponseBaseResponse) {
                                    dismiss();
                                }

                            });
                } else {
                    ToastUtils.ToastMessage(getContext(), R.string.wrong_pwd);
                    pwdEditText.clear();
                }
            }
        });
        rootView.findViewById(R.id.pwd_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

}
