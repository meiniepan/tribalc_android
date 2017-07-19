package com.gs.buluo.app.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.gs.buluo.app.R;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.common.widget.PwdEditText;

import org.xutils.common.util.MD5;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hjn on 2016/12/13.
 */
public class NewPasswordPanel extends Dialog {
    private OnPwdFinishListener onPwdFinishListener;
    private Context mContext;
    @Bind(R.id.pwd_board_pet)
    PwdEditText pwdEditText;
    private String myPwd;

    public NewPasswordPanel(Context context, String pwd, OnPwdFinishListener onPwdFinishListener) {
        super(context, R.style.pay_dialog);
        mContext = context;
        this.onPwdFinishListener = onPwdFinishListener;
        myPwd = pwd;
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.pwd_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = com.gs.buluo.common.utils.DensityUtils.dip2px(mContext, 450);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        pwdEditText.showKeyBoard();
        rootView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        pwdEditText.setInputCompleteListener(new com.gs.buluo.common.widget.PwdEditText.InputCompleteListener() {
            @Override
            public void inputComplete() {
                if (TextUtils.equals(MD5.md5(pwdEditText.getStrPassword()), myPwd)) {
                    onPwdFinishListener.onPwdFinishListener(pwdEditText.getStrPassword());
                    dismiss();
                } else {
                    ToastUtils.ToastMessage(getContext(), R.string.wrong_pwd);
                    pwdEditText.clear();
                }
            }
        });
    }

    public interface OnPwdFinishListener {
        void onPwdFinishListener(String strPassword);
    }
}
