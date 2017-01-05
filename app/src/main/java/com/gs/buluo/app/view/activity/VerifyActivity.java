package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.ResponseCode;
import com.gs.buluo.app.bean.ResponseBody.BaseCodeResponse;
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

import static com.gs.buluo.app.bean.UserSensitiveEntity.AuthorityStatus.FAILURE;
import static com.gs.buluo.app.bean.UserSensitiveEntity.AuthorityStatus.PROCESSING;
import static com.gs.buluo.app.bean.UserSensitiveEntity.AuthorityStatus.SUCCESS;

/**
 * Created by hjn on 2016/11/7.
 */
public class VerifyActivity extends BaseActivity implements View.OnClickListener, Callback<BaseCodeResponse<UserSensitiveEntity>> {
    @Bind(R.id.identify_birthdayTime)
    TextView mBirthTime;
    @Bind(R.id.verify_IdCardNumber)
    EditText mIdCardNumber;
    @Bind(R.id.verify_name)
    EditText mName;
    @Bind(R.id.identify_sex)
    TextView mSex;
    @Bind(R.id.identify_sign)
    ImageView mSign;

    @Bind(R.id.identify_finish)
    TextView mFinish;

    private long birthday;
    private UserSensitiveDao userSensitiveDao;
    private UserSensitiveEntity sensitiveEntity;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        userSensitiveDao = new UserSensitiveDao();
        findViewById(R.id.identify_back).setOnClickListener(this);
        mFinish.setOnClickListener(this);
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

            switch (sensitiveEntity.getEnumStatus()){
                case SUCCESS:
                    mSign.setVisibility(View.VISIBLE);
                    mSign.setImageResource(R.mipmap.identify_success);
                    mBirthTime.setOnClickListener(null);
                    mSex.setOnClickListener(null);
                    mIdCardNumber.setFocusable(false);
                    mName.setFocusable(false);
                    mFinish.setVisibility(View.GONE);
                    break;
                case PROCESSING:
                    ViewStub stub= (ViewStub) findViewById(R.id.identify_processing);
                    View view= stub.inflate();
                    view.findViewById(R.id.identify_processing_back).setOnClickListener(this);
                    findViewById(R.id.identify_parent).setVisibility(View.GONE);
                    mBirthTime.setOnClickListener(null);
                    mSex.setOnClickListener(null);
                    mIdCardNumber.setFocusable(false);
                    mName.setFocusable(false);
                    mFinish.setVisibility(View.GONE);
                    break;
                case FAILURE:
                    mSign.setVisibility(View.VISIBLE);
                    mSign.setImageResource(R.mipmap.identify_fail);
                    mFinish.setText("重新提交认证");
                    mFinish.setVisibility(View.VISIBLE);
                    mFinish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            doVerify();
                        }
                    });
                    break;
            }
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
                intent.putExtra(Constant.BIRTHDAY,mBirthTime.getText().toString().trim());
                startActivityForResult(intent, 201);
                break;
            case R.id.identify_sex:
                intent.putExtra(Constant.ForIntent.MODIFY,Constant.SEX);
                startActivityForResult(intent, 202);
                break;
            case R.id.identify_finish:
                String id = mIdCardNumber.getText().toString().trim();
                if (mBirthTime.length() == 0 || mIdCardNumber.length() == 0 || mName.length() == 0 || mSex.length() == 0) {
                    ToastUtils.ToastMessage(VerifyActivity.this, R.string.not_empty);
                    return;
                }
                if (id.length()!=18&&id.length()!=16&&id.length()!= 15){
                    ToastUtils.ToastMessage(VerifyActivity.this, getString(R.string.wrong_id_number));
                    return;
                }
                doVerify();
                break;
            case R.id.identify_back:
                finish();
            case R.id.identify_processing_back:
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
    public void onResponse(Call<BaseCodeResponse<UserSensitiveEntity>> call, Response<BaseCodeResponse<UserSensitiveEntity>> response) {
        dismissDialog();
        if (response.body()!=null&&response.code()== ResponseCode.GET_SUCCESS){
            UserSensitiveEntity data = response.body().data;
            switch (data.getEnumStatus()){
                case SUCCESS:
                    data.setMid(sensitiveEntity.getMid());
                    userSensitiveDao.update(data);
                    ToastUtils.ToastMessage(this,"身份认证成功");
                    break;
                case PROCESSING:
                    ToastUtils.ToastMessage(this,"身份认证处理中...");
                    break;
                case FAILURE:
                    ToastUtils.ToastMessage(this,"身份认证失败,请过段时间再试");
                    break;
            }
            finish();
        }else {
            ToastUtils.ToastMessage(this,R.string.connect_fail);
        }
    }

    @Override
    public void onFailure(Call<BaseCodeResponse<UserSensitiveEntity>> call, Throwable t) {
        ToastUtils.ToastMessage(this,R.string.connect_fail);
    }
}
