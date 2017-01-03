package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.PwdEditText;

import org.xutils.common.util.MD5;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/18.
 */
public class ConfirmActivity extends BaseActivity{
    @Bind(R.id.wallet_pwd_1)
    PwdEditText editText;

    private String password;
    private String myPwd;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        editText.requestFocus();
        editText.setInputCompleteListener(new PwdEditText.InputCompleteListener() {
            @Override
            public void inputComplete() {
                password=editText.getStrPassword();
            }
        });

        myPwd = getIntent().getStringExtra(Constant.WALLET_PWD);

        findViewById(R.id.wallet_pwd_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPwd();
            }
        });
        findViewById(R.id.wallet_pwd_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.pwd_forget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmActivity.this, PhoneVerifyActivity2.class);
                intent.putExtra("for_security",true);
                startActivity(intent);
            }
        });
    }

    private void checkPwd() {
        if (null==password) {
            ToastUtils.ToastMessage(ConfirmActivity.this,getString(R.string.pwd_not_6));
            return;
        }
        if (!TextUtils.equals(MD5.md5(password),myPwd)){
            ToastUtils.ToastMessage(this,R.string.wrong_pwd);
            editText.clear();
            return;
        }
        Intent intent = new Intent(this, UpdateWalletPwdActivity.class);
        intent.putExtra(Constant.OLD_PWD,password);
        startActivity(intent);
        finish();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_wallet_pwd;
    }
}
