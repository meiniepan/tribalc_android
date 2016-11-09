package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.FirstEvent;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.utils.AppManager;
import com.gs.buluo.app.utils.ToastUtils;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * Created by hjn on 2016/11/9.
 */
public class PhoneVerifyActivity2 extends BaseActivity{
    @Bind(R.id.verify_phone2)
    TextView mPhone;

    @Bind(R.id.bind_verify)
    EditText mVerify;

    private String phone;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        phone = getIntent().getStringExtra("phone");
        mPhone.setText(phone);

        findViewById(R.id.phone_bind_next_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkVerify();
            }
        });

        findViewById(R.id.phone2_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void checkVerify() {
        String verify=mVerify.getText().toString().trim();
        if (TextUtils.isEmpty(verify)){
            ToastUtils.ToastMessage(PhoneVerifyActivity2.this,R.string.verify_not_empty);
            return;
        }
        EventBus.getDefault().post(new FirstEvent(phone));
        finish();
        AppManager.getAppManager().finishActivity(PhoneVerifyActivity.class);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_phone2;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }
}