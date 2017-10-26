package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gs.buluo.app.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Solang on 2017/10/23.
 */

public class BoardroomParticipantNoneActivity extends BaseActivity {
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_boardroom_participant_none;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    @OnClick(R.id.btn_confirm)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                finish();
                startActivity(new Intent(this,BoardroomParticipantAddActivity.class));
                break;
        }
    }
}
