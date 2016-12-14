package com.gs.buluo.app.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.app.R;

public class BindCompanyProcessingActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.bind_processing_return).setOnClickListener(this);
        findViewById(R.id.bind_company_processing_back).setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bind_company_processing;
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
