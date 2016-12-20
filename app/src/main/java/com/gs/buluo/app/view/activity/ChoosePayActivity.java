package com.gs.buluo.app.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.app.R;

public class ChoosePayActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.choose_pay_back).setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_choose_pay;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_pay_back:
                finish();
                break;
        }
    }
}
