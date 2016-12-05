package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.view.View;

import com.gs.buluo.app.R;
import com.gs.buluo.app.presenter.BasePresenter;

/**
 * Created by hjn on 2016/11/7.
 */
public class VerifyActivity extends BaseActivity{
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
                finish();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_verify;
    }

}
