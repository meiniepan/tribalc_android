package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.app.R;

public class BindCompanySuccessfulActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void bindView(Bundle savedInstanceState) {
            findViewById(R.id.btn_return_main).setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bind_successful;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
