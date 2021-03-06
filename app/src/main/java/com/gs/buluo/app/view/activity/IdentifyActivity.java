package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.bean.RequestBodyBean.AuthorityRequest;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.network.MainApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.app.view.widget.panel.CustomIdInputPanel;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.TribeDateUtils;

import java.lang.reflect.Method;
import java.util.Date;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hjn on 2016/11/7.
 */
public class IdentifyActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.identify_birthdayTime)
    TextView mBirthTime;
    @BindView(R.id.verify_IdCardNumber)
    EditText mIdCardNumber;
    @BindView(R.id.verify_name)
    EditText mName;
    @BindView(R.id.identify_sex)
    TextView mSex;
    @BindView(R.id.identify_sign)
    ImageView mSign;

    @BindView(R.id.identify_finish)
    TextView mFinish;

    private long birthday;
    private UserInfoDao userDao;
    private UserInfoEntity infoEntity;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        userDao = new UserInfoDao();
        mFinish.setOnClickListener(this);
        mBirthTime.setOnClickListener(this);
        mSex.setOnClickListener(this);
        mIdCardNumber.setOnClickListener(this);
        infoEntity = userDao.findFirst();
        if (infoEntity.getIdNo() != null) {
            long date = Long.parseLong(infoEntity.getBirthday());
            birthday = date;
            mBirthTime.setText(TribeDateUtils.dateFormat5(new Date(date)));
            if (TextUtils.equals(infoEntity.getSex(), "MALE"))
                mSex.setText(getString(R.string.male));
            else
                mSex.setText(getString(R.string.female));

            mIdCardNumber.setInputType(InputType.TYPE_CLASS_TEXT);
            mIdCardNumber.setText(infoEntity.getIdNo());
            mName.setText(infoEntity.getName());
            switch (infoEntity.getEnumStatus()) {
                case SUCCESS:
                    mSign.setVisibility(View.VISIBLE);
                    mSign.setImageResource(R.mipmap.identify_success);
                    mBirthTime.setOnClickListener(null);
                    mSex.setOnClickListener(null);
                    mIdCardNumber.setEnabled(false);
                    mName.setFocusable(false);
                    mFinish.setVisibility(View.GONE);
                    break;
                default:
                    mSign.setVisibility(View.VISIBLE);
                    mSign.setImageResource(R.mipmap.identify_fail);
                    mIdCardNumber.setEnabled(true);
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
        showLoadingDialog();
        String sex = mSex.getText().toString().trim();
        if (TextUtils.equals(sex, getString(R.string.male))) {
            sex = "MALE";
        } else {
            sex = "FEMALE";
        }
        AuthorityRequest request = new AuthorityRequest();
        request.birthday = birthday + "";
        request.idNo = mIdCardNumber.getText().toString().trim();
        request.name = mName.getText().toString().trim();
        request.personSex = sex;

        TribeRetrofit.getInstance().createApi(MainApis.class).
                doAuthentication(TribeApplication.getInstance().getUserInfo().getId(), request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse<UserInfoEntity>>() {
                    @Override
                    public void onNext(BaseResponse<UserInfoEntity> response) {
                        UserInfoEntity data = response.data;
                        data.setToken(TribeApplication.getInstance().getUserInfo().getToken());
                        data.setMid(infoEntity.getMid());
                        userDao.update(data);
                        switch (data.getEnumStatus()) {
                            case SUCCESS:
                                ToastUtils.ToastMessage(IdentifyActivity.this, "身份认证成功");
                                startActivity(new Intent(getCtx(), MainActivity.class));
                                break;
                            case PROCESSING:
                                ToastUtils.ToastMessage(IdentifyActivity.this, "身份认证处理中...");
                                break;
                            case FAILURE:
                                ToastUtils.ToastMessage(IdentifyActivity.this, "身份认证失败,请过段时间再试");
                                break;
                        }
                        finish();
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_verify;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(IdentifyActivity.this, ModifyInfoActivity.class);
        switch (v.getId()) {
            case R.id.identify_birthdayTime:
                intent.putExtra(Constant.ForIntent.MODIFY, Constant.BIRTHDAY);
                intent.putExtra(Constant.BIRTHDAY, mBirthTime.getText().toString().trim());
                startActivityForResult(intent, 201);
                break;
            case R.id.identify_sex:
                intent.putExtra(Constant.ForIntent.MODIFY, Constant.SEX);
                startActivityForResult(intent, 202);
                break;
            case R.id.verify_IdCardNumber:
                disableShowSoftInput();
                mIdCardNumber.setFocusable(true);
                mIdCardNumber.setFocusableInTouchMode(true);
                mIdCardNumber.requestFocus();
                CustomIdInputPanel panel = new CustomIdInputPanel(getCtx(), mIdCardNumber);
                panel.show();
                break;
            case R.id.identify_finish:
                String id = mIdCardNumber.getText().toString().trim();
                if (mBirthTime.length() == 0 || mIdCardNumber.length() == 0 || mName.length() == 0 || mSex.length() == 0) {
                    ToastUtils.ToastMessage(IdentifyActivity.this, R.string.not_empty);
                    return;
                }
                if (id.length() != 18 && id.length() != 16 && id.length() != 15) {
                    ToastUtils.ToastMessage(IdentifyActivity.this, getString(R.string.wrong_id_number));
                    return;
                }
                doVerify();
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


    abstract static class NewNumberKeyListener extends NumberKeyListener {
        @Override
        protected char[] getAcceptedChars() {
            return new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'X', 'x'};
        }

        @Override
        public int getInputType() {
            return 3;
        }
    }

    public void disableShowSoftInput() {
        Class<EditText> cls = EditText.class;
        Method method;
        try {
            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(mIdCardNumber, false);
        } catch (Exception e) {
        }
    }
}