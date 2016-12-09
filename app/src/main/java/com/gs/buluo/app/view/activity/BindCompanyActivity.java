package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.BindCompanyPresenter;
import com.gs.buluo.app.utils.ToastUtils;

import butterknife.Bind;

public class BindCompanyActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.tv_company_name)
    TextView mCompanyName;
    @Bind(R.id.et_user_name)
    EditText mUsername;
    @Bind(R.id.et_part_name)
    EditText mPartName;
    @Bind(R.id.et_position_name)
    EditText mPositionName;
    @Bind(R.id.et_work_number)
    EditText mWorkNumber;


    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.bind_company).setOnClickListener(this);
        findViewById(R.id.bind_company_back).setOnClickListener(this);
        mCompanyName.setOnClickListener(this);
    }

    private void checkText(String conpanyName, String username, String partname, String position,String number) {
        if (TextUtils.isEmpty(conpanyName)||TextUtils.isEmpty(username)||TextUtils.isEmpty(partname)||TextUtils.isEmpty(position)||TextUtils.isEmpty(number)){
            ToastUtils.ToastMessage(this,getString(R.string.not_empty));
            return;
        }
    }

    @Override
    protected BasePresenter getPresenter() {
        return new BindCompanyPresenter();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bind_company;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bind_company:
                String conpanyName = mCompanyName.getText().toString().trim();
                String username = mUsername.getText().toString().trim();
                String partname = mPartName.getText().toString().trim();
                String position = mPositionName.getText().toString().trim();
                String number = mWorkNumber.getText().toString().trim();
                checkText(conpanyName,username,partname,position,number);


                break;
            case R.id.bind_company_back:
                finish();
                break;
            case R.id.tv_company_name:
                startActivity(new Intent(this,PickCommunityActivity.class));
                break;
        }
    }


}
