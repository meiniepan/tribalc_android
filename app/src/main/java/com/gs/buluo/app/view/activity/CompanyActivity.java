package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.app.R;

public class CompanyActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.company_back).setOnClickListener(this);
        findViewById(R.id.bind_enterprise).setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_company;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.company_back:
                finish();
                break;
            case R.id.bind_enterprise:
                startActivity(new Intent(this,BindCompanyActivity.class));
                finish();
                break;
        }
    }
}
