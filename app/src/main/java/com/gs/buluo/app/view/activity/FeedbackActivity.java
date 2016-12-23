package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.view.View;

import com.gs.buluo.app.R;
import com.gs.buluo.app.utils.ToastUtils;

/**
 * Created by hjn on 2016/12/23.
 */
public class FeedbackActivity extends BaseActivity{
    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.feedback_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.ToastMessage(FeedbackActivity.this,"意见反馈成功");
                finish();
            }
        });
        findViewById(R.id.feedback_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_feedback;
    }
}
