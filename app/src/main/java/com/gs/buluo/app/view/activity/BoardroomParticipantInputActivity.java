package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ContactsPersonEntity;
import com.gs.buluo.common.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Solang on 2017/12/1.
 */

public class BoardroomParticipantInputActivity extends BaseActivity {
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_number)
    EditText mEtNumber;
    @BindView(R.id.btn_new)
    Button mBtnNew;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_boardroom_participant_input;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    @OnClick({R.id.et_name, R.id.et_number, R.id.btn_new})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_new:
                ContactsPersonEntity entity = new ContactsPersonEntity();
                entity.name = mEtName.getText().toString();
                entity.phone = mEtNumber.getText().toString();
                if (TextUtils.isEmpty(entity.name) || TextUtils.isEmpty(entity.phone)) {
                    ToastUtils.ToastMessage(getCtx(), "填写未完成");
                    return;
                }
                Intent intent = new Intent(this, BoardroomParticipantActivity.class);
                intent.putExtra(Constant.CONTACT_DATA, entity);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
