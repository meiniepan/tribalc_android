package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.utils.SharePreferenceManager;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.widget.PwdEditText;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/21.
 */
public class UpdateWalletPwdActivity extends BaseActivity {
    @Bind(R.id.wallet_pwd_sign)
    TextView mText;

    @Bind(R.id.wallet_pwd_1)
    PwdEditText editText;

    String mPwd;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        if (SharePreferenceManager.getInstance(getApplicationContext()).getStringValue(Constant.WALLET_PWD)==null){
            mText.setText(R.string.input_pay_pwd);
        }else {
            mText.setText(R.string.input_new_pwd);
        }
        editText.requestFocus();
        editText.setInputCompleteListener(new PwdEditText.InputCompleteListener() {
            @Override
            public void inputComplete() {
                mPwd=editText.getStrPassword();
            }
        });

        findViewById(R.id.wallet_pwd_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null==mPwd) {
                    ToastUtils.ToastMessage(UpdateWalletPwdActivity.this,getString(R.string.pwd_not_6));
                    return;
                }
                Intent intent = new Intent(UpdateWalletPwdActivity.this, UpdateWalletPwdActivity2.class);
                intent.putExtra(Constant.WALLET_PWD,mPwd);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.wallet_pwd_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_wallet_pwd;
    }
}
