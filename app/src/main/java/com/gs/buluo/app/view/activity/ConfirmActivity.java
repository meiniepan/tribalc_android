package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Xml;
import android.view.View;

import com.baidu.platform.comapi.map.C;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.utils.SharePreferenceManager;
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

    String password;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        editText.requestFocus();
        editText.setInputCompleteListener(new PwdEditText.InputCompleteListener() {
            @Override
            public void inputComplete() {
                password=editText.getStrPassword();
            }
        });

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
    }

    private void checkPwd() {
        if (null==password) {
            ToastUtils.ToastMessage(ConfirmActivity.this,getString(R.string.pwd_not_6));
            return;
        }
        String localPwd  = SharePreferenceManager.getInstance(getApplicationContext()).getStringValue(Constant.WALLET_PWD);
        if (!TextUtils.equals(MD5.md5(password),localPwd)){
            ToastUtils.ToastMessage(this,getString(R.string.wrong_pwd));
            return;
        }
        startActivity(new Intent(this,UpdateWalletPwdActivity.class));
        finish();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_wallet_pwd;
    }
}
