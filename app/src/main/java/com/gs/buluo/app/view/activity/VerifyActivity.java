package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.ResponseCode;
import com.gs.buluo.app.bean.ResponseBody.SimpleCodeResponse;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.bean.UserSensitiveEntity;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.dao.UserSensitiveDao;
import com.gs.buluo.app.model.MainModel;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.utils.TribeDateUtils;

import java.util.Date;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/7.
 */
public class VerifyActivity extends BaseActivity implements View.OnClickListener, Callback<SimpleCodeResponse> {
    @Bind(R.id.identify_birthdayTime)
    TextView mBirthTime;
    @Bind(R.id.verify_IdCardNumber)
    EditText mIdCardNumber;
    @Bind(R.id.verify_name)
    EditText mName;
    @Bind(R.id.identify_sex)
    TextView mSex;
    private long birthday;
    private UserSensitiveDao userSensitiveDao;
    private UserSensitiveEntity sensitiveEntity;


    @Override
    protected void bindView(Bundle savedInstanceState) {
        userSensitiveDao = new UserSensitiveDao();
        findViewById(R.id.identify_back).setOnClickListener(this);
        findViewById(R.id.identify_finish).setOnClickListener(this);
        mBirthTime.setOnClickListener(this);
        mSex.setOnClickListener(this);

        sensitiveEntity = userSensitiveDao.findFirst();
        if (sensitiveEntity.getIdNo()!=null){
            UserInfoEntity infoEntity=new UserInfoDao().findFirst();
            mBirthTime.setText(TribeDateUtils.dateFormat5(new Date(Long.parseLong(infoEntity.getBirthday()))));
            if (TextUtils.equals(infoEntity.getSex(),"MALE"))
                mSex.setText(getString(R.string.male));
            else
                mSex.setText(getString(R.string.female));

            mIdCardNumber.setText(sensitiveEntity.getIdNo());
            mName.setText(sensitiveEntity.getName());
            mBirthTime.setOnClickListener(null);
            mSex.setOnClickListener(null);
            mIdCardNumber.setFocusable(false);
            mName.setFocusable(false);
            findViewById(R.id.identify_finish).setVisibility(View.GONE);
        }
    }

    private void doVerify() {
        String sex = mSex.getText().toString().trim();
        if (TextUtils.equals(sex, getString(R.string.male))) {
            sex="MALE";
        }else {
            sex = "FEMALE";
        }
        showLoadingDialog();
        new MainModel().doAuthentication(mName.getText().toString().trim(), sex,
                birthday, mIdCardNumber.getText().toString().trim(), this);

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_verify;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(VerifyActivity.this, ModifyInfoActivity.class);
        switch (v.getId()) {
            case R.id.identify_birthdayTime:
                intent.putExtra(Constant.ForIntent.MODIFY,Constant.BIRTHDAY);
                startActivityForResult(intent, 201);
                break;
            case R.id.identify_sex:
                intent.putExtra(Constant.ForIntent.MODIFY,Constant.SEX);
                startActivityForResult(intent, 202);
                break;
            case R.id.identify_finish:
                if (mBirthTime.length() == 0 || mIdCardNumber.length() == 0 || mName.length() == 0 || mSex.length() == 0) {
                    ToastUtils.ToastMessage(VerifyActivity.this, R.string.not_empty);
                    return;
                }
                doVerify();
                break;
            case R.id.identify_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 201) {
                birthday = Long.parseLong(data.getStringExtra(Constant.BIRTHDAY));
                mBirthTime.setText(TribeDateUtils.dateFormat5(new Date(birthday)));
            } else {
                mSex.setText(data.getStringExtra(Constant.SEX));
            }
        }
    }

    @Override
    public void onResponse(Call<SimpleCodeResponse> call, Response<SimpleCodeResponse> response) {
        dismissDialog();
        if (response.body()!=null&&response.code()== ResponseCode.GET_SUCCESS){
            sensitiveEntity.setName(mName.getText().toString().trim());
            sensitiveEntity.setIdNo(mIdCardNumber.getText().toString().trim());
            userSensitiveDao.update(sensitiveEntity);
            finish();
        }else {
            ToastUtils.ToastMessage(this,R.string.cancel);
        }
    }

    @Override
    public void onFailure(Call<SimpleCodeResponse> call, Throwable t) {
        ToastUtils.ToastMessage(this,R.string.connect_fail);
    }
}
