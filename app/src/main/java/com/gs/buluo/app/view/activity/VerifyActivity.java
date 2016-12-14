package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.UserInfoEntity;
import com.gs.buluo.app.bean.UserSensitiveEntity;
import com.gs.buluo.app.dao.UserInfoDao;
import com.gs.buluo.app.dao.UserSensitiveDao;
import com.gs.buluo.app.presenter.BasePresenter;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/7.
 */
public class VerifyActivity extends BaseActivity {
    @Bind(R.id.verify_birthTime)
    EditText mBirthTime;
    @Bind(R.id.verify_IdCardNumber)
    EditText mIdCardNumber;
    @Bind(R.id.verify_name)
    EditText mName;
    @Bind(R.id.verify_sex)
    EditText mSex;

    private static final String TAG = "VerifyActivity";

    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.identify_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.identify_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSensitiveDao userSensitiveDao = new UserSensitiveDao();
                UserSensitiveEntity entity = userSensitiveDao.findFirst();
                entity.setName(mName.getText().toString().trim());
                entity.setIdNo(mIdCardNumber.getText().toString().trim());
                userSensitiveDao.update(entity);
                Log.d(TAG, "onClick: "+entity);
//                finish();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_verify;
    }

}
