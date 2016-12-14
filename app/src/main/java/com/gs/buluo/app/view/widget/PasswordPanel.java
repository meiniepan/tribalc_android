package com.gs.buluo.app.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.utils.DensityUtils;
import com.gs.buluo.app.utils.SharePreferenceManager;
import com.gs.buluo.app.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hjn on 2016/12/13.
 */
public class PasswordPanel extends Dialog{
    private Context mContext;
    @Bind(R.id.pwd_board_pet)
    PwdEditText pwdEditText;

    public PasswordPanel(Context context) {
        super(context , R.style.pay_dialog);
        mContext=context;
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.pwd_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = DensityUtils.dip2px(mContext,180);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        pwdEditText.showKeyBoard();

        pwdEditText.setInputCompleteListener(new PwdEditText.InputCompleteListener() {
            @Override
            public void inputComplete() {
                String strPassword = pwdEditText.getStrPassword();
                String pwd = SharePreferenceManager.getInstance(mContext).getStringValue(Constant.WALLET_PWD);
                if (TextUtils.equals(strPassword,pwd)){
                    payMoney();
                }else {
                    ToastUtils.ToastMessage(getContext(),R.string.wrong_pwd);
                    pwdEditText.clear();
                }
            }
        });
    }

    private void payMoney() {
        ToastUtils.ToastMessage(getContext(),R.string.pay_success);
        dismiss();
    }
}
